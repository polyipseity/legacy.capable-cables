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

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiRectangleDrawable<N extends Number, T extends GuiRectangleDrawable<N, T>> extends GuiRectangle<N, T> {
	/* SECTION variables */

	@SuppressWarnings("NotNullFieldNotInitialized")
	protected IDrawable<N, ?> drawable;


	/* SECTION constructors */

	public GuiRectangleDrawable(Rectangle<N, ?> rect, Color color, /* REMARKS mutable */ IDrawable<N, ?> drawable) {
		super(rect, color);
		setDrawable(this, drawable);
	}

	public GuiRectangleDrawable(Rectangle<N, ?> rect, /* REMARKS mutable */ IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, drawable); }

	public GuiRectangleDrawable(GuiRectangleDrawable<N, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getDrawable()); }


	/* SECTION getters & setters */

	public IDrawable<N, ?> getDrawable() { return drawable; }

	public void setDrawable(IDrawable<N, ?> drawable) { setDrawable(this, drawable); }


	/* SECTION methods */

	@Override
	public void draw(Minecraft client) {
		super.draw(client);
		getDrawable().draw(client);
	}


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }

	@Override
	public String toString() { return getToStringString(this, super.toString(),
			new Object[]{"drawable", getDrawable()}); }

	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getDrawable()); }

	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getDrawable().equals(t.getDrawable())); }

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
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Color color, IDrawable<N, ?> drawable) { super(rect.toImmutable(), tryToImmutableUnboxedNonnull(color), drawable.toImmutable()); }

		public Immutable(Rectangle<N, ?> rect, IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, drawable); }

		public Immutable(GuiRectangleDrawable<N, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getDrawable()); }


		/* SECTION getters & setter */

		@Override
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		@Override
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }

		@Override
		public void setDrawable(IDrawable<N, ?> drawable) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
