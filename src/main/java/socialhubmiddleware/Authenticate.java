package socialhubmiddleware;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class Authenticate {
	
	/*
	 * Checks if the session-token is valid  
	 */
	public String authenticate(String input){
		
		BasicDBObject inputObj = (BasicDBObject) JSON.parse(input);

		BasicDBObject output = new BasicDBObject();
		
		// Create new mongo manager
		MongoManager mm = new MongoManager();

		// Check if user exists in the DB
		if (mm.checkIfUsernameExists(inputObj.getString("username"))) {
			// Check if token matches the given token
			if(mm.checkToken(inputObj.getString("username"),inputObj.getString("token"))){
				output.put("success", true);
				output.put("message", "Token valid");
			}else{
				output.put("success",false);
				output.put("message", "Token invalid");
			}
			// Respond
		}else{
			// return success=false
			output.put("success", false);
			output.put("message", "user does not exists");
		}
		
		mm.closeMongoConnection();
		return output.toString();
	}
	
}
