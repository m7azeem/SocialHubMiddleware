package socialhubmiddleware;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class RegisterUser {
	/*
	 * Overview: it registers a user
	 * Steps done by the register(....) method: 
	 * 1. Receive incoming JSON username,password and email as String 
	 * 2. Covert this JSON format string to BasicDBObject for manipulating the JSON inside
	 * 3. Check if the username exists in the DB 
	 * 4. If not, then save the new user's info in the DB. 
	 * Note: the method in the MongoManager class does manages password hashing.
	 */
	public String register(String input){
		boolean exists;
		BasicDBObject inputObj = (BasicDBObject) JSON.parse(input);
		//JSONObject jObj = new JSONObject(input);
		MongoManager mm =new MongoManager();
		//check if username or email already exists
		boolean success = true;
		String errorMessage = null;
		
		if (!mm.checkIfEmailExists(inputObj.getString("email"))){
			if (!mm.checkIfUsernameExists(inputObj.getString("username"))){
				mm.addUser(inputObj);
			} else {
				success = false;
				errorMessage = "Username already in use!";
				
			}
		} else {
			success = false;
			errorMessage = "Email address already in use!";
		}

		BasicDBObject output = new BasicDBObject();
		output.put("success", success);
		output.put("message", errorMessage);
		mm.closeMongoConnection();		
		return output.toString();
	}
		
}
