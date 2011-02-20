
package axirassa.services.email;

public enum EmailTemplateType {
	HTML(".html"),
	TEXT(".txt"),
	SUBJECT(".subject");

	private String extension;


	EmailTemplateType(String extension) {
		this.extension = extension;
	}


	public String getExtension() {
		return extension;
	}
}
