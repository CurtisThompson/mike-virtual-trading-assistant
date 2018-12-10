import java.util.ArrayList;
import java.io.*;

import javafx.concurrent.Task;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.application.*;

import java.util.TimerTask;
import java.util.Timer;

import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.awt.Desktop;
import java.net.URL;
import java.io.IOException;
import java.net.URISyntaxException;

//For the tray icon:
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

//For location:
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

/*
TERMINOLOGY:
stage - whole window including surround, minimise, maximise close buttons, title etc.
scene - within 'window' or stage


*/

public class GUI extends Application {

    //Sizing constants
    private final int SCENE_H = 500;
    private final int SCENE_W = 350;
    private final int GLOBAL_PADDING = 15;
    private final int STATUS_BAR_H = 50;
    private Stage primaryStage;
    private final int FEED_LENGTH = 100;
    private int numberOfMessages = 0;

    private ArrayList<Message> feed_message;
    private ArrayList<Labeled> feed_data;
    private ArrayList<Pane> feed_data_holder;

    private VBox feed = new VBox(FEED_LENGTH);
    private TextField inputField = new TextField();

    //For the tray icon:
    private TrayIcon icon;
    private SystemTray tray;
    private boolean minimised;
    private boolean red;

    private ImageView mikeLogo;
    Tooltip logoTooltip;

    private static final int CHECK_NEWS_TIME = 30000;

    private boolean muted;

    private IChatBot cbot = new ChatBot();
    private Text2Speech tts = new JavaTextToSpeech();

    private Desktop desktop = Desktop.getDesktop();

    /**
    * Default constructor.
    */
    public GUI() {
        feed_message = new ArrayList<Message>();
        feed_data = new ArrayList<Labeled>();
        feed_data_holder = new ArrayList<Pane>();
        muted = false;
        red = false;
    }

    public static void root(String[] args) {
        launch(args); //Method inherited from Application: sets up program as javaFX application
    }

