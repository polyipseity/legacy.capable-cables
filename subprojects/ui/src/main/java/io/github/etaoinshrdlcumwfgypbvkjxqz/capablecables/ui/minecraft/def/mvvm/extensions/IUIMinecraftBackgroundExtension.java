package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.MinecraftUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObject;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface IUIMinecraftBackgroundExtension
		extends IUIExtension<IIdentifier, IUIView<?>>, IUIRendererContainerContainer<IUIMinecraftBackgroundExtension.IBackgroundRenderer> {
	@OnlyIn(Dist.CLIENT)
	enum StaticHolder {
		;

		private static final IIdentifier KEY = ImmutableIdentifier.ofInterning(MinecraftUtilities.getNamespace(), "background");
		@SuppressWarnings("unchecked")
		private static final
		IRegistryObject<IExtensionType<IIdentifier, IUIMinecraftBackgroundExtension, IUIView<?>>> TYPE =
				UIExtensionRegistry.getInstance().register(getKey(), new ImmutableExtensionType<>(getKey(), (t, i) -> (Optional<? extends IUIMinecraftBackgroundExtension>) i.getExtension(t.getKey())));

		public static IIdentifier getKey() {
			return KEY;
		}

		public static IRegistryObject<IExtensionType<IIdentifier, IUIMinecraftBackgroundExtension, IUIView<?>>> getType() {
			return TYPE;
		}
	}

	@OnlyIn(Dist.CLIENT)
	interface IBackgroundRenderer
			extends IUIRenderer<IUIMinecraftBackgroundExtension> {
		void render(Screen screen, Point2D mouse, double partialTicks);
	}
}
