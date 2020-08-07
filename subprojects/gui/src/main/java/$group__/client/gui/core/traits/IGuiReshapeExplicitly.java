package $group__.client.gui.core.traits;

import $group__.client.gui.core.structures.IShapeDescriptor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ConcurrentModificationException;
import java.util.function.Function;

@FunctionalInterface
@OnlyIn(Dist.CLIENT)
public interface IGuiReshapeExplicitly<S extends IShapeDescriptor<?, ?>> {
	static boolean refresh(IGuiReshapeExplicitly<?> trait) { return trait.reshape(s -> true); }

	boolean reshape(Function<? super S, Boolean> action) throws ConcurrentModificationException;
}
