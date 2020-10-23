package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.Optional;
import java.util.Set;

public interface IUIComponentUserResizableExtension<C extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends IUIExtension<INamespacePrefixedString, IUIComponent>, IUIRendererContainerContainer<IUIComponentUserResizableExtension.IResizingRenderer> {
	@SuppressWarnings("UnstableApiUsage")
	@Override
	TypeToken<? extends C> getTypeToken();

	Optional<? extends Shape> getResizeShape();

	default boolean isResizing() { return getResizeData().isPresent(); }

	Optional<? extends IResizeData> getResizeData();

	interface IResizeData {
		Point2D getPreviousPointView();

		Set<? extends EnumUISide> getSidesView();

		Optional<? extends Point2D> getBaseView();

		long getInitialCursorHandle();

		<R extends RectangularShape> R handle(Point2D point,
		                                      RectangularShape source,
		                                      R destination);
	}

	interface IResizingRenderer
			extends IUIRenderer<IUIComponentUserResizableExtension<?>> {
		void render(IUIComponentContext context, IResizeData data);
	}

	enum StaticHolder {
		;

		private static final INamespacePrefixedString KEY = ImmutableNamespacePrefixedString.of(IUIExtension.StaticHolder.getDefaultNamespace(), "component.user_resizable");
		@SuppressWarnings("unchecked")
		private static final
		IRegistryObject<IExtensionType<INamespacePrefixedString, IUIComponentUserResizableExtension<?>, IUIComponent>> TYPE =
				UIExtensionRegistry.getInstance().register(getKey(), new ImmutableExtensionType<>(getKey(), (t, i) -> (Optional<? extends IUIComponentUserResizableExtension<?>>) i.getExtension(t.getKey())));

		public static INamespacePrefixedString getKey() {
			return KEY;
		}

		public static IRegistryObject<IExtensionType<INamespacePrefixedString, IUIComponentUserResizableExtension<?>, IUIComponent>> getType() {
			return TYPE;
		}
	}
}
