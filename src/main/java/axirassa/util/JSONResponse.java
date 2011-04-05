
package axirassa.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPOutputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.json.JSONCollection;
import org.apache.tapestry5.services.Response;

/**
 * Based on
 * http://tapestry.1045711.n5.nabble.com/JSON-GZip-compression-td2845041.html
 * 
 * @author wiktor
 */
public class JSONResponse implements StreamResponse {

	private static final String CHARSET = "UTF-8";
	private static final int MIN_DATA_SIZE = 512;

	private byte[] data;
	private boolean isCompressable;


	public JSONResponse (JSONCollection json) {
		try {
			data = json.toCompactString().getBytes(CHARSET);
			isCompressable = data.length >= MIN_DATA_SIZE;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(System.err);
		}
	}


	@Override
	public String getContentType () {
		return "application/json; charset=" + CHARSET;
	}


	@Override
	public InputStream getStream () throws IOException {
		if (!isCompressable)
			return new ByteArrayInputStream(data);

		ByteArrayOutputStream outStream = new ByteArrayOutputStream(data.length);
		GZIPOutputStream gzip = new GZIPOutputStream(outStream);
		gzip.write(data);
		gzip.close();

		byte[] compressed = outStream.toByteArray();
		System.out.println("%%%%%%%%%%%% JSONResponse compressed from: " + data.length + " to " + compressed.length);
		return new ByteArrayInputStream(compressed);
	}


	@Override
	public void prepareResponse (Response response) {
		if (isCompressable)
			response.setHeader("Content-Encoding", "gzip");
	}

}
