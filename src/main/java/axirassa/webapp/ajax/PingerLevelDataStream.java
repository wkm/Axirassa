
package axirassa.webapp.ajax;

import java.util.Calendar;
import java.util.Random;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.util.Logger;

public class PingerLevelDataStream {
	protected static final Logger log = Logger.getLogger(PingerLevelDataStream.class);
	private int pingerId;


	public void subscribe(int pingerId) throws InterruptedException {
		this.pingerId = pingerId;

		for (int i = 0; i < 5; i++) {
			Calendar cal = Calendar.getInstance();
			String time = cal.getTime().toString() + " ms: " + cal.get(Calendar.MILLISECOND);

			setDisplay(time);
			Thread.sleep(1000);
		}
	}


	public void setDisplay(final String output) {
		Browser.withCurrentPage(new Runnable() {
			@Override
			public void run() {
				int randomint = new Random().nextInt(100);
				ScriptSessions.addFunctionCall("addDataPoint", pingerId * 1000 + randomint);
			}
		});
	}
}
