package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IIntersection;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.Set;

public interface IUIComponentUserResizableExtension<C extends IUIComponent>
		extends IUIExtension<IIdentifier, IUIComponent>, IUIRendererContainerContainer<IUIComponentUserResizableExtension.IResizingRenderer> {
	@SuppressWarnings("UnstableApiUsage")
	@Override
	TypeToken<? extends C> getTypeToken();

	Optional<? extends Shape> getResizeShape();

	default boolean isResizing() { return getResizeData().isPresent(); }

	Optional<? extends IResizeData> getResizeData();

	enum StaticHolder {
		;

		private static final IIdentifier KEY = ImmutableIdentifier.of(IUIExtension.StaticHolder.getDefaultNamespace(), "component.user_resizable");
		@SuppressWarnings("unchecked")
		private static final
		IRegistryObject<IExtensionType<IIdentifier, IUIComponentUserResizableExtension<?>, IUIComponent>> TYPE =
				UIExtensionRegistry.getInstance().register(getKey(), new ImmutableExtensionType<>(getKey(), (t, i) -> (Optional<? extends IUIComponentUserResizableExtension<?>>) i.getExtension(t.getKey())));

		public static IIdentifier getKey() {
			return KEY;
		}

		public static IRegistryObject<IExtensionType<IIdentifier, IUIComponentUserResizableExtension<?>, IUIComponent>> getType() {
			return TYPE;
		}
	}

	interface IResizeData {
		Optional<? extends IIntersection<? extends IUIComponent, ? extends IUIReshapeExplicitly<?>>> getTargetComponent();

		Point2D getPreviousPointView();

		Set<? extends EnumUISide> getSidesView();

		Optional<? extends Point2D> getBaseView();

		long getInitialCursorHandle();

		Optional<? extends Shape> handle(Point2D point);
	}

	interface IResizingRenderer
			extends IUIRenderer<IUIComponentUserResizableExtension<?>> {
		void render(IUIComponentContext context, IResizeData data);
	}
}
