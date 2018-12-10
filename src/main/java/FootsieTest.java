import java.util.ArrayList;
import java.util.List;

public class FootsieTest {

    public static void main(String[] args) throws Exception {

        LSEScraper f1 = new LSEScraper();
        List<String> names = new ArrayList<>();
        names.add("CENTRICA");
        names.add("FERGUSON");
        names.add("GLENCORE");
        names.add("EXPERIAN");
        names.add("DCC");

        Stock stock1 = f1.getStock("Google");
        stock1.printStr();

        Stock stock2 = f1.getStock("asdsd");
        stock2.printStr();
        Stock stock3 = f1.getStock("wpp");
        stock3.printStr();

        Stock stock4 = f1.getStock("3I GRP");
        stock4.printStr();

        Stock stock5 = f1.getStock("jUst Eat");
        stock5.printStr();
//
//        Stock stock6 = f1.getStock("next");
//        stock6.printStr();
//        System.out.println(f1.getStocks(names));
    }
}
