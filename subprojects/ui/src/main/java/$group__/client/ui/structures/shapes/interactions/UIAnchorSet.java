package $group__.client.ui.structures.shapes.interactions;

import $group__.client.ui.core.structures.shapes.IShapeDescriptor;
import $group__.client.ui.core.structures.shapes.IUIAnchor;
import $group__.client.ui.core.structures.shapes.IUIAnchorSet;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.structures.EnumUISide;
import $group__.utilities.MapUtilities;
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

	public static Set<UIAnchor> getAnchorsToMatch(IUIComponent target, double borderThickness) {
		return Sets.newHashSet(
				new UIAnchor(target, EnumUISide.UP, EnumUISide.DOWN, borderThickness),
				new UIAnchor(target, EnumUISide.DOWN, EnumUISide.UP, borderThickness),
				new UIAnchor(target, EnumUISide.LEFT, EnumUISide.RIGHT, borderThickness),
				new UIAnchor(target, EnumUISide.RIGHT, EnumUISide.LEFT, borderThickness));
	}

	@SuppressWarnings("ObjectAllocationInLoop")
	@Override
	public boolean addAnchors(Iterable<? extends A> anchors) {
		boolean ret = false;
		for (A anchor : anchors) {
			switch (anchor.getOriginSide()) {
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
			getAnchors().put(anchor.getOriginSide(), anchor);
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
