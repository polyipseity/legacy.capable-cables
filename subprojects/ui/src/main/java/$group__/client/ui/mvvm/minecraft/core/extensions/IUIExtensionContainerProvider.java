package $group__.client.ui.mvvm.minecraft.core.extensions;

import $group__.client.ui.mvvm.core.IUIInfrastructure;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.utilities.NamespaceUtilities;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionContainerProvider<C extends IUIInfrastructure<?, ?, ?>>
		extends IUIExtension<C> {
	ResourceLocation KEY = new ResourceLocation(NamespaceUtilities.NAMESPACE_DEFAULT_PREFIX + "container");

	Container getContainer();
}
