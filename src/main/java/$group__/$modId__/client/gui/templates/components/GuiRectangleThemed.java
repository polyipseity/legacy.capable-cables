package $group__.$modId__.client.gui.templates.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawableThemed;
import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.awt.*;

import static $group__.$modId__.client.gui.utilities.helpers.Guis.popMatrix;
import static $group__.$modId__.client.gui.utilities.helpers.Guis.pushMatrix;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiRectangleThemed<N extends Number, TH extends IThemed.ITheme<TH>, T extends GuiRectangleThemed<N, TH, T>> extends GuiRectangle<N, T> implements IDrawableThemed<N, TH, T> {
	/* SECTION variables */

	protected TH theme;


	/* SECTION constructors */

	public GuiRectangleThemed(Rectangle<N, ?> rect, Color color, TH theme) {
		super(rect, color);
		this.theme = theme;
	}

	public GuiRectangleThemed(GuiRectangleThemed<N, TH, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getTheme()); }


	/* SECTION getters & setters */

	/** {@inheritDoc} */
	@Override
	public void setTheme(TH theme) { this.theme = theme; }

	/** {@inheritDoc} */
	@Override
	public TH getTheme() { return theme; }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public void draw(Minecraft client) {
		pushMatrix();
		getTheme().drawRect(getRect(), getColor());
		popMatrix();
	}


	/** {@inheritDoc} */
	@Override
	public T toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


	/** {@inheritDoc} */
	@Override
	public String toString() { return getToStringString(this, super.toString(),
				new Object[]{"theme", getTheme()}); }

	/** {@inheritDoc} */
	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getTheme()); }

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getTheme().equals(t.getTheme())); }

	@Override
	public T clone() {
		T r = super.clone();
		r.theme = tryCloneNonnull(theme);
		return r;
	}

	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, TH extends ITheme<TH>, T extends Immutable<N, TH, T>> extends GuiRectangleThemed<N, TH, T> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Color color, TH theme) { super(rect.toImmutable(), tryToImmutableNonnull(color), tryToImmutableNonnull(theme)); }

		public Immutable(GuiRectangleThemed<N, TH, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getTheme()); }


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


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
