
package packetkit.headers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import packetkit.headers.ChecksumMethod;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Checksum {

	ChecksumMethod type();

}
