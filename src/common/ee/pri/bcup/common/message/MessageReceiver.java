package ee.pri.bcup.common.message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation for methods to mark game message receivers.
 * 
 * @author Raivo Laanemets
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageReceiver {
	Class<? extends Message> value();
}
