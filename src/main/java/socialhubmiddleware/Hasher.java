package socialhubmiddleware;

/*
 *  All hashing is to be done just before any DB CRUD operations. This is to keep consistency in the code.
*/
public class Hasher {
	
	public static String hashPass(String password){
		//do some hashing here
		return password;
	}

}
