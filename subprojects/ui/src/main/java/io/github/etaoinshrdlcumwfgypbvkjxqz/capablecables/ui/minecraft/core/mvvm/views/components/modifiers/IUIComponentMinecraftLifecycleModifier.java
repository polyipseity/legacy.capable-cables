package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.BiConsumer;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
@OnlyIn(Dist.CLIENT)
public interface IUIComponentMinecraftLifecycleModifier {
	void tick(IUIComponentContext context);

	@OnlyIn(Dist.CLIENT)
	enum StaticHolder {
		;

		public static void handleComponentModifiers(IUIComponentMinecraft component,
		                                            Iterable<? extends IUIComponentModifier> modifiers,
		                                            IUIComponentContext context,
		                                            BiConsumer<? super IUIComponentMinecraftLifecycleModifier, ? super IUIComponentContext> action) {
			IUIComponentModifier.StaticHolder.handleComponentModifiers(component,
					modifiers,
					IUIComponentMinecraftLifecycleModifier.class,
					modifier -> action.accept(modifier, context));
		}
	}
}
