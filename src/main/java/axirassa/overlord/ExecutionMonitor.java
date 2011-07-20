
package axirassa.overlord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import axirassa.overlord.exceptions.ExceptionInMonitorError;

/**
 * A lightweight thread that monitors a process, restarting it if crashes.
 * 
 * @author wiktor
 */
@Slf4j
public class ExecutionMonitor implements Runnable {

	private final ExecutionTarget target;

	private final int id;

	@Getter
	private final boolean limitedRestarts = false;

	@Setter
	@Getter
	private int remainingRestarts = 0;

	@Getter
	private int restartCount = 0;

	private int startCount = 0;
	private final ProcessBuilder builder;
	private Process process;


	public ExecutionMonitor(ExecutionTarget target, int id, ProcessBuilder builder) {
		this.target = target;
		this.id = id;

		this.builder = builder;
	}


	@Override
	public void run() {
		Logger logger = LoggerFactory.getLogger(target.getTargetClass());

		while (remainingRestarts > 0 || limitedRestarts == false) {
			try {
				log.info("STARTING [{}]: {}", getId(), builder.command());
				process = builder.start();

				BufferedReader stdoutstream = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader stderrstream = new BufferedReader(new InputStreamReader(process.getErrorStream()));

				Thread stdoutThread = new Thread(new StreamContentLogger(log, process.getInputStream(), getId(), false));
				Thread stderrThread = new Thread(new StreamContentLogger(log, process.getErrorStream(), getId(), true));

				stdoutThread.start();
				stderrThread.start();

				restartCount++;
				startCount++;
				remainingRestarts--;

				process.waitFor();
				stdoutThread.interrupt();
				stderrThread.interrupt();

				if (!target.isAutoRestart())
					return;
			} catch (InterruptedException e) {
				log.warn("ExecutionMonitor interrupted.");
				return;
			} catch (Exception e) {
				throw new ExceptionInMonitorError(e);
			}
		}

		log.info("[{}] finished.", startCount);
	}


	public void killProcess() {
		if (process == null)
			return;

		log.info("  Killing process");
		process.destroy();
	}


	private String getId() {
		return String.format("[%d-%d]", id, restartCount);
	}
}

class StreamContentLogger implements Runnable {
	private final BufferedReader reader;
	private final boolean isWarning;
	private final String id;
	private final Logger logger;


	public StreamContentLogger(Logger logger, InputStream stream, String id, boolean isWarning) {
		this.logger = logger;
		this.id = id;
		this.isWarning = isWarning;

		reader = new BufferedReader(new InputStreamReader(stream));
	}


	@Override
	public void run() {
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				if (isWarning)
					logger.warn("{} : {}", id, line);
				else
					logger.info("{} : {}", id, line);
			}
		} catch (IOException e) {
			logger.error("Exception: ", e);
		}
	}
}