    /**
    * Method called on startup of GUI, drawn main elements in widow, which can be added to later.
    *
    * @param primaryStage Takes the Stage (window) in which to draw the GUI.
    */
    @Override //Overrides the version from Application class
    public void start(Stage primaryStage) { //Our root JavaFX code goes here.

        this.primaryStage = primaryStage;
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Mike"); //Set window title
        primaryStage.setMinHeight(450);
        primaryStage.setMinWidth(350);
        primaryStage.getIcons().add(new javafx.scene.image.Image("img_mike_logo.PNG"));

        minimised = false;
        Platform.setImplicitExit(false); //Ensures javafx application does not terminate when the only window is hidden 'to the tray'

        //Setup top bar, bottom bar.
        BorderPane root = new BorderPane();
        HBox status_bar = setupStatusBar(new HBox());
        root.setTop(status_bar);
        HBox input_bar = setupStatusBar(new HBox());
        root.setBottom(input_bar);

        //Setup scene, to contain root
        Scene scene = new Scene(root, SCENE_W, SCENE_H);

        //Resize window dynamically
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                for (int i = numberOfMessages - 1; i >= 0 && i >= numberOfMessages - 100; i--) {
                    if (feed_data.get(i).getParent() instanceof VBox) { //due to there being 3 labels per data holder for news, there is no longer a 1:1 ratio, so must be referenced like this
                        feed_data.get(i).setMaxWidth(newSceneWidth.doubleValue());
                    } else {
                        feed_data.get(i).setMaxWidth(newSceneWidth.doubleValue());
                    }
                }
            }
        });

        //Setup status_bar and icons
        Pane spacer = new Pane();
        spacer.setMinSize(10, 1);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        //Mike Logo
        mikeLogo = new ImageView(new javafx.scene.image.Image("img_mike_logo.png"));
        logoTooltip = new Tooltip("Mike"); //hover over info
        Tooltip.install(mikeLogo, logoTooltip);
        mikeLogo.setFitHeight(40);
        mikeLogo.setFitWidth(40);

        //Status button
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        javafx.scene.image.Image greenButton = new javafx.scene.image.Image("img_green_button.png");
        javafx.scene.image.Image redButton = new javafx.scene.image.Image("img_red_button.png");
        ImageView statusButton = new ImageView(greenButton);
        Tooltip statusButtonTooltip = new Tooltip("Toggle audio"); //hover over info describing function
        Tooltip.install(statusButton, statusButtonTooltip);
        statusButton.setFitHeight(20);
        statusButton.setFitWidth(20);
        buttonBox.getChildren().add(statusButton);

        statusButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Tile pressed ");
                if (statusButton.getImage() == redButton) {
                    statusButton.setImage(greenButton);
                    muted = false;
                } else {
                    statusButton.setImage(redButton);
                    muted = true;
                }
                event.consume();
            }
        });

        mikeLogo.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Logo Pressed ");
                getResponse(new KeyboardInput("stock summary updates"));
                setBlueIcons();
                event.consume();
            }
        });

        status_bar.getChildren().addAll(buttonBox, spacer, mikeLogo);

        //Setup main message feed view
        feed.prefWidthProperty().bind(scene.widthProperty());
        ScrollPane feed_scroll_wrapper = new ScrollPane();
        feed_scroll_wrapper.vvalueProperty().bind(feed.heightProperty());
        //Wrapper around the feed VBox that introduces scroll bars when necessary
        feed_scroll_wrapper.setContent(feed);
        feed_scroll_wrapper.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); //disable horizontal scrolling
        feed.setPadding(new Insets(GLOBAL_PADDING, GLOBAL_PADDING, GLOBAL_PADDING, GLOBAL_PADDING));
        feed.setSpacing(10);
        root.setCenter(feed_scroll_wrapper);

        //Setup input_bar
        inputField.setPromptText("Ask me something...");
        //inputField.setFocused(false); //FIXME
        HBox.setHgrow(inputField, Priority.ALWAYS);
        input_bar.getChildren().add(inputField);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> query(inputField.getText()));
        inputField.setOnAction(e -> query(inputField.getText()));
        //execute same action on pressing enter in textbox
        input_bar.getChildren().add(submitButton);

        //Finishing up
        scene.getStylesheets().add("style.css");
        //Adding a custom stylesheet to the scene.
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> exitHandler());
        //To handle user pressing close

        //To position GUI on screen
        Rectangle2D screenModel = Screen.getPrimary().getVisualBounds();
        //System.out.println("SIZE: W:" + screenModel.getWidth() + "   H: " + screenModel.getHeight());
        primaryStage.setX((screenModel.getWidth() - SCENE_W - 20)); //primaryStage.getWidth()) - 10);
        primaryStage.setY((screenModel.getHeight() - SCENE_H - 50)); //primaryStage.getHeight()) - 10);



        //Add tray icon - this will minimise and maximise the application to the tray
        URL resource = getClass().getResource("/img_mike_logo.png");
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(resource);
        try {
            icon = new TrayIcon(image);
            icon.setImageAutoSize(true);
            tray = SystemTray.getSystemTray();
            tray.add(icon);
            icon.setToolTip("Mike"); //text that shows when the user hovers over the tray icon
            icon.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        public void run() { //required so that hide()/show() can run on the appropriate javaFX thread, rather than the current AWT
                            if (minimised) {
                                primaryStage.show();
                                primaryStage.toFront(); //added to ensure it shows in front of other windows when called upon`
                                minimised = false;
                            } else {
                                primaryStage.hide();
                                minimised = true;
                            }
                        }
                    });
                }
            });
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added. It may not be supported by your system. ");
        }

        addResponse(new Response("Welcome back, here is today's summary:"));
        getResponse(new KeyboardInput("Stock Summary"));
        primaryStage.show();
        primaryStage.setWidth(SCENE_W+1);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                //System.out.println("Refreshing stocks...");
                Response rp = cbot.executeAction(new KeyboardInput("check summary updates"));
                if (rp.getChangeIcon()){
                    setRedIcons();
                    System.out.println("There is a new change");
                } else {
                    System.out.println("There are no new changes");
                }
            }
        }
        ,CHECK_NEWS_TIME, CHECK_NEWS_TIME);
    }


    private HBox setupStatusBar(HBox hbox) {
        hbox.setPadding(new Insets(GLOBAL_PADDING, GLOBAL_PADDING, GLOBAL_PADDING, GLOBAL_PADDING));
        hbox.getStyleClass().add("status-bar");
        hbox.setPrefSize(SCENE_W, STATUS_BAR_H);
        hbox.setSpacing(10);
        return hbox;
    }

    private void setRedIcons() {
        URL resource = getClass().getResource("/img_mike_logo_red.png");
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(resource);
        mikeLogo.setImage(new javafx.scene.image.Image("/img_mike_logo_red.png"));
        logoTooltip.setText("You have alerts, click here!");
        icon.setImage(image);
        red = true;
    }

    private void setBlueIcons() {
        URL resource = getClass().getResource("/img_mike_logo.png");
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(resource);
        mikeLogo.setImage(new javafx.scene.image.Image("/img_mike_logo.png"));
        logoTooltip.setText("Mike!");
        icon.setImage(image);
        red = false;
    }

    /**
    * Handles a query from the input field, calling appropriate methods.
    * @param query The query input for processing.
    */
    public void query(String query) {
        //Sends query for processing
        inputField.clear();
        if (query.length() != 0) {
            UserInput uinput = new KeyboardInput(query);
            addUserMessage(query);
            getResponse(uinput);
        }
    }

    private void getResponse(UserInput uinput) {
        Task task = new Task<Void>() {
            @Override
            protected Void call() {
                Response rp = cbot.executeAction(uinput);
                Platform.runLater(new Runnable() {
                    public void run() {
                        addResponse(rp);
                    }
                });
                if (!muted) {
                    String text = rp.getTextToDisplay();
                    if (text.length() <= 200) {
                        tts.textToSpeech(text);
                    }
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void addUserMessage(String message) {
        logMessage(message, "user");
    }

    private void addResponse(Response rp) {

        String text = rp.getTextToDisplay();
        if (rp.getChangeIcon()) {
            setRedIcons();
        }
        if (text.isEmpty() == false)
            logMessage(text, "chatBot");

        ArrayList<Article> news = rp.getNews();
        if (news != null && news.size() != 0) {
            if (rp.getTextToDisplay().isEmpty()) { //i.e response contains just news
                logNews(news, false);
            } else { //i.e. both news and text are included, thus news is part of stock summary
                logNews(news, true);
            }

        } else {
            System.out.print("No news included in response. Reason: ");
            if (news == null) {
                System.out.println("NULL");
            } else if (news.size() == 0) {
                System.out.println("EMPTY");
            } else {
                System.out.println("WEIRD REASON");
            }
        }
    }

    private void logMessage(String message, String author) {

        Message newMsg;
        Label newLabel;
        HBox newHBox;

        newMsg = new Message(message, author);
        newLabel = new Label(message);
        newLabel.setWrapText(true);
        newLabel.setMaxWidth(primaryStage.getWidth() * 0.6);
        if (message.equals("Sorry, I cannot understand your query.") || message.equals("Apologies, that stock or sector does not exist on the FTSE 100. Mike does not know the answer. ")) {
            newLabel.setStyle("-fx-font-weight: bold");
        }

        newHBox = new HBox();
        newHBox.getChildren().add(newLabel);

        if (author == "user") {
            newHBox.setAlignment(Pos.CENTER_RIGHT);
            newLabel.getStyleClass().add("feed-message");
        } else if (author == "chatBot") {
            newHBox.setAlignment(Pos.CENTER_LEFT);
            newLabel.getStyleClass().add("feed-response");
        }

        feed_message.add(newMsg);
        feed_data.add(newLabel);
        feed_data_holder.add(newHBox);
        feed.getChildren().add(newHBox);
        numberOfMessages++;
    }

    private void logNews(ArrayList<Article> news, boolean AIgenerated) {

        for (Article a : news) {

            Message newMessage = new Message(a.getTitle(), "chatBot");
            feed_message.add(newMessage);
            Message newMessage1 = new Message(a.getDescription(), "chatBot");
            feed_message.add(newMessage1);
            Message newMessage2 = new Message("Published "+a.getPrettyPublicationDate(), "chatBot");
            feed_message.add(newMessage2);

            Hyperlink link = new Hyperlink();
            link.setText(a.getTitle());
            link.setWrapText(true);
            link.setMaxWidth(primaryStage.getWidth());
            link.getStyleClass().add("feed-response-article-title");

            link.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
                @Override
                public void handle(javafx.event.ActionEvent e) {
                    try {
                        desktop.browse(new URL(a.getLink()).toURI());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            Label description = new Label(a.getDescription());
            description.setText(a.getDescription());
            description.setWrapText(true);
            description.setMaxWidth(primaryStage.getWidth());
            description.getStyleClass().add("feed-response-article-description");

            Label date;
            if (AIgenerated) {
                date = new Label("AI suggested article | Published " + a.getPrettyPublicationDate());
            } else {
                date = new Label("User requested article | Published " + a.getPrettyPublicationDate());
            }
            date.setWrapText(true);
            date.setMaxWidth(primaryStage.getWidth());
            date.getStyleClass().add("feed-response-article-date");
            date.setAlignment(Pos.CENTER_RIGHT);

            feed_data.add(link); //needed for dynamic resizing
            feed_data.add(description);
            feed_data.add(date);

            VBox newVBox = new VBox();
            newVBox.getChildren().add(link);
            newVBox.getChildren().add(description);
            newVBox.getChildren().add(date);
            newVBox.setAlignment(Pos.CENTER_LEFT);
            feed_data_holder.add(newVBox);
            feed.getChildren().add(newVBox);
            numberOfMessages = numberOfMessages + 3;
        }

    }

    private void exitHandler() {
        //System.out.println("Program exited cleanly.");
        //TODO remove tray icon
        tray.remove(icon);
        System.exit(0);
    }
}

class CheckUpdates extends TimerTask {
    private IChatBot cbot;
    public CheckUpdates(ChatBot cbot){
            this.cbot = cbot;
        }

    public void run() {
        //System.out.println("Refreshing stocks...");
        Response rp = cbot.executeAction(new KeyboardInput("check summary updates"));
        System.out.println(rp.getChangeIcon());
        if (rp.getChangeIcon()){

        }
        //System.out.println("Refreshed successfully...");

    }
}
