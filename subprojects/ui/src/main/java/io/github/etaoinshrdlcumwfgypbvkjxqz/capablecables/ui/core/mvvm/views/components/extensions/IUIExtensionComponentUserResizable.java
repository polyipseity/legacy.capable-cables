package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.DefaultExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IFunction4;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.Registry;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.Optional;
import java.util.Set;

public interface IUIExtensionComponentUserResizable<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends IUIExtension<INamespacePrefixedString, IUIComponent>, IHasGenericClass.Extended<IUIComponent, E> {
	INamespacePrefixedString KEY = new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.DEFAULT_NAMESPACE, "component.user_resizable");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIExtensionComponentUserResizable<?>, IUIComponent>> TYPE =
			UIExtensionRegistry.getInstance().registerApply(KEY, k -> new DefaultExtensionType<>(k, (t, i) -> (Optional<? extends IUIExtensionComponentUserResizable<?>>) i.getExtension(t.getKey())));

	Optional<? extends Shape> getResizeShape();

	default boolean isResizing() { return getResizeData().isPresent(); }

	Optional<? extends IResizeData> getResizeData();

	interface IResizeData {
		Point2D getCursorPositionView();

		Set<? extends EnumUISide> getSidesView();

		Optional<? extends Point2D> getBaseView();

		long getInitialCursorHandle();

		<T extends RectangularShape, TH extends Throwable> T handle(RectangularShape rectangular,
		                                                            Point2D cursorPosition,
		                                                            IFunction4<? super Double, ? super Double, ? super Double, ? super Double, ? extends T, ? extends TH> constructor)
				throws TH;
	}
}
