package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.components.rendering.UIComponentRendererMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.extensions.UIExtensionBackgroundMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.UIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.rendering.UIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class UIComponentManagerMinecraft
		extends UIComponentManager<Rectangle2D>
		implements IUIComponentMinecraft {
	protected final IUIRendererContainer<IUIComponentRendererMinecraft<?>> rendererContainer =
			new UIRendererContainer<>(new UIComponentRendererMinecraft<>(UIComponentManagerMinecraft.class));

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIComponentConstructor(type = UIComponentConstructor.EnumConstructorType.MAPPINGS__ID__SHAPE_DESCRIPTOR)
	public UIComponentManagerMinecraft(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, @Nullable String id, IShapeDescriptor<Rectangle2D> shapeDescriptor) {
		super(mappings, id, shapeDescriptor);

		IExtensionContainer.StaticHolder.addExtensionExtendedChecked(this, new UIExtensionBackgroundMinecraft<>(IUIComponentManager.class)); // COMMENT to ensure that 'GuiScreenEvent.BackgroundDrawnEvent' is fired
	}

	@Override
	public Optional<? extends IUIComponentRendererMinecraft<?>> getRenderer() { return getRendererContainer().getRenderer(); }

	@Override
	@Deprecated
	public void setRenderer(@Nullable IUIComponentRendererMinecraft<?> renderer) {
		IUIRendererContainer.StaticHolder.setRendererImpl(this, renderer,
				(s, r) -> s.getRendererContainer().setRenderer(r));
	}

	@Override
	public Class<? extends IUIComponentRendererMinecraft<?>> getDefaultRendererClass() { return getRendererContainer().getDefaultRendererClass(); }

	protected IUIRendererContainer<IUIComponentRendererMinecraft<?>> getRendererContainer() { return rendererContainer; }
}