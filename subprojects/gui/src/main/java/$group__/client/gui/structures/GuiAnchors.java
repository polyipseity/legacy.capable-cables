package $group__.client.gui.structures;

import $group__.client.gui.components.GuiComponent;
import $group__.utilities.Recursions;
import $group__.utilities.specific.Maps;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public final class GuiAnchors {
	private final ConcurrentMap<EnumGuiSide, Anchor> anchors = Maps.MAP_MAKER_SINGLE_THREAD.makeMap();
	@SuppressWarnings("CanBeFinal")
	public double border;

	public GuiAnchors() { this(0); }

	public GuiAnchors(double border) { this.border = border; }

	public ImmutableMap<EnumGuiSide, Anchor> getAnchorsView() { return ImmutableMap.copyOf(anchors); }

	public void add(Anchor... anchors) {
		for (Anchor anchor : anchors) {
			switch (anchor.fromSide) {
				case UP:
				case DOWN:
					remove(EnumGuiSide.VERTICAL);
					break;
				case VERTICAL:
					remove(EnumGuiSide.UP);
					remove(EnumGuiSide.DOWN);
					break;
				case LEFT:
				case RIGHT:
					remove(EnumGuiSide.HORIZONTAL);
					break;
				case HORIZONTAL:
					remove(EnumGuiSide.LEFT);
					remove(EnumGuiSide.RIGHT);
					break;
				default:
					throw BecauseOf.unexpected();
			}
			this.anchors.put(anchor.fromSide, anchor);
			anchor.onAdded();
		}
	}

	public void remove(EnumGuiSide... sides) {
		for (EnumGuiSide side : sides) {
			@Nullable Anchor anchor = anchors.remove(side);
			if (anchor != null) anchor.onRemoved();
		}
	}

	public boolean isEmpty() { return anchors.isEmpty(); }

	public void clear() { remove(EnumGuiSide.values()); }

	public Anchor[] getAnchorsToMatch(GuiComponent<?> from, GuiComponent<?> to) {
		return new Anchor[]{
				new Anchor(from, to, EnumGuiSide.UP, EnumGuiSide.UP),
				new Anchor(from, to, EnumGuiSide.DOWN, EnumGuiSide.DOWN),
				new Anchor(from, to, EnumGuiSide.LEFT, EnumGuiSide.LEFT),
				new Anchor(from, to, EnumGuiSide.RIGHT, EnumGuiSide.RIGHT)
		};
	}

	@OnlyIn(CLIENT)
	@Immutable
	public final class Anchor {
		public final GuiComponent<?> from;
		public final GuiComponent<?> to;
		public final EnumGuiSide fromSide;
		public final EnumGuiSide toSide;
		public final double border;
		private final Consumer<GuiComponent.Events.CReshapeParameter> listCReshape;
		private final Consumer<GuiComponent.Events.CDestroyedParameter> listCDestroyed;

		public Anchor(GuiComponent<?> from, GuiComponent<?> to, EnumGuiSide fromSide, EnumGuiSide toSide) { this(from, to, fromSide, toSide, 0); }

		public Anchor(GuiComponent<?> from, GuiComponent<?> to, EnumGuiSide fromSide, EnumGuiSide toSide, double border) {
			this.from = from;
			this.to = to;
			this.fromSide = fromSide;
			this.toSide = toSide;
			this.border = border;

			listCReshape = par -> { if (!reshape(par)) remove(fromSide); };
			listCDestroyed = par -> remove(fromSide);
			checkNoCycles();
		}

		public boolean reshape(GuiComponent.Events.CReshapeParameter parameter) {
			Rectangle2D rectangle = from.getShapeView().getBounds2D();
			fromSide.getSetter().accept(rectangle, fromSide.getApplyBorder().apply(toSide.getGetter().apply(to.getShapeView().getBounds2D()), GuiAnchors.this.border + border));
			from.setBounds(parameter.handler, parameter.invoker, rectangle);
			return true;
		}

		private void checkNoCycles() {
			Recursions.<Anchor, Void>recurseAsDepthFirstLoop(
					this,
					anchor -> null,
					anchor -> to.data.anchors.getAnchorsView().values(),
					anchor -> {
						throw new IllegalStateException("Anchor cycle from '" + anchor.to + " to " + anchor.from);
					},
					null
			);
		}

		private void onAdded() {
			to.data.events.cReshape.add(listCReshape);
			to.data.events.cDestroyed.add(listCDestroyed);
		}

		private void onRemoved() {
			to.data.events.cReshape.remove(listCReshape);
			to.data.events.cDestroyed.remove(listCDestroyed);
		}
	}
}
