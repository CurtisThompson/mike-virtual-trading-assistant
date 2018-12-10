import java.lang.Math;
import java.lang.String;

import java.util.*;
import java.text.DecimalFormat;

import org.joda.time.DateTime;
import org.json.*;



/**
 * Performs the majority of back end processing and communicates between GUI (front end) and data stores.
 */
public class ChatBot implements IChatBot {

    private NLP nlp;
    private StockGetter stockGetter;
    private NewsGetter newsGetter;
    private DataAnalyser analyser;
    private DBManager db;
    DecimalFormat format = new DecimalFormat("###,###,###,###.00");

    private HashSet<String> foundMajorChanges = new HashSet<>();
    Timer timer;

    public ChatBot() {
        this.nlp = new NLPRecast();
        DatabaseController dbController = new DatabaseController();
        this.stockGetter = dbController;
        this.newsGetter = new YahooRSSGetter();
        this.analyser = dbController;
        this.db = dbController;

        timer = new Timer();
        timer.schedule(new RefreshStocks(this), 0, 15*60*1000);
    }

    /**
     * Parses JSON object and calls appropriate methods in order to facilitate the query. Key method of this class, and arguably of Mike.
     */
    public Response executeAction(UserInput input) {
        /*
        JSON schema:

        {
          "uuid": "6fec4c80-7de4-4647-ab06-dfd7d4edffc8",
          "source": "Experian price",
          "intents": [
            {
              "slug": "stockprice",
              "confidence": 0.94
            }
          ],
          "act": "assert",
          "type": null,
          "sentiment": "neutral",
          "entities": {
            "organisation": [
              {
                "value": "experian",
                "raw": "Experian",
                "confidence": 0.73
              }
            ]
          },
          "language": "en",
          "processing_language": "en",
          "version": "2.11.0",
          "timestamp": "2018-02-23T17:16:53.990642+00:00",
          "status": 200
        }
         */
        Response response = new Response();

        // Exact strings produced by the GUI
        if (input.getText().equals("stock summary updates")) {
            return getStockSummaryUpdates();
        } else if (input.getText().equals("check summary updates")) {
            return checkStockSummaryUpdates();
        } else {
            String jsonString = nlp.determineIntent(input);
            JSONObject obj = new JSONObject(jsonString).getJSONObject("results");

            String intent = getIntent(obj);

            switch (intent) {
                case "stockprice": {
                    List<String> companies = getCompanies(obj);
                    List<String> sectors = getSectors(obj);
                    Date date = getDate(obj);
                    HashSet<String> foundStockNames = new HashSet<>();
                    //System.out.println(companies);
                    //System.out.println(sectors);
                    for (String company : companies) {
                        //System.out.println("Getting the price of " + company);
                        Stock stock = stockGetter.getStock(company);
                        if (stock != null && !foundStockNames.contains(stock.getName())) {
                            db.addStockQuery(stock.getName());
                            if (input.getText().toLowerCase().contains("tomorrow")) response.add(futurePrice(stock));
                            else response.add(currentPrice(stock));
                            foundStockNames.add(stock.getName());
                        }
                    }

                    for (String s : sectors) {
                        //System.out.println("Getting the price of " + s);
                        Sector sector = stockGetter.getSector(s);
                        if (input.getText().toLowerCase().contains("tomorrow")) response.add(futurePrice(sector));
                        else response.add(currentPrice(sector));
                    }
                    break;
                }

                case "stockchange": {
                    List<String> companies = getCompanies(obj);
                    List<String> sectors = getSectors(obj);
                    Date date = getDate(obj);
                    HashSet<String> foundStockNames = new HashSet<>();
                    String text = getText(obj);
                    for (String company : companies) {
                        //System.out.println("Getting the change of " + company);
                        Stock stock = stockGetter.getStock(company);
                        if (stock != null && !foundStockNames.contains(stock.getName())) {
                            db.addStockQuery(stock.getName());
                            if (text.contains("absolute") || text.toLowerCase().contains("abs")) {
                                response.add(priceChange(stock, false));
                            } else {
                                response.add(percentageChange(stock)); // TODO deal with 'trend' type questions
                            }
                            foundStockNames.add(stock.getName());
                        }
                    }
                    for (String s : sectors) {
                        //System.out.println("Getting the change of " + s);
                        Sector sector = stockGetter.getSector(s);
                        response.add(percentageChange(sector)); // TODO deal with 'trend' type questions
                    }
                    break;
                }

                case "stocksummary": {
                    response.add(stockSummary());
                    break;
                }

                case "stocknews": {
                    List<String> companies = getCompanies(obj);
                    List<String> sectors = getSectors(obj);
                    HashSet<String> foundStockNames = new HashSet<>();
                    for (String company : companies) {
                        //System.out.println("Getting news for " + company);
                        Stock stock = stockGetter.getStock(company);
                        if (stock != null && !foundStockNames.contains(stock.getName())) {
                            db.addStockQuery(stock.getName());
                            response.add(stockNews(stock));
                            foundStockNames.add(stock.getName());
                        }
                    }
                    for (String s : sectors) {
                        //System.out.println("Getting news for " + s);
                        Sector sector = stockGetter.getSector(s);
                        response.add(sectorNews(sector));
                    }
                    break;
                }

                default: {
                    response.add(new Response("Sorry, I cannot understand your query."));
                }
            }

            if (response.getText().isEmpty() && response.getNews() == null) {
                response.add(new Response("Apologies, that stock or sector does not exist on the FTSE 100. Mike does not know the answer. "));
            }
        }

        return response;
    }

