package $group__.client.gui.core.structures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public interface IGuiAnchorSet<A extends IGuiAnchor> extends Consumer<Rectangle2D> {
	boolean addAnchors(Iterable<A> anchors);

	@SuppressWarnings("UnstableApiUsage")
	default boolean removeAnchors(Iterable<A> anchors) { return removeSides(Streams.stream(anchors).sequential().map(IGuiAnchor::getFromSide).collect(ImmutableList.toImmutableList())); }

	boolean removeSides(Iterable<EnumGuiSide> sides);

	@Override
	default void accept(Rectangle2D rectangle) { anchor(); }

	default void anchor() { getAnchorsView().forEach((s, a) -> a.anchor(getFrom())); }

	Map<EnumGuiSide, A> getAnchorsView();

	IShapeDescriptor<?, ?> getFrom();

	default boolean isEmpty() { return getAnchorsView().isEmpty(); }

	boolean clear();
}
