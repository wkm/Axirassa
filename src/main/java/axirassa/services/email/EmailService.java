
package axirassa.services.email;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.antlr.stringtemplate.StringTemplate;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.tapestry5.json.JSONObject;
import org.hornetq.api.core.client.ClientSession;

import axirassa.services.Service;
import axirassa.services.pinger.InstrumentedHttpClient;

public class EmailService implements Service {
	private final ClientSession messagingSession;


	public EmailService(ClientSession messagingSession) {
		this.messagingSession = messagingSession;
	}


	private final static String token = "b0ee8591-9e0e-45b6-a7ed-ff1ec9586725";

	private final static String content = "{'From': 'alert@axirassa.com', 'To': 'wiktor@zanoccio.com', 'Subject':'Server [zanoccio.com] HTTP downtime alert (12 minutes)', 'TextBody':'The server zanoccio.com has been down with a 404 message for 12 minutes.'}";


	@Override
	public void execute() throws IllegalStateException, IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost("http://api.postmarkapp.com/email");

		request.addHeader("Accept", "application/json");
		request.addHeader("X-Postmark-Server-Token", token);

		StringTemplate template = EmailTemplateFactory.getTemplateInstance(EmailTemplate.USER_VERIFY_ACCOUNT);

		JSONObject json = new JSONObject();

		json.put("From", "alert@axirassa.com");
		json.put("To", "wmacura@gmail.com");
		json.put("Subject", "Password Reset --- Axirassa Server Monitor");

		// fill out
		template.setAttribute("email", "wmacura@gmail.com");
		template.setAttribute("axlink",
		                      "http://axirassa.com/user/confirmemail/asd87as89789981ad897a7878efadf6a7d57ad7f98afef");
		json.put("HtmlBody", template.toString());

		InputStreamEntity requestBody = new InputStreamEntity(new ByteArrayInputStream(json.toString()
		        .getBytes("UTF-8")), -1);
		requestBody.setContentType("application/json");

		request.setEntity(requestBody);

		HttpResponse response = httpClient.execute(request);

		System.out.println("RESPONSE:");
		System.out.println(InstrumentedHttpClient.readInputStreamBuffer(response.getEntity().getContent()));

	}
}
