
package com.zanoccio.axirassa.overlord.os;

public class LinuxSystemSupport extends AbstractOverlordSystemSupport {

	@Override
	public String getJavaExecutable() {
		return "java";
	}

	@Override
	public OverlordSystem getSystem() {
		return OverlordSystem.LINUX;
	}

}
