package $group__.client.ui.core.structures.shapes;

import $group__.client.ui.structures.EnumUISide;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;

import java.util.Map;
import java.util.Optional;

public interface IUIAnchorSet<A extends IUIAnchor> {
	boolean addAnchors(Iterable<? extends A> anchors);

	@SuppressWarnings("UnstableApiUsage")
	default boolean removeAnchors(Iterable<? extends A> anchors) { return removeSides(Streams.stream(anchors).sequential().map(IUIAnchor::getOriginSide).collect(ImmutableList.toImmutableList())); }

	boolean removeSides(Iterable<EnumUISide> sides);

	default void anchor() { anchor(false); }

	default void anchor(boolean check) {
		getFrom().ifPresent(f ->
				getAnchorsView().forEach((s, a) -> {
					if (!(check && a.isAnchoring()))
						a.anchor(f);
				}));
	}

	Map<EnumUISide, A> getAnchorsView();

	Optional<IShapeDescriptor<?>> getFrom();

	default boolean isEmpty() { return getAnchorsView().isEmpty(); }

	boolean clear();
}
