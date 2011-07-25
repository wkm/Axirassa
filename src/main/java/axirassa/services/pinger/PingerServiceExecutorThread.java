package axirassa.services.pinger;import java.util.concurrent.Callable;import lombok.extern.slf4j.Slf4j;import org.hornetq.api.core.client.ClientMessage;import org.hornetq.api.core.client.ClientSession;import zanoccio.javakit.lambda.Function1;import axirassa.messaging.util.AbortSurvivorMessageException;import axirassa.messaging.util.CommonBackoffStrategies;import axirassa.messaging.util.InfiniteLoopExceptionSurvivor;import axirassa.model.HttpStatisticsEntity;import axirassa.model.PingerEntity;import axirassa.util.MessagingTools;@Slf4jpublic class PingerServiceExecutorThread implements Runnable {	private ClientSession messaging;	private PingerServiceCoordinator pingRequestQueue;	private PingerServiceCoordinator pingResponseQueue;	private HttpPinger pinger;	public PingerServiceExecutorThread(ClientSession messaging,			PingerServiceCoordinator pingRequestQueue,			PingerServiceCoordinator pingResponseQueue) {		this.messaging = messaging;		this.pingRequestQueue = pingRequestQueue;		this.pingResponseQueue = pingResponseQueue;		this.pinger = new HttpPinger();	}	@Override	public void run() {		InfiniteLoopExceptionSurvivor executor = new InfiniteLoopExceptionSurvivor(		        CommonBackoffStrategies.EXPONENTIAL_BACKOFF_MESSAGING(),		        new Callable<Object>() {			        @Override			        public Object call() throws Exception {				        pingRequestQueue.wait();				        PingerServiceCoordinationMessage coordinationMessage = pingRequestQueue.pollFirst();				        if (coordinationMessage == null)					        // keep waiting					        return null;				        // kill this survivor				        if (coordinationMessage.isShutdownMessage()) {					        log.info("Received shutdown message for thread");					        throw new AbortSurvivorMessageException();				        }				        ClientMessage message = coordinationMessage.getClientMessage();				        PingerEntity entity = MessagingTools.fromMessageBytes(PingerEntity.class, message);				        System.out.println("PING " + entity.getUrl());				        HttpStatisticsEntity statistic = pinger.ping(entity);				        System.out.printf("%50s  TRIGGERS: %s\n", entity.getUrl(), pinger.getTriggers());				        				        coordinationMessage.setStatistic(statistic);				        pingResponseQueue.append(coordinationMessage);				        pingResponseQueue.notify();				        return null;			        }		        }, new Function1<Boolean, Throwable>() {			        @Override			        public Boolean call(Throwable e) throws Throwable {				        log.error("Ignoring exception: ", e);				        return null;			        }		        });		try {			executor.execute();		} catch (Throwable t) {			log.error("Exception", t);		}	}}