
package axirassa.services.phone;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.tapestry5.json.JSONObject;

import axirassa.services.pinger.InstrumentedHttpClient;

public abstract class TropoSender {
	public static final String TROPO_API_URL = "https://api.tropo.com/1.0/sessions";

	private String phoneNumber;
	private String message;


	public abstract String getToken();


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public void send(HttpClient httpClient) throws ClientProtocolException, IOException {
		HttpPost request = new HttpPost(TROPO_API_URL);

		request.addHeader("Accept", "application/json");

		JSONObject json = new JSONObject();
		json.put("token", getToken());
		json.put("phoneNumber", phoneNumber);
		json.put("messageBody", message);

		InputStreamEntity requestBody = new InputStreamEntity(new ByteArrayInputStream(json.toCompactString()
		        .getBytes("UTF-8")), -1);
		requestBody.setContentType("application/json");

		request.setEntity(requestBody);

		HttpResponse response = httpClient.execute(request);
		System.out.println("RESPONSE:");
		System.out.println(InstrumentedHttpClient.readInputStreamBuffer(response.getEntity().getContent()));
	}
}
