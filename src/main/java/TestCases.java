import java.util.*;

public class TestCases {
    private final static String NEW_LINE = "-------------------------";
    private final static String TEST_FAIL = "     Test Failed.";
    private final static String TEST_PASS = "     Test Passed.";
    private final static String TEST_ERROR = "     Error During Testing.";
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static int[] failSections = new int[5]; // c d n s
    private static int[] passSections = new int[5]; // c d n s
    
    public static void main(String[] args) {
        // If there are arguments for the test
        if ((args != null) && (args.length > 0)) {
            // Run the database controller tests
            if (args[0].equals("-d")) {
                databaseControllerTests();
            } else if (args[0].equals("-c")) {
                chatBotTests();
            } else if (args[0].equals("-s")) {
                stockAndSectorTests();
            } else if (args[0].equals("-n")) {
                namesTests();
            } else if (args[0].equals("-h")) {
                helpDialog();
            } else if (args[0].equals("-p")) {
                performanceTests();
            }
        }
        // If no arguments then run every test
        else {
            print();
            databaseControllerTests();
            print();
            chatBotTests();
            print();
            stockAndSectorTests();
            print();
            namesTests();
        }
        
        print();
        print();
        print(NEW_LINE);
        print("Overall Results");
        print(NEW_LINE);
        print("Tests Passed: " + testsPassed);
        print("Tests Failed: " + testsFailed);
        
        print();
        print(NEW_LINE);
        print("ChatBot");
        print(NEW_LINE);
        print("Tests Passed: " + passSections[0]);
        print("Tests Failed: " + failSections[0]);
        
        print();
        print(NEW_LINE);
        print("DatabaseController");
        print(NEW_LINE);
        print("Tests Passed: " + passSections[1]);
        print("Tests Failed: " + failSections[1]);
        
        print();
        print(NEW_LINE);
        print("Names (Natural Language)");
        print(NEW_LINE);
        print("Tests Passed: " + passSections[2]);
        print("Tests Failed: " + failSections[2]);
        
        print();
        print(NEW_LINE);
        print("Stocks and Sectors");
        print(NEW_LINE);
        print("Tests Passed: " + passSections[3]);
        print("Tests Failed: " + failSections[3]);
    }
    
    private static void helpDialog() {
        print("The following options are available to use:");
        print("     -c   Performs chatBot unit tests");
        print("     -d   Performs databaseController unit tests");
        print("     -h   Prints this help dialog (as I'm sure you already know)");
        print("     -n   Performs natural language tests");
        print("     -p   Tests the performance of the system");
        print("     -s   Performs stock and sector unit tests");
    }
    
    // The performance tests check how long it takes for the system to respond to a query
    private static void performanceTests() {
        ChatBot c = new ChatBot();
        
        long startTime = System.currentTimeMillis();
        // Each of the 20 queries is repeated 5 times so that 100 queries are essentially tested
        for (int i = 0; i < 5; i++) {
            c.executeAction(new KeyboardInput("what is the current price of bread?"));
            c.executeAction(new KeyboardInput("percentage change of banks"));
            c.executeAction(new KeyboardInput("what was the price of III yesterday?"));
            c.executeAction(new KeyboardInput("whats the price change diageo"));
            c.executeAction(new KeyboardInput("could you get me the price of Paddy Power Betfair?"));
            c.executeAction(new KeyboardInput("how many coconuts in a dozen?"));
            c.executeAction(new KeyboardInput("how is the national grid performing?"));
            c.executeAction(new KeyboardInput("what is the price of the national grid?"));
            c.executeAction(new KeyboardInput("What is the price change of the Retail Hospitality Sector?"));
            c.executeAction(new KeyboardInput("WHAT IS THE CURRENT PRICE OF PERSONAL GOODS?"));
            c.executeAction(new KeyboardInput("How much did the price of Pharmaceuticals change in February"));
            c.executeAction(new KeyboardInput("Forestry and Paper?"));
            c.executeAction(new KeyboardInput("What is the price of Smith's Group today?"));
            c.executeAction(new KeyboardInput("Could you get me the latest news on tobacco?"));
            c.executeAction(new KeyboardInput("What is the most recent news on Rio Tinto"));
            c.executeAction(new KeyboardInput("news about mondi"));
            c.executeAction(new KeyboardInput("news bunzl"));
            c.executeAction(new KeyboardInput("price carnival"));
            c.executeAction(new KeyboardInput("what is the news on Experian?"));
            c.executeAction(new KeyboardInput("Is the price change of BP positive or negative?"));
        }
        long endTime = System.currentTimeMillis();
        // Calculate the average standard query response time
        long performanceA = (endTime - startTime) / 100;
        
        startTime = System.currentTimeMillis();
        // Stock summary queries take longer to process, so they are tested separately
        for (int i = 0; i < 20; i++) {
            c.executeAction(new KeyboardInput("stock summary"));
        }
        endTime = System.currentTimeMillis();
        // Calculate the average stock summary query response time
        long performanceB = (endTime - startTime) / 20;
        
        // Output the results of the test
        print(NEW_LINE);
        print("Stock And Sector Tests");
        print(NEW_LINE);
        print("     Average Query Response Time:         " + performanceA + "ms");
        print("     Average Stock Summary Response Time: " + performanceB + "ms");
        print();
    }
    
