import com.google.gson.JsonObject;
// import org.junit.Test;
// import org.junit.Assert;

public class APITests extends junit.framework.TestCase {
    APIConnector ac;

    public void setUp() {
        ac = new APIConnector("eur", "usd", "https://api.exchangerate.host/convert?from=&to=");
    }

    public void testConstructURL() {
        assertNotNull(ac);
    }

    public void testgetJSONfromAPI() {
        assertNotNull(ac.getJSONfromAPI());
    }

    public void testgetExchangeRateNull() {
        JsonObject jsonobj = ac.getJSONfromAPI();
        assertNotNull(ac.getExchangeRate(jsonobj));
    }


    public void testgetExchangeRate() {
        JsonObject jsonobj = ac.getJSONfromAPI();
        double rate = ac.getExchangeRate(jsonobj);
        assertTrue(0.1 < rate && rate < 10.0);
    }

}
