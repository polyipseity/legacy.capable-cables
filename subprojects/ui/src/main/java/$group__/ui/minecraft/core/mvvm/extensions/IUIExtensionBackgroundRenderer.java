package $group__.ui.minecraft.core.mvvm.extensions;

import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionBackgroundRenderer
		extends IUIExtension<INamespacePrefixedString, IUIComponentManager<?>> {
	INamespacePrefixedString KEY = new NamespacePrefixedString(INamespacePrefixedString.DEFAULT_NAMESPACE, AREA_UI + ".background");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IType<INamespacePrefixedString, IUIExtensionBackgroundRenderer, IUIComponentManager<?>>> TYPE =
			ExtensionRegistry.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> (Optional<? extends IUIExtensionBackgroundRenderer>) i.getExtension(t.getKey())));
}