    private static void stockAndSectorTests() {
        DatabaseController db = new DatabaseController();
        
        print(NEW_LINE);
        print("Stock And Sector Tests");
        print(NEW_LINE);
        
        boolean testRes;
        
        // -------------------------
        // 3I and Banks Tests
        
        Stock st = db.getStock("3I");
        Sector se = db.getSector("Banks");
        
        testRes = false;
        if (st.getTicker().equals("III"))
            testRes = true;
        print("     ss001: getTicker");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (st.getName().equals("3I"))
            testRes = true;
        print("     ss002: getName");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (st.getCurrentPrice() > 0)
            testRes = true;
        print("     ss003: getCurrentPrice (" + st.getCurrentPrice() + ")");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (st.getPriceChange() != 0)
            testRes = true;
        print("     ss004: getPriceChange (" + st.getPriceChange() + ")");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (st.getPercentChange() != 0)
            testRes = true;
        print("     ss005: getPercentChange (" + st.getPercentChange() + ")");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (se.getCurrentPrice() > 0)
            testRes = true;
        print("     ss006: getCurrentPrice (" + se.getCurrentPrice() + ")");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (se.getPriceChange() != 0)
            testRes = true;
        print("     ss007: getPriceChange (" + se.getPriceChange() + ")");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (se.getPercentChange() != 0)
            testRes = true;
        print("     ss008: getPercentChange (" + se.getPercentChange() + ")");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (se.getName().toUpperCase().equals("BANKS"))
            testRes = true;
        print("     ss009: getName");
        testResult(testRes, 3);
        print();
        
        // -------------------------
        // Aviva and Mining Tests
        
        st = db.getStock("Aviva");
        se = db.getSector("Mining");
        
        testRes = false;
        if (st.getTicker().equals("AV"))
            testRes = true;
        print("     ss010: getTicker (attempt 2)");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (st.getName().equals("AVIVA"))
            testRes = true;
        print("     ss011: getName (attempt 2)");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (st.getCurrentPrice() > 0)
            testRes = true;
        print("     ss012: getCurrentPrice (" + st.getCurrentPrice() + ") (attempt 2)");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (st.getPriceChange() != 0)
            testRes = true;
        print("     ss013: getPriceChange (" + st.getPriceChange() + ") (attempt 2)");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (st.getPercentChange() != 0)
            testRes = true;
        print("     ss014: getPercentChange (" + st.getPercentChange() + ") (attempt 2)");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (se.getCurrentPrice() > 0)
            testRes = true;
        print("     ss015: getCurrentPrice (" + se.getCurrentPrice() + ") (attempt 2)");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (se.getPriceChange() != 0)
            testRes = true;
        print("     ss016: getPriceChange (" + se.getPriceChange() + ") (attempt 2)");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (se.getPercentChange() != 0)
            testRes = true;
        print("     ss017: getPercentChange (" + se.getPercentChange() + ") (attempt 2)");
        testResult(testRes, 3);
        print();
        
        testRes = false;
        if (se.getName().toUpperCase().equals("MINING"))
            testRes = true;
        print("     ss018: getName (attempt 2)");
        testResult(testRes, 3);
        print();
    }
    
