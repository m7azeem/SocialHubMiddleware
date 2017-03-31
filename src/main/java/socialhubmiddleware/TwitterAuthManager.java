package socialhubmiddleware;

import java.net.*;
import java.io.*;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TwitterAuthManager {
	
	// Returns a bearer token
	public String getBearerToken(){
		
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
	
	 
	}