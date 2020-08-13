package $group__.client.ui.mvvm.core.views.components.parsers;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIMethod {
	String value();
	// TODO make this annotation do something?
}
