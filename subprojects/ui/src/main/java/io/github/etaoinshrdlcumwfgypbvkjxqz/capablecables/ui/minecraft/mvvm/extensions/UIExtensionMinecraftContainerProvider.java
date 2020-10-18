package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions.IUIExtensionMinecraftContainerProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIExtensionMinecraftContainerProvider
		extends IHasGenericClass.Impl<IUIInfrastructure<?, ?, ?>>
		implements IUIExtensionMinecraftContainerProvider {
	protected final Container container;

	public UIExtensionMinecraftContainerProvider(Container container) {
		super(CastUtilities.castUnchecked(IUIInfrastructure.class)); // COMMENT class should not care about it
		this.container = container;
	}

	@Override
	public Container getContainer() { return container; }

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIInfrastructure<?, ?, ?>> getType() { return StaticHolder.getType().getValue(); }
}
