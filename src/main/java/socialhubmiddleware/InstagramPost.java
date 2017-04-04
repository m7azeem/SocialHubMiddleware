package socialhubmiddleware;

import java.io.Serializable;

public class InstagramPost implements Serializable {
	String user, type, source, timestamp, caption; 
	int likes;
	@Override
	public String toString() {
		return "InstagramPost [user=" + user + ", type=" + type + ", source=" + source + ", timestamp=" + timestamp
				+ ", caption=" + caption + ", likes=" + likes + "]";
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
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
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	

}
