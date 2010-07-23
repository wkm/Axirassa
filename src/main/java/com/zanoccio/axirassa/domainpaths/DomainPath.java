
package com.zanoccio.axirassa.domainpaths;


/**
 * Represents a series of domain model steps relatively closely linked to a
 * series of pages.
 * 
 * A path usually creates a variety of domain models from a couple of settings.
 * 
 * @author wiktor
 */
public interface DomainPath {
	public void execute() throws Exception;
}
