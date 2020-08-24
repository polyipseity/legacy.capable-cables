package $group__.client.ui.mvvm.minecraft.core.extensions;

import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.minecraft.core.views.IUIViewComponentMinecraft;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionBackgroundRenderer
		extends IUIExtension<INamespacePrefixedString, IUIViewComponentMinecraft<?, ?>> {
	INamespacePrefixedString KEY = new NamespacePrefixedString(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, AREA_UI + ".background");
	Registry.RegistryObject<IType<INamespacePrefixedString, IUIExtensionBackgroundRenderer, IUIViewComponentMinecraft<?, ?>>> TYPE =
			RegExtension.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));
}
