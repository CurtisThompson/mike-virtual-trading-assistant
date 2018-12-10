/**
 * Interface for managing data addition to the database.
 */
public interface DBManager {

    /**
     * Adds a new stock/company to the database.
     * @param name The name of the stock/company to add.
     * @param sector The name of the sector the stock is a part of
     */
    public void addStock(String ticker, String name, String sector);

    /**
     * Adds the current information about a particular stock: value, price change etc to the database.
     * @param stock the stock info to add: Stock class
     */
    public void addStockHistory(Stock stock);

    /**
     * Records that a stock has been queried at a particular time.
     * @param stockName The name of the stock that has been queried.
     */
    public void addStockQuery(String stockName);

    /**
     * Adds a new sector to the database.
     * @param name the name of the sector to add.
     */
    public void addSector(String name);

}
