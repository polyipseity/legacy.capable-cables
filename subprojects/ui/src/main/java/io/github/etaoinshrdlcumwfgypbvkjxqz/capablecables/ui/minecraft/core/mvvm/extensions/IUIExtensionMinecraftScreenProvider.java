package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.DefaultExtensionType;
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
	INamespacePrefixedString KEY = ImmutableNamespacePrefixedString.of(INamespacePrefixedString.StaticHolder.DEFAULT_NAMESPACE, "screen");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIExtensionMinecraftScreenProvider, IUIInfrastructure<?, ?, ?>>> TYPE =
			UIExtensionRegistry.getInstance().registerApply(KEY, k -> new DefaultExtensionType<>(k, (t, i) -> (Optional<? extends IUIExtensionMinecraftScreenProvider>) i.getExtension(t.getKey())));

	Optional<? extends Screen> getScreen();

	Optional<? extends Set<Integer>> getCloseKeys();

	Optional<? extends Set<Integer>> getChangeFocusKeys();

	boolean setPaused(boolean paused);

	boolean setTitle(ITextComponent title);
}
