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

public class InstagramPostsFetcher implements Callable{

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		String username = (String) eventContext.getMessage().getInvocationProperty("username");
		Map<String, String> queryParams = eventContext.getMessage().getInboundProperty("http.query.params");
		String token = (String) queryParams.get("token");
		
		MongoManager mm = new MongoManager();
		
		BasicDBObject output = new BasicDBObject();
		
		if (mm.checkToken(username, token)) {
			InstagramRequestManager instagramRequestManager = new InstagramRequestManager(mm.getInstagramToken(username));
			JSONArray jsonArray = instagramRequestManager.getUserMedia();
			if (jsonArray==null){
				//400 response			
				boolean success = false;
				String message = "Instagram connection failed. Instagram account doesn't exist.";
				output.put("success", success);
				output.put("message", message);
			} else {
				if (jsonArray.size()==0){
					boolean success = false;
					String message = "No posts made by this account";
					output.put("success", success);
					output.put("message", message);
				} else {
					//200 response
					ArrayList<InstagramPost> postsList = parseTweets(jsonArray);
					boolean success = true;
					output.put("success", success);
					BasicDBList feed = new BasicDBList();
		          	feed.addAll(postsList);
		          	output.append("feed", feed);
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
	
	public ArrayList<InstagramPost> parseTweets(JSONArray jsonArray) {
		ArrayList<InstagramPost> postsList = new ArrayList<InstagramPost>();

		for (int i = 0; i < jsonArray.size(); i++) {
			InstagramPost instaPost = new InstagramPost();
			JSONObject jPost = (JSONObject) jsonArray.get(i);
			
			instaPost.type = (String) jPost.get("type");
			instaPost.likes = (int) Math.toIntExact((long) ((JSONObject)jPost.get("likes")).get("count"));
			instaPost.user = (String) ((JSONObject)jPost.get("user")).get("full_name");
			instaPost.caption = (String) ((JSONObject)jPost.get("caption")).get("text");
			instaPost.source =  (String) ((JSONObject)((JSONObject)jPost.get("images")).get("standard_resolution")).get("url");
			instaPost.timestamp = (String) jPost.get("created_time");//in unix-time format
			Date date = new Date((long)Long.parseLong(instaPost.timestamp) *1000);
			instaPost.timestamp = new SimpleDateFormat("MMM dd HH:mm yyyy").format(date);
			postsList.add(instaPost);
		}
		return postsList;
	}

}
