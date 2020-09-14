package $group__.ui.minecraft.mvvm.components;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.rendering.IUIRendererContainer;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import $group__.ui.minecraft.mvvm.components.rendering.UIComponentRendererMinecraft;
import $group__.ui.minecraft.mvvm.extensions.UIExtensionBackgroundMinecraft;
import $group__.ui.mvvm.views.components.UIComponentManager;
import $group__.ui.mvvm.views.components.rendering.UIRendererContainer;
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
	protected final IUIRendererContainer<IUIComponentRendererMinecraft<?>> rendererContainer =
			new UIRendererContainer<>(new UIComponentRendererMinecraft<>(UIComponentManagerMinecraft.class));

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIComponentConstructor(type = UIComponentConstructor.EnumConstructorType.MAPPINGS__ID__SHAPE_DESCRIPTOR)
	public UIComponentManagerMinecraft(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, @Nullable String id, IShapeDescriptor<Rectangle2D> shapeDescriptor) {
		super(mappings, id, shapeDescriptor);

		IExtensionContainer.addExtensionExtendedChecked(this, new UIExtensionBackgroundMinecraft<>(IUIComponentManager.class)); // COMMENT to ensure that 'GuiScreenEvent.BackgroundDrawnEvent' is fired
	}

	@Override
	public Optional<? extends IUIComponentRendererMinecraft<?>> getRenderer() { return getRendererContainer().getRenderer(); }

	@Override
	public void setRenderer(@Nullable IUIComponentRendererMinecraft<?> renderer) {
		IUIRendererContainer.setRendererImpl(this, renderer,
				(s, r) -> s.getRendererContainer().setRenderer(r));
	}

	@Override
	public Class<? extends IUIComponentRendererMinecraft<?>> getDefaultRendererClass() { return getRendererContainer().getDefaultRendererClass(); }

	protected IUIRendererContainer<IUIComponentRendererMinecraft<?>> getRendererContainer() { return rendererContainer; }
}