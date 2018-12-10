import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class YahooRSSGetter implements NewsGetter {

    private static final String stockNewsURL = "http://finance.yahoo.com/rss/headline?s=";
    private static final String sectorNewsURL = "https://feeds.finance.yahoo.com/rss/2.0/headline?s=YHOO";

    private static final String endString = "&region=UK&lang=en-US";

    private static final int MAXARTICLES = 5;

    private StockGetter db = new DatabaseController();

    /**
     * parses a date in "EEE, d MMM yyyy HH:mm:ss Z" format to a Date object
     * @param stringDate a string in the given format
     * @return a date object, NOW if the date couldn't be parsed.
     */
    private static Date stringToDate(String stringDate){
        SimpleDateFormat f = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        try {
            return f.parse(stringDate);
        } catch (ParseException e) {
            // If it can't find a publication date, set it to NOW
            return new Date();
        }
    }

    private List<String> stocksToTickers(List<String> stocks) {
        List<String> tickers = new ArrayList<>();
        for (String s : stocks){
            tickers.add(db.getTicker(s) + ".L"); // Add .L onto a ticker to get the Yahoo version of it.
        }
        return tickers;
    }


    private NewsFeed getStocksNews(List<String> tickers){

        String tickersString = String.join(",", tickers);
        String url = "https://feeds.finance.yahoo.com/rss/2.0/headline?s="+tickersString+"&region=UK&lang=en-US";
        NewsFeed news = new NewsFeed(url);
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");

            for (int x = 0; x < Math.min(MAXARTICLES, nList.getLength()); x++) {
                Node nNode = nList.item(x);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String desc = eElement.getElementsByTagName("description").item(0).getTextContent();
                    String title = eElement.getElementsByTagName("title").item(0).getTextContent();
                    String link = eElement.getElementsByTagName("link").item(0).getTextContent();
                    Date publicationDate = stringToDate(eElement.getElementsByTagName("pubDate").item(0).getTextContent());
                    news.addArticle(new Article(title, publicationDate, link, desc));
                }
            }
            return news;
        } catch (Exception e) {
            // If can't parse feed, then return a NewsFeed with as many articles as possible.
            return news;
        }
    }

    public NewsFeed getStockNews(String stock){
        List<String> stocks = new ArrayList<>();
        stocks.add(stock);
        return getStocksNews(stocksToTickers(stocks));
    }

    /*TODO: find a proper rss feed for sector news articles.
        This is solved buy just adding multiple companies into the Yahoo api.
    */
    public NewsFeed getSectorNews(String sectorName){
        return getStocksNews(stocksToTickers(db.getSectorStocks(sectorName)));
    }

    public static void main(String[] args){
        YahooRSSGetter r = new YahooRSSGetter();
        List<String> tickers = new ArrayList<>();
        tickers.add("GFS.L");
        tickers.add("DLG.L");
        System.out.println(r.getStocksNews(tickers).getArticles());
    }

}
