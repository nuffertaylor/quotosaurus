package main;

import model.QuoteFetcher;

import model.TwitterBuilder;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class main
{
    public static void main(String args[])
    {
        Tweeter tweeter = new Tweeter();
        tweeter.postNewTweet();
    }

}
