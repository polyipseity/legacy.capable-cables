package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.DefaultExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.registering.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionMinecraftContainerProvider
		extends IUIExtension<INamespacePrefixedString, IUIInfrastructure<?, ?, ?>> {
	INamespacePrefixedString KEY = ImmutableNamespacePrefixedString.of(StaticHolder.getDefaultNamespace(), "container");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIExtensionMinecraftContainerProvider, IUIInfrastructure<?, ?, ?>>> TYPE =
			UIExtensionRegistry.getInstance().registerApply(KEY, k -> new DefaultExtensionType<>(k, (t, i) -> (Optional<? extends IUIExtensionMinecraftContainerProvider>) i.getExtension(t.getKey())));

	Container getContainer();
}
