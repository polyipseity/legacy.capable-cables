package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components.impl;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.rendering.IUIMinecraftComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components.rendering.UIDefaultMinecraftComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl.UIButtonComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
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
public class UIMinecraftButtonComponent
		extends UIButtonComponent
		implements IUIComponentMinecraft {
	private final AtomicReference<IUIRendererContainer<IUIMinecraftComponentRenderer<?>>> rendererContainerReference = new AtomicReference<>();

	@UIComponentConstructor
	public UIMinecraftButtonComponent(UIComponentConstructor.IArguments arguments) { super(arguments); }

	@Override
	public IUIRendererContainer<? extends IUIMinecraftComponentRenderer<?>> getRendererContainer()
			throws IllegalStateException { return Optional.ofNullable(getRendererContainerReference().get()).orElseThrow(IllegalStateException::new); }

	@Override
	public void initializeRendererContainer(@NonNls CharSequence name)
			throws IllegalStateException {
		IUIRendererContainer<IUIMinecraftComponentRenderer<?>> rendererContainer = new UIDefaultRendererContainer<>(name, this, CastUtilities.castUnchecked(DefaultRenderer.class));
		if (!getRendererContainerReference().compareAndSet(null, rendererContainer))
			throw new IllegalStateException();
		getBinderObserverSupplier().ifPresent(rendererContainer::initializeBindings);
	}

	protected AtomicReference<IUIRendererContainer<IUIMinecraftComponentRenderer<?>>> getRendererContainerReference() { return rendererContainerReference; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.initializeBindings(
				Optional.ofNullable(getRendererContainerReference().get()).map(ImmutableList::of).orElseGet(ImmutableList::of),
				binderObserverSupplier
		);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.cleanupBindings(binderObserverSupplier);
		BindingUtilities.cleanupBindings(
				Optional.ofNullable(getRendererContainerReference().get()).map(ImmutableList::of).orElseGet(ImmutableList::of),
				binderObserverSupplier
		);
	}

	@Override
	public void tick(IUIComponentContext context) {}

	@OnlyIn(Dist.CLIENT)
	public static class DefaultRenderer<C extends UIMinecraftButtonComponent>
			extends UIDefaultMinecraftComponentRenderer<C> {
		@NonNls
		public static final String PROPERTY_BASE_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.button.base.color";
		@NonNls
		public static final String PROPERTY_BASE_BORDER_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.button.base.border.color";
		@NonNls
		public static final String PROPERTY_HOVERING_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.button.hovering.color";
		@NonNls
		public static final String PROPERTY_HOVERING_BORDER_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.button.hovering.border.color";
		@NonNls
		public static final String PROPERTY_PRESSED_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.button.pressed.color";
		@NonNls
		public static final String PROPERTY_PRESSED_BORDER_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.button.pressed.border.color";

		private static final INamespacePrefixedString PROPERTY_BASE_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyBaseColor());
		private static final INamespacePrefixedString PROPERTY_BASE_BORDER_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyBaseBorderColor());
		private static final INamespacePrefixedString PROPERTY_HOVERING_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyHoveringColor());
		private static final INamespacePrefixedString PROPERTY_HOVERING_BORDER_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyHoveringBorderColor());
		private static final INamespacePrefixedString PROPERTY_PRESSED_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyPressedColor());
		private static final INamespacePrefixedString PROPERTY_PRESSED_BORDER_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyPressedBorderColor());

		@UIProperty(PROPERTY_BASE_COLOR)
		private final IBindingField<Color> colorBase;
		@UIProperty(PROPERTY_BASE_BORDER_COLOR)
		private final IBindingField<Color> colorBaseBorder;
		@UIProperty(PROPERTY_HOVERING_COLOR)
		private final IBindingField<Color> colorHovering;
		@UIProperty(PROPERTY_HOVERING_BORDER_COLOR)
		private final IBindingField<Color> colorHoveringBorder;
		@UIProperty(PROPERTY_PRESSED_COLOR)
		private final IBindingField<Color> colorPressed;
		@UIProperty(PROPERTY_PRESSED_BORDER_COLOR)
		private final IBindingField<Color> colorPressedBorder;

		@UIRendererConstructor
		public DefaultRenderer(UIRendererConstructor.IArguments arguments) {
			super(arguments);

			Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
			this.colorBase = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.DARK_GRAY,
					mappings.get(getPropertyBaseColorLocation()));
			this.colorBaseBorder = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.DARK_GRAY,
					mappings.get(getPropertyBaseBorderColorLocation()));
			this.colorHovering = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.GRAY,
					mappings.get(getPropertyHoveringColorLocation()));
			this.colorHoveringBorder = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.GRAY,
					mappings.get(getPropertyHoveringBorderColorLocation()));
			this.colorPressed = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.LIGHT_GRAY,
					mappings.get(getPropertyPressedColorLocation()));
			this.colorPressedBorder = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.LIGHT_GRAY,
					mappings.get(getPropertyPressedBorderColorLocation()));
		}

		public static INamespacePrefixedString getPropertyBaseColorLocation() {
			return PROPERTY_BASE_COLOR_LOCATION;
		}

		public static INamespacePrefixedString getPropertyBaseBorderColorLocation() {
			return PROPERTY_BASE_BORDER_COLOR_LOCATION;
		}

		public static INamespacePrefixedString getPropertyHoveringColorLocation() {
			return PROPERTY_HOVERING_COLOR_LOCATION;
		}

		public static INamespacePrefixedString getPropertyHoveringBorderColorLocation() {
			return PROPERTY_HOVERING_BORDER_COLOR_LOCATION;
		}

		public static INamespacePrefixedString getPropertyPressedColorLocation() {
			return PROPERTY_PRESSED_COLOR_LOCATION;
		}

		public static INamespacePrefixedString getPropertyPressedBorderColorLocation() {
			return PROPERTY_PRESSED_BORDER_COLOR_LOCATION;
		}

		public static String getPropertyBaseColor() {
			return PROPERTY_BASE_COLOR;
		}

		public static String getPropertyBaseBorderColor() {
			return PROPERTY_BASE_BORDER_COLOR;
		}

		public static String getPropertyHoveringColor() {
			return PROPERTY_HOVERING_COLOR;
		}

		public static String getPropertyHoveringBorderColor() {
			return PROPERTY_HOVERING_BORDER_COLOR;
		}

		public static String getPropertyPressedColor() {
			return PROPERTY_PRESSED_COLOR;
		}

		public static String getPropertyPressedBorderColor() {
			return PROPERTY_PRESSED_BORDER_COLOR;
		}

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

		@Override
		@OverridingMethodsMustInvokeSuper
		public void cleanupBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
			super.cleanupBindings(binderObserverSupplier);
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
					() -> ImmutableBinderAction.unbind(
							getColorBase(), getColorBaseBorder(),
							getColorHovering(), getColorHoveringBorder(),
							getColorPressed(), getColorPressedBorder()
					));
		}

		protected IBindingField<Color> getColorBase() {
			return colorBase;
		}

		protected IBindingField<Color> getColorBaseBorder() {
			return colorBaseBorder;
		}

		protected IBindingField<Color> getColorHovering() {
			return colorHovering;
		}

		protected IBindingField<Color> getColorHoveringBorder() {
			return colorHoveringBorder;
		}

		protected IBindingField<Color> getColorPressed() { return colorPressed; }

		protected IBindingField<Color> getColorPressedBorder() {
			return colorPressedBorder;
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
	}
}
