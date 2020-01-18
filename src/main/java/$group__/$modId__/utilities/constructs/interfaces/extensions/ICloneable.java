package $group__.$modId__.utilities.constructs.interfaces.extensions;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.unexpectedThrowable;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static java.util.Objects.requireNonNull;

public interface ICloneable<T> extends Cloneable {
	/* SECTION methods */

	/** {@inheritDoc} */
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	T clone();


	@OverridingStatus(group = GROUP, when = When.NEVER)
	default T copy() { return clone(); }


	/* SECTION static methods */

	@Nullable
	static <T> T tryClone(@Nullable T o) throws SecurityException {
		if (o instanceof Cloneable) {
			if (o instanceof ICloneable<?>) return castUnchecked(o, (ICloneable<T>) null).clone();
			Method m;
			try { m = Object.class.getDeclaredMethod("clone"); } catch (NoSuchMethodException e) { throw unexpectedThrowable(e); }
			m.setAccessible(true);
			try { return castUnchecked(m.invoke(o)); }
			catch (IllegalAccessException e) { throw unexpectedThrowable(e); }
			catch (InvocationTargetException e) { return o; }
		}
		return o;
	}

	static <T> T tryCloneNonnull(T o) throws SecurityException { return requireNonNull(tryClone(o)); }
}
