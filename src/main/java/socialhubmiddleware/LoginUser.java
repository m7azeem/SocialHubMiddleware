/* Author(s) : Azeem, Balraj
 * Date : 21/03/2017
 */

package socialhubmiddleware;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class LoginUser {
	/*
	 * Steps done by the login(....) method: 
	 * 1. Receive incoming JSON username/password as String 
	 * 2. Covert this JSON format string to BasicDBObject for manipulating the JSON inside
	 * 3. Check if the username exists in the DB 
	 * 4. If so, then retrieve the hashed password of this user from the DB.
	 * 5. Compare both the hashed-password and the password in JSON request using the Hasher class
	 * 6. return success=true if Hasher class confirms similar password, also return an 
	 *    authentication key which will be used by the user for later API calls
	 * 7. else return success=false with error message
	 */
	public String login(String input) {
		BasicDBObject inputObj = (BasicDBObject) JSON.parse(input);

		BasicDBObject output = new BasicDBObject();

		// check username/password in db
		MongoManager mm = new MongoManager();

		if (mm.checkIfUsernameExists(inputObj.getString("username"))) {
			if (Hasher.checkPass(inputObj.getString("password"), mm.getPassword(
					inputObj.getString("username")))) {
				// return success with token
				output.put("success", true);
				output.put("message", null);
				output.put("token", TokenGenerator.getToken());
			} else {
				// return success=false
				output.put("success", false);
				output.put("message", "Incorrect password!");
			}
		} else {
			// return success=false
			output.put("success", false);
			output.put("message", "Incorrect username!");
		}
		/*
		 * if (mm.checkIfUsernameExists(inputObj.getString("username"),
		 * inputObj.getString("password"))){ //return success with token
		 * output.put("success", true); output.put("message", null);
		 * output.put("token",
		 * TokenGenerator.getToken(inputObj.getString("username"))); } else {
		 * //return success=false output.put("success", false); if
		 * (mm.checkIfUsernameExists(inputObj.getString("username"))){
		 * output.put("message", "Incorrect password!"); } else {
		 * output.put("message", "Incorrect username!"); }
		 * 
		 * }
		 */
		mm.closeMongoConnection();
		return output.toString();
	}

}
