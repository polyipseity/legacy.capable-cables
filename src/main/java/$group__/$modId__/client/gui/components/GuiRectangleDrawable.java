package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.awt.*;

import static $group__.$modId__.client.gui.utilities.helpers.Guis.translateAndScaleFromTo;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static net.minecraft.client.renderer.GlStateManager.popMatrix;
import static net.minecraft.client.renderer.GlStateManager.pushMatrix;

@SideOnly(Side.CLIENT)
public class GuiRectangleDrawable<N extends Number, T extends GuiRectangleDrawable<N, T>> extends GuiRectangle<N, T> {
	/* SECTION variables */

	protected IDrawable<N, ?> drawable;


	/* SECTION constructors */

	public GuiRectangleDrawable(Rectangle<N, ?> rect, IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, drawable); }

	public GuiRectangleDrawable(Rectangle<N, ?> rect, Color color, IDrawable<N, ?> drawable) {
		super(rect, color);
		this.drawable = drawable;
	}

	public GuiRectangleDrawable(GuiRectangleDrawable<N, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getDrawable()); }


	/* SECTION getters & setters */

	public IDrawable<N, ?> getDrawable() { return drawable; }

	public void setDrawable(IDrawable<N, ?> drawable) { this.drawable = drawable; }


	/* SECTION methods */

	@Override
	public void draw(Minecraft client) {
		super.draw(client);

		pushMatrix();
		IDrawable<N, ?> dt = getDrawable();
		spec().ifPresent(t -> dt.spec().ifPresent(d -> translateAndScaleFromTo(d, t)));
		dt.draw(client);
		popMatrix();
	}


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }

	@Override
	public int hashCode() {
		return isImmutable() ? getHashCode(this, super.hashCode(), getDrawable()) : getHashCode(this, super.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		return isImmutable() ? isEqual(this, o, super.equals(o),
				t -> getDrawable().equals(t.getDrawable())) : isEqual(this, o, super.equals(o));
	}

	@Override
	public T clone() {
		T r = super.clone();
		r.drawable = drawable.copy();
		return r;
	}

	@Override
	public String toString() {
		return getToStringString(this, super.toString(),
				new Object[]{"drawable", getDrawable()});
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends GuiRectangleDrawable<N, T> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, drawable); }

		public Immutable(Rectangle<N, ?> rect, Color color, IDrawable<N, ?> drawable) { super(rect.toImmutable(), tryToImmutableUnboxedNonnull(color), drawable.toImmutable()); }

		public Immutable(GuiRectangleDrawable<N, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getDrawable()); }


		/* SECTION getters & setter */

		@Override
		@Deprecated
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
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
