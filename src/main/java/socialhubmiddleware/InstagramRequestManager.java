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

public class InstagramRequestManager {

	// Can be aquired from :
	// https://www.instagram.com/oauth/authorize?client_id=24fdee85a464408caea2ec1ab8f5d42f&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fdocs%2Findex.html&response_type=token&scope=basic+public_content
	// Default params
	private static String ACCESS_TOKEN = "";

	// Constructor
	public InstagramRequestManager(String access_token) {
		ACCESS_TOKEN = access_token;
	}

	// Returns a JSON Array containing user's Instagram media
	public JSONArray getUserMedia() throws ParseException, UnknownHostException, IOException {

		String urlString = "https://api.instagram.com/v1/users/self/media/recent/?access_token=" + ACCESS_TOKEN;

		String response = makeRequest(urlString);

		JSONParser parser = new JSONParser();
		JSONObject json;
		try {
			json = (JSONObject) parser.parse(response);
		} catch (java.lang.ClassCastException e) {
			return null;
		}
		JSONArray jsonArray = (JSONArray) json.get("data");
		return jsonArray;

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
			String header = "GET " + path + " HTTP/1.0\r\n" + "Host: " + host + "\r\n" + "\r\n";

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
	 * public static void main(String[] args) throws UnknownHostException,
	 * ParseException, IOException { // TODO Auto-generated method stub
	 * InstagramRequestManager instagram = new InstagramRequestManager(
	 * "3304955396.24fdee8.e243c20a324f4e599093b09b57743f1b"); JSONArray
	 * jsonArray = instagram.getUserMedia(); }
	 * 
	 */
}
