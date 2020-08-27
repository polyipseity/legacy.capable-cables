package $group__.ui.minecraft.mvvm.components.common;

import $group__.ui.core.mvvm.binding.IBindingField;
import $group__.ui.core.mvvm.binding.IHasBinding;
import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRendererContainer;
import $group__.ui.core.parsers.binding.UIProperty;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.parsers.components.UIRendererConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import $group__.ui.minecraft.mvvm.components.rendering.UIComponentRendererMinecraft;
import $group__.ui.mvvm.views.components.common.UIComponentButton;
import $group__.ui.mvvm.views.components.rendering.UIComponentRendererContainer;
import $group__.ui.utilities.BindingUtilities;
import $group__.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class UIComponentButtonMinecraft
		extends UIComponentButton
		implements IUIComponentMinecraft {
	protected final IUIComponentRendererContainer<IUIComponentRendererMinecraft<?>> rendererContainer =
			new UIComponentRendererContainer<>(new DefaultRenderer<>(ImmutableMap.of(), UIComponentButtonMinecraft.class));

	@UIComponentConstructor(type = UIComponentConstructor.ConstructorType.MAPPING__SHAPE_DESCRIPTOR)
	public UIComponentButtonMinecraft(Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping, IShapeDescriptor<?> shapeDescriptor) { super(mapping, shapeDescriptor); }

	@Override
	public Optional<? extends IUIComponentRendererMinecraft<?>> getRenderer() { return getRendererContainer().getRenderer(); }

	@Override
	public void setRenderer(@Nullable IUIComponentRendererMinecraft<?> renderer) {
		IUIComponentRendererContainer.setRendererImpl(this, renderer,
				(s, r) -> s.getRendererContainer().setRenderer(r));
	}

	protected IUIComponentRendererContainer<IUIComponentRendererMinecraft<?>> getRendererContainer() { return rendererContainer; }

	public static class DefaultRenderer<C extends UIComponentButtonMinecraft>
			extends UIComponentRendererMinecraft<C> {
		public static final String PROPERTY_COLOR_BASE = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.base";
		public static final String PROPERTY_COLOR_BASE_BORDER = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.base.border";
		public static final String PROPERTY_COLOR_HOVERING = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.hovering";
		public static final String PROPERTY_COLOR_HOVERING_BORDER = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.hovering.border";
		public static final String PROPERTY_COLOR_PRESSED = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.pressed";
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

		@UIRendererConstructor(type = UIRendererConstructor.ConstructorType.MAPPING__CONTAINER_CLASS)
		public DefaultRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping, Class<C> containerClass) {
			super(mapping, containerClass);

			this.colorBase = IHasBinding.createBindingField(Color.class,
					this.mapping.get(PROPERTY_COLOR_BASE_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.DARK_GRAY);
			this.colorBaseBorder = IHasBinding.createBindingField(Color.class,
					this.mapping.get(PROPERTY_COLOR_BASE_BORDER_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.DARK_GRAY);
			this.colorHovering = IHasBinding.createBindingField(Color.class,
					this.mapping.get(PROPERTY_COLOR_HOVERING_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.GRAY);
			this.colorHoveringBorder = IHasBinding.createBindingField(Color.class,
					this.mapping.get(PROPERTY_COLOR_HOVERING_BORDER_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.GRAY);
			this.colorPressed = IHasBinding.createBindingField(Color.class,
					this.mapping.get(PROPERTY_COLOR_PRESSED_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.LIGHT_GRAY);
			this.colorPressedBorder = IHasBinding.createBindingField(Color.class,
					this.mapping.get(PROPERTY_COLOR_PRESSED_BORDER_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.LIGHT_GRAY);
		}

		@Override
		public void render(C container, IAffineTransformStack stack, Point2D cursorPosition, double partialTicks, boolean pre) {
			if (pre) {
				AffineTransform transform = stack.getDelegated().peek();
				Shape transformed = transform.createTransformedShape(container.getShapeDescriptor().getShapeOutput());
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
