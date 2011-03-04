
package axirassa.webapp.services;

import java.io.IOException;

import org.hornetq.api.core.HornetQException;

import axirassa.services.phone.PhoneTemplate;

public interface VoiceNotifyService {
	public abstract void startMessage(PhoneTemplate template);


	public abstract void addAttribute(String key, String value);


	public abstract void send() throws HornetQException, IOException;


	public abstract void setPhoneNumber(String phoneNumber);


	public abstract void setExtension(String extension);
}
