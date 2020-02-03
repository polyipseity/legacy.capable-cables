package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.client.gui.utilities.helpers.Guis;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.awt.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IDirty.isDirty;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiRectangle<N extends Number, T extends GuiRectangle<N, T>> extends Gui implements IDrawable<N, T> {
	/* SECTION variables */

	protected Rectangle<N, ?> rect;
	protected Color color;
	protected long dirtiness;

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@Nullable
	protected Optional<Rectangle.Immutable<N, ?>> cachedSpec;
	protected final AtomicLong cachedSpecDirtiness = new AtomicLong();


	/* SECTION constructors */

	public GuiRectangle(GuiRectangle<N, ?> copy) {
		this(copy.getRect(), copy.getColor());
		dirtiness = copy.dirtiness;
		synchronized (cachedSpecDirtiness) {
			cachedSpec = copy.cachedSpec;
			cachedSpecDirtiness.set(copy.cachedSpecDirtiness.get());
		}
	}

	public GuiRectangle(Rectangle<N, ?> rect, Color color) {
		this.rect = rect;
		this.color = color;
	}


	/* SECTION getters & setters */

	public Rectangle<N, ?> getRect() { return rect; }

	public void setRect(Rectangle<N, ?> rect) {
		this.rect = rect;
		markDirty();
	}

	public Color getColor() { return color; }

	public void setColor(Color color) {
		this.color = color;
		markDirty();
	}


	/* SECTION methods */

	@Override
	public void draw(Minecraft client) { Guis.drawRect(this, getRect(), getColor()); }

	@SuppressWarnings("OptionalAssignedToNull")
	@Override
	public Optional<Rectangle.Immutable<N, ?>> spec() {
		synchronized (cachedSpecDirtiness) {
			if (isDirty(this, cachedSpecDirtiness) || cachedSpec == null) {
				return cachedSpec = Optional.of(new Rectangle.Immutable<>(getRect()));
			} else return cachedSpec;
		}
	}

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }


	@Override
	@OverridingStatus(group = GROUP)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = GROUP)
	public final boolean equals(Object o) { return isEqual(this, o, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	@OverridingMethodsMustInvokeSuper
	public T clone() { return ICloneable.clone(() -> super.clone()); }

	@Override
	@OverridingStatus(group = GROUP)
	public final String toString() { return getToStringString(this, super.toString()); }


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends GuiRectangle<N, T> {
		/* SECTION constructors */

		public Immutable(GuiRectangle<N, ?> copy) { this(copy.getRect(), copy.getColor()); }

		public Immutable(Rectangle<N, ?> rect, Color color) { super(rect.toImmutable(), tryToImmutableUnboxedNonnull(color)); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP)
		public final boolean isImmutable() { return true; }
	}
}
