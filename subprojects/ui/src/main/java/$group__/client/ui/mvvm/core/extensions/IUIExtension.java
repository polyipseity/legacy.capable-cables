package $group__.client.ui.mvvm.core.extensions;

import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import net.minecraft.util.ResourceLocation;

public interface IUIExtension<K extends ResourceLocation, C extends IExtensionContainer<? super K, ?>>
		extends IExtension<K, C> {}
