package socialhubmiddleware;

import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class RegisterUser {
	
	public String register(String input){
		boolean exists;
		JSONObject jObj = new JSONObject(input);
		MongoManager mm =new MongoManager();
		//check if username or email already exists
		boolean success = true;
		String errorMessage = null;
		if (!mm.checkIfEmailExists(jObj.getString("email"))){
			if (!mm.checkIfUsernameExists(jObj.getString("username"))){
				mm.addUser(jObj);
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
