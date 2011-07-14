package axirassa.messaging.util;

import java.util.concurrent.Callable;

import zanoccio.javakit.lambda.Function1;

public class InfiniteLoopExceptionSurvivor {
	private final Callable<?> callable;
	private final BackoffStrategy strategy;
	private final Function1<?, Throwable> exceptionCallback;


	public InfiniteLoopExceptionSurvivor(BackoffStrategy strategy, final Callable<?> callable,
	        final Function1<?, Throwable> exceptionCallback) {
		this.strategy = strategy;
		this.callable = callable;
		this.exceptionCallback = exceptionCallback;
	}
	

	public void execute() throws Exception {
		while(true) {
			try {
				callable.call();
				strategy.tickSuccess();
			} catch (Throwable t) {
				strategy.tickFailure();
				exceptionCallback.call(t);
			}

			long delay = strategy.computeBackoffDelay();
			if (delay > 0)
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					return;
				}
		}
	}
}
