package $group__.$modId__.utilities.concurrent;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.utilities.extensions.IStrictObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.$modId__.utilities.Constants.PACKAGE;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;

@Immutable
public class MutatorImmutable<T extends MutatorImmutable<T>> implements Serializable, IStrictObject,
		IMutatorImmutablizable<T, T> {
	/* SECTION static variables */

	public static final MutatorImmutable<?> INSTANCE = new MutatorImmutable<>(null);

	private static final Logger LOGGER = LogManager.getLogger(MutatorImmutable.class);
	private static final long serialVersionUID = -123212561635975024L;


	/* SECTION constructors */

	@SuppressWarnings("unused")
	protected MutatorImmutable() {}

	private MutatorImmutable(@SuppressWarnings("unused") @Nullable Object u) { requireRunOnceOnly(LOGGER); }


	/* SECTION getters & setters */

	@Override
	public <O> O mutate(Supplier<O> action, boolean initialize) throws UnsupportedOperationException {
		if (initialize) return action.get();
		throw rejectUnsupportedOperation();
	}

	@Override
	public <O> boolean trySet(Consumer<O> setter, @Nullable O target, boolean initialize) {
		if (initialize) setter.accept(target);
		return initialize;
	}


	/* SECTION methods */

	@Override
	@OverridingStatus(group = PACKAGE)
	@Deprecated
	public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

	@Override
	@OverridingStatus(group = PACKAGE)
	public final boolean isImmutable() { return true; }
}
