
package axirassa.services.sms;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.tapestry5.json.JSONObject;

import axirassa.services.pinger.InstrumentedHttpClient;

public class SendSMS {

	private final String number;
	private final String message;


	public SendSMS(String number, String message) {
		this.number = number;
		this.message = message;
	}


	public void send() throws ClientProtocolException, IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost("https://api.tropo.com/1.0/sessions");

		request.addHeader("Accept", "application/json");

		JSONObject json = new JSONObject();
		json.put("token", "acba13447a0d0046a2691c590ab46ec2c6a672b151cfa1d42d6de32c26411c6ef9357cc70c5bb3348e7e0362");
		json.put("phoneNumber", number);
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
