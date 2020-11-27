package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.resizable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputPointerDevice;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class UIDefaultComponentUserResizableExtensionPreviewingResizingRenderer
		extends UIAbstractComponentUserResizeableExtensionResizingRenderer {
	@NonNls
	public static final String PROPERTY_PREVIEW_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.previewing.preview.color";
	private static final INamespacePrefixedString PROPERTY_PREVIEW_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyPreviewColor());
	@UIProperty(PROPERTY_PREVIEW_COLOR)
	private final IBindingField<Color> previewColor; // TODO Color to Paint

	@Override
	public final void render(IUIComponentContext context, IUIComponentUserResizableExtension.IResizeData data) {
		context.getViewContext().getInputDevices().getPointerDevice()
				.map(IInputPointerDevice::getPositionView)
				.flatMap(data::handle)
				.ifPresent(shape -> render0(context, shape));
	}

	@UIRendererConstructor
	public UIDefaultComponentUserResizableExtensionPreviewingResizingRenderer(IUIRendererArguments arguments) {
		super(arguments);

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.previewColor =
				IUIPropertyMappingValue.createBindingField(Color.class, Color.DARK_GRAY,
						mappings.get(getPropertyPreviewColorLocation()));
	}

	public static INamespacePrefixedString getPropertyPreviewColorLocation() { return PROPERTY_PREVIEW_COLOR_LOCATION; }

	public static String getPropertyPreviewColor() { return PROPERTY_PREVIEW_COLOR; }

	protected void render0(IUIComponentContext context, Shape shape) {
		try (IUIComponentContext contextCopy = context.clone()) {
			contextCopy.getMutator().pop(contextCopy); // COMMENT draw in the parent context
			try (AutoCloseableGraphics2D currentGraphics = AutoCloseableGraphics2D.of(context.createGraphics());
			     AutoCloseableGraphics2D graphics = AutoCloseableGraphics2D.of(contextCopy.createGraphics())) {
				graphics.setTransform(currentGraphics.getTransform()); // COMMENT but with the current transform
				graphics.setColor(getPreviewColor().getValue());
				// COMMENT the result is that the clipping is relaxed
				graphics.draw(shape);
			}
		}
	}

	protected IBindingField<Color> getPreviewColor() { return previewColor; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(
						getPreviewColor()
				));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
						() -> ImmutableBinderAction.unbind(
								getPreviewColor()
						))
		);
		super.cleanupBindings();
	}
}
