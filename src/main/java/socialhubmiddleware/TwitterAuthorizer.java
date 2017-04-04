package socialhubmiddleware;

import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class TwitterAuthorizer implements Callable{

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		Map<String, String> queryParams = eventContext.getMessage().getInboundProperty("http.query.params");
		
		String username = (String) eventContext.getMessage().getInvocationProperty("username");
		String token = (String) queryParams.get("token");
		BasicDBObject inputObj = (BasicDBObject) JSON.parse(eventContext.getMessageAsString());
		
		MongoManager mm = new MongoManager();
		
		BasicDBObject output = new BasicDBObject();
		
		if (mm.checkToken(username, token)) {
			TwitterRequestManager twitterRequestManager = new TwitterRequestManager();
			JSONArray jsonArray;
			try {
				jsonArray = twitterRequestManager.getUserTimeline(inputObj.getString("twitter_username"), "1");
			} catch (java.lang.ClassCastException e) {
				jsonArray=null;
			}
			if (jsonArray!=null){
				//save twitter username to db
				mm.updateTwitterUsername(username, inputObj.getString("twitter_username"));
				//prepare output
				boolean success = true;
				String message = "Twitter successfully connected";
				output.put("success", success);
				output.put("message", message);
			} else {//means array is null 
				//400 response			
				boolean success = false;
				String message = "Twitter connection failed. Twitter account doesn't exist.";
				output.put("success", success);
				output.put("message", message);
			}
		} else {
			//400 response
			boolean success = false;
			String message = "Invalid token | user doesn't exist";
			output.put("success", success);
			output.put("message", message);
		}
		
		return output.toString();
	}

}
