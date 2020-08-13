package $group__.client.ui.mvvm.core.models;

import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.utilities.extensions.IExtensionContainer;
import net.minecraft.util.ResourceLocation;

public interface IUIModel
		extends IExtensionContainer<ResourceLocation, IUIExtension<ResourceLocation, ? super IUIModel>> {}
