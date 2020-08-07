package $group__.client.gui.core.structures;

import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

@OnlyIn(Dist.CLIENT)
public class GuiAnchorSet<A extends IGuiAnchor> implements IGuiAnchorSet<A> {
	protected final ConcurrentMap<EnumGuiSide, A> anchors = MapUtilities.getMapMakerSingleThreaded().initialCapacity(EnumGuiSide.values().length).makeMap();
	protected final IShapeDescriptor<?, ?> from;

	public GuiAnchorSet(IShapeDescriptor<?, ?> from) { this.from = from; }

	public static Set<GuiAnchor> getAnchorsToMatch(IShapeDescriptor<?, ?> to, double borderThickness) {
		return Sets.newHashSet(
				new GuiAnchor(to, EnumGuiSide.UP, EnumGuiSide.DOWN, borderThickness),
				new GuiAnchor(to, EnumGuiSide.DOWN, EnumGuiSide.UP, borderThickness),
				new GuiAnchor(to, EnumGuiSide.LEFT, EnumGuiSide.RIGHT, borderThickness),
				new GuiAnchor(to, EnumGuiSide.RIGHT, EnumGuiSide.LEFT, borderThickness));
	}

	@Override
	public IShapeDescriptor<?, ?> getFrom() { return from; }

	@Override
	public ImmutableMap<EnumGuiSide, A> getAnchorsView() { return ImmutableMap.copyOf(getAnchors()); }

	@SuppressWarnings("ObjectAllocationInLoop")
	@Override
	public boolean addAnchors(Iterable<A> anchors) {
		boolean ret = false;
		for (A anchor : anchors) {
			switch (anchor.getFromSide()) {
				case UP:
				case DOWN:
					removeSides(EnumSet.of(EnumGuiSide.VERTICAL));
					break;
				case VERTICAL:
					removeSides(EnumSet.of(EnumGuiSide.UP));
					removeSides(EnumSet.of(EnumGuiSide.DOWN));
					break;
				case LEFT:
				case RIGHT:
					removeSides(EnumSet.of(EnumGuiSide.HORIZONTAL));
					break;
				case HORIZONTAL:
					removeSides(EnumSet.of(EnumGuiSide.LEFT));
					removeSides(EnumSet.of(EnumGuiSide.RIGHT));
					break;
				default:
					throw new InternalError();
			}
			getAnchors().put(anchor.getFromSide(), anchor);
			anchor.onContainerAdd(this);
			ret = true;
		}
		return ret;
	}

	@Override
	public boolean removeSides(Iterable<EnumGuiSide> sides) {
		final boolean[] ret = {false};
		sides.forEach(side -> {
			Optional.ofNullable(getAnchors().remove(side)).ifPresent(IGuiAnchor::onContainerRemove);
			ret[0] = true;
		});
		return ret[0];
	}

	@Override
	public boolean clear() { return removeSides(EnumSet.allOf(EnumGuiSide.class)); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<EnumGuiSide, A> getAnchors() { return anchors; }
}
