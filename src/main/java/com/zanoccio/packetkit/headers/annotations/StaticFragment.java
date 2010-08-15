
package com.zanoccio.packetkit.headers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link StaticFragment} annotation is used for automating construction of
 * simple packets.
 * 
 * @author wiktor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StaticFragment {
	int DEFAULT_SLOT = -1;
	int DEFAULT_SIZE = -1;


	/**
	 * The position of this fragment; slots are sorted from smallest to largest.
	 * 
	 * Fields annotated with {@link Data} are automatically assigned slots
	 * starting from 100.
	 * 
	 * Fields annotated with {@link Checksum} are automatically assigned slots
	 * starting from 200.
	 */
	int slot() default DEFAULT_SLOT;


	/**
	 * The size of this fragment in bytes.
	 */
	int size() default DEFAULT_SIZE;


	/**
	 * Constrain the fragment to exactly the specified amount of bytes.
	 */
	boolean fixed() default false;
}
