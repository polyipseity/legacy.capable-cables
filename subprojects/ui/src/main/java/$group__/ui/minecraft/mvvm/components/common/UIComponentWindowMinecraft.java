package $group__.ui.minecraft.mvvm.components.common;

import $group__.ui.core.binding.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.rendering.IUIRendererContainer;
import $group__.ui.core.parsers.binding.UIProperty;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.parsers.components.UIRendererConstructor;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import $group__.ui.minecraft.mvvm.components.rendering.UIComponentRendererMinecraft;
import $group__.ui.mvvm.views.components.common.UIComponentWindow;
import $group__.ui.mvvm.views.components.rendering.UIRendererContainer;
import $group__.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.binding.core.fields.IBindingField;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.RectangularShape;
import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class UIComponentWindowMinecraft
		extends UIComponentWindow
		implements IUIComponentMinecraft {
	protected final IUIRendererContainer<IUIComponentRendererMinecraft<?>> rendererContainer =
			new UIRendererContainer<>(new DefaultRenderer<>(ImmutableMap.of(), UIComponentWindowMinecraft.class));

	@UIComponentConstructor(type = UIComponentConstructor.EnumConstructorType.MAPPINGS__ID__SHAPE_DESCRIPTOR)
	public UIComponentWindowMinecraft(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, @Nullable String id, IShapeDescriptor<RectangularShape> shapeDescriptor) { super(mappings, id, shapeDescriptor); }

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

	@OnlyIn(Dist.CLIENT)
	public static class DefaultRenderer<C extends UIComponentWindowMinecraft>
			extends UIComponentRendererMinecraft<C> {
		@NonNls
		public static final String PROPERTY_COLOR_BACKGROUND = INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "window.colors.background";
		@NonNls
		public static final String PROPERTY_COLOR_BORDER = INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "window.colors.border";

		public static final INamespacePrefixedString PROPERTY_COLOR_BACKGROUND_LOCATION = new ImmutableNamespacePrefixedString(PROPERTY_COLOR_BACKGROUND);
		public static final INamespacePrefixedString PROPERTY_COLOR_BORDER_LOCATION = new ImmutableNamespacePrefixedString(PROPERTY_COLOR_BORDER);

		@UIProperty(PROPERTY_COLOR_BACKGROUND)
		protected final IBindingField<Color> colorBackground;
		@UIProperty(PROPERTY_COLOR_BORDER)
		protected final IBindingField<Color> colorBorder;

		@UIRendererConstructor(type = UIRendererConstructor.EnumConstructorType.MAPPINGS__CONTAINER_CLASS)
		public DefaultRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, Class<C> containerClass) {
			super(mappings, containerClass);

			this.colorBackground = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.BLACK,
					this.mappings.get(PROPERTY_COLOR_BACKGROUND_LOCATION));
			this.colorBorder = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.WHITE,
					this.mappings.get(PROPERTY_COLOR_BORDER_LOCATION));
		}

		@Override
		public void render(IUIComponentContext context, C container, EnumRenderStage stage, double partialTicks) {
			if (stage == EnumRenderStage.PRE_CHILDREN) {
				Shape transformed = context.getTransformStack().element().createTransformedShape(container.getShapeDescriptor().getShapeOutput());
				getColorBackground().getValue().ifPresent(c ->
						DrawingUtilities.drawShape(transformed, true, c, 0));
				getColorBorder().getValue().ifPresent(c ->
						DrawingUtilities.drawShape(transformed, true, c, 0));
			}
		}

		public IBindingField<Color> getColorBackground() { return colorBackground; }

		public IBindingField<Color> getColorBorder() { return colorBorder; }
	}
}
