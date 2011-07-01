
package axirassa.ioc;

import static org.mockito.Mockito.mock;

import org.apache.tapestry5.ioc.MappedConfiguration;

import axirassa.dao.FeedbackDAO;
import axirassa.dao.PasswordResetTokenDAO;
import axirassa.dao.PingerDAO;
import axirassa.dao.UserEmailAddressDAO;
import axirassa.dao.UserPhoneNumberDAO;

public class DAOMockingModule {
	public static void contributeServiceOverride(MappedConfiguration<Class<?>, Object> configuration) {
		configuration.add(FeedbackDAO.class, mock(FeedbackDAO.class));
		configuration.add(PasswordResetTokenDAO.class, mock(PasswordResetTokenDAO.class));
		configuration.add(PingerDAO.class, mock(PingerDAO.class));
		configuration.add(UserEmailAddressDAO.class, mock(UserEmailAddressDAO.class));
		configuration.add(UserPhoneNumberDAO.class, mock(UserPhoneNumberDAO.class));
	}
}
