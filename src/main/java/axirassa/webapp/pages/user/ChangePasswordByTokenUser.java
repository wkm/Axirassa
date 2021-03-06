package axirassa.webapp.pages.user;


import axirassa.dao.PasswordResetTokenDAO;
import axirassa.model.PasswordResetTokenEntity;
import axirassa.model.UserEntity;
import axirassa.webapp.components.AxForm;
import axirassa.webapp.components.AxPasswordField;
import axirassa.webapp.pages.Index;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;


public class ChangePasswordByTokenUser {

	@Inject
	private Session database;

	@Inject
	private PasswordResetTokenDAO passwordResetTokenDAO;

	@Property
	@Persist
	private String token;

	@Persist
	private PasswordResetTokenEntity tokenEntity;

	@Property
	@Persist
	private UserEntity user;

	@Property
	private boolean isTokenInvalid = false;

	@Property
	private String newPassword;

	@Property
	private String confirmPassword;

	@Component
	private AxPasswordField confirmPasswordField;

	@Component
	private AxForm form;


	public Object onActivate () {
		return Index.class;
	}


	public Object onActivate (String token) {
		this.token = token;
		tokenEntity = passwordResetTokenDAO.getByToken(token);

		if (tokenEntity == null) {
			isTokenInvalid = true;
			return true;
		}

		isTokenInvalid = false;
		user = tokenEntity.getUser();

		return true;
	}


	public Object onPassivate () {
		System.out.println("passivating");
		return token;
	}


	public void onValidateFromForm () {
		System.out.println("validating form");
		if (newPassword != null && confirmPassword != null && !newPassword.equals(confirmPassword))
			form.recordError(confirmPasswordField, "Passwords do not match");
	}


	@CommitAfter
	public Object onSuccessFromForm () {
		System.out.println("form success");

		if (user == null)
			return true;

		if (!isTokenInvalid) {
			System.out.println("saving");
			user.createPassword(newPassword);
			database.update(user);
			database.delete(tokenEntity);
		}

		return LoginUser.class;
	}
}
