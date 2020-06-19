package model;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class QuoteFetcher
{
    private static final String QUOTES_URL = "http://taylorjonas.com/quotosaurus/quotosaurus.json";

    public static String next()
    {
        try
        {
            Quote[] quotes = loadQuotesFromJson(QUOTES_URL);
            Random random = new Random();
            Quote quote = quotes[random.nextInt(quotes.length)];
            StringBuilder quotation = new StringBuilder();
            quotation.append(quote.getQuote());
            quotation.append(" -" + quote.getAuthor());
            quotation.append(" (" + quote.getWork() + ")");
            return quotation.toString();
        }
        catch (Exception e) {return "failure";}
    }

    private static Quote[] loadQuotesFromJson(String urlString) throws IOException
    {
        QuoteData quoteData;
        HttpURLConnection connection = null;
        try
        {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                // Get response body input stream
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                quoteData = (new Gson()).fromJson(br, QuoteData.class);
            }
            else {throw new IOException("Unable to read from url. Response code: " + connection.getResponseCode());}
            connection.disconnect();
        }
        finally { if(connection != null) connection.disconnect(); }

        //can't run a method on null data, so run this check while returning
        return quoteData == null ? null : quoteData.getQuotes();
    }

    static class Quote
    {
        @SuppressWarnings("unused")
        private String quote;
        private String author;
        private String work;
        public String getQuote(){return quote;}
        public String getAuthor(){return author;}
        public String getWork(){return work;}

    }

    static class QuoteData
    {
        @SuppressWarnings("unused")
        private Quote [] data;
        private Quote [] getQuotes() {
            return data;
        }
    }

}
