import org.joda.time.DateTime;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.Date;

public class DatabaseController implements StockGetter, DBManager, DataAnalyser {
    
    private final double CONST_MAJOR_STOCK_BOUNDARY = 5.0;
    private final double CONST_MAJOR_SECTOR_BOUNDARY = 5.0;

/*  //Testing method:
	public static void main(String[] args){
		DatabaseController db = new DatabaseController();
		Date d1 = new Date();
       Date d2 = new DateTime("2018-03-03T02:36:14+00:00").toDate();
		Stock s = db.getStock("tesco", d1);
       System.out.println();
		Stock t = db.getStock("tesco", d2);

       System.out.println(d1);
       System.out.println(d2);

       s.printStr();
       t.printStr();
	}*/

    /**
     * Creates a connection to the stock/sector database
     * @return Connection to the database
     */
	public static Connection getConnection(){

		Connection conn = null;
		try {
			String url = "jdbc:sqlite:chatbot.db";
			conn = DriverManager.getConnection(url);  
			//System.out.println("Connection Established");
		} catch(SQLException e) {
            e.printStackTrace();
            //System.out.println("DB Connection failed");
            return null;
        }
		return conn;
	}

	/**
     * Adds a new stock/company to the database.
     * @param name The name of the stock/company to add.
     * @param sector The name of the sector the stock is a part of
     */
    public void addStock(String ticker, String name, String sector){
    	Connection conn = getConnection();

    	//CHECKS if the ticker already Exists:
        PreparedStatement stmt;
        try {
            String checkStock = "Select * FROM stock WHERE Ticker = ?";
            stmt = conn.prepareStatement(checkStock);
            stmt.setString(1, ticker);
            ResultSet stocks = stmt.executeQuery();

            if(stocks.next()) {
                //System.out.println("Ticker "+ ticker +" already exists");
                closeConn(conn);
                return;
            }
        } catch(SQLException e) {
            //System.out.println("Error checking");
        }


    	//Check if stock exists - THIS COULD BE OMMITTED???
    	try {
            String checkStock = "Select * FROM stock WHERE StockName = ?";
            stmt = conn.prepareStatement(checkStock);
            stmt.setString(1, name);
            ResultSet stocks = stmt.executeQuery();
            
    		if(stocks.next()) {
    			//System.out.println(name + " stock already exists");
                closeConn(conn);
    			return;
    		}
    	} catch(SQLException e) {
    		//System.out.println("Error checking");
    	}

    	//If sector does not exist, add the sector to sector table
        PreparedStatement secStmt = null;
    	try {
            String check = "Select * FROM sector WHERE SectorName = ?";
            secStmt = conn.prepareStatement(check);
            secStmt.setString(1, sector);
            ResultSet exists = secStmt.executeQuery();
        
    		if(!exists.next()) {
    			//System.out.println(sector + " sector does not exist");
    			addSector(sector);
    		}
    	} catch(SQLException e) {
    		//System.out.println("Error checking");
    	}

    	int sectorId = 1;

    	PreparedStatement secQStmt = null;
    	try{
            String sectorQ = "SELECT SectorId FROM sector WHERE SectorName = ?";
            secQStmt = conn.prepareStatement(sectorQ);
            secQStmt.setString(1, sector);
            ResultSet sectorIds = secQStmt.executeQuery();
        
    		if(sectorIds.next()){
    			sectorId = sectorIds.getInt(1);
    			//System.out.println("sectorid is : " + sectorId);
    		}
    		else {
    			//System.out.println("This sector is not in the FTSE 100");
    		}
    	} catch(SQLException e){
    		//System.out.println("Error selecting sectorId");
    	}

    	String query = "INSERT INTO stock VALUES(";
    	query += "NULL" + ", ";
    	query += "'" + ticker +"', ";
    	query += "'" + name + "', ";
    	query += sectorId + ")";
    	insert(conn, query);
        
        closeConn(conn);
    }


