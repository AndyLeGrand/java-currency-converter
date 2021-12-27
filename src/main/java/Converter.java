import com.google.gson.JsonObject;

import java.util.Scanner;


public class Converter
{
    //attributes
    String fromCur;
    String toCur;
    double amountToConvert;

    public Converter(String fromCur, String toCur, double amountToConvert) throws IllegalArgumentException {
        if (amountToConvert < 0) {
            throw new IllegalArgumentException("Amount to be converted must be >= 0");
        } else {
            this.fromCur = fromCur;
            this.toCur = toCur;
            this.amountToConvert = amountToConvert;
        }
    }

    // getter & setter methods
    public String getFromCur() {
        return this.fromCur;
    }

    public String getToCur() {
        return this.toCur;
    }

    public double getAmountToConvert() {
        return this.amountToConvert;
    }

    public void setFromCur(String fromCur) {
        this.fromCur = fromCur;
    }

    public void setToCur (String toCur) {
        this.toCur = toCur;
    }

    public void setAmountToConvert (double amountToConvert) {
        this.amountToConvert = amountToConvert;
    }

    public double convertAmount() throws IllegalArgumentException {
        APIConnector apiConnector = new APIConnector(this.getFromCur(),
                this.getToCur());
        JsonObject apiResponse = apiConnector.getJSONfromAPI();
        double exchangeRate = apiConnector.getExchangeRate(apiResponse);
        return exchangeRate * this.getAmountToConvert();
    }


    public String toString() {
        double converted = this.convertAmount();
        return "\nConversion from: " + this.getFromCur() + " to " + this.getToCur() + "\n"
                + "Amount: " + this.getAmountToConvert() + "\n" + "Result: " + converted;
    }

    public void print() {
        System.out.println(this.toString());
    }

    public static void main(String[] args) {
        try {
            // TODO: api endpoint handles invalid base currencies automatically as eur
            // find a way to deal with this
            Scanner userInput = new Scanner(System.in);
            System.out.println("Enter currency from which you whish to convert: ");
            String fromCurrency = userInput.nextLine();

            System.out.println("Enter currency to which you whish to convert: ");
            String toCurrency = userInput.nextLine();

            System.out.println("Enter amount: ");
            double amount = Double.parseDouble(userInput.nextLine());

            Converter curConversion = new Converter(fromCurrency, toCurrency, amount);

            curConversion.print();

        } catch (NumberFormatException nfex) {
            System.out.println("Please enter numerical value.");
            nfex.printStackTrace();
        }
    }
}
