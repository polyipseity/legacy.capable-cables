package $group__.client.ui.core.mvvm.views.components.parsers;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIProperty {
	String value();
}