    private String getIntent(JSONObject json) {
        //System.out.println(json);
        try {
            return json.getJSONArray("intents").getJSONObject(0).getString("slug");
        } catch (org.json.JSONException e) {
            return "";
        }
    }

    private List<String> getCompanies(JSONObject json) {
        List<String> companies = new ArrayList<>();
        JSONArray orgs;
        try {
            orgs = json.getJSONObject("entities").getJSONArray("organization");
            companies.addAll(getCompanies(orgs));
        } catch (org.json.JSONException e) { }
        try {
            orgs = json.getJSONObject("entities").getJSONArray("organisation");
            companies.addAll(getCompanies(orgs));
        } catch (org.json.JSONException f) { }
        try {
            orgs = json.getJSONObject("entities").getJSONArray("ticker");
            companies.addAll(getCompanies(orgs));
        } catch (org.json.JSONException g) { }
        return companies;
    }

    private List<String> getCompanies(JSONArray orgs){
        List<String> companies = new ArrayList<>();
        for (int i = 0; i < orgs.length(); i++){
            try {
                companies.add(orgs.getJSONObject(i).getString("value"));
            } catch(org.json.JSONException e){
                companies.add(orgs.getJSONObject(i).getString("raw"));
            }
        }
        return companies;
    }

    private Date getDate(JSONObject json){
            try {
                String date = json.getJSONObject("entities").getJSONArray("datetime")
                        .getJSONObject(0).getString("iso");
                System.out.println(date);
                Date dt = new DateTime(date).toDate() ;
                System.out.println(dt);
                return dt;
            } catch (org.json.JSONException e) { }

            return new Date();
    }

    private List<String> getSectors(JSONObject json){
        List<String> sectors = new ArrayList<>();
        JSONArray orgs;
        try {
            orgs = json.getJSONObject("entities").getJSONArray("sector");
        } catch (org.json.JSONException e) {
            return sectors;
        }
        for (int i = 0; i < orgs.length(); i++){
            try {
                sectors.add(orgs.getJSONObject(i).getString("value"));
            } catch(org.json.JSONException e){
                sectors.add(orgs.getJSONObject(i).getString("raw"));
            }
        }
        return sectors;
    }

    private String getText(JSONObject json) {
        return json.getString("source");
    }

