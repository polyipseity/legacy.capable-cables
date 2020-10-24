package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.relocatable.UIAbstractComponentUserRelocatableExtensionPreviewingRelocatingRenderer;
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
public class UIComponentUserRelocatableMinecraftExtensionPreviewingRelocatingRenderer
		extends UIAbstractComponentUserRelocatableExtensionPreviewingRelocatingRenderer {
	@NonNls
	public static final String PROPERTY_PREVIEW_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.previewing.preview.color";
	private static final INamespacePrefixedString PROPERTY_PREVIEW_COLOR_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyPreviewColor());

	@UIProperty(PROPERTY_PREVIEW_COLOR)
	private final IBindingField<Color> previewColor;

	@UIRendererConstructor
	public UIComponentUserRelocatableMinecraftExtensionPreviewingRelocatingRenderer(UIRendererConstructor.IArguments arguments) {
		super(arguments);

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.previewColor = IUIPropertyMappingValue.createBindingField(Color.class, true, Color.DARK_GRAY, mappings.get(getPropertyPreviewColorLocation()));
	}

	public static INamespacePrefixedString getPropertyPreviewColorLocation() { return PROPERTY_PREVIEW_COLOR_LOCATION; }

	public static String getPropertyPreviewColor() { return PROPERTY_PREVIEW_COLOR; }

	@Override
	public void render0(IUIComponentContext context, Rectangle2D rectangle) {
		getPreviewColor().getValue()
				.ifPresent(previewColor ->
						MinecraftDrawingUtilities.drawRectangle(IUIComponentContext.getCurrentTransform(context),
								rectangle,
								previewColor.getRGB(),
								0));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(
						getPreviewColor()
				));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.cleanupBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.unbind(
						getPreviewColor()
				));
	}

	protected IBindingField<Color> getPreviewColor() { return previewColor; }
}
