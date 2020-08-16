package $group__.client.ui.mvvm.minecraft.core.extensions;

import $group__.client.ui.mvvm.core.IUIInfrastructure;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.structures.Registry;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionContainerProvider
		extends IUIExtension<ResourceLocation, IUIInfrastructure<?, ?, ?>> {
	ResourceLocation KEY = new ResourceLocation(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, AREA_UI + ".container");
	Registry.RegistryObject<IType<ResourceLocation, IUIExtensionContainerProvider, IUIInfrastructure<?, ?, ?>>> TYPE =
			RegExtension.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));

	Container getContainer();
}
