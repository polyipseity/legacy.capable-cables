package $group__.client.gui.components;

import $group__.annotations.OverridingStatus;
import $group__.client.gui.traits.IColored;
import $group__.utilities.Constants;
import $group__.utilities.extensions.IStructure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static $group__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;

@Immutable
public final class GuiColorNull<T> implements IColored<T>, IStructure<GuiColorNull<T>, GuiColorNull<T>> {
	public static final GuiColorNull<?> INSTANCE;

	private static final Logger LOGGER = LogManager.getLogger(GuiColorNull.class);


	static {
		INSTANCE = new GuiColorNull<>();
	}


	private GuiColorNull() { requireRunOnceOnly(LOGGER); }


	public static <T> GuiColorNull<T> getInstance() { return castUncheckedUnboxedNonnull(INSTANCE); }


	@Nullable
	@Override
	public T getColor() { return null; }

	@Override
	public boolean trySetColor(@Nullable T color) { return false; }


	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	@Deprecated
	public final GuiColorNull<T> toImmutable() { return this; }

	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final boolean isImmutable() { return true; }


	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final boolean equals(Object obj) { return areEqual(this, obj, super::equals); }
}
