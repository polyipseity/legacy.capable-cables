package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.events.bus;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.events.bus.IUIMinecraftRenderEventExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Immutable
@OnlyIn(Dist.CLIENT)
public final class UIImmutableMinecraftRenderEventExtension
		implements IUIMinecraftRenderEventExtension {
	private final double partialTicks;

	private UIImmutableMinecraftRenderEventExtension(double partialTicks) {
		this.partialTicks = partialTicks;
	}

	public static UIImmutableMinecraftRenderEventExtension of(double partialTicks) {
		return new UIImmutableMinecraftRenderEventExtension(partialTicks);
	}

	@Override
	public IExtensionType<Class<?>, ?, UIAbstractViewBusEvent<?>> getType() {
		return IUIMinecraftRenderEventExtension.StaticHolder.getType();
	}

	@Override
	public Class<UIAbstractViewBusEvent<?>> getGenericClass() {
		return CastUtilities.castUnchecked(UIAbstractViewBusEvent.class);
	}

	@Override
	public double getPartialTicks() {
		return partialTicks;
	}
}
