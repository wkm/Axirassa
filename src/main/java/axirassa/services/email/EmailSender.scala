
package axirassa.services.email

import java.io.ByteArrayInputStream
import java.io.IOException

import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.InputStreamEntity
import org.hornetq.utils.json.JSONException
import org.hornetq.utils.json.JSONObject

import zanoccio.javakit.StringUtilities
import freemarker.template.TemplateException

object EmailSender {
    val EMAIL_WEBAPP = "http://api.postmarkapp.com/email"
    val EMAIL_WEBAPP_TOKEN = "b0ee8591-9e0e-45b6-a7ed-ff1ec9586725"
}

class EmailSender(
    composer : EmailTemplateComposer,
    toAddress : String) {

    def send(client : HttpClient) {    	
        val request = new HttpPost(EmailSender.EMAIL_WEBAPP)

        request.addHeader("Accept", "application/json")
        request.addHeader("X-Postmark-Server-Token", EmailSender.EMAIL_WEBAPP_TOKEN)

        val json = new JSONObject()

        json.put("From", composer.getEmailTemplate.fromAddress)
        json.put("To", toAddress)

        composer.addAttribute("recipient", toAddress)

        val html = composer.composeHtml()
        val text = composer.composeText()
        val subject = composer.composeSubject()

        json.put("Subject", subject)
        json.put("HtmlBody", html)
        json.put("TextBody", text)

        val requestBody = new InputStreamEntity(new ByteArrayInputStream(json.toString()
            .getBytes("UTF-8")), -1)

        requestBody.setContentType("application/json")

        request.setEntity(requestBody)

        val response = client.execute(request)
        System.out.println(StringUtilities.stringFromStream(response.getEntity().getContent()))
    }
}
