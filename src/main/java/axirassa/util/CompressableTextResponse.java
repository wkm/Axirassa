
package axirassa.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

abstract public class CompressableTextResponse implements StreamResponse {

	protected static final int MIN_DATA_SIZE = 512;
	protected byte[] dataForSending;
	protected boolean isCompressable;


	protected void setResponseData(byte[] response) throws IOException {
		if (response.length >= MIN_DATA_SIZE)
			isCompressable = true;
		else
			isCompressable = false;

		if (!isCompressable) {
			dataForSending = response;
			return;
		}

		dataForSending = compressData(response);
	}


	private byte[] compressData(byte[] data) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(data.length);
		GZIPOutputStream gzip = new GZIPOutputStream(outStream);
		gzip.write(data);
		gzip.close();

		return outStream.toByteArray();
	}


	@Override
	public InputStream getStream() throws IOException {
		return new ByteArrayInputStream(dataForSending);
	}


	@Override
	public void prepareResponse(Response response) {
		if (isCompressable) {
			response.setHeader("Content-Encoding", "gzip");
			response.setIntHeader("Content-Length", dataForSending.length);
		}
	}

}
