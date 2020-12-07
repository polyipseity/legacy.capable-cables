package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.MinecraftUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface IUIMinecraftContainerProviderExtension
		extends IUIExtension<IIdentifier, IUIInfrastructure<?, ?, ?>> {

	Container getContainer();

	@OnlyIn(Dist.CLIENT)
	enum StaticHolder {
		;

		private static final IIdentifier KEY = ImmutableIdentifier.of(MinecraftUtilities.getNamespace(), "container");
		@SuppressWarnings("unchecked")
		private static final
		IRegistryObject<IExtensionType<IIdentifier, IUIMinecraftContainerProviderExtension, IUIInfrastructure<?, ?, ?>>> TYPE =
				UIExtensionRegistry.getInstance().register(getKey(), new ImmutableExtensionType<>(getKey(), (t, i) -> (Optional<? extends IUIMinecraftContainerProviderExtension>) i.getExtension(t.getKey())));

		public static IIdentifier getKey() {
			return KEY;
		}

		public static IRegistryObject<IExtensionType<IIdentifier, IUIMinecraftContainerProviderExtension, IUIInfrastructure<?, ?, ?>>> getType() {
			return TYPE;
		}
	}
}
