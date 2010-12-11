
package axirassa.overlord.os;

public class WindowsSystemSupport extends AbstractOverlordSystemSupport {
	@Override
	public String getJavaExecutable() {
		return "java.exe";
	}

	@Override
	public OverlordSystem getSystem() {
		return OverlordSystem.WINDOWS;
	}
}
