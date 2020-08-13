package $group__.client.ui.mvvm.core.binding;

import net.minecraft.util.ResourceLocation;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBindingKey {
	Optional<ResourceLocation> getBindingKey();
}
