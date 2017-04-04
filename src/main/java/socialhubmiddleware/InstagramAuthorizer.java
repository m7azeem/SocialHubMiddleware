package socialhubmiddleware;

import java.util.Map;

import org.json.simple.JSONArray;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class InstagramAuthorizer implements Callable{

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		Map<String, String> queryParams = eventContext.getMessage().getInboundProperty("http.query.params");
		
		String username = (String) eventContext.getMessage().getInvocationProperty("username");
		String token = (String) queryParams.get("token");
		BasicDBObject inputObj = (BasicDBObject) JSON.parse(eventContext.getMessageAsString());
		
		MongoManager mm = new MongoManager();
		
		BasicDBObject output = new BasicDBObject();
		
		if (mm.checkToken(username, token)) {
			mm.updateInstagramToken(username, inputObj.getString("instagram_access_token"));
			
			boolean success = true;
			String message = "Token successfully aquired";
			output.put("success", success);
			output.put("message", message);
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
