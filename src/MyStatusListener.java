import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.URLEntity;


public class MyStatusListener implements StatusListener{
	
	MongoCon mc = new MongoCon("localhost", 27017);
	
	@Override
	public void onStatus(Status status) {
		ArrayList<HashtagEntity> hashtags = new ArrayList<HashtagEntity>();
		ArrayList<URLEntity> urlEntities = new ArrayList<URLEntity>();
		ArrayList<String> hashTexts = new ArrayList<String>();
		ArrayList<String> expandedUrls = new ArrayList<String>();
		Double latitude, longitude;
		
        Long tweetId = status.getId();
        String text = status.getText();
        hashtags.addAll(Arrays.asList(status.getHashtagEntities()));
        for (HashtagEntity he : hashtags) {
        	hashTexts.add(he.getText());
        }
        urlEntities.addAll(Arrays.asList(status.getURLEntities()));
        for (URLEntity ue : urlEntities) {
        	expandedUrls.add(ue.getExpandedURL().toString());
        }
        Date timestamp = status.getCreatedAt();
        GeoLocation geol = status.getGeoLocation();
        try {
        	latitude = geol.getLatitude();
        	longitude = geol.getLongitude();
        } catch(NullPointerException e) {
        	latitude = null;
        	longitude = null;
        }
        Long userId = status.getUser().getId();
        String userName = status.getUser().getScreenName();
        int userFollowersCount = status.getUser().getFollowersCount();
        int userFriendsCount = status.getUser().getFriendsCount();
        
        
        //mc.insertTweet(tweetId, text, hashTexts, expandedUrls, timestamp.toString(), geol.getLatitude(), geol.getLongitude(), userId, userName, userFollowersCount, userFriendsCount);
        mc.insertTweet(tweetId, text, hashTexts, expandedUrls, timestamp, latitude, longitude, userId, userName, userFollowersCount, userFriendsCount);
        System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
        /*System.out.println("Tweet Id: " + tweetId);
        System.out.println("Text: " + text);
        System.out.println("Hashtags: ");
        for (String h : hashTexts) {
        	System.out.print(h + ", ");
        }
        System.out.println("Urls: ");
        for (URLEntity ue : urlEntities) {
        	System.out.print(ue.getExpandedURL());
        }
        System.out.println();
        System.out.println("Created at: " + timestamp.toLocaleString());
        try {
        	System.out.println("Geolocation: " + geol.getLatitude() + ", " + geol.getLongitude());
        } catch (NullPointerException e){
        	System.out.println("No geo information.");
        }
        System.out.println("UserId: " + userId);
        System.out.println("User name: " + userName);
        System.out.println("User Followers Count: " + userFollowersCount);
        System.out.println("User Friends Count: " + userFriendsCount);
        System.out.println();*/
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
		System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
	}
	
	@Override
	public void onException(Exception ex) {
		ex.printStackTrace();
	}
}
