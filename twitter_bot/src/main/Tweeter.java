package main;

import model.QuoteFetcher;
import model.TwitterBuilder;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tweeter
{

    private Twitter twitter;

    public Tweeter()
    {
        twitter = TwitterBuilder.getTwitter();
    }

    public void postNewTweet()
    {
        //step 1: fetch a new quote from the repository of quotes
        String quote = QuoteFetcher.next();

        //step 2: check if the quote has been posted by the bot before. If so, skip and fetch a new quote
        while (postedPreviously(quote))
            quote = QuoteFetcher.next();

        //TODO: create two different quotosaurus files, removing used quotes from the one and adding them to another

        //step 3: verify the quote isn't too long, splice into two tweets if so
        List<String> quoteList = listifyQuote(quote);

        //step 4: post the tweet(s)
        postTweets(quoteList);
    }

    private boolean postedPreviously(String quote)
    {
        if (fetchStatuses("Quotosaurus1").contains(quote)) return true;
        else return false;
    }

    private Set<String> fetchStatuses(String handle)
    {
        Set<String> statuses = new HashSet<>();
        Paging page = new Paging(1,200);
        for (int p = 1; p < 11; p++)
        {
            page.setPage(p);
            try
            {
                List<Status> statusList = twitter.getUserTimeline(handle, page);
                for (Status s : statusList)
                    statuses.add(s.getText());
            }
            catch (TwitterException e)
            {
                System.out.println(e.getMessage());
            }
        }
        return statuses;
    }

    private List<String> listifyQuote(String quote)
    {
        if (quote.length() > 280) return splitQuote(quote);
        List<String> quoteList = new ArrayList<>();
        quoteList.add(quote);
        return quoteList;
    }

    private List<String> splitQuote(String quote)
    {
        List<String> quoteList = new ArrayList<>();
        int numberOfTweets = calcNumberOfTweetsRequired(quote.length());
        int curTweet = 1;
        while (quote.length() > 280)
        {
            //start at 273 (to account for '(1/4) ' and go backwards until you find a space
            for (int i = 273; i > 0; i--)
            {
                if (quote.charAt(i) == ' ')
                {
                    String counter = "(" + String.valueOf(curTweet) + "/" + String.valueOf(numberOfTweets) + ") ";
                    String quoteToAdd = counter + quote.substring(0, i);
                    quoteList.add(quoteToAdd);
                    quote = quote.substring(i + 1);
                    curTweet++;
                    break;
                }
            }
        }
        String counter = "(" + String.valueOf(curTweet) + "/" + String.valueOf(numberOfTweets) + ") ";
        quoteList.add(counter + quote);

        return quoteList;
    }

    private int calcNumberOfTweetsRequired(int length)
    {
        for (int i = 1; i < 100; i++) //absolute max number of tweets is 100, though it should NEVER be that big
        {
            if (length/i < 280) return i;
        }
        return 0;
    }

    private void postTweets(List<String> quotes)
    {
        for(String quote : quotes)
            postTweet(quote);
    }

    private void postTweet(String quote)
    {
        try
        {
            Status status = twitter.updateStatus(quote);
            System.out.println("successfully posted: " + status.getText());
        }
        catch (TwitterException e)
        {
            System.out.println("failed");
            e.printStackTrace();
        }
    }
}
