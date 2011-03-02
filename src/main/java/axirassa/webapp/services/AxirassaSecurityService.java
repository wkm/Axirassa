package axirassa.webapp.services;

public interface AxirassaSecurityService {

	public abstract String getEmail();

	public abstract boolean isUser();

	public abstract boolean isGuest();

	public abstract boolean isAuthenticated();

}
