package $group__.ui.minecraft.core.mvvm.extensions;

import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.mvvm.extensions.UIExtensionRegistry;
import $group__.utilities.extensions.DefaultExtensionType;
import $group__.utilities.extensions.core.IExtensionType;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.structures.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionBackgroundRenderer
		extends IUIExtension<INamespacePrefixedString, IUIComponentManager<?>> {
	INamespacePrefixedString KEY = new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.DEFAULT_NAMESPACE, "background");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIExtensionBackgroundRenderer, IUIComponentManager<?>>> TYPE =
			UIExtensionRegistry.getInstance().registerApply(KEY, k -> new DefaultExtensionType<>(k, (t, i) -> (Optional<? extends IUIExtensionBackgroundRenderer>) i.getExtension(t.getKey())));
}
