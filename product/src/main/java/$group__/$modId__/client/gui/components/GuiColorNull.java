package $group__.$modId__.client.gui.components;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.client.gui.traits.IColored;
import $group__.$modId__.utilities.extensions.IStructure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static $group__.$modId__.utilities.Constants.PACKAGE;
import static $group__.$modId__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.$modId__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;

@Immutable
public final class GuiColorNull<T> implements IColored<T>, IStructure<GuiColorNull<T>, GuiColorNull<T>> {
	/* SECTION static variables */

	public static final GuiColorNull<?> INSTANCE = new GuiColorNull<>();

	private static final Logger LOGGER = LogManager.getLogger(GuiColorNull.class);


	/* SECTION constructors */

	private GuiColorNull() { requireRunOnceOnly(LOGGER); }


	/* SECTION static methods */

	public static <T> GuiColorNull<T> getInstance() { return castUncheckedUnboxedNonnull(INSTANCE); }


	/* SECTION methods */

	@Nullable
	@Override
	public T getColor() { return null; }

	@Override
	public boolean trySetColor(@Nullable T color) { return false; }


	@Override
	@OverridingStatus(group = PACKAGE)
	@Deprecated
	public final GuiColorNull<T> toImmutable() { return this; }

	@Override
	@OverridingStatus(group = PACKAGE)
	public final boolean isImmutable() { return true; }


	@Override
	@OverridingStatus(group = PACKAGE)
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	@OverridingStatus(group = PACKAGE)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = PACKAGE)
	public final boolean equals(Object obj) { return areEqual(this, obj, super::equals); }
}
