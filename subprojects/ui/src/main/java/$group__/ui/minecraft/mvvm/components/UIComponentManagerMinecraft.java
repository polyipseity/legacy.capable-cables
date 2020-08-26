package $group__.ui.minecraft.mvvm.components;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRenderer.RegRenderer;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRendererContainer;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import $group__.ui.minecraft.mvvm.components.rendering.UIComponentRendererMinecraft;
import $group__.ui.minecraft.mvvm.extensions.UIExtensionBackgroundMinecraft;
import $group__.ui.mvvm.views.components.UIComponentManager;
import $group__.ui.mvvm.views.components.rendering.UIComponentRendererContainer;
import $group__.utilities.CastUtilities;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class UIComponentManagerMinecraft
		extends UIComponentManager<Rectangle2D>
		implements IUIComponentMinecraft {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final RegRenderer<IUIComponentRendererMinecraft<? super UIComponentManagerMinecraft>> RENDERER_REG =
			RegRenderer.createInstance(UIComponentManagerMinecraft.class, CastUtilities.castUnchecked(IUIComponentRendererMinecraft.class),
					() -> new UIComponentRendererMinecraft<>(UIComponentManagerMinecraft.class, ImmutableMap.of()), LOGGER);
	protected final IUIComponentRendererContainer<IUIComponentRendererMinecraft<?>> rendererContainer =
			new UIComponentRendererContainer<>(RegRenderer.getDefault(RENDERER_REG).getValue().get());

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIComponentConstructor(type = UIComponentConstructor.ConstructorType.SHAPE_DESCRIPTOR__MAPPING)
	public UIComponentManagerMinecraft(IShapeDescriptor<Rectangle2D> shapeDescriptor, Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping) {
		super(shapeDescriptor, mapping);

		IExtensionContainer.addExtensionExtendedChecked(this, new UIExtensionBackgroundMinecraft<>(IUIComponentManager.class)); // COMMENT to ensure that 'GuiScreenEvent.BackgroundDrawnEvent' is fired
	}

	@Override
	public Optional<? extends IUIComponentRendererMinecraft<?>> getRenderer() { return getRendererContainer().getRenderer(); }

	@Override
	public void setRenderer(@Nullable IUIComponentRendererMinecraft<?> renderer) { getRendererContainer().setRenderer(renderer); }

	protected IUIComponentRendererContainer<IUIComponentRendererMinecraft<?>> getRendererContainer() { return rendererContainer; }
}
