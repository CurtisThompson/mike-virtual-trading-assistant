import java.util.ArrayList;

/**
 * Created by Akash on 13/02/2018.
 */
public class Response {
    private String text;
    private ArrayList<Article> news;
    private boolean changeIcon = false;

    public Response(){
        this.text = "";
        this.news = null;
    }

    public Response(String text) {
    	this.text = text;
    	this.news = null; //Set to null to indicate no data is included in the particular response object. 
    }

    public Response(ArrayList<Article> news) {
    	this.text = ""; //Set to null to indicate no data is included in the particular response object.
    	this.news = news;
    }

    public Response(boolean changeIcon ) {
        this.text = "";
        this.news = null; //Set to null to indicate no data is included in the particular response object.
        this.changeIcon = changeIcon;
    }

    public Response(ArrayList<Article> news, boolean changeIcon) {
        this.text = ""; //Set to null to indicate no data is included in the particular response object.
        this.news = news;
        this.changeIcon = changeIcon;
    }

    public Response(String text, ArrayList<Article> news, boolean changeIcon) {
        this.text = text;
        this.news = news;
        this.changeIcon = changeIcon;
    }

    public Response(String text, ArrayList<Article> news) {
    	this.text = text;
    	this.news = news;
    }

    public String getText() {
        return text;
    }


    public String getTextToDisplay() {
        return this.getText();
    }

    public ArrayList<Article> getNews() {
        return news;
    }

    public boolean getChangeIcon() {
        return changeIcon;
    }

    public void add(Response r) {
        this.text += (text == "") ? (r.getText()) : ("\n" + r.getText());
        if (this.news == null) {
            this.news = r.getNews();
        } else {
            this.news.addAll(r.getNews());
        }
    }
}
