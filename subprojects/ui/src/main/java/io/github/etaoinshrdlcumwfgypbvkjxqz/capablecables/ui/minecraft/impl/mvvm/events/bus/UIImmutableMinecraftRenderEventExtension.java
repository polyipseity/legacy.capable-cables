package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.events.bus;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.events.bus.IUIMinecraftRenderEventExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Immutable
@OnlyIn(Dist.CLIENT)
public final class UIImmutableMinecraftRenderEventExtension
		implements IUIMinecraftRenderEventExtension {
	private final double partialTicks;
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<UIAbstractViewBusEvent<?>> typeToken = TypeToken.of(CastUtilities.castUnchecked(UIAbstractViewBusEvent.class));

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

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<? extends UIAbstractViewBusEvent<?>> getTypeToken() {
		return typeToken;
	}

	@Override
	public double getPartialTicks() {
		return partialTicks;
	}
}