    /**
     * Generates a report detailing
     * - Favourite stocks
     * - Favourite Sectors
     * - Any major changes in stocks or sectors.
     * The report may also include NewsArticles relevant to the favourite stocks and major changes.
     * This report will be returned to the GUI as a Response object, which the GUI will parse and display.
     *
     * @return Response object containing a custom String and a custom NewsFeed object.
     */
    public Response stockSummary() {
        /*
    	For display by GUI:
    	1 - Find X favourite stocks over past N days
    	2 - Find Y favourite sectors over past N days
    	3 - Find any major changes in sectors or stocks
		4 - Find 1 news article on each favourite stock that is a major changer....
		5 - Construct Response for GUI - all text will be in a single string with line breaks, and any news articles returned will be stored as a NewsFeed within Response.
		*/

        final int X = 3;
        final int Y = 2;
        final int N = 3;
        final int THRESHOLD = 5;


        //--1--
        ArrayList<Stock> favouriteStocks = analyser.favouriteStocks(N);

        //--2---
        ArrayList<Sector> favouriteSectors = analyser.favouriteSectors(N);

        //--3--

        ArrayList<Stock> majorStockChanges = analyser.majorStockChanges(N);
        ArrayList<Sector> majorSectorChanges = analyser.majorSectorChanges(N);

        //--4--
        //To construct return NewsFeed:
		/*
		for majorChange
			for favourite
				if majorChange.name == favourite.name
					Add 1st article from stock's NewsFeed to custom NewsFeed for stockSummary

		*/
        NewsFeed stockSummaryNews = new NewsFeed();
        System.out.println("Number of major changers: " + majorStockChanges.size()   );
        System.out.println("Number of favourites: " + favouriteStocks.size());
        for (int majorChangeIndex = 0; majorChangeIndex < majorStockChanges.size(); majorChangeIndex++) {
            for (int favouriteIndex = 0; favouriteIndex < favouriteStocks.size() && favouriteIndex < THRESHOLD; favouriteIndex++) {
                System.out.println("Is " + majorStockChanges.get(majorChangeIndex).getName().toUpperCase() + " the same as " + favouriteStocks.get(favouriteIndex).getName().toUpperCase());
                if (majorStockChanges.get(majorChangeIndex).getName().toUpperCase().equals( favouriteStocks.get(favouriteIndex).getName().toUpperCase()) ){ //if stock is both a favourite and major changer, add it to the list
                    NewsFeed news = newsGetter.getStockNews(favouriteStocks.get(favouriteIndex).getName());
                    List<Article> articles = news.getArticles();
                    if (articles.size() > 0) {
                        stockSummaryNews.addArticle(articles.get(0));
                        System.out.println("yes, added news article");
                    }
                }
            }
        }



        // Constructing return String:
        String stockSummary = ""; //XXX (add) Something to indicate that the following is the stock summary - perhaps the GUI could infer than any Response with both attributes filled is a stock summary, or we could add a flag to Response.

        // Add the price changes for the favourite stocks to the stock summary
        for (int i = 0; i < X; i++) {
            Stock a = favouriteStocks.get(i);
            if (a.getPriceChange() >= 0)
                stockSummary += a.getName() + " is at " + pretty(a.getCurrentPrice()) + " : up " + pretty(a.getPriceChange()) + " (" + pretty(a.getPercentChange()) + "%) \n";
            else
                stockSummary += a.getName() + " is at " + pretty(a.getCurrentPrice()) + " : down " + pretty(Math.abs(a.getPriceChange())) + " (" + pretty(Math.abs(a.getPercentChange())) + "%) \n";
        }
        stockSummary += "\n";

        // Add the price changes for favourite sectors to the stock summary
        for (int i = 0; i < Y; i++) {
            Sector a = favouriteSectors.get(i);
            if (a.getPercentChange() >= 0)
                stockSummary += a.getName() + " is at " + pretty(a.getCurrentPrice()) + " : up "+ pretty(a.getPercentChange()) + "% \n";
            else
                stockSummary += a.getName() + " is at " + pretty(a.getCurrentPrice()) + " : down " + pretty(Math.abs(a.getPercentChange())) + "% \n";
        }
        stockSummary += "\n";


        stockSummary += checkMajorChanges().getText();

        stockSummary += "\nAny urgent news articles follow. \n";
        // Combining both in to a response object and returning:
        System.out.println(foundMajorChanges);
        return new Response(stockSummary, stockSummaryNews.getArticles());
    }

    private Response checkStockSummaryUpdates() {
        final int N = 5;
        final int THRESHOLD = 5;
        List<Stock> favouriteStocks = analyser.favouriteStocks(N).subList(0, THRESHOLD);
        List<Stock> majorStockChanges = analyser.majorStockChanges(N);

        System.out.print("Favourites Stocks: ");
        System.out.println(favouriteStocks);

        System.out.print("Major Stocks: ");
        System.out.println(majorStockChanges);

        for (Stock majorStock : majorStockChanges) {
            for (Stock favouriteStock : favouriteStocks) {
                if (majorStock.getName().equals(favouriteStock.getName()) && !foundMajorChanges.contains(favouriteStock.getName())){
                    System.out.println("Found: " + majorStock.getName());
                    return new Response(true);
                }
            }
        }
        return new Response(false);
    }

