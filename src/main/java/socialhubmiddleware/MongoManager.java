/*
 * Author(s) : Azeem, Balraj
 * Date : 21/03/2017
 */

package socialhubmiddleware;

import java.net.UnknownHostException;

import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class MongoManager {
	MongoClient mongoClient = null;
	DBCollection usersCollection = null;
	
	public MongoManager(){
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
	
	public String getPassword(String username){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("username", username);
		DBCursor cursor = usersCollection.find(searchQuery);
		if (cursor.hasNext()) {
			return cursor.next().get("password").toString();
		}
		return null;
	}
	
	public boolean checkIfUsernameExists(String username, String password){
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("username", username);
		searchQuery.put("password", Hasher.hashPass(password));
		DBCursor cursor = usersCollection.find(searchQuery);
		boolean exists =false;
		if (cursor.length()>0) {
			exists = true;
		}
		return exists;
	}
	
	public String addUser(BasicDBObject document){
		
		// hash the password
		document.replace("password", Hasher.hashPass((String) document.get("password")));
		
		// Add default description and profilePictureUrl
		document.append("description","You have not added any description yet.");
		document.append("profilePictureUrl", "https://d30y9cdsu7xlg0.cloudfront.net/png/138926-200.png");
		
		// Debug
		System.out.println(document.toString());
		System.out.println();
		
		// Insert into mongoDB
		Object obj = usersCollection.insert(document);
		
		// Debug
		System.out.println(document.toString());
		
		// Return added document
		return document.toString();
		
	}
	
	public void closeMongoConnection(){
		mongoClient.close();
	}

}
