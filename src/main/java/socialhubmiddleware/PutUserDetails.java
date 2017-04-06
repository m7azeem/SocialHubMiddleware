package socialhubmiddleware;

import java.util.Map;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class PutUserDetails implements Callable{
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		//get incoming data
		String username =  eventContext.getMessage().getInvocationProperty("username");
		BasicDBObject userDetails = (BasicDBObject) JSON.parse(eventContext.getMessageAsString());
		
		MongoManager mongoManager = new MongoManager();
		
		// Set default response variables
		boolean success = true;
		String message = null;
		
		Map<String, String> queryParams = eventContext.getMessage().getInboundProperty("http.query.params");
		String token = (String) queryParams.get("token");
		
		//save details if user exists
		if (mongoManager.checkIfUsernameExists(username)){
			if (mongoManager.checkToken(username, token)) {
				mongoManager.updateUserDetails(username, userDetails);
				message="User details were updated successfully";
			}
		} else {
			success= false;
			message = "Invalid token | user doens't exists";
		}
		
		// Setup response and return 
		BasicDBObject output = new BasicDBObject();
		output.put("success", success);
		output.put("message", message);
		mongoManager.closeMongoConnection();		
		return output.toString();
		
	}
}
