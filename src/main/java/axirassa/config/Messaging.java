
package axirassa.config;

/**
 * A namespacing class containing queue names.
 * 
 * @author wiktor
 */
public class Messaging {
	public static String PINGER_REQUEST_QUEUE = "Pinger.Request";
	public static String PINGER_RESPONSE_QUEUE = "Pinger.Response";

	public static String NOTIFY_EMAIL_REQUEST = "Notify.Email.Request";
	public static String NOTIFY_EMAIL_RESPONSE = "Notify.Email.Response";

	public static String NOTIFY_SMS_REQUEST = "Notify.Sms.Request";
	public static String NOTIFY_SMS_RESPONSE = "Notify.Sms.Response";

	public static String NOTIFY_VOICE_REQUEST = "Notify.Voice.Request";
	public static String NOTIFY_VOICE_RESPONSE = "Notify.Voice.Response";
}
