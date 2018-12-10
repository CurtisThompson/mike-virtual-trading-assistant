/**
 * Interface controlling news feeds about stocks and sectors
 */
public interface NewsGetter {

    /**
     * Gets current news information given a stock name/ticker
     * @param name the name or ticker of the company
     * @return NewsFeed object containing a list of articles and some other information.
     */
    public NewsFeed getStockNews(String name);

    /**
     * Gets current news information about a given sector
     * @param name the name sector
     * @return NewsFeed object containing a list of articles and some other information.
     */
    public NewsFeed getSectorNews(String name);
}
