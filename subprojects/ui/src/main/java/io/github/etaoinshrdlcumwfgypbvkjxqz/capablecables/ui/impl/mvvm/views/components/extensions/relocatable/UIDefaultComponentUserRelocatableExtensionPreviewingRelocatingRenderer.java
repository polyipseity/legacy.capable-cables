package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.relocatable;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IPointerDevice;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIDefaultComponentUserRelocatableExtensionPreviewingRelocatingRenderer
		extends UIAbstractComponentUserRelocatableExtensionRelocatingRenderer {
	@NonNls
	public static final String PROPERTY_PREVIEW_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.previewing.preview.color";
	private static final IIdentifier PROPERTY_PREVIEW_COLOR_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyPreviewColor());
	@UIProperty(PROPERTY_PREVIEW_COLOR)
	private final IBindingField<Color> previewColor; // TODO Color to Paint

	@UIRendererConstructor
	public UIDefaultComponentUserRelocatableExtensionPreviewingRelocatingRenderer(IUIRendererArguments arguments) {
		super(arguments);

		Map<IIdentifier, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.previewColor = IUIPropertyMappingValue.createBindingField(Color.class, ConstantValue.of(Color.DARK_GRAY), mappings.get(getPropertyPreviewColorIdentifier()));
	}

	public static IIdentifier getPropertyPreviewColorIdentifier() { return PROPERTY_PREVIEW_COLOR_IDENTIFIER; }

	public static String getPropertyPreviewColor() { return PROPERTY_PREVIEW_COLOR; }

	@Override
	public final void render(IUIComponentContext context, IUIComponentUserRelocatableExtension.IRelocateData data) {
		context.getViewContext().getInputDevices().getPointerDevice()
				.map(IPointerDevice::getPositionView)
				.flatMap(data::handle)
				.ifPresent(shape -> render0(context, shape));
	}

	protected void render0(IUIComponentContext context, Shape shape) {
		try (IUIComponentContext parentContext = context.clone()) {
			parentContext.getMutator().pop(parentContext); // COMMENT draw in the parent context
			IUIComponentContext.withGraphics(context, graphics -> IUIComponentContext.withGraphics(parentContext, parentGraphics -> {
				parentGraphics.setTransform(graphics.getTransform()); // COMMENT but with the current transform
				parentGraphics.setColor(getPreviewColor().getValue());
				// COMMENT the result is that the clipping is relaxed
				parentGraphics.draw(shape);
			}));
		}
	}

	protected IBindingField<Color> getPreviewColor() { return previewColor; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		super.initializeBindings(bindingActionConsumerSupplier);
		BindingUtilities.supplyBindingAction(bindingActionConsumerSupplier,
				() -> ImmutableBindingAction.bind(ImmutableList.of(
						getPreviewColor()
				)));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumer ->
				BindingUtilities.supplyBindingAction(bindingActionConsumer,
						() -> ImmutableBindingAction.unbind(ImmutableList.of(
								getPreviewColor()
						)))
		);
		super.cleanupBindings();
	}
}
