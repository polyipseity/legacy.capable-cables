package $group__.client.ui.mvvm.core.structures;

import $group__.client.ui.mvvm.structures.EnumUISide;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public interface IUIAnchorSet<A extends IUIAnchor> extends Consumer<Rectangle2D> {
	boolean addAnchors(Iterable<A> anchors);

	@SuppressWarnings("UnstableApiUsage")
	default boolean removeAnchors(Iterable<A> anchors) { return removeSides(Streams.stream(anchors).sequential().map(IUIAnchor::getFromSide).collect(ImmutableList.toImmutableList())); }

	boolean removeSides(Iterable<EnumUISide> sides);

	@Override
	default void accept(Rectangle2D rectangle) { anchor(); }

	default void anchor() { getAnchorsView().forEach((s, a) -> a.anchor(getFrom())); }

	Map<EnumUISide, A> getAnchorsView();

	IShapeDescriptor<?, ?> getFrom();

	default boolean isEmpty() { return getAnchorsView().isEmpty(); }

	boolean clear();
}
