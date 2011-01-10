
package axirassa.webapp.utilities;

import org.apache.tapestry5.ValueEncoder;

import axirassa.domain.PingerFrequency;

public class PingerFrequencyValueEncoder implements ValueEncoder<PingerFrequency> {

	@Override
	public String toClient(PingerFrequency value) {
		if (value == null)
			return null;
		else
			return value.getLabel();
	}


	@Override
	public PingerFrequency toValue(String clientValue) {
		if (clientValue == null)
			return null;

		return PingerFrequency.valueOf(clientValue);
	}

}
