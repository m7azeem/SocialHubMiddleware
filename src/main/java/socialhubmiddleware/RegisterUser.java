package socialhubmiddleware;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class RegisterUser {
	
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
		/*if (!mm.checkIfEmailExists(jObj.getString("email"))){
			if (!mm.checkIfUsernameExists(jObj.getString("username"))){
				mm.addUser(jObj);
			} else {
				success = false;
				errorMessage = "Username already in use!";
				
			}
		} else {
			success = false;
			errorMessage = "Email address already in use!";
		}*/
		BasicDBObject output = new BasicDBObject();
		output.put("success", success);
		output.put("message", errorMessage);
		
		mm.closeMongoConnection();
		
		return output.toString();
	}
		
}
