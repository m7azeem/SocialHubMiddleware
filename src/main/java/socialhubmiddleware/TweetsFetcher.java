package socialhubmiddleware;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class TweetsFetcher implements Callable{

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		String username = (String) eventContext.getMessage().getInvocationProperty("username");
		Map<String, String> queryParams = eventContext.getMessage().getInboundProperty("http.query.params");
		String token = (String) queryParams.get("token");
		
		MongoManager mm = new MongoManager();
		
		BasicDBObject output = new BasicDBObject();
		
		if (mm.checkToken(username, token)) {
			//check if twiter-username is saved
			if (!mm.checkIfTwitterAccessExists(username)){
				boolean success = false;
				String message = "No Twitter username saved.";
				output.put("success", success);
				output.put("message", message);
				mm.closeMongoConnection();
				return output;
			}
			
			
			//update username variable (which was socialHub username) with twitterUsername
			username = mm.getTwitterUsername(username);
			TwitterRequestManager twitterRequestManager = new TwitterRequestManager();
			JSONArray jsonArray = twitterRequestManager.getUserTimeline(username, "3");
			if (jsonArray==null){
				//400 response			
				boolean success = false;
				String message = "Twitter connection failed. Twitter account doesn't exist.";
				output.put("success", success);
				output.put("message", message);
			} else {
				if (jsonArray.size()==0){
					boolean success = false;
					String message = "No tweets made by this account";
					output.put("success", success);
					output.put("message", message);
				} else {
					
					//200 response
					ArrayList<TweetPost> tweetsList = parseTweets(jsonArray);			
					boolean success = true;
					output.put("success", success);
					BasicDBList data = new BasicDBList();
					data.addAll(tweetsList);
					output.append("data", data);
				}
			}
		} else {
			boolean success = false;
			String message = "Invalid token | user doens't exists";
			output.put("success", success);
			output.put("message", message);
		}

		mm.closeMongoConnection();
		return output;
	}
	
	public ArrayList<TweetPost> parseTweets(JSONArray jsonArray) {
		ArrayList<TweetPost> tweetsList = new ArrayList<TweetPost>();

		for (int i = 0; i < jsonArray.size(); i++) {
			TweetPost tweetPost = new TweetPost();
			JSONObject jTweet = (JSONObject) jsonArray.get(i);
			JSONObject jUserInfo = (JSONObject) jTweet.get("user");
			tweetPost.user = (String) jUserInfo.get("name");
			tweetPost.tweet = (String) jTweet.get("text");
			tweetPost.timestamp = (String) jTweet.get("created_at");
			try {
				final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
				SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
				sf.setLenient(true);
				Date date = sf.parse(tweetPost.timestamp);
				tweetPost.timestamp = new SimpleDateFormat("MMM dd HH:mm yyyy").format(date);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			} 
			// for tweetPost.type
			JSONObject jEntities = (JSONObject) jTweet.get("entities");
			if (jEntities != null && jEntities.containsKey("media")) {
				JSONArray jMedia = (JSONArray) jEntities.get("media");
				tweetPost.type = (String) ((JSONObject) jMedia.get(0)).get("type");
			}
			JSONObject jExtendedEntities = (JSONObject) jTweet.get("extended_entities");
			if (jExtendedEntities != null && jExtendedEntities.containsKey("media")) {
				JSONArray jMedia = (JSONArray) jExtendedEntities.get("media");
				tweetPost.sourceType = (String) ((JSONObject) jMedia.get(0)).get("type");
				tweetPost.sourceUrl = (String) ((JSONObject) jMedia.get(0)).get("media_url");
			}
			if (tweetPost.type == null) {
				tweetPost.type = "text";
			}
			
			//for tweetPost.likes
			tweetPost.likes = (int) Math.toIntExact((long)jTweet.get("favorite_count"));
			JSONObject jRetweet= (JSONObject) jTweet.get("retweeted_status");
			if (jRetweet != null && jRetweet.containsKey("favorite_count")) {
				//update likes
				tweetPost.likes = (int) Math.toIntExact((long)jRetweet.get("favorite_count"));
				//update retweets
				tweetPost.retweets = (int) Math.toIntExact((long)jRetweet.get("retweet_count"));
			}
			
			tweetsList.add(tweetPost);
		}
		return tweetsList;
	}

}