    /**
     * Adds a new sector to the database.
     * @param name the name of the sector to add.
     */
    public void addSector(String name){
    	
    	Connection conn = getConnection();

    	//This will check if the sector already exists, if it does it will not add anything.
    	/* ---- QUESTION - COULD METHOD BE OMMITED - dont think so!? */
        PreparedStatement checkStmt = null;
    	
    	try {
            String check = "Select * FROM sector WHERE SectorName = ?";
            checkStmt = conn.prepareStatement(check);
            checkStmt.setString(1, name);
            ResultSet exists = checkStmt.executeQuery();
    		
            if(exists.next()){
    			//System.out.println(name + " sector already exists");
                closeConn(conn);
    			return;
    		}
    	} catch(SQLException e){
    		//System.out.println("Error checking");
    	}

        PreparedStatement insertQ = null;
        try {
            String query = "INSERT INTO sector VALUES(NULL, ?)";
            insertQ = conn.prepareStatement(query);
            insertQ.setString(1, name);
            insertQ.executeUpdate();
        } catch(SQLException e){
    		//System.out.println("Insertion error");
    	}
        
        closeConn(conn);
    }
    
    /**
     * Adds the current information about a particular stock: value, price change etc to the database.
     * @param stock the stock info to add: Stock class
     */

    //QUESTION - If stock does not exist - add? Do not know the sector so ?
    public void addStockHistory(Stock stock){
    	Connection conn = getConnection();

    	// stockId - select from stock table - stock.getName
    	int stockId = 1;
        String stockName = stock.getName();
        PreparedStatement selStmt = null;
    	try {
            String selectId = "SELECT StockId FROM stock WHERE StockName = ?";
            selStmt = conn.prepareStatement(selectId);
            selStmt.setString(1, stockName);
            ResultSet ids = selStmt.executeQuery();
            
    		if(ids.next()){
    			stockId = ids.getInt(1);
    			//System.out.println("Stock id for " + stockName +" is: " + stockId);
    		}
    	} catch(SQLException e){
    		//System.out.println("Error selecting id, this stock does not exist");

    	}

    	// price - stock.getCurrentPrice()
    	double price = stock.getCurrentPrice();
    	// Percentage Change - stock.getPercentChange()
    	double percentageChange = stock.getPercentChange();
    	// Price Change - stock.getPriceChange()
    	double priceChange = stock.getPriceChange();
    	
        PreparedStatement insertStmt = null;
        try {
            String insertHist = "INSERT INTO stock_history (Id, StockId, Price, PercentageChange, PriceChange) VALUES(NULL, ?, ?, ?, ?)";
            insert(conn, insertHist);
            insertStmt = conn.prepareStatement(insertHist);
            insertStmt.setInt(1, stockId);
            insertStmt.setDouble(2, price);
            insertStmt.setDouble(3, percentageChange);
            insertStmt.setDouble(4, priceChange);
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            //System.out.println("Insertion error");
        }
        
        closeConn(conn);
    }

    /**
     * Records that a stock has been queried at a particular time.
     * @param stockName The name of the stock that has been queried.
     */
    public void addStockQuery(String stockName){
    	Connection conn = getConnection();
        PreparedStatement inStmt;
        try {
            String insertQuery = "INSERT INTO stock_queries (StockId) " +
                    "SELECT StockId FROM stock_tag WHERE stock_tag.Tag = ?";
            inStmt = conn.prepareStatement(insertQuery);
            inStmt.setString(1, stockName);
            inStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("Insertion error");
        }
        
        closeConn(conn);
    }

