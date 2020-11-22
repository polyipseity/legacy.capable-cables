package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.extensions;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions.IUIMinecraftContainerProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIImmutableMinecraftContainerProviderExtension
		implements IUIMinecraftContainerProviderExtension {
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<IUIInfrastructure<?, ?, ?>> typeToken = TypeToken.of(CastUtilities.castUnchecked(IUIInfrastructure.class));
	private final Container container;

	public UIImmutableMinecraftContainerProviderExtension(Container container) {
		this.container = container;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<IUIInfrastructure<?, ?, ?>> getTypeToken() {
		return typeToken;
	}

	@Override
	public Container getContainer() { return container; }

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIInfrastructure<?, ?, ?>> getType() { return StaticHolder.getType().getValue(); }
}
