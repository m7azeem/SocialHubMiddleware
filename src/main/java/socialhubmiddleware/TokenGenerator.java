/* Author(s) : Azeem, Balraj
 * Date : 21/03/2017
 */

package socialhubmiddleware;

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

public class TokenGenerator {
	
	// Generatres a token and returns it.
	public static String getToken(){
		
		// Uses SecureRandom library to generate a 32 byte token
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[32]; // 256 bits are converted to 32 bytes;
		random.nextBytes(bytes);
		
		// Base64 encode to string to remove weird characters
		String token = Base64.encodeBase64String(bytes);
		return token;
	}
	
}
