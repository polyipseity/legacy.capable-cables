package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.ImmutableExtensionType;
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

	Container getContainer();

	@OnlyIn(Dist.CLIENT)
	enum StaticHolder {
		;

		private static final INamespacePrefixedString KEY = ImmutableNamespacePrefixedString.of(IUIExtension.StaticHolder.getDefaultNamespace(), "container");
		@SuppressWarnings("unchecked")
		private static final
		Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIExtensionMinecraftContainerProvider, IUIInfrastructure<?, ?, ?>>> TYPE =
				UIExtensionRegistry.getInstance().registerApply(getKey(), k -> new ImmutableExtensionType<>(k, (t, i) -> (Optional<? extends IUIExtensionMinecraftContainerProvider>) i.getExtension(t.getKey())));

		public static INamespacePrefixedString getKey() {
			return KEY;
		}

		public static Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIExtensionMinecraftContainerProvider, IUIInfrastructure<?, ?, ?>>> getType() {
			return TYPE;
		}
	}
}
