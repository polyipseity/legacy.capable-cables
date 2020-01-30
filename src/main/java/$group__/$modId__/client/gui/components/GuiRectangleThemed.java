package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawableThemed;
import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.awt.*;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiRectangleThemed<N extends Number, TH extends IThemed.ITheme<TH>, T extends GuiRectangleThemed<N, TH, T>> extends GuiRectangle<N, T> implements IDrawableThemed<N, TH, T> {
	/* SECTION variables */

	protected TH theme;


	/* SECTION constructors */

	public GuiRectangleThemed(GuiRectangleThemed<N, TH, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getTheme()); }

	public GuiRectangleThemed(Rectangle<N, ?> rect, Color color, TH theme) {
		super(rect, color);
		this.theme = theme;
	}


	/* SECTION getters & setters */

	@Override
	public TH getTheme() { return theme; }

	@Override
	public void setTheme(TH theme) { this.theme = theme; }


	/* SECTION methods */

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }

	@Override
	public int hashCode() {
		return isImmutable() ? getHashCode(this, super.hashCode(), getTheme()) : getHashCode(this, super.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		return isImmutable() ? isEqual(this, o, super.equals(o),
				t -> getTheme().equals(t.getTheme())) : isEqual(this, o, super.equals(o));
	}

	@Override
	public T clone() {
		T r = super.clone();
		r.theme = tryCloneUnboxedNonnull(theme);
		return r;
	}

	@Override
	public String toString() {
		return getToStringString(this, super.toString(),
				new Object[]{"theme", getTheme()});
	}

	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, TH extends ITheme<TH>, T extends Immutable<N, TH, T>> extends GuiRectangleThemed<N, TH, T> {
		/* SECTION constructors */

		public Immutable(GuiRectangleThemed<N, TH, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getTheme()); }

		public Immutable(Rectangle<N, ?> rect, Color color, TH theme) { super(rect.toImmutable(), tryToImmutableUnboxedNonnull(color), tryToImmutableUnboxedNonnull(theme)); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setTheme(TH theme) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}