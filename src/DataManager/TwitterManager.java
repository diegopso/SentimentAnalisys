package DataManager;

import SentimentAnalisys.SentimentClassifier;
import java.io.IOException;
import java.util.ArrayList;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author johnbyron <https://github.com/johnbyron>
 * @author diegopso <https://github.com/diegopso>
 */
public class TwitterManager {

    SentimentClassifier sentClassifier;
    int LIMIT = 500; //the number of retrieved tweets
    ConfigurationBuilder cb;
    Twitter twitter;

    public TwitterManager() {
        cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("***");
        cb.setOAuthConsumerSecret("***");
        cb.setOAuthAccessToken("***");
        cb.setOAuthAccessTokenSecret("***");
        twitter = new TwitterFactory(cb.build()).getInstance();
        sentClassifier = new SentimentClassifier();
    }

    public void performQuery(String inQuery) throws InterruptedException, IOException, TwitterException {
        Query query = new Query(inQuery);
        query.setCount(100);
        try {
            int count = 0;
            QueryResult r;
            do {
                r = twitter.search(query);
                ArrayList ts = (ArrayList) r.getTweets();

                for (int i = 0; i < ts.size() && count < LIMIT; i++) {
                    count++;
                    Status t = (Status) ts.get(i);
                    String text = t.getText();
                    System.out.println("Text: " + text);
                    String name = t.getUser().getScreenName();
                    System.out.println("User: " + name);
                    String sent = sentClassifier.classify(t.getText());
                    System.out.println("Sentiment: " + sent);
                }
            } while ((query = r.nextQuery()) != null && count < LIMIT);
        } catch (TwitterException te) {
            System.out.println("Couldn't connect: " + te);
        }
    }

}
