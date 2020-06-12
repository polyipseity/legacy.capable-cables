package $group__.utilities.concurrent;

import $group__.annotations.OverridingStatus;
import $group__.utilities.Constants;
import $group__.utilities.extensions.IStrictObject;
import $group__.utilities.helpers.specific.Throwables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;

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
		throw Throwables.rejectUnsupportedOperation();
	}

	@Override
	public <O> boolean trySet(Consumer<O> setter, @Nullable O target, boolean initialize) {
		if (initialize) setter.accept(target);
		return initialize;
	}


	/* SECTION methods */

	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	@Deprecated
	public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final boolean isImmutable() { return true; }
}
