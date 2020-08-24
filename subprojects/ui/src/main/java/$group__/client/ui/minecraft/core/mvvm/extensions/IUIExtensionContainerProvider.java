package $group__.client.ui.mvvm.minecraft.core.extensions;

import $group__.client.ui.mvvm.core.IUIInfrastructure;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.Registry;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionContainerProvider
		extends IUIExtension<INamespacePrefixedString, IUIInfrastructure<?, ?, ?>> {
	INamespacePrefixedString KEY = new NamespacePrefixedString(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, AREA_UI + ".container");
	Registry.RegistryObject<IType<INamespacePrefixedString, IUIExtensionContainerProvider, IUIInfrastructure<?, ?, ?>>> TYPE =
			RegExtension.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));

	Container getContainer();
}