    /**
     * Runs an insert query on the given database connection
     * @param conn The connection to the database
     * @param query The query to be executed
     */
    private static void insert(Connection conn, String query) {
	    try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
			//System.out.println("Inserted");
			//System.out.println("success in inserting");
	    }catch(SQLException e) {
			//System.out.println("Could not insert");
            System.out.println(e.getMessage());
			return;
	    }
	}
    
    /**
     * Runs a select query on the given database connection
     * @param conn The connection to the database
     * @param query The query to be executed
     * @return The results of the select query as a ResultSet
     */
	private static ResultSet select(Connection conn, String query) {
		Statement stmt = null;
		ResultSet res = null;
		try {
			stmt = conn.createStatement();
			res = stmt.executeQuery(query);
			return res;
		} catch(SQLException e){
		    e.printStackTrace();
			//System.out.println("exception on select");
			return null;
		}
	}

	/**
     * A method that will get stock information about a particular company.
     * @param name The name/ticker of the company for which stocks are required.
     * @return A Stock class with information about the companies stock.
     */
    public Stock getStock(String name, Date date){
    	Connection conn = getConnection();
    	/*	1- Get relevant data from the database using sql queries, 
    		   selecting the record with the same name as that passed into the method
			2 - add this data to a stock class in Java
				You might need to add a constructor method to the stock object, not sure if it's there already*/

		String ticker = getTicker(name);
        double currentPrice = 0;
        double priceChange = 0;
		double percentageChange = 0;

        PreparedStatement selStmt = null;
        
        // If there has been an error, null is returned, but the conn must be closed first
        boolean error = true;
        
		try{
            String select = "SELECT sh.Price, sh.PercentageChange, sh.PriceChange, Stock.StockName, (strftime('%s',?) - strftime('%s', sh.TimeStamp)) as diff" +
                        " FROM stock_history sh INNER JOIN stock_tag s " +
                        " ON s.StockId = sh.StockId INNER JOIN Stock ON sh.stockId = Stock.stockId" +
                        " WHERE s.Tag = ?" +
                        " ORDER BY ABS(diff) ASC ";
            selStmt = conn.prepareStatement(select);
            selStmt.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
            selStmt.setString(2, name);
            ResultSet set = selStmt.executeQuery();


			if(set.next()){
				currentPrice = set.getDouble(1);
				percentageChange = set.getDouble(2);
				priceChange = set.getDouble(3);
				name = set.getString(4);
				error = false;
			}

		}catch(Exception e){
		    e.printStackTrace();
			//System.out.println("No info on this stock in stock_history");
		}
        //new stock class
        closeConn(conn);
        if (error) {
            return null;
        }
        return new Stock(ticker, name, currentPrice, priceChange, percentageChange);
    }

    /**
     * Overrided method for when the date is not provided
     * @param name
     * @return
     */
    public Stock getStock(String name) {
        return getStock(name, new Date());
    }

    /**
     * A method that will get stock information about a particular sector.
     * @param name The name of the sector.
     * @return A Sector class with information about sector.
     */
    public Sector getSector(String name, Date date){
        // Retrieve the stocks in the sector
        ArrayList<String> stocksNames = getSectorStocks(name);
        ArrayList<Stock> stocks = new ArrayList<Stock>();
        for (String sName : stocksNames) {
            stocks.add(getStock(sName, date));
        }
        
        // Calculate the current price and the old price of the sector
        double sectorPrice = 0;
        double sectorOldPrice = 0;
        for(Stock currStock : stocks) {
            sectorPrice += currStock.getCurrentPrice();
            sectorOldPrice += currStock.getPriceChange();
        }
        
        // Calculate the price percentage change for the sector
        double percentChange = calculateSectorPercentChange(name);
        
        // Return the sector
        Sector theSec = new Sector(name, sectorPrice, percentChange);
    	return theSec;
    }

    /**
     * Overrided method for when the date is not provided for getSector
     * @param name
     * @return the secor info for the given date
     */
    public Sector getSector(String name) {
        return getSector(name, new Date());
    }
    
    /**
     * Calculates the percent change for a given sector
     * @param name The name of the sector
     * @return The percent change for the sector
     */
    public double calculateSectorPercentChange(String name) {
        // Retrieve the stocks in the sector
        ArrayList<String> stocksNames = getSectorStocks(name);
        ArrayList<Stock> stocks = new ArrayList<Stock>();

        for (String stockName : stocksNames) {
            stocks.add(getStock(stockName));
        }
        
        // Calculate the current price and the old price of the sector
        double sectorPrice = 0;
        double sectorPriceChange = 0;
        for(Stock currStock : stocks) {
            sectorPrice += currStock.getCurrentPrice();
            sectorPriceChange += currStock.getPriceChange();
        }

        double sectorOldPrice = sectorPrice - sectorPriceChange;

        // Calculate the price percentage change for the sector
        double percentChange = 0;
        if (sectorOldPrice == 0) {
            percentChange = 0;
        } else {
            percentChange = ((sectorPrice / sectorOldPrice) - 1) * 100;
        }
        
        // Return the sector
    	return percentChange;
    }

    /**
     * Gets all the companies from a particular sector
     * @param sectorName the name of the sector
     * @return a list of tickers of stocks from that sector
     */
    public ArrayList<String> getSectorStocks(String sectorName){
        // Create a new database connection
        Connection conn = getConnection();
        
        PreparedStatement stmt = null;
        
        // Loop through each stock to add to the list
        ArrayList<String> stockList = new ArrayList<String>();
        try {
            // Create a query to get stocks from a sector
            String query = "SELECT s.StockName FROM stock s INNER JOIN sector se ON s.SectorId = se.SectorID WHERE se.SectorName = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, sectorName);
            ResultSet stocks = stmt.executeQuery();
        
            while(stocks.next()){
                stockList.add(stocks.getString(1));
            }
        } catch(Exception e){
            //System.out.println("No stocks for this sector");
        }
        
        closeConn(conn);
        return stockList;
    }

    /**
     * Gets the ticker of a particular stock
     * @param stockName The stock name
     * @return the stock's ticker
     */
    public String getTicker(String stockName) {
        // Create a new connectin to the db
        Connection conn = getConnection();
        
        PreparedStatement stmt = null; 
        
        // Retrieve the ticker from the database or print an error
        String ticker = "";
        try {
            // Create the ticker query
            String query = "SELECT Ticker FROM stock s INNER JOIN stock_tag t ON s.StockId = t.StockId" +
                    " WHERE Tag = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, stockName);
            ResultSet results = stmt.executeQuery();
        
    		if (results.next()){
    			ticker = results.getString(1);
    			//System.out.println("Ticker for " + stockName + " is: " + ticker);
                
    		}
    	} catch(Exception e){
    		//System.out.println("Error retrieving ticker, this stock does not exist.");
    	}
        
        // Return the ticker
        
        closeConn(conn);
        return ticker;
    }

    /**
     * A method that retrieves the latest stock data and adds to the database
     */
    public void refresh(){
        if (getLastRefreshed() > 15*60*1000) {
            WebScraper lse = new LSEScraper();
            List<Stock> stocks = lse.getStocks();
            System.out.println(stocks.size() + " stocks loaded from LSE");

            //System.out.println(stocks);
            Connection conn = getConnection();

            PreparedStatement stmt;
            for (Stock stock : stocks) {
                try {
                    String q = "INSERT INTO stock_history (id, stockId, Price, PercentageChange, PriceChange)" +
                            " SELECT NULL, stock_tag.StockId, ?, ?, ? FROM stock_tag WHERE tag = ?";
                    //System.out.println("Refreshing : " + stock.getName());

                    stmt = conn.prepareStatement(q);
                    stmt.setDouble(1, stock.getCurrentPrice());
                    stmt.setDouble(2, stock.getPercentChange());
                    stmt.setDouble(3, stock.getPriceChange());
                    stmt.setString(4, stock.getName());
                    stmt.executeUpdate();

                } catch (Exception e) {
                    e.printStackTrace();
                    stock.printStr();
                    System.err.println("Could not update stock!");
                }
            }
            closeConn(conn);
        } else {
            System.out.println("Already refreshed in last 15 mins");
        }
    }
    /**
     * To get a list of the most queried stocks over the last x days
     * @param days no. of days of queries to search
     * @return list of favourite stocks, sorted by "most common first"
     */
    public ArrayList<Stock> favouriteStocks(int days){

    	Connection conn = getConnection();
        String newDate = subtractDays(days);
		
		
		String selectStocks = "SELECT StockName, COUNT(stock_queries.StockId) Frequency FROM stock_queries INNER JOIN stock" +
                " ON stock_queries.stockId = stock.StockId WHERE (TimeStamp > '";
		selectStocks += newDate + "') GROUP BY stock_queries.StockId ORDER BY Frequency DESC"; //No limit is placed on favourites - this will be refined if need be when used. 
		ResultSet favStocks = select(conn, selectStocks);
    	ArrayList<Stock> stocks = new ArrayList<Stock>();
    	//STOCK - getStock
    	try{
			while(favStocks.next()){
				String stockName = favStocks.getString(1);
				stocks.add(getStock(stockName));
			}
		} catch (Exception e){
    	    e.printStackTrace();
			//System.out.println("Exception!");
		}
        closeConn(conn);

    	return stocks;
    }

    /**
     * To get a list of the most queried sectors over the last x days.
     * @param days no. of days of queries to search.
     * @return list of favourite sectors, sorted by "most common first"
     */
    public ArrayList<Sector> favouriteSectors(int days){
    	Connection conn = getConnection();

        String newDate = subtractDays(days);
        //System.out.println(newDate);

		ArrayList<Sector> sectors = new ArrayList<Sector>();
        
        PreparedStatement stmt = null;
    	try{
            String selectFavs = "SELECT sh.StockId, s.SectorId, count(SectorId) freq"; 
            selectFavs += " FROM stock_queries sh INNER JOIN stock s";
            selectFavs += " ON sh.StockId = s.StockId";
            selectFavs += " WHERE sh.TimeStamp > ";
            selectFavs += "? GROUP BY s.SectorId ORDER BY freq DESC Limit 5";
            stmt = conn.prepareStatement(selectFavs);
            stmt.setString(1, newDate);
            ResultSet favSectors = stmt.executeQuery();
            
    		while(favSectors.next()){
    			int sectorId = favSectors.getInt(2);
                PreparedStatement secStmt = null;
    			String q = "SELECT SectorName FROM sector WHERE SectorId = ?";
                secStmt = conn.prepareStatement(q);
                secStmt.setInt(1, sectorId);
    			ResultSet set = secStmt.executeQuery();
                
    			try{
    				if(set.next()){
    					String name = set.getString(1);
    					sectors.add(getSector(name));
    				}
    			}catch(Exception e){
    				//System.out.println("Inner exception");
    			}
    		}
    	}catch(Exception e){
    		//System.out.println("Exception!");
    	}


        closeConn(conn);

    	return sectors;
    }


	//TODO: correct these return types to reflect the type/scale of change found...
    //TODO output of these methods should be sorted in some way, possibly/ideally biggest change first
    public ArrayList<Stock> majorStockChanges(int days){
    	// Create a new connection to db
        Connection conn = getConnection();
        
        /*
         * Below is a working algorithm, but it does not do as intended
        // Query to get major stock changes
        String date = subtractDays(days);
        String query = "SELECT DISTINCT StockName FROM stock INNER JOIN stock_history ON stock.StockId = stock_history.StockId";
        query += " WHERE (stock_history.PercentageChange > " + CONST_MAJOR_STOCK_BOUNDARY;
        query += " OR stock_history.PercentageChange < -" + CONST_MAJOR_STOCK_BOUNDARY + ")";
        query += " AND stock_history.TimeStamp > '" + date + "'";
        query += " ORDER BY stock_history.PercentageChange DESC";
        
        ArrayList<Stock> mStocks = new ArrayList<Stock>();
        ResultSet majorStocks = select(conn, query);
        try {
            if (majorStocks.next()) {
                mStocks.add(getStock(majorStocks.getString(1)));
                System.out.println("Stock : " + majorStocks.getString(1) + " is had a major price change");
            }
        } catch (Exception e) {
            System.out.println("Error retrieveing major stock changes");
        }
        
        closeConn(conn);

        return mStocks;
        */
        
        // Data structure to store major change stocks
        ArrayList<Stock> mStocks = new ArrayList<Stock>();
        
        // Query to get the names of all stocks that have been stored
        String query = "Select StockName FROM stock";
        ArrayList<String> stockNames = new ArrayList<String>();
        ResultSet stockNamesSet = select(conn, query);
        try {
            while (stockNamesSet.next()) {
                stockNames.add(stockNamesSet.getString(1));
            }
        } catch (SQLException e) {
            //System.out.println("Error retrieveing stocks");
        }
        // Check each stock for a major change in the last x days
        String oldDate = subtractDays((int) days); //Change parameter to float
        for (String stock : stockNames) {
            double oldPrice = 0.00000001;
//            System.out.println(stock);
            Stock currentStock = getStock(stock);
            
            // Get the price of the stock x days ago
            PreparedStatement opStmt;
            try {
                String oldPriceQuery = "SELECT Price FROM stock_history INNER JOIN stock ON stock_history.StockId = stock.StockId";
                oldPriceQuery += " WHERE (stock.StockName = ?)";
                oldPriceQuery += " AND (stock_history.TimeStamp > ?)";
                oldPriceQuery += " LIMIT 1";
                opStmt = conn.prepareStatement(oldPriceQuery);
                opStmt.setString(1, stock);
                opStmt.setString(2, oldDate);
                ResultSet oldPriceSet = opStmt.executeQuery();
                
                if (oldPriceSet.next()) {
                    oldPrice = oldPriceSet.getDouble(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("Error retrieveing major stock changes");
            }
            
            // Check how much the stock has changed
            double currentPrice = currentStock.getCurrentPrice();
            double percentChange = (oldPrice == 0) ? 0 : ((currentPrice / oldPrice) - 1) * 100;


            // If there has been a major change, add to major changes data structure
            if (Math.abs(percentChange) > CONST_MAJOR_STOCK_BOUNDARY) {
                mStocks.add(new Stock(currentStock.getTicker(), stock, currentPrice, currentPrice - oldPrice, percentChange));
            }
        }
        
        // Return all stocks that have had a major change
        closeConn(conn);
        return mStocks;
    }


    public ArrayList<Sector> majorSectorChanges(int days){

        // Create connection
        Connection conn = getConnection();
        
        // Get sector prices from x days ago
        // TO DO
        ArrayList<Sector> mSectors = new ArrayList<Sector>();
        String query = "SELECT SectorName FROM sector";
        ArrayList<String> sectorNames = new ArrayList<String>();
        //ArrayList<Integer> sectorIds = new ArrayList<Integer>();
        ResultSet sectorNamesSet = select(conn, query);
        try {
            while (sectorNamesSet.next()) {
                sectorNames.add(sectorNamesSet.getString(1));
            }
        } catch (SQLException e) {
            //System.out.println("Error retrieveing sectors");
        }

        String oldDate = subtractDays((int) days); 
        for(String sector : sectorNames) {
            //Get sum of prices for all the stocks in that sector
            double oldPrice = 0.00000001;
            Sector currentSector = getSector(sector);
            ArrayList<String> stocks = new ArrayList<String>();
            stocks = getSectorStocks(sector);

            for(String stock : stocks){
                PreparedStatement opStmt = null;
                try{
                    String stocksQuery = "SELECT Price FROM stock_history INNER JOIN stock ON stock_history.StockId = stock.StockId " +
                            "WHERE (stock.StockName = ?) AND (stock_history.TimeStamp > ?)";
//                    stocksQuery += " ORDER BY stock_history.TimeStamp DESC";
                    stocksQuery += " LIMIT 1";

                    opStmt = conn.prepareStatement(stocksQuery);
                    opStmt.setString(1, stock);
                    opStmt.setString(2, oldDate);
                    ResultSet oldPricesSet = opStmt.executeQuery();
                    if(oldPricesSet.next()){
                        oldPrice += oldPricesSet.getDouble(1);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    //System.out.println("Error retreiving major sector changes");
                }
            }

            double currentPrice = currentSector.getCurrentPrice();
            double percentChange = (oldPrice == 0) ? 0 : ((currentPrice / oldPrice) - 1) * 100;

            if (Math.abs(percentChange) > CONST_MAJOR_SECTOR_BOUNDARY) {
                mSectors.add(new Sector(sector, currentPrice, percentChange));
            }

        }
        
        // Get current sector prices
        // TO DO
        
        // Calculate difference for each sector and find major changes (CONST_MAJOR_SECTOR_BOUNDARY)
        // TO DO
        
        // Return sectors with major changes
        closeConn(conn);
        return mSectors;
    }

    private int getLastRefreshed() {
        Connection conn = getConnection();
        
        int toReturn = -10000000;
        
        //CHECKS if the ticker already Exists:
        PreparedStatement stmt;
        try {
            String time = "Select TimeStamp FROM stock_history" +
                    " ORDER BY TimeStamp DESC" +
                    " LIMIT 1";
            stmt = conn.prepareStatement(time);
            ResultSet times = stmt.executeQuery();

            if(times.next()) {
                Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(times.getString(1));
                toReturn = (int) (new Date().getTime() - d.getTime());
            }
        } catch(Exception e) {
            e.printStackTrace();
            toReturn = -10000000;
        }
        
        closeConn(conn);
        return toReturn;
    }

    /**
     * Subtracts a given number of days from the current date and returns as a string
     * @param days The number of days to be taken away
     * @return The result of taking days away from the current date
     */
    private String subtractDays(int days){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, -days);
        String newDate = dateFormat.format(today.getTime());
        return newDate;
    }

    private void closeConn(Connection conn){
        try {
            conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}