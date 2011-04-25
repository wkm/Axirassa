
package zanoccio.javakit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.Getter;

/**
 * Utility class for asynchronously reading from out stream and writing to
 * another. Both read and write happen directly without buffering. (of course,
 * you can pass in buffered objects to manually control buffering)
 * 
 * @author wiktor
 */
public class DirectStreamForwarder extends Thread {
	@Getter
	private InputStream input;

	@Getter
	private OutputStream output;


	public DirectStreamForwarder (InputStream input, OutputStream output) {
		this.input = input;
		this.output = output;
	}


	@Override
    public void run() {
		try {			
			int singleByte;
			while((singleByte = input.read()) != -1)
				output.write(singleByte);
		} catch(IOException e) {
			throw new Error(e);
		}
	}
}
