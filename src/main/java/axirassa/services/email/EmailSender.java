
package axirassa.services.email;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.antlr.stringtemplate.StringTemplate;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;

import zanoccio.javakit.StringUtilities;

public class EmailSender {
	public static final String EMAIL_WEBAPP = "http://api.postmarkapp.com/email";
	public static final String EMAIL_WEBAPP_TOKEN = "b0ee8591-9e0e-45b6-a7ed-ff1ec9586725";

	private final Object toAddress;
	private final EmailTemplateComposer composer;


	public EmailSender(EmailTemplateComposer composer, String toAddress) {
		this.toAddress = toAddress;
		this.composer = composer;
	}


	public void send(HttpClient client) throws JSONException, ClientProtocolException, IOException {
		HttpPost request = new HttpPost(EMAIL_WEBAPP);

		request.addHeader("Accept", "application/json");
		request.addHeader("X-Postmark-Server-Token", EMAIL_WEBAPP_TOKEN);

		JSONObject json = new JSONObject();

		json.put("From", composer.getEmailTemplate().getFromAddress());
		json.put("To", toAddress);

		StringTemplate html = composer.composeHtml();
		StringTemplate text = composer.composeText();
		StringTemplate subject = composer.composeSubject();

		html.setAttribute("recipient", toAddress);
		text.setAttribute("recipient", toAddress);
		subject.setAttribute("recipient", toAddress);

		json.put("Subject", subject.toString());
		json.put("HtmlBody", html.toString());
		json.put("TextBody", text.toString());

		System.out.println(json.toString());

		InputStreamEntity requestBody = new InputStreamEntity(new ByteArrayInputStream(json.toString()
		        .getBytes("UTF-8")), -1);

		requestBody.setContentType("application/json");

		request.setEntity(requestBody);

		HttpResponse response = client.execute(request);
		System.out.println(StringUtilities.stringFromStream(response.getEntity().getContent()));
	}
}
