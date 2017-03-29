package socialhubmiddleware;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.io.*; 
import java.util.*;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TwitterAuthManager {
	
	private static String CONSUMER_KEY = "KGiMs7bLmPxvnKYo1H1UQl9N9";
	private static String CONSUMER_SECRET = "2rqi8bUMcx7BZiAxMRBFHwb7MJ6WEu9SmeAHRGM33VGAHJD198";
	private static String CALLBACK_URL = "http://localhost:8080";


	public static String encode(String value) 
	{
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        StringBuilder buf = new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }
	
	private static String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException 
	{
	    SecretKey secretKey = null;

	    byte[] keyBytes = keyString.getBytes();
	    secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

	    Mac mac = Mac.getInstance("HmacSHA1");
	    mac.init(secretKey);

	    byte[] text = baseString.getBytes();

	    return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
	}
	
	private static String generateNonce(){
		String uuid_string = UUID.randomUUID().toString();
		uuid_string = uuid_string.replaceAll("-", "");
		String nonce = uuid_string; // any relatively random alphanumeric string will work here
		return nonce;
	}
	
	// Returns a JSON object containing oauth_token, oauth_token_secret and oauth_callback_confirmed
	private JSONObject getOAuthToken(){

		JSONParser parser = new JSONParser();
		JSONObject json = null;
		
		try {
			
			// Parameters
			String urlStr = "https://api.twitter.com/oauth/request_token";
			String oauth_callback = URLEncoder.encode(CALLBACK_URL,"UTF-8");
			String oauth_consumer_key = CONSUMER_KEY;
			String oauth_nonce= generateNonce();
			String oauth_signature = "";
			String oauth_signature_method = "HMAC-SHA1";
			String oauth_timestamp= "";
			String oauth_version="1.0";
			
			// get the timestamp
			Calendar tempcal = Calendar.getInstance();
			long ts = tempcal.getTimeInMillis();// get current time in milliseconds
			oauth_timestamp = (new Long(ts/1000)).toString(); // then divide by 1000 to get seconds
			
			// Generate signature
			String parameter_string = encode("oauth_consumer_key")+"="+encode(oauth_consumer_key)+"&"+encode("oauth_nonce")+"="+encode(oauth_nonce)+"&"+encode("oauth_signature_method")+"="+encode(oauth_signature_method)+"&"+"oauth_timestamp"+"="+encode(oauth_timestamp)+"&"+encode("oauth_version")+"="+encode("1.0"); 
			String signature_base_string = "POST&"+encode(urlStr)+"&"+encode(parameter_string);
			// System.out.println(signature_base_string);
			String signingKey = encode(CONSUMER_SECRET);
			oauth_signature = computeSignature(signature_base_string, signingKey+"&");
			// System.out.println(oauth_signature);
			
			// Get host and path
			URI uri = new URI( urlStr); 
			String host = uri.getHost( ); 
			String path = uri.getRawPath( ); 
			if (path == null || path.length( ) == 0) {
			    path = "/";
			} 
			
			// Get port
			String protocol = uri.getScheme( ); 
			int port = uri.getPort( ); 
			if (port == -1) {
			    if (protocol.equals("http")) { 
			        port = 80; // http port 
			    }
			    else if (protocol.equals("https")) {
			        port = 443; // https port 
			    }
			}
			
			// Create a socket
			SSLSocketFactory sslFactory=(SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket  = (SSLSocket) sslFactory.createSocket(host,port);
			
			// Initalize buffer reader and writer
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			
			// Create header
			String header = "POST "+path+" HTTP/1.0\r\n" + 
                    "Host: "+host+"\r\n"
                    + "Accept: */*\r\n"
                    + "Authorization:OAuth "
                    + "oauth_consumer_key=\"" +oauth_consumer_key
                    + "\",oauth_nonce=\""+oauth_nonce
                    + "\",oauth_signature=\""+URLEncoder.encode(oauth_signature, "UTF-8")
                    + "\",oauth_signature_method=\""+oauth_signature_method
                    + "\",oauth_timestamp=\""+oauth_timestamp
                    + "\",oauth_version=\""+oauth_version
                    + "\"\r\n"
                    + "\r\n";
			
					// DEBUG
					// System.out.println(header);
					
					// Write request
					out.write(header);		
					out.flush();
		
					// Read params
					String inputLine;  
					String response = "";
					
					// Read input
					while ((inputLine = in.readLine()) != null) { 
						// DEBUG
						//System.out.println(inputLine);
						// We assume the lastline should be the response
						response = inputLine;
					}
					
					json = (JSONObject) parser.parse(response);
					
					// Close connections
					out.close();
					in.close(); 

					// DEBUG
					//System.out.println("Done."); 
					
		}catch (Exception e) { 
			e.printStackTrace(); 
		}
		
		return json;
		
	}
	
	private String getBearerToken(){
		
		String access_token = "";
		
		try { 
			
			// Parameters
			String urlStr = "https://api.twitter.com/oauth2/token";
			String base64Token = "S0dpTXM3YkxtUHh2bktZbzFIMVVRbDlOOToycnFpOGJVTWN4N0JaaUF4TVJCRkh3YjdNSjZXRXU5U21lQUhSR00zM1ZHQUhKRDE5OA==";
			
			// Get host and path
			URI uri = new URI( urlStr); 
			String host = uri.getHost( ); 
			String path = uri.getRawPath( ); 
			if (path == null || path.length( ) == 0) {
			    path = "/";
			} 
			
			// Get port
			String protocol = uri.getScheme( ); 
			int port = uri.getPort( ); 
			if (port == -1) {
			    if (protocol.equals("http")) { 
			        port = 80; // http port 
			    }
			    else if (protocol.equals("https")) {
			        port = 443; // https port 
			    }
			}
			
			// Create a socket
			SSLSocketFactory sslFactory=(SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket  = (SSLSocket) sslFactory.createSocket(host,port);
			
			// DEBUG
			// System.out.println("Session ciphersuite is: "+socket.getSession().getCipherSuite() );

			// Create request body
			String data = URLEncoder.encode("grant_type", "UTF-8") + "=" + URLEncoder.encode("client_credentials", "UTF-8");

			// Initalize buffer reader and writer
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			
			// Create header
			String header = "POST "+path+" HTTP/1.0\r\n" + 
                    "Host: "+host+"\r\n"
                    + "Content-Type: application/x-www-form-urlencoded\r\n"
                    + "User-Agent: nacw-middleware\r\n"
                    + "Authorization:Basic "+base64Token+"\r\n"
                    + "Content-Length:29\r\n"
                    +"Cache-Control: no-cache\r\n"
                    + "\r\n";
			
			// DEBUG
			// System.out.println(header+data);
			
			// Write request
			out.write(header);		
			out.write(data);
			out.flush();

			// Read params
			String inputLine;  
			String response = "";
			
			// Read input
			while ((inputLine = in.readLine()) != null) { 
				// DEBUG
				// System.out.println(count); 
				// System.out.println(inputLine);
				// We assume the lastline should be the response
				response = inputLine;
			}

			// Close connections
			out.close();
			in.close(); 

			// DEBUG
			// System.out.println("Done."); 
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			
			access_token =  json.get("access_token").toString();
			
			} catch (Exception e) { 
				e.printStackTrace(); 
			}
		return access_token;
		
	}
	
	// DEBUG
	/*
		public static void main(String [] args){
			TwitterAuthManager tm = new TwitterAuthManager();
			tm.getOAuthToken();
		}
	 */
	}