/* Author(s) : Azeem, Balraj
 * Date : 21/03/2017
 */

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

		// Set default response variables
		boolean success = true;
		String errorMessage = null;
		
		
		if (!mm.checkIfUsernameExists(inputObj.getString("username"))){
			
			// Create new user if does not exists
			mm.addUser(inputObj);
			errorMessage = "Registration successful";
			
		} else {
			
			success = false;
			errorMessage = "Username already in use!";
			
		}

		
		// Setup response and return 
		BasicDBObject output = new BasicDBObject();
		output.put("success", success);
		output.put("message", errorMessage);
		mm.closeMongoConnection();		
		return output.toString();
	}
		
}
