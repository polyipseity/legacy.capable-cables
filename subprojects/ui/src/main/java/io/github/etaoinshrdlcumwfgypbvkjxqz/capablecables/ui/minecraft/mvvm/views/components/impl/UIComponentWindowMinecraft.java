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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.impl.UIComponentWindow;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering.UIDefaultRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields.IBindingField;
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
public class UIComponentWindowMinecraft
		extends UIComponentWindow
		implements IUIComponentMinecraft {
	@UIComponentConstructor
	public UIComponentWindowMinecraft(UIComponentConstructor.IArguments arguments) { super(arguments); }

	private final AtomicReference<IUIRendererContainer<IUIComponentRendererMinecraft<?>>> rendererContainerReference = new AtomicReference<>();

	@Override
	public IUIRendererContainer<? extends IUIComponentRendererMinecraft<?>> getRendererContainer()
			throws IllegalStateException { return Optional.ofNullable(getRendererContainerReference().get()).orElseThrow(IllegalStateException::new); }

	@Override
	public void initializeRendererContainer(String name)
			throws IllegalStateException {
		IUIRendererContainer<IUIComponentRendererMinecraft<?>> rendererContainer = new UIDefaultRendererContainer<>(name, this, CastUtilities.castUnchecked(DefaultRenderer.class));
		if (!getRendererContainerReference().compareAndSet(null, rendererContainer))
			throw new IllegalStateException();
		getBinderObserverSupplier().ifPresent(rendererContainer::initializeBindings);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		Optional.ofNullable(getRendererContainerReference().get())
				.ifPresent(rendererContainer -> rendererContainer.initializeBindings(binderObserverSupplier));
	}

	protected AtomicReference<IUIRendererContainer<IUIComponentRendererMinecraft<?>>> getRendererContainerReference() { return rendererContainerReference; }

	@Override
	public void tick(IUIComponentContext context) {}

	@OnlyIn(Dist.CLIENT)
	public static class DefaultRenderer<C extends UIComponentWindowMinecraft>
			extends NullUIComponentRendererMinecraft<C> {
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

		@Override
		@OverridingMethodsMustInvokeSuper
		public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
			super.initializeBindings(binderObserverSupplier);
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
					() -> ImmutableBinderAction.bind(getColorBackground(), getColorBorder()));
		}

		@UIRendererConstructor
		public DefaultRenderer(UIRendererConstructor.IArguments arguments) {
			super(arguments);

			Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
			this.colorBackground = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.BLACK,
					mappings.get(PROPERTY_COLOR_BACKGROUND_LOCATION));
			this.colorBorder = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.WHITE,
					mappings.get(PROPERTY_COLOR_BORDER_LOCATION));
		}

		@Override
		public void render(IUIComponentContext context, C container, EnumRenderStage stage, double partialTicks) {
			if (stage.isPreChildren()) {
				Shape transformed = IUIComponent.StaticHolder.getContextualShape(context, container);
				getColorBackground().getValue().ifPresent(c ->
						MinecraftDrawingUtilities.drawShape(transformed, true, c, 0));
				getColorBorder().getValue().ifPresent(c ->
						MinecraftDrawingUtilities.drawShape(transformed, true, c, 0));
			}
		}

		public IBindingField<Color> getColorBackground() { return colorBackground; }

		public IBindingField<Color> getColorBorder() { return colorBorder; }
	}
}
