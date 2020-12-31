package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.viewmodels.IUIViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.MinecraftUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObject;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
// COMMENT reason for allowing 'IUIViewModel' only: tick is game logic, not UI logic
public interface IUIMinecraftTickerExtension
		extends IUIExtension<IIdentifier, IUIViewModel<?>> {
	void tick();

	@OnlyIn(Dist.CLIENT)
	enum StaticHolder {
		;

		private static final IIdentifier KEY = ImmutableIdentifier.ofInterning(MinecraftUtilities.getNamespace(), "ticker");
		@SuppressWarnings("unchecked")
		private static final IRegistryObject<IExtensionType<IIdentifier, IUIMinecraftTickerExtension, IUIViewModel<?>>> TYPE =
				UIExtensionRegistry.getInstance().register(getKey(), new ImmutableExtensionType<>(getKey(), (t, i) -> (Optional<? extends IUIMinecraftTickerExtension>) i.getExtension(t.getKey())));

		public static IIdentifier getKey() {
			return KEY;
		}

		public static IRegistryObject<IExtensionType<IIdentifier, IUIMinecraftTickerExtension, IUIViewModel<?>>> getType() {
			return TYPE;
		}
	}
}
