
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.*;
import java.io.*;
import java.lang.Double;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A complete Java class that shows how to open a URL, then read data (text) from that URL,
 * just using the URL class, in combination with an InputStreamReader and BufferedReader.
 *
 * @author alvin alexander, devdaily.com.
 */
public class LSEScraper implements WebScraper {

    private String cachedData;
    private static final String PORT = "4040";

    public LSEScraper() {
        cachedData = "";
    }


    private Stock getStockFromString(String string){
        String stockInfo = string.substring(5);
        //Declares individual variables for stock attributes
        String ticker = stockInfo.substring(0, stockInfo.indexOf(",!"));
        stockInfo = stockInfo.substring(stockInfo.indexOf(",!") + 2);
        String name = stockInfo.substring(0, stockInfo.indexOf(",!"));
        stockInfo = stockInfo.substring(stockInfo.indexOf(",!") + 2);
        stockInfo = stockInfo.substring(stockInfo.indexOf(",!") + 2);
        double price = Double.parseDouble(stockInfo.substring(0, stockInfo.indexOf(",!")).replace(",", ""));
        stockInfo = stockInfo.substring(stockInfo.indexOf(",!") + 2);
        double poundChange = Double.parseDouble(stockInfo.substring(0, stockInfo.indexOf(",!")).replace(",", ""));
        stockInfo = stockInfo.substring(stockInfo.indexOf(",!") + 2);
        double percentChange = Double.parseDouble(stockInfo.substring(0, stockInfo.indexOf(",!")).replace(",", ""));
        stockInfo = stockInfo.substring(stockInfo.indexOf(",!") + 2);

        return new Stock(ticker, name, price, poundChange, percentChange);
    }


    public Stock getStock(String stock) {

        //Checks if cached data is old and updates it if necessary
        String data;
        if (cachedData == "") {
            try {
                data = getData();
            } catch (Exception e) {
                return new Stock("SNF", "Stock not found", 0, 0, 0);
            }
        }
        else data = cachedData;


        stock = stock.toUpperCase();

        //Handles invalid input
        if (data.indexOf(stock) == -1) {
            return new Stock("SNF", "Stock not found", 0, 0, 0);
        }

        //Separates out the data for the individual stock
        String stockInfo = data.substring(data.indexOf(stock));
        stockInfo = stockInfo.substring(stockInfo.indexOf("?"), stockInfo.indexOf("</description"));

        return getStockFromString(stockInfo);
    }

    public List<Stock> getStocks() {
        List<Stock> stocks = new ArrayList<>();

        //Checks if cached data is old and updates it if necessary
        String data;
        if (cachedData == "") {
            try {
                data = getData();
            } catch (Exception e) {
                return stocks;
            }
        }
        else data = cachedData;

        InputSource is = new InputSource(new StringReader(data));


        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");

            for (int x = 0; x < nList.getLength(); x++) {
                Node nNode = nList.item(x);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String desc = eElement.getElementsByTagName("description").item(0).getTextContent();
                    stocks.add(getStockFromString(desc));
                }
            }
            return stocks;
        } catch (Exception e) {
            // If can't parse, then return as many stocks as possible.
            return stocks;
        }
    }

    private static String getData() throws Exception {
        String urlString = "http://localhost:" + PORT + "/";

        // Creates the url
        URL url = new URL(urlString);

        // Opens the url stream, wrap it an a few "readers"
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        // Stores output in data string
        String data = "";
        String line;
        while ((line = reader.readLine()) != null) {
            data = data + line;
        }
        // Closes reader
        reader.close();
        return data;
    }
}
