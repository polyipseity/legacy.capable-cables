package $group__.client.ui.coredeprecated.traits;

import $group__.client.ui.coredeprecated.structures.IShapeDescriptor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ConcurrentModificationException;
import java.util.function.Function;

@FunctionalInterface
@OnlyIn(Dist.CLIENT)
public interface IUIReshapeExplicitly<S extends IShapeDescriptor<?, ?>> {
	static boolean refresh(IUIReshapeExplicitly<?> trait) { return trait.reshape(s -> true); }

	boolean reshape(Function<? super S, Boolean> action) throws ConcurrentModificationException;
}
