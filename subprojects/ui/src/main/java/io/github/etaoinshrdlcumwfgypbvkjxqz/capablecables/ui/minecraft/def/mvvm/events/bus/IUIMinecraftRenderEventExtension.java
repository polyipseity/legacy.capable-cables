package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.def.IEventExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.ImmutableExtensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface IUIMinecraftRenderEventExtension
		extends IEventExtension<UIAbstractViewBusEvent<?>> {
	double getPartialTicks();

	@OnlyIn(Dist.CLIENT)
	enum StaticHolder {
		;

		@SuppressWarnings("unchecked")
		private static final IExtensionType<Class<?>, IUIMinecraftRenderEventExtension, UIAbstractViewBusEvent<?>> TYPE =
				new ImmutableExtensionType<>(IUIMinecraftRenderEventExtension.class,
						(type, container) -> (Optional<? extends IUIMinecraftRenderEventExtension>) container.getExtension(type.getKey()));

		public static IExtensionType<Class<?>, IUIMinecraftRenderEventExtension, UIAbstractViewBusEvent<?>> getType() { return TYPE; }
	}
}
