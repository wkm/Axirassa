
package axirassa.webapp.pages.monitor;

import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.annotations.Property;

import axirassa.domain.PingerFrequency;
import axirassa.webapp.utilities.PingerFrequencyValueEncoder;

public class CreateMonitor {
	@Property
	private final PingerFrequencyValueEncoder frequencyEncoder = new PingerFrequencyValueEncoder();

	@Property
	private String url;

	@Property
	private PingerFrequency monitorFrequency;

	@Property
	private final List<PingerFrequency> frequencies = Arrays.asList(PingerFrequency.values());

	@Property
	private PingerFrequency frequencyValue;
}
