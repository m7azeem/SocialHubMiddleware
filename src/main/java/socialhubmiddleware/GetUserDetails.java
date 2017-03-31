package socialhubmiddleware;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

import com.mongodb.BasicDBObject;

public class GetUserDetails implements Callable{
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		boolean exists;
		String username = (String) eventContext.getMessage().getInvocationProperty("username");

		MongoManager mm = new MongoManager();

		BasicDBObject output = new BasicDBObject();
		

		if (mm.checkIfUsernameExists(username)) {
			boolean success = true;
			output.put("success", success);
			
			BasicDBObject data = mm.getUserDetails(username);
			data.remove("password");
			output.append("data", data);
		} else {
			boolean success = false;
			String message = "Invalid token | user doens't exists";
			output.put("success", success);
			output.put("message", message);
		}
		// Setup response and return 

		mm.closeMongoConnection();		
		return output.toString();
	}
	
}
