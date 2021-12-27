import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * <h1>Convert currencies using up-to-date exchange rates</h1>
 * This program implements a simple currency converter using the
 * exhange rate API provided by https:exchangerate.host/.
 *
 * @author  Andreas Kreitschmann
 * @version 1.0
 * @since   2021-12-25
 */

// TODO: add comments to all methods
public class APIConnector {
    // attributes
    // private Converter curConversion;
    private String fromCur;
    private String toCur;
    private String apiEndpoint;

    //constructors
    public APIConnector(String fromCur, String toCur, String apiEndpoint) {
        // this.curConversion = curConversion;
        this.fromCur = fromCur;
        this.toCur = toCur;
        this.apiEndpoint = apiEndpoint;
    }

    public APIConnector(String fromCur, String toCur) {
        this(fromCur, toCur, "https://api.exchangerate.host/convert?from=&to=");
    }

    //methods
    /**
     * This method returns a Java URL object under which the API can be reached.
     * @param
     * @return apiURL This is the Java URL object .
     */
    private URL constructURL() {
        // using StringBuilder object ot manipulate url in place
        StringBuilder url =  new StringBuilder(this.apiEndpoint);

        //inser fromCur after first = sign
        int posFromCur = apiEndpoint.indexOf("=");
        url.insert(posFromCur + 1, this.fromCur.toUpperCase());

        //append toCur at the end of URL
        url.append(this.toCur.toUpperCase());

        String urlString = url.toString();

        URL apiURL = null;
        try {
            apiURL = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return apiURL;
    }

    /**
     * This method creates a Java HttpURLConnection object and opens it.
     * @param APIurl java URL object.
     * @return void .
     */
    private HttpURLConnection createHttpURLConnection(URL APIurl) {
        HttpURLConnection request = null;
        try {
            request = (HttpURLConnection) APIurl.openConnection();
            // request.connect();
        } catch (UnknownHostException uhex) {
            System.out.println("Please verify if your internet connection is working.");
            uhex.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (NullPointerException np) {
            np.printStackTrace();
        }
        return request;
    }

    public JsonObject getJSONfromAPI () {
        // TODO throw java.net.UnknownHostException exception
        // call helper methods
        URL apiURL = this.constructURL();
        HttpURLConnection request = this.createHttpURLConnection(apiURL);

        JsonObject jsonobj = null;
        try {
            // TODO replace deprecated JsonParser constructor / method
            JsonParser jp = new JsonParser();
            InputStreamReader stream = new InputStreamReader((InputStream) request.getContent());
            JsonElement root = jp.parse(stream);
            jsonobj = root.getAsJsonObject();
            System.out.println("...contacting " + apiURL + " to get current exchange rate");
            TimeUnit.SECONDS.sleep(1);
        } catch (IOException | InterruptedException ioex) {
            ioex.printStackTrace();
        }
        return jsonobj;
    }

    public String getApiResponseAsString (JsonObject apiResponse) {
        return apiResponse.toString();
    }

    public double getExchangeRate (JsonObject apiResponse) throws UnsupportedOperationException {
        JsonElement exchangeRate = null;
        try {
            exchangeRate = apiResponse.getAsJsonObject("info").get("rate");
        } catch (NullPointerException npex) {
            System.out.println("Seems like no data was returned from the API. Check your internet connection");
            npex.printStackTrace();
        }

        if (exchangeRate == null) {
            throw new UnsupportedOperationException("Invalid base or target currency");
        } else {
            return exchangeRate.getAsDouble();
        }

    }

}
