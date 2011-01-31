
package axirassa.messaging;

public class PingerResponseMessage {
	private String url;
	private long latencymillis;
	private long responsetime;


	public void setUrl(String url) {
		this.url = url;
	}


	public String getUrl() {
		return url;
	}


	public void setLatencymillis(long latencymillis) {
		this.latencymillis = latencymillis;
	}


	public long getLatencymillis() {
		return latencymillis;
	}


	public void setResponsetime(long responsetime) {
		this.responsetime = responsetime;
	}


	public long getResponsetime() {
		return responsetime;
	}
}
