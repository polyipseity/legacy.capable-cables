package $group__.$modId__.client.gui.templates.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.awt.*;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiRectangleDrawable<N extends Number, T extends GuiRectangleDrawable<N, T>> extends GuiRectangle<N, T> {
	/* SECTION variables */

	protected IDrawable<N, ?> drawable;


	/* SECTION constructors */

	public GuiRectangleDrawable(Rectangle<N, ?> rect, Color color, /* REMARKS mutable */ IDrawable<N, ?> drawable) {
		super(rect, color);
		setDrawable(this, drawable);
	}

	public GuiRectangleDrawable(Rectangle<N, ?> rect, /* REMARKS mutable */ IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, drawable); }

	public GuiRectangleDrawable(GuiRectangleDrawable<N, ?> c) { this(c.getRect(), c.getColor(), c.getDrawable()); }


	/* SECTION getters & setters */

	public IDrawable<N, ?> getDrawable() { return drawable; }

	public void setDrawable(IDrawable<N, ?> drawable) { setDrawable(this, drawable); }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public void draw(Minecraft game) {
		super.draw(game);
		IDrawable<N, ?> d = getDrawable();
		d.spec().setOffsetAndSize(getRect());
		d.draw(game);
	}


	/** {@inheritDoc} */
	@Override
	public T toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }

	/** {@inheritDoc} */
	@Override
	public String toString() { return getToStringString(this, super.toString(),
			new Object[]{"drawable", getDrawable()}); }

	/** {@inheritDoc} */
	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getDrawable()); }

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getDrawable().equals(t.getDrawable())); }

	/** {@inheritDoc} */
	@Override
	public T clone() {
		T r = super.clone();
		r.drawable = drawable.clone();
		return r;
	}


	/* SECTION static methods */

	protected static <N extends Number> void setDrawable(GuiRectangleDrawable<N, ?> t, IDrawable<N, ?> d) {
		if (d.isImmutable()) throw rejectArguments(d);
		t.drawable = d;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends GuiRectangleDrawable<N, T> {
		/* SECTION variables */

		/* REMARKS internal */ protected final GuiRectangle<N, T> guiRect = new GuiRectangle<>(this);


		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Color color, IDrawable<N, ?> drawable) {
			super(rect.toImmutable(), color, drawable);
			drawable.spec().setOffsetAndSize(getRect());
			this.drawable = drawable.toImmutable();
		}

		public Immutable(Rectangle<N, ?> rect, IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, drawable); }

		public Immutable(GuiRectangleDrawable<N, ?> c) { this(c.getRect(), c.getColor(), c.getDrawable()); }


		/* SECTION getters & setter */

		/** {@inheritDoc} */
		@Override
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setDrawable(IDrawable<N, ?> drawable) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public void draw(Minecraft game) {
			guiRect.draw(game); // CODE super.super.draw(game);
			getDrawable().draw(game);
		}


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
