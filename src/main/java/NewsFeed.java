import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Stores information relevant to a news feed about a particular company sector.
 */
public class NewsFeed {
    private String feedLink;

    public ArrayList<Article> getArticles() {
        return articles;
    }

    private ArrayList<Article> articles;
    private Date lastRefreshed;

    /**
     * Constructor for making a custom NewsFeed Object. 
     * A custom NewsFeed will be needed for the stockSummary ('daily digest'), such that articles from multiple stocks or sectors may be passed to the GUI (as part of a Response object) for display. 
     * 
     * @param articles Custom-constructed ArrayList of Articles. 
     */
    public NewsFeed(ArrayList<Article> articles) {
        this();
        this.articles = articles;
    }

    public NewsFeed(String feedLink) {
        this();
        this.feedLink = feedLink;
    }

    public NewsFeed(String feedLink, ArrayList<Article> articles) {
        this();
        this.articles = articles;
        this.feedLink = feedLink;
    }

    /**
     * Default constructor. 
     */
    public NewsFeed() {
    	this.articles = new ArrayList<Article>();
        this.feedLink = "";
        this.lastRefreshed = new Date();
    }

    /**
     * Method to add a new article to a news feed.
     */
    public void addArticle(Article news) {
    	articles.add(news);
    }

    public void addArticles(List<Article> articles){
        this.articles.addAll(articles);
    }


    //TODO Write this method
    public ArrayList<Article> refreshArticles(){

        return new ArrayList<Article>();
    }

}
