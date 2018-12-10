/**
 * Created by Akash on 19/02/2018.
 *
 * Class to interact with the Recast NLP api to get intent of a query.
 */


import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
public class NLPRecast implements NLP {

    private static final String endpoint = "https://api.recast.ai/v2/request";
//    private static final String token = "8418f3788e24eb6b353adf77dbf2383b"; // THIS IS THE OLD TOKEN
    private static final String token = "64a9a1617b8b01b50be79b231ba59cf4";


    /**
     * Uses the Recast NLP api to get the intent for a given string.
     * @param inputString a sentence that is the users query.
     * @return a JSON string containing intent information. See https://recast.ai/docs/text-analysis-api for details.
     * @throws IOException
     */
    private String getRecastIntent(String inputString) throws IOException {
        URL obj = new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Token "+token);

        String params = "text="+ inputString.toLowerCase() +"&language=en";

        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(params.getBytes());
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else { // failiure
            return errorJSON();
        }
    }

    /**
     * A utility function to get an 'error' json string.
     * @return json string {results: null}
     */
    private static String errorJSON(){
        return "{\"results\" : null}";
    }

    /**
     * Function to implement the NLP interface.
     * @param input UserInput object from which the intent is to be determined.
     * @return JSON string containing details about the intent.
     */
    public String determineIntent(UserInput input){
        String json;
        try{
            json = getRecastIntent(input.getText());
        } catch (IOException e) {
            json = errorJSON();
        }
        return json;
    }

}
