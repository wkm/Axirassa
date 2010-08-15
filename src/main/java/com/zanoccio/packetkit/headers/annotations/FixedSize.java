
package com.zanoccio.packetkit.headers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a packet fragment, header, or frame is of fixed size.
 * 
 * @author wiktor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FixedSize {

	final int DYNAMIC = -1;


	/**
	 * @return the size of the object
	 */
	int size();

}
