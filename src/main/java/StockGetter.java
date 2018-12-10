import java.util.ArrayList;
import java.util.Date;

/**
 * Interface for getting stock data
 */
public interface StockGetter {


    /**
     * A method that will get stock information about a particular company.
     * @param name The name OR ticker of the company for which stocks are required.
     * @return A Stock class with information about the companies stock.
     */
    public Stock getStock(String name);

    /**
     * A method that will get stock information about a particular company, given a date.
     * @param name The name OR ticker of the company for which stocks are required.
     * @param date The date from which to get the stock info
     * @return A Stock class with information about the companies stock.
     */
    public Stock getStock(String name, Date date);


    /**
     * A method that will get stock information about a particular sector.
     * @param name The name of the sector.
     * @return A Sector class with information about sector.
     */
    public Sector getSector(String name);

    /**
     * A method that will get stock information about a particular sector.
     * @param name The name of the sector.
     * @param date The date from which to get info.
     * @return A Sector class with information about sector.
     */
    public Sector getSector(String name, Date date);

    /**
     * Gets all the companies from a particular sector
     * @param sectorName the name of the sector
     * @return a list of tickers of stocks from that sector
     */
    public ArrayList<String> getSectorStocks(String sectorName);

    /**
     * Gets the ticker of a particular stock
     * @param stockName The stock name
     * @return the stock's ticker
     */
    public String getTicker(String stockName);
    

    /**
     * A method that retrieves the latest stock data and adds to the database
     */
    public void refresh();
}
