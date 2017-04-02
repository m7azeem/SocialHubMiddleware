package socialhubmiddleware;

import java.io.Serializable;

public class TweetPost implements Serializable{
	String user,tweet,timestamp,type, source, sourceType, sourceUrl;
	int likes=0, retweets=0;

	@Override
	public String toString() {
		return "TweetPost [user=" + user + ", tweet=" + tweet + ", timestamp=" + timestamp + ", type=" + type
				+ ", source=" + source + ", sourceType=" + sourceType + ", sourceUrl=" + sourceUrl + ", likes=" + likes
				+ "]";
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getRetweets() {
		return retweets;
	}

	public void setRetweets(int retweets) {
		this.retweets = retweets;
	}
	
	
	
	
	

}
