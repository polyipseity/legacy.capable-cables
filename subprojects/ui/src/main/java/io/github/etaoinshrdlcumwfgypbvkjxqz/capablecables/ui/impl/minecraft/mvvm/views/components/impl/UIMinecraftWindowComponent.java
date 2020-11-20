package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components.impl;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.rendering.IUIMinecraftComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components.rendering.UIDefaultMinecraftComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl.UIWindowComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIRotation;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
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
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class UIMinecraftWindowComponent
		extends UIWindowComponent
		implements IUIComponentMinecraft {
	@SuppressWarnings({"ThisEscapedInObjectConstruction", "unchecked", "rawtypes"})
	private final IUIRendererContainerContainer<IUIMinecraftComponentRenderer<?>> rendererContainerContainer =
			new UIDefaultRendererContainerContainer<>(this,
					UIDefaultRendererContainerContainer.createRendererContainerInitializer(DefaultRenderer.class));

	@UIComponentConstructor
	public UIMinecraftWindowComponent(IUIComponentArguments arguments) { super(arguments); }

	@Override
	public IUIRendererContainer<? extends IUIMinecraftComponentRenderer<?>> getRendererContainer()
			throws IllegalStateException {
		return getRendererContainerContainer().getRendererContainer();
	}

	protected IUIRendererContainerContainer<IUIMinecraftComponentRenderer<?>> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	@Override
	public void initializeRendererContainer(@NonNls CharSequence name)
			throws IllegalStateException {
		getRendererContainerContainer().initializeRendererContainer(name);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.initializeBindings(
				ImmutableList.of(getRendererContainerContainer()),
				binderObserverSupplier
		);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.cleanupBindings(binderObserverSupplier);
		BindingUtilities.cleanupBindings(
				ImmutableList.of(getRendererContainerContainer()),
				binderObserverSupplier
		);
	}

	@Override
	public void tick(IUIComponentContext context) {}

	@OnlyIn(Dist.CLIENT)
	public static class DefaultRenderer<C extends UIMinecraftWindowComponent>
			extends UIDefaultMinecraftComponentRenderer<C> {
		@NonNls
		public static final String PROPERTY_BACKGROUND_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.background.color";
		@NonNls
		public static final String PROPERTY_BORDER_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.color";

		private static final INamespacePrefixedString PROPERTY_BACKGROUND_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyBackgroundColor());
		private static final INamespacePrefixedString PROPERTY_BORDER_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyBorderColor());

		@UIProperty(PROPERTY_BACKGROUND_COLOR)
		private final IBindingField<Color> backgroundColor;
		@UIProperty(PROPERTY_BORDER_COLOR)
		private final IBindingField<Color> controlsColor;

		@UIRendererConstructor
		public DefaultRenderer(IUIRendererArguments arguments) {
			super(arguments);

			Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
			this.backgroundColor = IUIPropertyMappingValue.createBindingField(Color.class, Color.BLACK,
					mappings.get(getPropertyBackgroundColorLocation()));
			this.controlsColor = IUIPropertyMappingValue.createBindingField(Color.class, Color.WHITE,
					mappings.get(getPropertyBorderColorLocation()));
		}

		public static INamespacePrefixedString getPropertyBackgroundColorLocation() {
			return PROPERTY_BACKGROUND_COLOR_LOCATION;
		}

		public static INamespacePrefixedString getPropertyBorderColorLocation() {
			return PROPERTY_BORDER_COLOR_LOCATION;
		}

		public static String getPropertyBackgroundColor() {
			return PROPERTY_BACKGROUND_COLOR;
		}

		public static String getPropertyBorderColor() {
			return PROPERTY_BORDER_COLOR;
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
			super.initializeBindings(binderObserverSupplier);
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
					() -> ImmutableBinderAction.bind(
							getBackgroundColor(), getControlsColor()
					)
			);
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public void cleanupBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
			super.cleanupBindings(binderObserverSupplier);
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
					() -> ImmutableBinderAction.unbind(
							getBackgroundColor(), getControlsColor()
					)
			);
		}

		public IBindingField<Color> getBackgroundColor() { return backgroundColor; }

		public IBindingField<Color> getControlsColor() { return controlsColor; }

		@Override
		public void render(IUIComponentContext context, EnumRenderStage stage, C component, double partialTicks) {
			if (stage.isPreChildren()) {
				Shape relativeShape = IUIComponent.getShape(component);
				try (AutoCloseableGraphics2D graphics = AutoCloseableGraphics2D.of(context.createGraphics())) {
					graphics.setColor(getBackgroundColor().getValue());
					graphics.fill(relativeShape);
				}
			} else if (stage.isPostChildren()) {
				// COMMENT renders the controls
				EnumUISide controlsSide = component.getControlsSide().getValue();

				Rectangle2D containerShapeBounds = IUIComponent.getShape(component).getBounds2D();
				EnumUISide oppositeBorderSide = controlsSide.getOpposite().orElseThrow(IllegalStateException::new);
				oppositeBorderSide.setValue(containerShapeBounds,
						controlsSide.getValue(containerShapeBounds)
								+ controlsSide.inwardsBy(component.getControlsThickness().getValue())
								.orElseThrow(IllegalStateException::new));

				try (AutoCloseableGraphics2D graphics = AutoCloseableGraphics2D.of(context.createGraphics())) {
					graphics.setColor(getControlsColor().getValue());
					graphics.fill(containerShapeBounds);
				}

				// TODO
				EnumUIRotation controlsDirection = component.getControlsDirection().getValue();
			}
		}
	}
}
