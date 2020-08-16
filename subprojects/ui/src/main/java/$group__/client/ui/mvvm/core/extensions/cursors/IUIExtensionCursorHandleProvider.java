package $group__.client.ui.mvvm.core.extensions.cursors;

import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.views.IUIView;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.structures.Registry;
import net.minecraft.util.ResourceLocation;

public interface IUIExtensionCursorHandleProvider
		extends IUIExtension<ResourceLocation, IUIView<?>>, ICursorHandleProvider {
	ResourceLocation KEY = new ResourceLocation(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, AREA_UI + ".cursor_custom");
	Registry.RegistryObject<IType<ResourceLocation, IUIExtensionCursorHandleProvider, IUIView<?>>> TYPE =
			RegExtension.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));
}
