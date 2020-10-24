package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.EnumCropMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches.UIDefaultCacheExtension;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public interface IUIMinecraftComponentRenderer<C extends IUIComponent & IUIComponentMinecraft>
		extends IUIComponentRenderer<C> {
	void render(IUIComponentContext context, C container, EnumRenderStage stage, double partialTicks);

	default void crop(IUIComponentContext context, C container, EnumCropStage stage, EnumCropMethod method, double partialTicks) {
		IUIMinecraftComponentRenderer.cropImpl(context, container, stage, method, partialTicks);
	}

	static void cropImpl(IUIComponentContext componentContext, IUIComponent container, EnumCropStage stage, EnumCropMethod method, @SuppressWarnings("unused") double partialTicks) {
		stage.invoke(method, IUIComponent.getContextualShape(componentContext, container),
				UIDefaultCacheExtension.CacheUniversal.getZ().getValue().get(container).orElseThrow(AssertionError::new));
	}

	@OnlyIn(Dist.CLIENT)
	enum EnumRenderStage {
		PRE_CHILDREN,
		POST_CHILDREN,
		;

		public boolean isPreChildren() { return this == PRE_CHILDREN; }

		public boolean isPostChildren() { return this == POST_CHILDREN; }
	}

	@OnlyIn(Dist.CLIENT)
	enum EnumCropStage {
		CROP {
			@Override
			public void invoke(EnumCropMethod cropMethod, Shape shape, int z) {
				cropMethod.crop(shape, z);
			}
		},
		UN_CROP {
			@Override
			public void invoke(EnumCropMethod cropMethod, Shape shape, int z) {
				cropMethod.unCrop(shape);
			}
		},
		;

		public abstract void invoke(EnumCropMethod cropMethod, Shape shape, int z);
	}
}
