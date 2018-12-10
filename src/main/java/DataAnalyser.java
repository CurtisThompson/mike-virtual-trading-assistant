import java.util.ArrayList;

/**
 * Interface defining methods required for a class to provide AI functionality, both for queries and stock analysis
 */
public interface DataAnalyser {

    /**
     * To get a list of the most queried stocks over the last x days
     * @param days no. of days of queries to search
     * @return list of favourite stocks, sorted by "most common first"
     */
    ArrayList<Stock> favouriteStocks(int days);

    /**
     * To get a list of the most queried sectors over the last x days.
     * @param days no. of days of queries to search.
     * @return list of favourite sectors, sorted by "most common first"
     */
    ArrayList<Sector> favouriteSectors(int days);

    //TODO: correct these return types to reflect the type/scale of change found...
    //TODO output of these methods should be sorted in some way, possibly/ideally biggest change first
    ArrayList<Stock> majorStockChanges(int days);

    ArrayList<Sector> majorSectorChanges(int days);


}
