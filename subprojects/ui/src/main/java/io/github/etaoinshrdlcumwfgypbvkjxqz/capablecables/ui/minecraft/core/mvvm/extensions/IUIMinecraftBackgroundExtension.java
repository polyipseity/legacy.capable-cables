package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.IUIMinecraftViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.MinecraftUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface IUIMinecraftBackgroundExtension
		extends IUIExtension<IIdentifier, IUIMinecraftViewComponent<?, ?>>, IUIRendererContainerContainer<IUIMinecraftBackgroundExtension.IBackgroundRenderer> {
	@OnlyIn(Dist.CLIENT)
	enum StaticHolder {
		;

		private static final IIdentifier KEY = ImmutableIdentifier.of(MinecraftUtilities.getNamespace(), "background");
		@SuppressWarnings("unchecked")
		private static final
		IRegistryObject<IExtensionType<IIdentifier, IUIMinecraftBackgroundExtension, IUIMinecraftViewComponent<?, ?>>> TYPE =
				UIExtensionRegistry.getInstance().register(getKey(), new ImmutableExtensionType<>(getKey(), (t, i) -> (Optional<? extends IUIMinecraftBackgroundExtension>) i.getExtension(t.getKey())));

		public static IIdentifier getKey() {
			return KEY;
		}

		public static IRegistryObject<IExtensionType<IIdentifier, IUIMinecraftBackgroundExtension, IUIMinecraftViewComponent<?, ?>>> getType() {
			return TYPE;
		}
	}

	@OnlyIn(Dist.CLIENT)
	interface IBackgroundRenderer
			extends IUIRenderer<IUIMinecraftBackgroundExtension> {
		void render(Screen screen, Point2D mouse, double partialTicks);
	}
}
