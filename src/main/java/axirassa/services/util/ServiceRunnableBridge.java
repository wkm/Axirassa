
package axirassa.services.util;

import lombok.extern.slf4j.Slf4j;
import axirassa.services.Service;

@Slf4j
public class ServiceRunnableBridge implements Runnable {
	private Service service;


	public ServiceRunnableBridge(Service service) {
		this.service = service;
	}


	@Override
	public void run() {
		try {
			log.info("Starting service: {}", service);
			service.execute();
		} catch (Throwable t) {
			log.error("Exception ", t);
		}
	}
}
