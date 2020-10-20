package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.extensions.IUIMinecraftContainerProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIImmutableMinecraftContainerProviderExtension
		extends IHasGenericClass.Impl<IUIInfrastructure<?, ?, ?>>
		implements IUIMinecraftContainerProviderExtension {
	private final Container container;

	public UIImmutableMinecraftContainerProviderExtension(Container container) {
		super(CastUtilities.castUnchecked(IUIInfrastructure.class)); // COMMENT class should not care about it
		this.container = container;
	}

	@Override
	public Container getContainer() { return container; }

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIInfrastructure<?, ?, ?>> getType() { return StaticHolder.getType().getValue(); }
}
