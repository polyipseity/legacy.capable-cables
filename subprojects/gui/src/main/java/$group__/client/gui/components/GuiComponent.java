package $group__.client.gui.components;

import $group__.client.gui.structures.GuiAnchors;
import $group__.client.gui.structures.GuiConstraints;
import net.minecraft.client.gui.IRenderable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.Optional;

import static $group__.utilities.helpers.specific.ComparableUtilities.greaterThanOrEqualTo;

@OnlyIn(Dist.CLIENT)
public class GuiComponent implements IRenderable, IGuiEventListenerImproved {
	@Nullable
	protected WeakReference<GuiContainer> parent = null;
	protected final Rectangle2D rectangle = new Rectangle();
	protected final GuiAnchors anchors = new GuiAnchors();
	protected final GuiConstraints constraints = GuiConstraints.CONSTRAINTS_NONE;
	protected EnumState state = EnumState.NEW;

	public Rectangle2D getRectangleView() { return (Rectangle2D) rectangle.clone(); }

	public Optional<Rectangle2D> getPreferredRectangle() { return Optional.of(getRectangleView()); }

	public void render(Point2D mouse, float partialTicks) {}

	protected Optional<? extends GuiRoot> getRoot() {
		if (this instanceof GuiRoot) return Optional.of((GuiRoot) this);
		else if (parent != null) return Optional.ofNullable(parent.get()).flatMap(GuiComponent::getRoot);
		return Optional.empty();
	}

	@OverridingMethodsMustInvokeSuper
	protected void onAdded(GuiContainer parent) { this.parent = new WeakReference<>(parent); }

	@OverridingMethodsMustInvokeSuper
	protected void onInitialize() { state = EnumState.INITIALIZED; }

	@OverridingMethodsMustInvokeSuper
	protected void onResize() {
		anchors.accept(rectangle);
		constraints.accept(rectangle);
		state = EnumState.READY;
	}

	protected void onTick() {}

	@OverridingMethodsMustInvokeSuper
	protected void onRemoved(GuiContainer parent) {
		this.parent = null;
		state = EnumState.NEW;
	}

	@OverridingMethodsMustInvokeSuper
	protected void onClose() { state = EnumState.CLOSED; }

	@OverridingMethodsMustInvokeSuper
	protected void onDestroy() { state = EnumState.DESTROYED; }

	@Override
	@Deprecated
	public final void render(int mouseX, int mouseY, float partialTicks) { render(new Point(mouseX, mouseY), partialTicks); }

	public enum EnumState {
		NEW,
		INITIALIZED,
		READY,
		CLOSED,
		DESTROYED;

		public boolean isReachedBy(EnumState state) { return greaterThanOrEqualTo(state, this); }
	}
}
