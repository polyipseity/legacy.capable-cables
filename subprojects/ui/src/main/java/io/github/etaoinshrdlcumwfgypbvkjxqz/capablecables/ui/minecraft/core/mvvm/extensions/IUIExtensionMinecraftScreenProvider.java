package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.registering.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionMinecraftScreenProvider
		extends IUIExtension<INamespacePrefixedString, IUIInfrastructure<?, ?, ?>> {

	Optional<? extends Screen> getScreen();

	Optional<? extends Set<Integer>> getCloseKeys();

	Optional<? extends Set<Integer>> getChangeFocusKeys();

	boolean setPaused(boolean paused);

	boolean setTitle(ITextComponent title);

	@OnlyIn(Dist.CLIENT)
	enum StaticHolder {
		;

		private static final INamespacePrefixedString KEY = ImmutableNamespacePrefixedString.of(IUIExtension.StaticHolder.getDefaultNamespace(), "screen");
		@SuppressWarnings("unchecked")
		private static final Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIExtensionMinecraftScreenProvider, IUIInfrastructure<?, ?, ?>>> TYPE =
				UIExtensionRegistry.getInstance().registerApply(getKey(), k -> new ImmutableExtensionType<>(k, (t, i) -> (Optional<? extends IUIExtensionMinecraftScreenProvider>) i.getExtension(t.getKey())));

		public static INamespacePrefixedString getKey() {
			return KEY;
		}

		public static Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIExtensionMinecraftScreenProvider, IUIInfrastructure<?, ?, ?>>> getType() {
			return TYPE;
		}
	}
}
