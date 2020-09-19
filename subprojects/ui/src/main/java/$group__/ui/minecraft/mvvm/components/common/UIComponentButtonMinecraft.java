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
import $group__.ui.mvvm.views.components.common.UIComponentButton;
import $group__.ui.mvvm.views.components.rendering.UIRendererContainer;
import $group__.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.binding.core.fields.IBindingField;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class UIComponentButtonMinecraft
		extends UIComponentButton
		implements IUIComponentMinecraft {
	protected final IUIRendererContainer<IUIComponentRendererMinecraft<?>> rendererContainer =
			new UIRendererContainer<>(new DefaultRenderer<>(ImmutableMap.of(), UIComponentButtonMinecraft.class));

	@UIComponentConstructor(type = UIComponentConstructor.EnumConstructorType.MAPPINGS__ID__SHAPE_DESCRIPTOR)
	public UIComponentButtonMinecraft(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, @Nullable String id, IShapeDescriptor<?> shapeDescriptor) { super(mappings, id, shapeDescriptor); }

	@Override
	public Optional<? extends IUIComponentRendererMinecraft<?>> getRenderer() { return getRendererContainer().getRenderer(); }

	@SuppressWarnings("deprecation")
	@Override
	public void setRenderer(@Nullable IUIComponentRendererMinecraft<?> renderer) {
		StaticHolder.setRendererImpl(this, renderer,
				(s, r) -> s.getRendererContainer().setRenderer(r));
	}

	@Override
	public Class<? extends IUIComponentRendererMinecraft<?>> getDefaultRendererClass() { return getRendererContainer().getDefaultRendererClass(); }

	protected IUIRendererContainer<IUIComponentRendererMinecraft<?>> getRendererContainer() { return rendererContainer; }

	@OnlyIn(Dist.CLIENT)
	public static class DefaultRenderer<C extends UIComponentButtonMinecraft>
			extends UIComponentRendererMinecraft<C> {
		@NonNls
		public static final String PROPERTY_COLOR_BASE = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.base";
		@NonNls
		public static final String PROPERTY_COLOR_BASE_BORDER = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.base.border";
		@NonNls
		public static final String PROPERTY_COLOR_HOVERING = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.hovering";
		@NonNls
		public static final String PROPERTY_COLOR_HOVERING_BORDER = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.hovering.border";
		@NonNls
		public static final String PROPERTY_COLOR_PRESSED = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.pressed";
		@NonNls
		public static final String PROPERTY_COLOR_PRESSED_BORDER = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.pressed.border";

		public static final INamespacePrefixedString PROPERTY_COLOR_BASE_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_BASE);
		public static final INamespacePrefixedString PROPERTY_COLOR_BASE_BORDER_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_BASE_BORDER);
		public static final INamespacePrefixedString PROPERTY_COLOR_HOVERING_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_HOVERING);
		public static final INamespacePrefixedString PROPERTY_COLOR_HOVERING_BORDER_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_HOVERING_BORDER);
		public static final INamespacePrefixedString PROPERTY_COLOR_PRESSED_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_PRESSED);
		public static final INamespacePrefixedString PROPERTY_COLOR_PRESSED_BORDER_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_PRESSED_BORDER);

		@UIProperty(PROPERTY_COLOR_BASE)
		protected final IBindingField<Color> colorBase;
		@UIProperty(PROPERTY_COLOR_BASE_BORDER)
		protected final IBindingField<Color> colorBaseBorder;
		@UIProperty(PROPERTY_COLOR_HOVERING)
		protected final IBindingField<Color> colorHovering;
		@UIProperty(PROPERTY_COLOR_HOVERING_BORDER)
		protected final IBindingField<Color> colorHoveringBorder;
		@UIProperty(PROPERTY_COLOR_PRESSED)
		protected final IBindingField<Color> colorPressed;
		@UIProperty(PROPERTY_COLOR_PRESSED_BORDER)
		protected final IBindingField<Color> colorPressedBorder;

		@UIRendererConstructor(type = UIRendererConstructor.EnumConstructorType.MAPPINGS__CONTAINER_CLASS)
		public DefaultRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, Class<C> containerClass) {
			super(mappings, containerClass);

			this.colorBase = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.DARK_GRAY,
					this.mappings.get(PROPERTY_COLOR_BASE_LOCATION));
			this.colorBaseBorder = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.DARK_GRAY,
					this.mappings.get(PROPERTY_COLOR_BASE_BORDER_LOCATION));
			this.colorHovering = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.GRAY,
					this.mappings.get(PROPERTY_COLOR_HOVERING_LOCATION));
			this.colorHoveringBorder = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.GRAY,
					this.mappings.get(PROPERTY_COLOR_HOVERING_BORDER_LOCATION));
			this.colorPressed = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.LIGHT_GRAY,
					this.mappings.get(PROPERTY_COLOR_PRESSED_LOCATION));
			this.colorPressedBorder = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.LIGHT_GRAY,
					this.mappings.get(PROPERTY_COLOR_PRESSED_BORDER_LOCATION));
		}

		@Override
		public void render(IUIComponentContext context, C container, EnumRenderStage stage, double partialTicks) {
			if (stage == EnumRenderStage.PRE_CHILDREN) {
				Shape transformed = context.getTransformStack().element().createTransformedShape(container.getShapeDescriptor().getShapeOutput());
				if (container.getButtonStates().contains(IButtonState.PRESSING)) {
					getColorPressed().getValue().ifPresent(c ->
							DrawingUtilities.drawShape(transformed, true, c, 0));
					getColorPressedBorder().getValue().ifPresent(c ->
							DrawingUtilities.drawShape(transformed, false, c, 0));
				} else if (container.getButtonStates().contains(IButtonState.HOVERING)) {
					getColorHovering().getValue().ifPresent(c ->
							DrawingUtilities.drawShape(transformed, true, c, 0));
					getColorHoveringBorder().getValue().ifPresent(c ->
							DrawingUtilities.drawShape(transformed, false, c, 0));
				} else {
					getColorBase().getValue().ifPresent(c ->
							DrawingUtilities.drawShape(transformed, true, c, 0));
					getColorBaseBorder().getValue().ifPresent(c ->
							DrawingUtilities.drawShape(transformed, false, c, 0));
				}
			}
		}

		protected IBindingField<Color> getColorPressed() { return colorPressed; }

		protected IBindingField<Color> getColorPressedBorder() {
			return colorPressedBorder;
		}

		protected IBindingField<Color> getColorHovering() {
			return colorHovering;
		}

		protected IBindingField<Color> getColorHoveringBorder() {
			return colorHoveringBorder;
		}

		protected IBindingField<Color> getColorBase() {
			return colorBase;
		}

		protected IBindingField<Color> getColorBaseBorder() {
			return colorBaseBorder;
		}
	}
}
