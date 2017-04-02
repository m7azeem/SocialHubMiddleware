package socialhubmiddleware;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TwitterRequestManager {

	// Default Parameters
	private static String BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAANuRzgAAAAAASmQOHUOl9ArndizrwQejiE2FJSU%3Dvqw77hN26IQyUyIabBTV9cVCHOZuQweJbZsBvO3JZJu2wxvexN";

	// Constructor (use TwitterAuthManager to get bearer token when
	// initializing)
	public TwitterRequestManager(String bearerToken) {
		BEARER_TOKEN = bearerToken;
	}
	
	public TwitterRequestManager() {
	}

	// Gets a given user timeline with tweets limited to a given count
	// Returns a JSON object containing all the tweets
	public JSONArray getUserTimeline(String user, String count)
			throws UnknownHostException, IOException, ParseException {

		String urlString = "https://api.twitter.com/1.1/statuses/user_timeline.json?count=" + count + "&screen_name="
				+ user;

		String response = makeRequest(urlString);

		JSONParser parser = new JSONParser();
		JSONArray json = (JSONArray) parser.parse(response);

		return json;
	}

	// A helper function to make GET requests
	private String makeRequest(String urlString) throws UnknownHostException, IOException {

		String response = "";

		try {

			URI uri = new URI(urlString);

			String host = uri.getHost();
			String path = uri.getRawPath();
			if (path == null || path.length() == 0) {
				path = "/";
			}
			String query = uri.getRawQuery();
			if (query != null && query.length() > 0) {
				path += "?" + query;
			}

			// Get port
			String protocol = uri.getScheme();
			int port = uri.getPort();
			if (port == -1) {
				if (protocol.equals("http")) {
					port = 80; // http port
				} else if (protocol.equals("https")) {
					port = 443; // https port
				} else {

				}
			}

			// Create a PORT
			SSLSocketFactory sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket) sslFactory.createSocket(host, port);

			// Initalize buffer reader and writer
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

			// Create header
			String header = "GET " + path + " HTTP/1.0\r\n" + "Host: " + host + "\r\n"
					+ "User-Agent: nacw-middleware\r\n" + "Authorization:Bearer " + BEARER_TOKEN + "\r\n" + "\r\n";

			System.out.println(header);

			// Write request
			out.write(header);
			out.flush();

			// Read params
			String inputLine;

			// Read input
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
				response = inputLine;
			}

			// Close connections
			out.close();
			in.close();

			// DEBUG
			System.out.println("Done.");

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	/*
	 * EXAMPLE USAGE
	 * 
	 * public static void main(String [] args) throws UnknownHostException,
	 * IOException, ParseException{
	 * 
	 * TwitterAuthManager twitterAuth = new TwitterAuthManager();
	 * TwitterRequestManager twitter = new
	 * TwitterRequestManager(twitterAuth.getBearerToken());
	 * 
	 * JSONArray timeline = twitter.getUserTimeline("balrajbains1337", "10");
	 * 
	 * System.out.println(timeline.get(0)); }
	 * 
	 */

}