    private Response getStockSummaryUpdates() {
        final int N = 5;
        final int THRESHOLD = 5;
        List<Stock> favouriteStocks = analyser.favouriteStocks(N).subList(0,THRESHOLD);
        List<Stock> majorStockChanges = analyser.majorStockChanges(N);
        NewsFeed stockSummaryNews = new NewsFeed();

        String stockSummary = "Update!\n" +
                "There have been major changes in the following stocks and sectors in the last " + N + " days: \n";
        for (Stock majorStock : majorStockChanges) {
            for (Stock favouriteStock : favouriteStocks) {
                if (majorStock.getName().equals(favouriteStock.getName()) && !foundMajorChanges.contains(favouriteStock.getName())){
                    foundMajorChanges.add(favouriteStock.getName());
                    if (favouriteStock.getPriceChange() >= 0)
                        stockSummary += favouriteStock.getName() + " is at " + pretty(favouriteStock.getCurrentPrice()) + " : up " + pretty(majorStock.getPercentChange()) + "% \n";
                    else
                        stockSummary += favouriteStock.getName() + " is at " + pretty(favouriteStock.getCurrentPrice()) + " : down " + pretty(Math.abs(favouriteStock.getPercentChange())) + "% \n";
                    NewsFeed news = newsGetter.getStockNews(favouriteStock.getName());
                    List<Article> articles = news.getArticles();
                    if (articles.size() > 0) {
                        stockSummaryNews.addArticle(articles.get(0));
                    }
                }
            }
        }
        return new Response(stockSummary, stockSummaryNews.getArticles());
    }

    /**
     * Checks which stocks and sectors have had major price changes in the recent history
     *
     * @return Response object containing the names of stocks and sectors with major changes
     */
    public Response checkMajorChanges() {
        // Get major changes from the last N days
        final int N = 7;

        // Get the list of stocks and sectors with major changes
        ArrayList<Stock> majorStockChanges = analyser.majorStockChanges(N);
        ArrayList<Sector> majorSectorChanges = analyser.majorSectorChanges(N);
        String responseText;
        // The return string for major changes
        if (majorSectorChanges.size() > 0 || majorStockChanges.size() > 0) {
            responseText = "There have been major changes in the following stocks and sectors in the last " + N + " days: \n";

            // Major changes to stocks
            for (int i = 0; i < majorStockChanges.size(); i++) {
                Stock s = majorStockChanges.get(i);
                responseText += s.getName() + "(" + pretty(s.getPercentChange()) + "%)\n";
                foundMajorChanges.add(s.getName());
            }

            // Major changes to sectors
            for (int i = 0; i < majorSectorChanges.size(); i++) {
                Sector sec = majorSectorChanges.get(i);
                responseText += sec.getName() + "(" + pretty(sec.getPercentChange()) + "%)\n";
                foundMajorChanges.add(sec.getName());
            }
        } else {
            responseText = "There have been no major changes for stocks or sectors in the last "+ N +"days.";
        }
        return new Response(responseText);
    }

    /**
     * Retrieves the latest stock data and adds it to the database
     */
    public void updateStockHistory() {
        stockGetter.refresh();
    }

    /**
     * Calculates the price change for a given stock
     *
     * @param stock The stock to check for the price change
     * @param trend ...
     * @return Response object containing the price change for a given stock
     */
    public Response priceChange(Stock stock, boolean trend) {
        //XXX trend? ### if 'trend' is true then it wasn't an opinionated answer instead of just values
        double priceChange = stock.getPriceChange();
        //TODO include current price in response too?
        String stockName = stock.getName();

        String responseText;
        if (priceChange > 0) {
            responseText = "The price of " + stockName + " has increased by \u00A3" + pretty(priceChange) + ". ";
        } else if (priceChange < 0) {
            responseText = "The price of " + stockName + " has decreased by \u00A3" + pretty(Math.abs(priceChange)) + ". ";
        } else { //if 0
            responseText = "The price of " + stockName + " has not changed today. ";
        }

        return new Response(responseText);
    }

    /**
     * Retrieves the price percentage change of a given stock
     *
     * @param stock The stock to check for the current price
     * @return Response object containing the percentage change of the stock
     */
    public Response percentageChange(Stock stock) {
        double percentageChange = stock.getPercentChange();
        double absoluteChange = stock.getPriceChange();
        String stockName = stock.getName();

        String responseText = "";
        //System.out.println("\n\n");
        stock.printStr();
        if (percentageChange > 0) {
            responseText = stockName + " has increased \u00A3"+ absoluteChange + " ("  + pretty(percentageChange) + "% ).";
        } else if (percentageChange < 0) {
            responseText = stockName + " has decreased \u00A3"+ Math.abs(absoluteChange) + " ("  + pretty(percentageChange) + "% ).";
        } else { //if 0
            responseText = "The price of " + stockName + " has not changed today. ";
        }
        return new Response(responseText);
    }

