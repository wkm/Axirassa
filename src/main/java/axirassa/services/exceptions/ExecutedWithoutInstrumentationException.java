
package axirassa.services.exceptions;

import axirassa.services.pinger.InstrumentedHttpClient;

public class ExecutedWithoutInstrumentationException extends PingerServiceException {
	private static final long serialVersionUID = -4429081532458643148L;


	public ExecutedWithoutInstrumentationException(InstrumentedHttpClient client) {
		super("InstrumentedHttpClient executed without instrumentation");
	}

}
