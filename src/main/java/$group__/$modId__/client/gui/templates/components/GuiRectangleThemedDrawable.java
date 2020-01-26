package $group__.$modId__.client.gui.templates.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Casts;
import $group__.$modId__.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.awt.*;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiRectangleThemedDrawable<N extends Number, TH extends IThemed.ITheme<TH>, T extends GuiRectangleThemedDrawable<N, TH, T>> extends GuiRectangleThemed<N, TH, T> {
	/* SECTION variables */

	@SuppressWarnings("NotNullFieldNotInitialized")
	protected IDrawable<N, ?> drawable;


	/* SECTION constructors */

	public GuiRectangleThemedDrawable(Rectangle<N, ?> rect, Color color, TH theme, /* REMARKS mutable */ IDrawable<N, ?> drawable) {
		super(rect, color, theme);
		setDrawable(this, drawable);
	}

	public GuiRectangleThemedDrawable(Rectangle<N, ?> rect, TH theme, /* REMARKS mutable */ IDrawable<N, ?> drawable) { this(rect, Color.WHITE, theme, drawable); }

	public GuiRectangleThemedDrawable(GuiRectangleThemedDrawable<N, TH, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getTheme(), copy.getDrawable()); }


	/* SECTION getters & setters */

	public IDrawable<N, ?> getDrawable() { return drawable; }

	public void setDrawable(IDrawable<N, ?> drawable) { setDrawable(this, drawable); }

	@Override
	public void setTheme(TH theme) {
		super.setTheme(theme);
		Casts.<IThemed<TH>>castChecked(drawable, castUncheckedUnboxedNonnull(IThemed.class)).ifPresent(t -> t.setTheme(theme));
	}


	/* SECTION methods */

	@Override
	public void draw(Minecraft client) {
		super.draw(client);
		getDrawable().draw(client);
	}


	@Override
	public String toString() { return getToStringString(this, super.toString(),
				new Object[]{"drawable", getDrawable()}); }

	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getDrawable(), getTheme()); }

	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getDrawable().equals(t.getDrawable())); }

	@Override
	public T clone() {
		T r = super.clone();
		r.drawable = drawable.clone();
		return r;
	}


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }


	/* SECTION static methods */

	protected static <N extends Number, T extends ITheme<T>> void setDrawable(GuiRectangleThemedDrawable<N, T, ?> t, IDrawable<N, ?> d) {
		if (d.isImmutable()) throw rejectArguments(d);
		t.drawable = d;
		t.setTheme(t.getTheme());
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, TH extends ITheme<TH>, T extends Immutable<N, TH, T>> extends GuiRectangleThemedDrawable<N, TH, T> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Color color, TH theme, IDrawable<N, ?> drawable) { super(rect.toImmutable(), tryToImmutableUnboxedNonnull(color), tryToImmutableUnboxedNonnull(theme), drawable.toImmutable()); }

		public Immutable(Rectangle<N, ?> rect, TH theme, IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, theme, drawable); }

		public Immutable(GuiRectangleThemedDrawable<N, TH, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getTheme(), copy.getDrawable()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setTheme(TH theme) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setDrawable(IDrawable<N, ?> drawable) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
