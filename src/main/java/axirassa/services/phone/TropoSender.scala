package axirassa.services.phone

;


import java.io.ByteArrayInputStream;







import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.tapestry5.json.JSONObject;

import io.Source
;





object TropoSender {
  var TROPO_API_URL: String = "https://api.tropo.com/1.0/sessions"
}





abstract class TropoSender (phoneNumber: String, message: String) {
  def getToken: String


  def send (httpClient: HttpClient) {
    val request = new HttpPost(TropoSender.TROPO_API_URL);
    request.addHeader("Accept", "application/json");

    val json = new JSONObject();
    json.put("token", getToken);
    json.put("phoneNumber", phoneNumber);
    json.put("messageBody", message);

    println("REQUEST: " + json);

    val requestBody = new InputStreamEntity(new ByteArrayInputStream(json.toCompactString().getBytes("UTF-8")), -1);

    requestBody.setContentType("application/json");
    request.setEntity(requestBody);

    val response = httpClient.execute(request);
    val entity = response.getEntity

    println("RESPONSE:");
    println(Source.fromInputStream(entity.getContent, entity.getContentEncoding.getValue).mkString)
  }
}
