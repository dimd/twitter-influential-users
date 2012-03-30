package twitter.data.collection;

import java.util.ArrayList;
import java.util.Arrays;

import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class TwitterStreamListener {
	/**
     * Main entry of this application.
     *
     * @param args track(comma separated filter terms)
     * @throws twitter4j.TwitterException
     */
    public static void main(String[] args) throws TwitterException {
        if (args.length < 1) {
            System.out.println("Usage: java TweetStreamFirstAttempt [track(comma separated filter terms)]");
            System.exit(-1);
        }
        
        StatusListener listener = new MyStatusListener();
        
        TwitterStreamFactory tsf = new TwitterStreamFactory();
        TwitterStream twitterStream = tsf.getInstance();
        twitterStream.addListener(listener);
     
      
        ArrayList<String> track = new ArrayList<String>();
        for (String arg : args) {
        	track.addAll(Arrays.asList(arg.split(",")));
        }
  
        String[] trackArray = track.toArray(new String[track.size()]);

        // filter() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        twitterStream.filter(new FilterQuery(0, null, trackArray));
    }

}
