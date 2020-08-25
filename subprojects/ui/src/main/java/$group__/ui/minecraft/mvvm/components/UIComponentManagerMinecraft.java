package $group__.ui.minecraft.mvvm.components;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRendererContainer;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import $group__.ui.minecraft.mvvm.components.rendering.InvisibleUIComponentRendererMinecraft;
import $group__.ui.minecraft.mvvm.extensions.UIExtensionBackgroundMinecraft;
import $group__.ui.mvvm.views.components.UIComponentManager;
import $group__.ui.mvvm.views.components.rendering.UIComponentRendererContainer;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;
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
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIComponentConstructor(type = UIComponentConstructor.ConstructorType.SHAPE_DESCRIPTOR__MAPPING)
	public UIComponentManagerMinecraft(IShapeDescriptor<Rectangle2D> shapeDescriptor, Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping) {
		super(shapeDescriptor, mapping);

		IExtensionContainer.addExtensionSafeExtended(this, new UIExtensionBackgroundMinecraft<>(IUIComponentManager.class)); // COMMENT to ensure that 'GuiScreenEvent.BackgroundDrawnEvent' is fired
	}

	protected final IUIComponentRendererContainer<IUIComponentRendererMinecraft> rendererContainer =
			new UIComponentRendererContainer<>(new InvisibleUIComponentRendererMinecraft());

	@Override
	public Optional<? extends IUIComponentRendererMinecraft> getRenderer() { return getRendererContainer().getRenderer(); }

	@Override
	public void setRenderer(@Nullable IUIComponentRendererMinecraft renderer) { getRendererContainer().setRenderer(renderer); }

	protected IUIComponentRendererContainer<IUIComponentRendererMinecraft> getRendererContainer() { return rendererContainer; }
}
