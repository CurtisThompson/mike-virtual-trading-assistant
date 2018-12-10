import java.util.List;

public interface WebScraper {

    public Stock getStock(String name);

    public List<Stock> getStocks();
}

