
package axirassa.webapp.ajax;

import java.util.Calendar;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.directwebremoting.Browser;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.ui.dwr.Util;
import org.directwebremoting.util.Logger;

import axirassa.webapp.ajax.util.DaemonThreadFactory;

public class TextChat implements Runnable {
	protected static final Logger log = Logger.getLogger(TextChat.class);


	public TextChat() {
		log.error("OH HAI ######################");
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory());
		executor.scheduleAtFixedRate(this, 1, 50, TimeUnit.MILLISECONDS);
		log.error("SCHEDULER IS RUNNNNNNING");
	}


	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
		String time = cal.getTime().toString() + " ms: " + cal.get(Calendar.MILLISECOND);

		setDisplay(time);
	}


	public void setDisplay(final String output) {
		String page = ServerContextFactory.get().getContextPath();
		page += "/monitor/widget/49";

		Browser.withPage(page, new Runnable() {
			@Override
			public void run() {
				Util.setValue("field", output);
			}
		});
	}
}
