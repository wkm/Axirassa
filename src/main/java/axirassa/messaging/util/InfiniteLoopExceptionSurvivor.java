package axirassa.messaging.util;

import java.util.concurrent.Callable;

import zanoccio.javakit.lambda.Function1;

public class InfiniteLoopExceptionSurvivor {
	private final Callable<?> callable;
	private final BackoffStrategy strategy;
	private final Function1<Boolean, Throwable> exceptionCallback;


	public InfiniteLoopExceptionSurvivor(BackoffStrategy strategy, final Callable<?> callable,
	        final Function1<Boolean, Throwable> exceptionCallback) {
		this.strategy = strategy;
		this.callable = callable;
		this.exceptionCallback = exceptionCallback;
	}
	

	public void execute() throws Throwable {
		while(true) {
			try {
				callable.call();
				strategy.tickSuccess();
			} catch(AbortSurvivorMessageException e) {
				break;
			} catch (Throwable t) {
				strategy.tickFailure();
				if(exceptionCallback.call(t) == false)
					break;
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
