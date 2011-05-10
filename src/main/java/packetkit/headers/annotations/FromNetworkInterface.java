
package packetkit.headers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import packetkit.NetworkInterface;

/**
 * The {@link FromNetworkInterface} annotation indicates that this field should
 * be automatically populated by the {@link NetworkInterface} object through
 * which the packet is sent.
 * 
 * @author wiktor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FromNetworkInterface {
	// empty annotation
}
