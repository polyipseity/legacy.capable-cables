package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.rendering.IUIComponentRendererMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components.rendering.NullUIComponentRendererMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.UIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.DefaultUIRendererContainer;
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
			new DefaultUIRendererContainer<>(new NullUIComponentRendererMinecraft<>(UIComponentManagerMinecraft.class));

	@UIComponentConstructor(type = UIComponentConstructor.EnumConstructorType.MAPPINGS__NAME__SHAPE_DESCRIPTOR)
	public UIComponentManagerMinecraft(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, @Nullable String name, IShapeDescriptor<Rectangle2D> shapeDescriptor) {
		super(mappings, name, shapeDescriptor);
	}

	@Override
	public Optional<? extends IUIComponentRendererMinecraft<?>> getRenderer() { return getRendererContainer().getRenderer(); }

	@Override
	@Deprecated
	public void setRenderer(@Nullable IUIComponentRendererMinecraft<?> renderer) {
		IUIRendererContainer.StaticHolder.setRendererImpl(this, renderer, getRendererContainer()::setRenderer);
	}

	@Override
	public Class<? extends IUIComponentRendererMinecraft<?>> getDefaultRendererClass() { return getRendererContainer().getDefaultRendererClass(); }

	protected IUIRendererContainer<IUIComponentRendererMinecraft<?>> getRendererContainer() { return rendererContainer; }

	@Override
	public void tick(IUIComponentContext context) {}
}
