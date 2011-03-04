
package axirassa.services;

import org.liquibase.maven.plugins.LiquibaseDatabaseDiff;

public class DatabaseDeltaService {

	public void main(String[] args) {
		LiquibaseDatabaseDiff diff = new LiquibaseDatabaseDiff();
	}
}
