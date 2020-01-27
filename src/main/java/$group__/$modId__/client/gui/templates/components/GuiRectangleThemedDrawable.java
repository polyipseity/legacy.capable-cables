package $group__.$modId__.client.gui.templates.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.IDrawableThemed;
import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Casts;
import $group__.$modId__.utilities.helpers.Colors;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.awt.*;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiRectangleThemedDrawable<N extends Number, TH extends IThemed.ITheme<TH>, T extends GuiRectangleThemedDrawable<N, TH, T>> extends GuiRectangleDrawable<N, T> implements IDrawableThemed<N, TH, T> {
	/* SECTION variables */

	protected TH theme;


	/* SECTION constructors */

	public GuiRectangleThemedDrawable(Rectangle<N, ?> rect, Color color, IDrawable<N, ?> drawable, TH theme) {
		super(rect, color, drawable);
		this.theme = theme;
		setTheme(getTheme());
	}

	public GuiRectangleThemedDrawable(Rectangle<N, ?> rect, IDrawable<N, ?> drawable, TH theme) { this(rect, Colors.COLORLESS, drawable, theme); }

	public GuiRectangleThemedDrawable(GuiRectangleThemedDrawable<N, TH, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getDrawable(), copy.getTheme()); }


	/* SECTION getters & setters */

	@Override
	public void setDrawable(IDrawable<N, ?> drawable) {
		super.setDrawable(drawable);
		setTheme(getTheme());
	}

	@Override
	public void setTheme(TH theme) {
		this.theme = theme;
		Casts.<IThemed<TH>>castChecked(drawable, castUncheckedUnboxedNonnull(IThemed.class)).ifPresent(t -> t.setTheme(getTheme()));
	}

	@Override
	public TH getTheme() { return theme; }


	/* SECTION methods */

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }


	@Override
	public String toString() { return getToStringString(this, super.toString(),
			new Object[]{"theme", getTheme()}); }

	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getTheme()); }

	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getTheme().equals(t.getTheme())); }

	@Override
	public T clone() {
		T r = super.clone();
		r.theme = tryCloneUnboxedNonnull(theme);
		return r;
	}

	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, TH extends IThemed.ITheme<TH>, T extends Immutable<N, TH, T>> extends GuiRectangleThemedDrawable<N, TH, T> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Color color, IDrawable<N, ?> drawable, TH theme) { super(rect.toImmutable(), tryToImmutableUnboxedNonnull(color), tryToImmutableUnboxedNonnull(drawable), tryToImmutableUnboxedNonnull(theme)); }

		public Immutable(GuiRectangleThemedDrawable<N, TH, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getDrawable(), copy.getTheme()); }


		/* SECTION getters & setters */

		@Override
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		@Override
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }

		@Override
		public void setDrawable(IDrawable<N, ?> drawable) { throw rejectUnsupportedOperation(); }

		@Override
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
