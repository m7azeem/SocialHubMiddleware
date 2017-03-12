package socialhubmiddleware;

import org.mindrot.jbcrypt.*;

/*
 *  All hashing is to be done just before any DB CRUD operations. This is to keep consistency in the code.
*/
public class Hasher {
	
	public static String hashPass(String password){

		// Hash a password for the first time
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		return hashedPassword;
	}

	// Check that an unencrypted password matches one that has
	// previously been hashed
	public static boolean checkPass(String password, String hashedPassword){
		if(BCrypt.checkpw(password, hashedPassword)){
			return true;
		}else{
			return false;
		}
	}

}
