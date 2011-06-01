
package axirassa.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.tapestry5.json.JSONCollection;

/**
 * Based on:
 * http://tapestry.1045711.n5.nabble.com/JSON-GZip-compression-td2845041.html
 * http://tapestry.1045711.n5.nabble.com/T5-2-Tapestry-IoC-Configuration-remove-
 * td2840319.html
 * 
 * @author wiktor
 */
public class JSONResponse extends CompressableTextResponse {

	private static final String CHARSET = "UTF-8";


	public JSONResponse(JSONCollection json) throws IOException {
		try {
			setResponseData(json.toCompactString().getBytes(CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(System.err);
		}
	}


	@Override
	public String getContentType() {
		return "application/json; charset=" + CHARSET;
	}
}
