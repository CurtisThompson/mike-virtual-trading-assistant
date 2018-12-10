import java.util.Scanner;
import java.lang.String;

/**
 * Created by Akash on 01/02/2018.
 */
public class KeyboardInput implements UserInput {
    private String text;

    public KeyboardInput(String input) {
        this.text = input;
        text = text.replaceAll("%", "percentage");
    }

    /**
     * Returns the text got from the user.
     * @return the query sentence
     */
    public String getText(){
        return this.text;
    }
}
