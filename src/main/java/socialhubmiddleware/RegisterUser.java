package socialhubmiddleware;

import org.json.JSONObject;

import com.google.gson.JsonObject;

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
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("success", success);
		jsonObject.addProperty("message", errorMessage);
		System.out.println(jsonObject.toString());
		System.out.println();
		System.out.println();
		return jsonObject.toString();
	}
	
	
}
