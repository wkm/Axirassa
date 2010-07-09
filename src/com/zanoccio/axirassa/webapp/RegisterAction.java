
package com.zanoccio.axirassa.webapp;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.zanoccio.axirassa.domain.User;
import com.zanoccio.axirassa.util.HibernateUtil;

public class RegisterAction extends ActionSupport {
	private static final long serialVersionUID = -2620872177029113364L;

	private String password;
	private String confirmpassword;
	private String email;
	private String confirmemail;


	@Override
	public String execute() {

		System.err.println("Executing...");

		// execute
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		session.save(user);
		session.getTransaction().commit();

		return SUCCESS;
	}


	// @Override
	// public void validate() {
	// if (email != null && !email.equals(confirmemail)) {
	// addFieldError("confirmemail", getText("emailnomatch"));
	// }
	// if (password != null && !password.equals(confirmpassword)) {
	// addFieldError("confirmpassword", getText("passwordnomatch"));
	// }
	// }

	public void setPassword(String password) {
		this.password = password;
	}


	public String getPassword() {
		return password;
	}


	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}


	public String getConfirmpassword() {
		return confirmpassword;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getEmail() {
		return email;
	}


	public void setConfirmemail(String confirmemail) {
		this.confirmemail = confirmemail;
	}


	public String getConfirmemail() {
		return confirmemail;
	}

}
