package $group__.client.ui.mvvm.core.structures;

import $group__.client.ui.core.IShapeDescriptor;
import $group__.client.ui.structures.EnumUISide;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface IUIAnchorSet<A extends IUIAnchor> {
	boolean addAnchors(Iterable<A> anchors);

	@SuppressWarnings("UnstableApiUsage")
	default boolean removeAnchors(Iterable<A> anchors) { return removeSides(Streams.stream(anchors).sequential().map(IUIAnchor::getFromSide).collect(ImmutableList.toImmutableList())); }

	boolean removeSides(Iterable<EnumUISide> sides);

	default void anchor() { getFrom().ifPresent(f -> getAnchorsView().forEach((s, a) -> a.anchor(f))); }

	Map<EnumUISide, A> getAnchorsView();

	Optional<IShapeDescriptor<?>> getFrom();

	default boolean isEmpty() { return getAnchorsView().isEmpty(); }

	boolean clear();
}
