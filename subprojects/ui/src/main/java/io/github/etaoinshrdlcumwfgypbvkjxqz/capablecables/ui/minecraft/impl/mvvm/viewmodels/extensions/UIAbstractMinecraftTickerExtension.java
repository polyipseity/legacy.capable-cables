package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.viewmodels.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.viewmodels.IUIViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.viewmodels.extensions.IUIMinecraftTickerExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class UIAbstractMinecraftTickerExtension<CE extends IUIViewModel<?>>
		extends AbstractContainerAwareExtension<IIdentifier, IUIViewModel<?>, CE>
		implements IUIMinecraftTickerExtension {
	protected UIAbstractMinecraftTickerExtension(Class<CE> type) {
		super(type);
	}

	@Override
	public IExtensionType<IIdentifier, ?, IUIViewModel<?>> getType() {
		return StaticHolder.getType().getValue();
	}
}
