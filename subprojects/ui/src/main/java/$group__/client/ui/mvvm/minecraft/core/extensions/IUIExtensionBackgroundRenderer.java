package $group__.client.ui.mvvm.minecraft.core.extensions;

import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.minecraft.core.views.IUIViewComponentMinecraft;
import $group__.utilities.NamespaceUtilities;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IUIExtensionBackgroundRenderer<C extends IUIViewComponentMinecraft<?, ?>>
		extends IUIExtension<C> {
	ResourceLocation KEY = new ResourceLocation(NamespaceUtilities.NAMESPACE_DEFAULT_PREFIX + "background");
}