    /**
     * Retrieves the current price of a given stock
     *
     * @param stock The stock to check for the current price
     * @return Response object containing the current price of the stock
     */
    public Response currentPrice(Stock stock) {
        double currentPrice = stock.getCurrentPrice();
        String stockName = stock.getName();

        String responseText = "The current price of " + stockName + " is \u00A3" + pretty(currentPrice) + ". ";

        return new Response(responseText);
    }

    public Response futurePrice(Stock stock) {
        double currentPrice = stock.getCurrentPrice();
        double percentChange = stock.getPercentChange();
        double futurePrice = currentPrice * (1 + (percentChange / 100));
        String stockName = stock.getName();

        String responseText = "The price of " + stockName + " is predicted to be \u00A3" + pretty(futurePrice) + ".";

        return new Response(responseText);
    }

    /**
     * Calculates the price change for a given sector
     *
     * @param sector The sector to check for the price change
     * @param trend  ...
     * @return Response object containing the price change for a given sector
     */
    public Response priceChange(Sector sector, boolean trend) {
        // Calculate the last price of the sector
        // TO DO calculate the price change
        double priceChange = 0;

        String sectorName = sector.getName();

        String responseText;
        if (priceChange > 0) {
            responseText = "The price of " + sectorName + " has increased by \u00A3" + pretty(priceChange) + ". ";
        } else if (priceChange < 0) {
            responseText = "The price of " + sectorName + " has decreased by \u00A3" + pretty(Math.abs(priceChange)) + ". ";
        } else { //if 0
            responseText = "The price of " + sectorName + " has not changed today. ";
        }
        return new Response(responseText);
    }

    /**
     * Retrieves the current price of a given sector
     *
     * @param sector The sector to check for current price
     * @return Response object containing the current price for a given sector
     */
    public Response currentPrice(Sector sector) {
        double currentPrice = sector.getCurrentValue();
        String sectorName = sector.getName();

        String responseText = "The current price of " + sectorName + " is \u00A3" + pretty(currentPrice) + ". ";

        return new Response(responseText);
    }

    public Response futurePrice(Sector sector) {
        double currentPrice = sector.getCurrentValue();
        double percentChange = sector.getPercentChange();
        double futurePrice = currentPrice * (1 + (percentChange / 100));
        String sectorName = sector.getName();

        String responseText = "The price of " + sectorName + " is predicted to be \u00A3" + pretty(futurePrice) + ".";

        return new Response(responseText);
    }

    /**
     * Retrieves the percentage change of a given sector
     *
     * @param sector The sector to check for current price
     * @return Response object containing the percentage change for a given sector
     */
    public Response percentageChange(Sector sector) {
        double percentChange = sector.getPercentChange();
        double absoluteChange = sector.getPriceChange();
        String sectorName = sector.getName();

        String responseText = "";
        if (percentChange > 0) {
            responseText = sectorName + " has increased \u00A3"+ pretty(absoluteChange) + " ("  + pretty(percentChange) + "% ).";
        } else if (percentChange < 0) {
            responseText = sectorName + " has decreased \u00A3"+ pretty(Math.abs(absoluteChange)) + " ("  + pretty(percentChange) + "% ).";
        } else { //if 0
            responseText = "The price of " + sectorName + " has not changed today. ";
        }

        return new Response(responseText);
    }

    /**
     * Retrieves the latest news for a given sector
     *
     * @param sector The sector to find latest news for
     * @return Response object containing the latest news for a given sector
     */
    public Response sectorNews(Sector sector) {
        NewsFeed sectorNews = newsGetter.getSectorNews(sector.getName());
        return new Response(sectorNews.getArticles());
    }

    /**
     * Retrieves the latest news for a given stock
     *
     * @param stock The stock to find latest news for
     * @return Response object containing the latest news for a given stock
     */
    public Response stockNews(Stock stock) {
        NewsFeed stockNews = newsGetter.getStockNews(stock.getName());
        // TO DO - DO WE NEED TO ONLY GET THE MOST RECENT X?
        return new Response(stockNews.getArticles());
    }


    private static String pretty(Number n){
        return String.format("%.2f", n);
    }

}


/**
 * Class used to schedule the updating of stock history
 */
class RefreshStocks extends TimerTask {

    ChatBot cbot;

    public RefreshStocks(ChatBot cbot){
        this.cbot = cbot;
    }

    public void run() {
        //System.out.println("Refreshing stocks...");
        cbot.updateStockHistory();
        //System.out.println("Refreshed successfully...");

    }
}
