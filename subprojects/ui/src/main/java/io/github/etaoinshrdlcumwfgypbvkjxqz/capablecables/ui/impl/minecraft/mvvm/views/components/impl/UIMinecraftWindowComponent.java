package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components.impl;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.rendering.IUIMinecraftComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components.rendering.UIDefaultMinecraftComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl.UIWindowComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIRotation;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IField;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class UIMinecraftWindowComponent
		extends UIWindowComponent
		implements IUIComponentMinecraft {
	private final AtomicReference<IUIRendererContainer<IUIMinecraftComponentRenderer<?>>> rendererContainerReference = new AtomicReference<>();

	@UIComponentConstructor
	public UIMinecraftWindowComponent(UIComponentConstructor.IArguments arguments) { super(arguments); }

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
	public static class DefaultRenderer<C extends UIMinecraftWindowComponent>
			extends UIDefaultMinecraftComponentRenderer<C> {
		@NonNls
		public static final String PROPERTY_BACKGROUND_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.background.color";
		@NonNls
		public static final String PROPERTY_BORDER_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.border.color";

		private static final INamespacePrefixedString PROPERTY_BACKGROUND_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyBackgroundColor());
		private static final INamespacePrefixedString PROPERTY_BORDER_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyBorderColor());

		@UIProperty(PROPERTY_BACKGROUND_COLOR)
		private final IBindingField<Color> backgroundColor;
		@UIProperty(PROPERTY_BORDER_COLOR)
		private final IBindingField<Color> borderColor;

		@UIRendererConstructor
		public DefaultRenderer(UIRendererConstructor.IArguments arguments) {
			super(arguments);

			Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
			this.backgroundColor = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.BLACK,
					mappings.get(getPropertyBackgroundColorLocation()));
			this.borderColor = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.WHITE,
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
		public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
			super.initializeBindings(binderObserverSupplier);
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
					() -> ImmutableBinderAction.bind(
							getBackgroundColor(), getBorderColor()
					)
			);
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public void cleanupBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
			super.cleanupBindings(binderObserverSupplier);
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
					() -> ImmutableBinderAction.unbind(
							getBackgroundColor(), getBorderColor()
					)
			);
		}

		public IBindingField<Color> getBackgroundColor() { return backgroundColor; }

		public IBindingField<Color> getBorderColor() { return borderColor; }

		@Override
		public void render(IUIComponentContext context, EnumRenderStage stage, C component, double partialTicks) {
			if (stage.isPreChildren()) {
				Shape relativeShape = IUIComponent.getShape(component);
				getBackgroundColor().getValue().ifPresent(color -> {
					try (AutoCloseableGraphics2D graphics = AutoCloseableGraphics2D.of(context.createGraphics())) {
						graphics.setColor(color);
						graphics.fill(relativeShape);
					}
				});
			} else if (stage.isPostChildren()) {
				// COMMENT renders the control bar
				component.getControlBarSide().getValue()
						.ifPresent(controlBarSide -> {
							getBorderColor().getValue()
									.ifPresent(controlBarColor -> {
										Rectangle2D containerShapeBounds = IUIComponent.getShape(component).getBounds2D();
										EnumUISide oppositeBorderSide = controlBarSide.getOpposite().orElseThrow(IllegalStateException::new);
										oppositeBorderSide.setValue(containerShapeBounds,
												controlBarSide.getValue(containerShapeBounds)
														+ controlBarSide.inwardsBy(IField.getValueNonnull(component.getControlBarThickness()))
														.orElseThrow(IllegalStateException::new));

										try (AutoCloseableGraphics2D graphics = AutoCloseableGraphics2D.of(context.createGraphics())) {
											graphics.setColor(controlBarColor);
											graphics.fill(containerShapeBounds);
										}
									});
							// TODO
							EnumUIRotation controlBarDirection = IField.getValueNonnull(component.getControlBarDirection());
						});
			}
		}
	}
}
