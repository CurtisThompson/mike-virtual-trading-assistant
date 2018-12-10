import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
public class Test {
	public static void main(String[] args){
		DatabaseController db = new DatabaseController();
        List<Stock> stocks = db.favouriteStocks(6);
		for (Stock s : stocks) {
			s.printStr();
		}
        stocks = db.majorStockChanges(6);
        for (Stock s : stocks) {
            s.printStr();
        }
//        db.getSector("Aerospace and Defense").printStr();
//        db.getSector("Mining").printStr();
//        db.getSector("Banks").printStr();
//        System.out.println(db.calculateSectorPercentChange("Aerospace and Defense"));
//        System.out.println(db.calculateSectorPercentChange("Mining"));
//        System.out.println(db.calculateSectorPercentChange("Banks"));
	}
}