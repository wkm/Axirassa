
package packetkit.headers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a packet fragment, header, or frame is dynamically sized.
 * 
 * @author wiktor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DynamicSize {
	/**
	 * @return an estimate on the size of the object; this is the initial buffer
	 *         size.
	 */
	int suggestedsize() default -1;
}
