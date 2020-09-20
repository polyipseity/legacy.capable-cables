package $group__.ui.minecraft.core.mvvm.extensions;

import $group__.ui.core.mvvm.IUIInfrastructure;
import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.utilities.extensions.AbstractExtensionRegistry;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.structures.Registry;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionContainerProvider
		extends IUIExtension<INamespacePrefixedString, IUIInfrastructure<?, ?, ?>> {
	INamespacePrefixedString KEY = new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.getDefaultNamespace(), AREA_UI + ".container");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IType<INamespacePrefixedString, IUIExtensionContainerProvider, IUIInfrastructure<?, ?, ?>>> TYPE =
			AbstractExtensionRegistry.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> (Optional<? extends IUIExtensionContainerProvider>) i.getExtension(t.getKey())));

	Container getContainer();
}
