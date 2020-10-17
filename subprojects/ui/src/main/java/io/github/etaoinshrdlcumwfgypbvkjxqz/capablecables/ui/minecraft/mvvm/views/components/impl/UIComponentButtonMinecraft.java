package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.rendering.IUIComponentRendererMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components.rendering.NullUIComponentRendererMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.utilities.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.impl.UIComponentButton;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.UIDefaultRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class UIComponentButtonMinecraft
		extends UIComponentButton
		implements IUIComponentMinecraft {
	@UIComponentConstructor
	public UIComponentButtonMinecraft(UIComponentConstructor.IArguments arguments) { super(arguments); }

	private final AtomicReference<IUIRendererContainer<IUIComponentRendererMinecraft<?>>> rendererContainerReference = new AtomicReference<>();

	@Override
	public IUIRendererContainer<? extends IUIComponentRendererMinecraft<?>> getRendererContainer()
			throws IllegalStateException { return Optional.ofNullable(getRendererContainerReference().get()).orElseThrow(IllegalStateException::new); }

	@Override
	public void initializeRendererContainer(@NonNls CharSequence name)
			throws IllegalStateException {
		IUIRendererContainer<IUIComponentRendererMinecraft<?>> rendererContainer = new UIDefaultRendererContainer<>(name, this, CastUtilities.castUnchecked(DefaultRenderer.class));
		if (!getRendererContainerReference().compareAndSet(null, rendererContainer))
			throw new IllegalStateException();
		getBinderObserverSupplier().ifPresent(rendererContainer::initializeBindings);
	}

	protected AtomicReference<IUIRendererContainer<IUIComponentRendererMinecraft<?>>> getRendererContainerReference() { return rendererContainerReference; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		Optional.ofNullable(getRendererContainerReference().get())
				.ifPresent(rendererContainer -> rendererContainer.initializeBindings(binderObserverSupplier));
	}

	@Override
	public void tick(IUIComponentContext context) {}

	@OnlyIn(Dist.CLIENT)
	public static class DefaultRenderer<C extends UIComponentButtonMinecraft>
			extends NullUIComponentRendererMinecraft<C> {
		@NonNls
		public static final String PROPERTY_COLOR_BASE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "button.colors.base";
		@NonNls
		public static final String PROPERTY_COLOR_BASE_BORDER = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "button.colors.base.border";
		@NonNls
		public static final String PROPERTY_COLOR_HOVERING = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "button.colors.hovering";
		@NonNls
		public static final String PROPERTY_COLOR_HOVERING_BORDER = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "button.colors.hovering.border";
		@NonNls
		public static final String PROPERTY_COLOR_PRESSED = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "button.colors.pressed";
		@NonNls
		public static final String PROPERTY_COLOR_PRESSED_BORDER = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "button.colors.pressed.border";

		public static final INamespacePrefixedString PROPERTY_COLOR_BASE_LOCATION = ImmutableNamespacePrefixedString.of(PROPERTY_COLOR_BASE);
		public static final INamespacePrefixedString PROPERTY_COLOR_BASE_BORDER_LOCATION = ImmutableNamespacePrefixedString.of(PROPERTY_COLOR_BASE_BORDER);
		public static final INamespacePrefixedString PROPERTY_COLOR_HOVERING_LOCATION = ImmutableNamespacePrefixedString.of(PROPERTY_COLOR_HOVERING);
		public static final INamespacePrefixedString PROPERTY_COLOR_HOVERING_BORDER_LOCATION = ImmutableNamespacePrefixedString.of(PROPERTY_COLOR_HOVERING_BORDER);
		public static final INamespacePrefixedString PROPERTY_COLOR_PRESSED_LOCATION = ImmutableNamespacePrefixedString.of(PROPERTY_COLOR_PRESSED);
		public static final INamespacePrefixedString PROPERTY_COLOR_PRESSED_BORDER_LOCATION = ImmutableNamespacePrefixedString.of(PROPERTY_COLOR_PRESSED_BORDER);

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

		@Override
		@OverridingMethodsMustInvokeSuper
		public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
			super.initializeBindings(binderObserverSupplier);
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
					() -> ImmutableBinderAction.bind(
							getColorBase(), getColorBaseBorder(),
							getColorHovering(), getColorHoveringBorder(),
							getColorPressed(), getColorPressedBorder()
					));
		}

		@UIRendererConstructor
		public DefaultRenderer(UIRendererConstructor.IArguments arguments) {
			super(arguments);

			Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
			this.colorBase = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.DARK_GRAY,
					mappings.get(PROPERTY_COLOR_BASE_LOCATION));
			this.colorBaseBorder = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.DARK_GRAY,
					mappings.get(PROPERTY_COLOR_BASE_BORDER_LOCATION));
			this.colorHovering = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.GRAY,
					mappings.get(PROPERTY_COLOR_HOVERING_LOCATION));
			this.colorHoveringBorder = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.GRAY,
					mappings.get(PROPERTY_COLOR_HOVERING_BORDER_LOCATION));
			this.colorPressed = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.LIGHT_GRAY,
					mappings.get(PROPERTY_COLOR_PRESSED_LOCATION));
			this.colorPressedBorder = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.LIGHT_GRAY,
					mappings.get(PROPERTY_COLOR_PRESSED_BORDER_LOCATION));
		}

		@Override
		public void render(IUIComponentContext context, C container, EnumRenderStage stage, double partialTicks) {
			if (stage.isPreChildren()) {
				Shape transformed = IUIComponent.getContextualShape(context, container);
				if (container.getButtonStates().contains(IButtonState.PRESSING)) {
					getColorPressed().getValue().ifPresent(c ->
							MinecraftDrawingUtilities.drawShape(transformed, true, c, 0));
					getColorPressedBorder().getValue().ifPresent(c ->
							MinecraftDrawingUtilities.drawShape(transformed, false, c, 0));
				} else if (container.getButtonStates().contains(IButtonState.HOVERING)) {
					getColorHovering().getValue().ifPresent(c ->
							MinecraftDrawingUtilities.drawShape(transformed, true, c, 0));
					getColorHoveringBorder().getValue().ifPresent(c ->
							MinecraftDrawingUtilities.drawShape(transformed, false, c, 0));
				} else {
					getColorBase().getValue().ifPresent(c ->
							MinecraftDrawingUtilities.drawShape(transformed, true, c, 0));
					getColorBaseBorder().getValue().ifPresent(c ->
							MinecraftDrawingUtilities.drawShape(transformed, false, c, 0));
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
