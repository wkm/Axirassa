
package axirassa.webapp.pages.monitor;

import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.util.EnumValueEncoder;

import axirassa.domain.PingerFrequency;

public class CreateMonitor {
	@Property
	private final ValueEncoder<PingerFrequency> frequencyEncoder = new EnumValueEncoder(PingerFrequency.class);

	@Property
	private String url;

	@Property
	private boolean httpMonitor;

	@Property
	private boolean httpsMonitor;

	@Property
	private boolean icmpMonitor;

	@Property
	private PingerFrequency monitorFrequency;

	@Property
	private final List<PingerFrequency> frequencies = Arrays.asList(PingerFrequency.values());


	public String onSuccess() {
		System.out.println("HTTP: " + httpMonitor);
		System.out.println("HTTPS: " + httpsMonitor);
		System.out.println("ICMP: " + icmpMonitor);
		System.out.println("FREQ: " + monitorFrequency);
		System.out.println("URL: " + url);

		return "Index"; // TODO should forward to summary page for that monitor
	}
}
