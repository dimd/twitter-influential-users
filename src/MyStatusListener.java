import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;


public class MyStatusListener implements StatusListener{
	
	//Create a MongoCon object. This object opens a connection to the mongo server. Default database is "mydb".
	//Tweets are stored in the "tweets" collection and users in the "users" collections.
	//For more info look at the class implemention.
	MongoCon mc = new MongoCon("localhost", 27017);
	
	@Override
	public void onStatus(Status status) {
		ArrayList<HashtagEntity> hashtags = new ArrayList<HashtagEntity>();
		ArrayList<URLEntity> urlEntities = new ArrayList<URLEntity>();
		ArrayList<UserMentionEntity> mentionEntities = new ArrayList<UserMentionEntity>();
		ArrayList<String> hashTexts = new ArrayList<String>();
		ArrayList<String> expandedUrls = new ArrayList<String>();
		ArrayList<Long> mentions = new ArrayList<Long>();
		Double latitude, longitude;
		Long sourceTweetId = null;
		
		//Get tweet id.
        Long tweetId = status.getId();
        
        //Get tweet text.
        String text = status.getText();
        
        //Get hashtags. Convert hashtag objects array to an arraylist of Strings containing the hashtags names.
        hashtags.addAll(Arrays.asList(status.getHashtagEntities()));
        for (HashtagEntity he : hashtags) {
        	hashTexts.add(he.getText());
        }
        
        //Get urls. Convert urlEntity objects array to an arraylist of Strings containing just the (expanded) urls.
        urlEntities.addAll(Arrays.asList(status.getURLEntities()));
        for (URLEntity ue : urlEntities) {
        	expandedUrls.add(ue.getExpandedURL().toString());
        }
        
        //Get mentions. Convert mentionEntity objects array to an arraylist of Longs containing just the ids of the users mentioned.
        mentionEntities.addAll(Arrays.asList(status.getUserMentionEntities()));
        for (UserMentionEntity ume : mentionEntities){
        	mentions.add(ume.getId());
        }
        
        //Get date the tweet was created.
        Date timestamp = status.getCreatedAt();
        
        //Get tweet geolocation information if available. getLatitude and getLongitude throw a NullPonterException if no info is available.
        GeoLocation geol = status.getGeoLocation();
        try {
        	latitude = geol.getLatitude();
        	longitude = geol.getLongitude();
        } catch(NullPointerException e) {
        	latitude = null;
        	longitude = null;
        }
        
        //If tweet is a retweet get the user id of the original poster.
        if (status.isRetweet()) {
        	sourceTweetId = status.getRetweetedStatus().getId();
        }
        
        //Get user information. user id, username, number of followers and number of friends.
        Long userId = status.getUser().getId();
        String userName = status.getUser().getScreenName();
        Integer userFollowersCount = status.getUser().getFollowersCount();
        Integer userFriendsCount = status.getUser().getFriendsCount();
        
        //Pass the gathered information to the mongo connection handler for storing.
        mc.insertTweet(tweetId, text, hashTexts, expandedUrls, mentions, timestamp, latitude, longitude, 
        		sourceTweetId, userId, userName, userFollowersCount, userFriendsCount);
        //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
        /*System.out.println("Tweet Id: " + tweetId);*/
        //System.out.println("Text: " + text);
        /*System.out.println("Hashtags: ");
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
