
package axirassa.messaging;

import axirassa.util.AutoSerializingObject;

public class PingerResponseMessage extends AutoSerializingObject {
	private static final long serialVersionUID = 5842601804063738634L;

	private String url;
	private long latencyMillis;
	private long responseTimeMillis;
	private int statusCode;
	private long responseSizeBytes;


	public void setUrl(String url) {
		this.url = url;
	}


	public String getUrl() {
		return url;
	}


	public void setLatencyMillis(long latencyMillis) {
		this.latencyMillis = latencyMillis;
	}


	public long getLatencyMillis() {
		return latencyMillis;
	}


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}


	public int getStatusCode() {
		return statusCode;
	}


	public void setResponseTimeMillis(long responseTimeMillis) {
		this.responseTimeMillis = responseTimeMillis;
	}


	public long getResponseTimeMillis() {
		return responseTimeMillis;
	}


	public void setResponseSizeBytes(long responseSizeBytes) {
		this.responseSizeBytes = responseSizeBytes;
	}


	public long getResponseSizeBytes() {
		return responseSizeBytes;
	}
}
