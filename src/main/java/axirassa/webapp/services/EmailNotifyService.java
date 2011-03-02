
package axirassa.webapp.services;

import java.io.IOException;

import org.hornetq.api.core.HornetQException;

import axirassa.services.email.EmailTemplate;

public interface EmailNotifyService {

	public abstract void startMessage(EmailTemplate template);


	public abstract void addAttribute(String key, Object value);


	public abstract void send() throws HornetQException, IOException;


	public abstract void setToAddress(String email);

}
