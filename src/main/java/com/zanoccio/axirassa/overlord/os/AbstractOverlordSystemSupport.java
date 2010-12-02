
package com.zanoccio.axirassa.overlord.os;

import com.zanoccio.axirassa.overlord.exceptions.OverlordException;
import com.zanoccio.axirassa.overlord.exceptions.UnsupportedPlatformException;

public abstract class AbstractOverlordSystemSupport implements OverlordSystemSupport {
	public static OverlordSystemSupport getSystemSupport() throws OverlordException {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("windows") >= 0)
			return new WindowsSystemSupport();
		else if (os.indexOf("linux") >= 0)
			return new LinuxSystemSupport();
		else if (os.indexOf("mac") >= 0)
			return new MacSystemSupport();
		else
			throw new UnsupportedPlatformException(os);
	}
}
