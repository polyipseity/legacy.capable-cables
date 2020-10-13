package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.IUIViewComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.DefaultExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.registering.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionMinecraftBackground
		extends IUIExtension<INamespacePrefixedString, IUIViewComponentMinecraft<?, ?>>, IUIRendererContainerContainer<IUIExtensionMinecraftBackground.IBackgroundRenderer> {
	INamespacePrefixedString KEY = new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.DEFAULT_NAMESPACE, "background");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIExtensionMinecraftBackground, IUIViewComponentMinecraft<?, ?>>> TYPE =
			UIExtensionRegistry.getInstance().registerApply(KEY, k -> new DefaultExtensionType<>(k, (t, i) -> (Optional<? extends IUIExtensionMinecraftBackground>) i.getExtension(t.getKey())));

	@OnlyIn(Dist.CLIENT)
	interface IBackgroundRenderer
			extends IUIRenderer<IUIExtensionMinecraftBackground> {
		void render(Screen screen, Point2D mouse, double partialTicks);
	}
}
