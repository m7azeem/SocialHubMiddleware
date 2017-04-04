package socialhubmiddleware;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class LogoutManager {
	

	public String logout(String input){
		
		boolean exists;
		
		BasicDBObject inputObj = (BasicDBObject) JSON.parse(input);
		//JSONObject jObj = new JSONObject(input);
		
		MongoManager mm =new MongoManager();

		// Set default response variables
		boolean success = true;
		String message = null;
		
		//check if username/token exists
		if (!mm.checkToken(inputObj.getString("username"), inputObj.getString("token"))){
			success= false;
			message = "Invalid token | user doens't exists";
		} else {
			//delete token
			mm.deleteToken(inputObj.getString("username"));
			success = true;
			message = "Logout successful";
		}

		
		// Setup response and return 
		BasicDBObject output = new BasicDBObject();
		output.put("success", success);
		output.put("message", message);
		mm.closeMongoConnection();		
		return output.toString();
	}
}
