
package com.zanoccio.axirassa.overlord.maven;

import org.apache.maven.plugin.AbstractMojo;

/**
 * Executes an overlord configuration
 * 
 * @goal axoverlord
 * @author wiktor
 */
public class OverlordMavenPlugin extends AbstractMojo {
	public void execute() {
		getLog().info("HELLO WORLD");
	}
}
