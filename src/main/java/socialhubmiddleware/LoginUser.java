package socialhubmiddleware;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class LoginUser {
	public String login(String input){
		BasicDBObject inputObj = (BasicDBObject) JSON.parse(input);
		
		BasicDBObject output = new BasicDBObject();
		
		//check username/password in db
		MongoManager mm = new MongoManager();
		
		if (mm.checkIfUsernameExists(inputObj.getString("username"), inputObj.getString("password"))){
			//return success with token
			output.put("success", true);
			output.put("message", null);
			output.put("token", TokenGenerator.getToken(inputObj.getString("username")));
		} else {
			//return success=false
			output.put("success", false);
			if (mm.checkIfUsernameExists(inputObj.getString("username"))){
				output.put("message", "Incorrect password!");
			} else {
				output.put("message", "Incorrect username!");
			}
			
		}
		System.out.println(mm.checkIfUsernameExists(inputObj.getString("username")));
		mm.closeMongoConnection();
		return output.toString();
	}

}
