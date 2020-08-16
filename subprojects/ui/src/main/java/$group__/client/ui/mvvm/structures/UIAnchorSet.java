package $group__.client.ui.mvvm.structures;

import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.structures.IUIAnchor;
import $group__.client.ui.mvvm.core.structures.IUIAnchorSet;
import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.lang.ref.WeakReference;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class UIAnchorSet<A extends IUIAnchor>
		implements IUIAnchorSet<A> {
	protected final ConcurrentMap<EnumUISide, A> anchors = MapUtilities.getMapMakerSingleThreaded().initialCapacity(EnumUISide.values().length).makeMap();
	protected final WeakReference<IShapeDescriptor<?>> from;

	public UIAnchorSet(IShapeDescriptor<?> from) {
		this.from = new WeakReference<>(from);
	}

	public static Set<UIAnchor> getAnchorsToMatch(IShapeDescriptor<?> to, double borderThickness) {
		return Sets.newHashSet(
				new UIAnchor(to, EnumUISide.UP, EnumUISide.DOWN, borderThickness),
				new UIAnchor(to, EnumUISide.DOWN, EnumUISide.UP, borderThickness),
				new UIAnchor(to, EnumUISide.LEFT, EnumUISide.RIGHT, borderThickness),
				new UIAnchor(to, EnumUISide.RIGHT, EnumUISide.LEFT, borderThickness));
	}

	@SuppressWarnings("ObjectAllocationInLoop")
	@Override
	public boolean addAnchors(Iterable<A> anchors) {
		boolean ret = false;
		for (A anchor : anchors) {
			switch (anchor.getFromSide()) {
				case UP:
				case DOWN:
					removeSides(EnumSet.of(EnumUISide.VERTICAL));
					break;
				case VERTICAL:
					removeSides(EnumSet.of(EnumUISide.UP));
					removeSides(EnumSet.of(EnumUISide.DOWN));
					break;
				case LEFT:
				case RIGHT:
					removeSides(EnumSet.of(EnumUISide.HORIZONTAL));
					break;
				case HORIZONTAL:
					removeSides(EnumSet.of(EnumUISide.LEFT));
					removeSides(EnumSet.of(EnumUISide.RIGHT));
					break;
				default:
					throw new InternalError();
			}
			getAnchors().put(anchor.getFromSide(), anchor);
			anchor.onContainerAdded(this);
			ret = true;
		}
		return ret;
	}

	@Override
	public boolean removeSides(Iterable<EnumUISide> sides) {
		final boolean[] ret = {false};
		sides.forEach(side -> {
			Optional.ofNullable(getAnchors().remove(side)).ifPresent(IUIAnchor::onContainerRemoved);
			ret[0] = true;
		});
		return ret[0];
	}

	@Override
	public Map<EnumUISide, A> getAnchorsView() { return ImmutableMap.copyOf(getAnchors()); }

	@Override
	public Optional<IShapeDescriptor<?>> getFrom() { return Optional.ofNullable(from.get()); }

	@Override
	public boolean clear() { return removeSides(EnumSet.allOf(EnumUISide.class)); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<EnumUISide, A> getAnchors() { return anchors; }
}
