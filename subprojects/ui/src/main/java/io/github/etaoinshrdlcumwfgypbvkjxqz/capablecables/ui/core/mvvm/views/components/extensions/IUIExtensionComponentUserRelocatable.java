package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.DefaultExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.Registry;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.Optional;

public interface IUIExtensionComponentUserRelocatable<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends IUIExtension<INamespacePrefixedString, IUIComponent>, IHasGenericClass.Extended<IUIComponent, E>, IUIRendererContainer<IUIExtensionComponentUserRelocatable.IRelocatingRenderer> {
	INamespacePrefixedString KEY = new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.DEFAULT_NAMESPACE, "component.user_relocatable");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIExtensionComponentUserRelocatable<?>, IUIComponent>> TYPE =
			UIExtensionRegistry.getInstance().registerApply(KEY, k -> new DefaultExtensionType<>(k, (t, i) -> (Optional<? extends IUIExtensionComponentUserRelocatable<?>>) i.getExtension(t.getKey())));

	Optional<? extends Shape> getRelocateShape();

	default boolean isRelocating() { return getRelocateData().isPresent(); }

	Optional<? extends IRelocateData> getRelocateData();

	interface IRelocateData {
		Point2D getCursorPositionView();

		<R extends RectangularShape> R handle(Point2D cursorPosition,
		                                      RectangularShape source,
		                                      R destination);
	}

	interface IRelocatingRenderer
			extends IUIRenderer<IUIExtensionComponentUserRelocatable<?>> {
		void render(IUIComponentContext context, IRelocateData data);
	}
}
