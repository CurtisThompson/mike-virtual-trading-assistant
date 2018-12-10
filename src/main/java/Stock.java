import java.util.ArrayList;

/**
 * Class to store all data relevant to a particular stock. 
 */
public class Stock {
    private String ticker;
    private String name;
    private ArrayList<String> aliases;
    private double currentPrice;
    private double priceChange;
    private double percentChange;

    // TODO: Need to figure out how to get stock sector info.  ANSWER?: It's predefined, doesn't change so no need to 'get' it each time, just store persistently?
    private String sector;

    public Stock(String ticker, String name, double currentPrice, double priceChange, double percentChange){
        this.ticker = ticker;
        this.name = name;
        this.currentPrice = currentPrice;
        this.priceChange = priceChange;
        this.percentChange = percentChange;
    }

    public Stock(String ticker, String name, double currentPrice, double priceChange, double percentChange, String sector){
        this(ticker, name, currentPrice, priceChange, percentChange);
        this.sector = sector;
    }

    public String getTicker(){
        return ticker;
    }

    public String getName() {
        return name.toUpperCase();
    }

    public double getCurrentPrice(){
        return currentPrice;
    }

    public double getPriceChange() {
        return priceChange;
    }

    // TODO: maybe add seperate methods for "pretty" output of these. E.G. rounded to 2 sig. figs?
    public String getPrettyPercentageChange(){
        return String.format("%.2f", percentChange);
    }

    public double getPercentChange(){
        return percentChange;
    }

    public void printStr(){
        System.out.println(ticker + " " + name + " " + currentPrice + " " + priceChange + " " + percentChange);
    }
    public String toString(){
        return this.getName();
    }
}
