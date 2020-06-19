package main;

import model.QuoteFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TweeterTest
{
    private Tweeter tweeter;

    @BeforeEach
    void setup()
    {
        tweeter = new Tweeter();
    }

    @Test
    void longTweetTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        String quote = QuoteFetcher.next();

        while (quote.length() < 280)
            quote = QuoteFetcher.next();

        Method method = Tweeter.class.getDeclaredMethod("listifyQuote", String.class);
        method.setAccessible(true);
        List<String> quoteList = (List<String>) method.invoke(tweeter, quote);

        assert(quoteList.size() > 1);

        for (String s : quoteList)
            System.out.println(s);
    }

    @Test
    void alreadyBeenPostedTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        String quote = "I have no mercy or compassion in me for a society that will crush people, and then penalize them for not being able to stand up under the weight. -Malcolm X  (The Autobiography of Malcolm X)";

        Method method = Tweeter.class.getDeclaredMethod("postedPreviously", String.class);
        method.setAccessible(true);
        boolean postedPreviously = (boolean) method.invoke(tweeter, quote);
        assert(postedPreviously);

        if(postedPreviously) System.out.println("already been posted.");
        else System.out.println("not posted");
    }
}