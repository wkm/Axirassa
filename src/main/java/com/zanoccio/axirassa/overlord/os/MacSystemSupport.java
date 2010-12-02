
package com.zanoccio.axirassa.overlord.os;

public class MacSystemSupport extends AbstractOverlordSystemSupport {

	@Override
	public OverlordSystem getSystem() {
		return OverlordSystem.MAC;
	}


	@Override
	public String getJavaExecutable() {
		return "java";
	}

}
