import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Akash on 01/02/2018.
 * Modified by Elliot on 12/02/2018.
 */
public class Article {
    private String title;
    private Date publicationDate;
    private String link;
    private String description;
    private ArrayList<String> tags;

    public Article(String title, Date publicationDate, String link, String description){
        this.title = title;
        this.publicationDate = publicationDate;
        this.link = link;
        this.description = description;
        this.tags = new ArrayList<String>(); //not yet utilised
    }

    public String getTitle() {
        return title;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public String getPrettyPublicationDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd\\MM\\yyyy");
        return dateFormat.format(this.publicationDate);
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        if (description.length() <= 200){
            return description;
        } else {
            return description.substring(0, 200) + "...";
        }
    }

    public String getFullDescription() {
        return description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String toString(){
        return "Title : " + title + "\n"+
                "Desc : " + description + "\n"+
                "Link : "+link + "\n"+
                "Date : "+ publicationDate;
    }
}