    private static void chatBotTests() {
        ChatBot c = new ChatBot();
        DatabaseController db = new DatabaseController();
        
        print(NEW_LINE);
        print("chatBot Tests");
        print(NEW_LINE);
        
        boolean testRes;
        
        // -------------------------
        // stockNews Tests
        testRes = false;
        try {
            Response news = c.stockNews(db.getStock("Barclays"));
            if ((news != null) && (news.getNews() != null))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb001: Obtaining news for a stock (stockNews)");
        testResult(testRes, 0);
        print();
        
        testRes = false;
        try {
            Response news = c.stockNews(db.getStock("Blobby"));
            if ((news == null) || (news.getNews() == null))
                testRes = true;
        } catch (Exception e) {
            testError(e);
            testRes = true;
        }
        print("     cb002: Obtaining news for a stock that does not exist (stockNews)");
        testResult(testRes, 0);
        print();
        
        // -------------------------
        // sectorNews Tests
        
        testRes = false;
        try {
            Response news = c.sectorNews(db.getSector("Banks"));
            if ((news != null) && (news.getNews() != null))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb003: Obtaining news for a sector (sectorNews)");
        testResult(testRes, 0);
        print();
        
        testRes = false;
        try {
            Response news = c.sectorNews(db.getSector("Blobby"));
            if ((news == null) || (news.getNews() == null))
                testRes = true;
        } catch (Exception e) {
            testError(e);
            testRes = true;
        }
        print("     cb004: Obtaining news for a sector that does not exist (sectorNews)");
        testResult(testRes, 0);
        print();
        
        // -------------------------
        // percentageChange Tests
        
        testRes = false;
        try {
            Sector s = db.getSector("Banks");
            if ((c.percentageChange(s).getText().contains(String.format("%.2f", Math.abs(s.getPercentChange())))) || (c.percentageChange(s).getText().contains("not changed")))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb005: Get the percentage change for a sector (percentageChange)");
        testResult(testRes, 0);
        print();
        
        testRes = false;
        try {
            Stock s = db.getStock("Barclays");
            if ((c.percentageChange(s).getText().contains(Double.toString(Math.abs(s.getPercentChange())))) || (c.percentageChange(s).getText().contains("not changed")))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb006: Get the percentage change for a stock (percentageChange)");
        testResult(testRes, 0);
        print();
        
        // -------------------------
        // currentPrice Tests
        
        testRes = false;
        try {
            Sector s = db.getSector("Banks");
            if (c.currentPrice(s).getText().contains(Double.toString(Math.abs(s.getCurrentValue()))))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb007: Get the price for a sector (currentPrice)");
        testResult(testRes, 0);
        print();
        
        testRes = false;
        try {
            Stock s = db.getStock("Barclays");
            if (c.currentPrice(s).getText().contains(Double.toString(Math.abs(s.getCurrentPrice()))))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb008: Get the price for a stock (currentPrice)");
        testResult(testRes, 0);
        print();
        
        // -------------------------
        // priceChange Tests
        
        testRes = false;
        try {
            Sector s = db.getSector("Banks");
            if ((c.priceChange(s, false).getText().contains(Double.toString(Math.abs(s.getPriceChange())))) || (c.priceChange(s, false).getText().contains("not changed")))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb009: Get the price change for a sector (priceChange)");
        testResult(testRes, 0);
        print();
        
        testRes = false;
        try {
            Stock s = db.getStock("Barclays");
            if ((c.priceChange(s, false).getText().contains(Double.toString(Math.abs(s.getPriceChange())))) || (c.priceChange(s, false).getText().contains("not changed")))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb010: Get the price change for a stock (priceChange)");
        testResult(testRes, 0);
        print();
        
        // -------------------------
        // stockSummary Tests
        
        testRes = false;
        try {
            Response s = c.stockSummary();
            if (!s.getText().equals(""))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb011: The stock summary contains stock information (stockSummary)");
        testResult(testRes, 0);
        print();
        
        testRes = false;
        try {
            Response s = c.stockSummary();
            if (s.getNews() != null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb012: The stock summary contains stock news (stockSummary)");
        testResult(testRes, 0);
        print();
        
        // -------------------------
        // stockNews Tests
        
        testRes = false;
        try {
            Response s = c.stockNews(db.getStock("Barclays"));
            if (s.getNews() != null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb013: Get news on a stock (stockNews)");
        testResult(testRes, 0);
        print();
        
        // -------------------------
        // sectorNews Tests
        
        testRes = false;
        try {
            Response s = c.sectorNews(db.getSector("Banks"));
            if (s.getNews() != null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb014: Get news on a sector (sectorNews)");
        testResult(testRes, 0);
        print();
        
        // -------------------------
        // executeAction Tests
        
        testRes = false;
        try {
            Response s = c.executeAction(new KeyboardInput("price of III?"));
            if (!s.getText().equals(""))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb015: A query can be executed (executeAction)");
        testResult(testRes, 0);
        print();
        
        // -------------------------
        // checkMajorChanges Tests
        
        testRes = false;
        try {
            Response s = c.checkMajorChanges();
            if (!s.getText().equals(""))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     cb016: The stock summary contains stock information (checkMajorChanges)");
        testResult(testRes, 0);
        print();
    }
    
    private static void namesTests() {
        ChatBot c = new ChatBot();
        DatabaseController db = new DatabaseController();
        
        print(NEW_LINE);
        print("Names Tests");
        print(NEW_LINE);
        
        boolean testRes;
        
        // -------------------------
        // stockNews Tests
        ArrayList<String> namesToTest = new ArrayList<String>();
        namesToTest.add("3i Group");
        namesToTest.add("Associated British Food");
        namesToTest.add("Admiral");
        namesToTest.add("Anglo American");
        namesToTest.add("Antofagasta");
        namesToTest.add("Ashtead Group");
        namesToTest.add("Astrazeneca");
        namesToTest.add("Aviva");
        namesToTest.add("BAE Systems");
        namesToTest.add("Barclays");
        namesToTest.add("Barratt Developments");
        namesToTest.add("Berkeley GP.Hld");
        namesToTest.add("BHP Billiton");
        namesToTest.add("BP");
        namesToTest.add("British American Tobacco");
        namesToTest.add("British Land");
        namesToTest.add("BT Group");
        namesToTest.add("Bunzl");
        namesToTest.add("Burberry Group");
        namesToTest.add("Carnival");
        namesToTest.add("Centrica");
        namesToTest.add("Cocacola");
        namesToTest.add("Compass Group");
        namesToTest.add("CRH");
        namesToTest.add("Croda International");
        namesToTest.add("DCC");
        namesToTest.add("Diageo");
        namesToTest.add("Direct Line");
        namesToTest.add("Easyjet");
        namesToTest.add("Evraz");
        namesToTest.add("Experian");
        namesToTest.add("Ferguson");
        namesToTest.add("Fresnillo");
        namesToTest.add("G4S");
        namesToTest.add("GKN");
        namesToTest.add("Glaxosmithkline");
        namesToTest.add("Glencore");
        namesToTest.add("Halma");
        namesToTest.add("Hammerson");
        namesToTest.add("Hargreaves Lansdown");
        namesToTest.add("HSBC");
        namesToTest.add("Imperial Brands");
        namesToTest.add("Informa");
        namesToTest.add("Intercontinental Hotels");
        namesToTest.add("Intertek Group");
        namesToTest.add("Intl Consol Air");
        namesToTest.add("ITV");
        namesToTest.add("Johnson Matthey");
        namesToTest.add("Just Eat");
        namesToTest.add("Kingfisher");
        namesToTest.add("Land Secs");
        namesToTest.add("Legal and General");
        namesToTest.add("Lloyds");
        namesToTest.add("London Stock Exchange");
        namesToTest.add("Marks and Spencer");
        namesToTest.add("Mediclinic");
        namesToTest.add("Micro Focus");
        namesToTest.add("Mondi");
        namesToTest.add("Morrison");
        namesToTest.add("National Grid");
        namesToTest.add("Next");
        namesToTest.add("NMC Health");
        namesToTest.add("Old Mutual");
        namesToTest.add("Paddy Power Betfair");
        namesToTest.add("Pearson");
        namesToTest.add("Persimmon");
        namesToTest.add("Prudential");
        namesToTest.add("Randgold Resources");
        namesToTest.add("RDS A");
        namesToTest.add("RDS B");
        namesToTest.add("Reckitt Ben GP");
        namesToTest.add("Relx");
        namesToTest.add("Rentokil HLG");
        namesToTest.add("Rio Tinto");
        namesToTest.add("Rolls Royce");
        namesToTest.add("Royal Bank of Scotland");
        namesToTest.add("RSA Insurance");
        namesToTest.add("Sage Group");
        namesToTest.add("Sainsbury");
        namesToTest.add("Schroders");
        namesToTest.add("Scottish Mortgage Investment Trust");
        namesToTest.add("Segro");
        namesToTest.add("Severn Trent");
        namesToTest.add("Shire");
        namesToTest.add("Sky PLC");
        namesToTest.add("Smith and Nephew");
        namesToTest.add("Smith DS");
        namesToTest.add("Smiths Group");
        namesToTest.add("Smurfit Kap");
        namesToTest.add("SSE");
        namesToTest.add("St James Place");
        namesToTest.add("Standard Chartered");
        namesToTest.add("Std Life Aberdeen");
        namesToTest.add("Taylor Wimpey");
        namesToTest.add("Tesco");
        namesToTest.add("TUI AG");
        namesToTest.add("Unilever");
        namesToTest.add("United Utilities");
        namesToTest.add("Vodafone Group");
        namesToTest.add("Whitbread");
        namesToTest.add("WPP");
        //---
        namesToTest.add("Aerospace and Defence");
        namesToTest.add("Automobiles and Parts");
        namesToTest.add("Banks");
        namesToTest.add("Beverages");
        namesToTest.add("Chemicals");
        namesToTest.add("Construction and Materials");
        namesToTest.add("Electricity");
        namesToTest.add("Electronic and Electrical Equipment");
        namesToTest.add("Financial Services");
        namesToTest.add("Fixed Line Telecommunications");
        namesToTest.add("Food and Drug Retailers");
        namesToTest.add("Food Producers");
        namesToTest.add("Forestry and Paper");
        namesToTest.add("Gas, Water and Multiutilities");
        namesToTest.add("General Industries");
        namesToTest.add("General Retailers");
        namesToTest.add("Health Care Equipment and Services");
        namesToTest.add("Household Goods and Home Construction");
        namesToTest.add("Industrial Metals and Mining");
        namesToTest.add("Life Insurance");
        namesToTest.add("Media");
        namesToTest.add("Mining");
        namesToTest.add("Mobile Telecommunications");
        namesToTest.add("Nonlife Insurance");
        namesToTest.add("Oil and Gas Producers");
        namesToTest.add("Personal Goods");
        namesToTest.add("Pharmaceuticals");
        namesToTest.add("Real Estate Investment Trusts");
        namesToTest.add("Retail Hospitality");
        namesToTest.add("Software and Computer Services");
        namesToTest.add("Support Services");
        namesToTest.add("Tobacco");
        namesToTest.add("Travel and Leisure");
        
        int i = 0;
        for (String name : namesToTest) {
            // Capitalised
            testRes = false;
            try {
                Response s = c.executeAction(new KeyboardInput("price of " + name + "?"));
                if (s.getText().contains("price"))
                    testRes = true;
            } catch (Exception e) {
                testError(e);
            }
            print("     na" + numShow(i) + ": " + name + " can be queried (executeAction)");
            testResult(testRes, 2);
            print();
            i++;
            
            // Upper case
            testRes = false;
            try {
                Response s = c.executeAction(new KeyboardInput("price of " + name.toUpperCase() + "?"));
                if (s.getText().contains("price"))
                    testRes = true;
            } catch (Exception e) {
                testError(e);
            }
            print("     na" + numShow(i) + ": " + name.toUpperCase() + " can be queried (executeAction)");
            testResult(testRes, 2);
            print();
            i++;
            
            // Lower case
            testRes = false;
            try {
                Response s = c.executeAction(new KeyboardInput("price of " + name.toLowerCase() + "?"));
                if (s.getText().contains("price"))
                    testRes = true;
            } catch (Exception e) {
                testError(e);
            }
            print("     na" + numShow(i) + ": " + name.toLowerCase() + " can be queried (executeAction)");
            testResult(testRes, 2);
            print();
            i++;
        }
    }
    
    private static void databaseControllerTests() {
        DatabaseController db = new DatabaseController();
        
        print(NEW_LINE);
        print("databaseController Tests");
        print(NEW_LINE);
        
        boolean testRes;
        
        // -------------------------
        // addStock Tests
        testRes = false;
        try {
            db.addStock("Test", "Test Stock", "Banks");
            if (db.getStock("Test Stock") != null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db001: Adding a new stock normally (addStock)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            db.addStock("BARC", "Barcking Mad", "Banks");
            if (db.getStock("Barcking Mad") == null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db002: Ticker already exists (addStock)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            db.addStock("Test2", "TwoTwoTwo", "Dodgy");
            if (db.getSector("Dodgy") != null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db003: Adding a new stock to a new sector (addStock)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            db.addStock("", "TwoTwoTwo3", "Banks");
            if (db.getStock("TwoTwoTwo3") == null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db004: Ticker is empty (addStock)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            db.addStock("Test4", "TwoTwoTwo4", "");
            if (db.getStock("TwoTwoTwo4") == null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db005: Sector is empty (addStock)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            db.addStock("Test5", "", "Banks");
            if (db.getStock("") == null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
            testRes = true;
        }
        print("     db006: Stock is empty (addStock)");
        testResult(testRes, 1);
        print();
        
        // -------------------------
        // addSector Tests
        testRes = false;
        try {
            db.addSector("NewTestSector");
            if (db.getSector("NewTestSector") != null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db007: New sector added normally (addSector)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            db.addSector("");
            if (db.getSector("") == null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db008: Sector is empty (addSector)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            db.addSector("Banks");
            if (db.getSector("Banks").getCurrentValue() > 0)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db009: Sector already exists (addSector)");
        testResult(testRes, 1);
        print();
        
        // -------------------------
        // addStockHistory Tests
        
        db.addStockHistory(new Stock("Test", "Test Stock", 100, 5, 5));
        print("     db010: Add stock history (addStockHistory))");
        testResult(true, 1);
        print();
        
        // -------------------------
        // getStock Tests
        
        testRes = false;
        try {
            Stock s = db.getStock("3I");
            if (s.getName().equals("3I"))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db011: Obtain a stock from the database (getStock)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            Stock s = db.getStock("3I");
            if (s.getCurrentPrice() > 0)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db012: Obtained stock from database has price set (getStock)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            Stock s = db.getStock("3I");
            if (s.getPriceChange() != 0)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db013: Obtained stock from database has price change set (getStock)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            Stock s = db.getStock("3I");
            if (s.getPercentChange() != 0)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db014: Obtained stock from database has percent change set (getStock)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            Stock s = db.getStock("3I");
            if (s.getTicker() != null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db015: Obtained stock from database has ticker set (getStock)");
        testResult(testRes, 1);
        print();
        
        // -------------------------
        // getSector Tests
        
        testRes = false;
        try {
            Sector s = db.getSector("Banks");
            if (s.getName() != null)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db016: Obtain a sector from the database (getSector)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            Sector s = db.getSector("Banks");
            if (s.getCurrentPrice() > 0)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db017: Obtained sector from database has price set (getSector)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            Sector s = db.getSector("Banks");
            if (s.getPriceChange() != 0)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db018: Obtained sector from database has price change set (getSector)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            Sector s = db.getSector("Banks");
            if (s.getPercentChange() != 0)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db019: Obtained sector from database has percent change set (getSector)");
        testResult(testRes, 1);
        print();
        
        // -------------------------
        // getSectorStocks Tests
        
        testRes = false;
        try {
            ArrayList<String> s = db.getSectorStocks("Support Services");
            if (s.size() == 8)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db020: Can retrieve all stocks in a sector (getSectorStocks)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            ArrayList<String> s = db.getSectorStocks("Beverages");
            if (s.size() == 2)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db021: Can retrieve all stocks in a sector (attempt 2) (getSectorStocks)");
        testResult(testRes, 1);
        print();
        
        testRes = false;
        try {
            ArrayList<String> s = db.getSectorStocks("Mining");
            if (s.size() == 7)
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db022: Can retrieve all stocks in a sector (attempt 3) (getSectorStocks)");
        testResult(testRes, 1);
        print();
        
        // -------------------------
        // getTicker Tests
        
        testRes = false;
        try {
            String s = db.getTicker("3i");
            if (s.equals("III"))
                testRes = true;
        } catch (Exception e) {
            testError(e);
        }
        print("     db023: Get ticker of a stock (getTicker)");
        testResult(testRes, 1);
        print();
    }
    
    private static void guiTests() {
        GUI g = new GUI();
        
        print(NEW_LINE);
        print("GUI Tests");
        print(NEW_LINE);
        
        boolean testRes;
    }
    
    // Prints a string out
    private static void print(String s) {
        System.out.println(s);
    }
    
    // Prints an empty line out
    private static void print() {
        System.out.println();
    }
    
    // Prints out the results of a test an updates the overall counter
    private static void testResult(boolean b) {
        if (b == false) {
            print(TEST_FAIL);
            testsFailed++;
        }
        else {
            print(TEST_PASS);
            testsPassed++;
        }
    }
    
    // Prints out the results of a test and updates the overall and section counters
    private static void testResult(boolean b, int i) {
        if (b == false) {
            print(TEST_FAIL);
            testsFailed++;
            failSections[i]++;
        }
        else {
            print(TEST_PASS);
            testsPassed++;
            passSections[i]++;
        }
    }
    
    // Prints out a test error
    private static void testError(Exception e) {
        print(TEST_ERROR);
        e.getMessage();
    }
    
    // Converts an integer to an integer with preceding 0s (such that there are at least 3 digits)
    private static String numShow(int i) {
        if (i < 10)
            return ("00" + i);
        if (i < 100)
            return ("0" + i);
        return ("" + i);
    }
}