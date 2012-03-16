import java.net.UnknownHostException;
import java.util.Date;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.DB;

public class MongoCon {
	private Mongo m;
	private DB db;
	private DBCollection tweets, users;
	
	public MongoCon(String localhost, int port) {
		try {
			m = new Mongo(localhost, port);
			db = m.getDB("mydb");
			tweets = db.getCollection("tweets");
			users = db.getCollection("users");
		} catch(UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	//public void insertTweet(Long tweetId, String text, ArrayList<String> hashTexts, ArrayList<String> urls, 
//			String date, double longitude, double latitude, Long userId, String username, int userFollowersCount, int userFriendsCount) {
	public void insertTweet(Long tweetId, String text, ArrayList<String> hashTexts, ArrayList<String> expandedUrls, 
			Date timestamp, Double latitude, Double longitude,Long userId, String username, int userFollowersCount, int userFriendsCount) {
		//tweet storing
		BasicDBObject tweet = new BasicDBObject();
		tweet.put("_id", tweetId);
		tweet.put("text", text);
		tweet.put("hashtags", hashTexts);
		tweet.put("urls", expandedUrls);
		tweet.put("date", timestamp);
		
		BasicDBObject geo = new BasicDBObject();
		geo.put("latitude", latitude);
		geo.put("longitude", longitude);
		
		tweet.put("geoLoc", geo);
		tweet.put("user", userId);
		tweets.save(tweet);
		
		//user storing
		BasicDBObject user = new BasicDBObject();
		user.put("_id", userId);
		user.put("name", username);
		user.put("followersCount", userFollowersCount);
		user.put("friendsCount", userFriendsCount);
		users.save(user);
	}
	
}
