package socialhubmiddleware;

import java.net.UnknownHostException;

import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class MongoManager {
	DBCollection usersCollection = null;
	
	public MongoManager(){
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		DB db = mongoClient.getDB("socialHub");
		usersCollection= db.getCollection("users");
	}
	

	public boolean checkIfEmailExists(String email){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("email", email);
		DBCursor cursor = usersCollection.find(searchQuery);
		boolean exists =false;
		while (cursor.hasNext()) {
			exists = true;
			break;
		}
		return exists;
	}
	
	public boolean checkIfUsernameExists(String username){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("username", username);
		DBCursor cursor = usersCollection.find(searchQuery);
		boolean exists =false;
		while (cursor.hasNext()) {
			exists = true;
			break;
		}
		return exists;
	}
	
	public String addUser(JSONObject jObj){
		BasicDBObject document = new BasicDBObject();
		document.put("username", jObj.getString("username"));
		String password = jObj.getString("password");
		//hash password
		document.put("password", password);
		document.put("email", jObj.getString("email"));
		Object obj = usersCollection.insert(document);
		System.out.println(obj.toString());
		return null;
	}

}
