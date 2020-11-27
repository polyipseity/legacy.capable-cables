package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IIntersection;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;

public interface IUIComponentUserRelocatableExtension<C extends IUIComponent>
		extends IUIExtension<INamespacePrefixedString, IUIComponent>, IUIRendererContainerContainer<IUIComponentUserRelocatableExtension.IRelocatingRenderer> {
	@SuppressWarnings("UnstableApiUsage")
	@Override
	TypeToken<? extends C> getTypeToken();

	Optional<? extends Shape> getRelocateShape();

	boolean isRelocating();

	Optional<? extends IRelocateData> getRelocateData();

	interface IRelocateData {
		Optional<? extends IIntersection<? extends IUIComponent, ? extends IUIReshapeExplicitly<?>>> getTargetComponent();

		Point2D getPreviousPointView();

		Optional<? extends Shape> handle(Point2D point);
	}

	interface IRelocatingRenderer
			extends IUIRenderer<IUIComponentUserRelocatableExtension<?>> {
		void render(IUIComponentContext context, IRelocateData data);
	}

	enum StaticHolder {
		;

		private static final INamespacePrefixedString KEY = ImmutableNamespacePrefixedString.of(IUIExtension.StaticHolder.getDefaultNamespace(), "component.user_relocatable");
		@SuppressWarnings("unchecked")
		private static final
		IRegistryObject<IExtensionType<INamespacePrefixedString, IUIComponentUserRelocatableExtension<?>, IUIComponent>> TYPE =
				UIExtensionRegistry.getInstance().register(getKey(), new ImmutableExtensionType<>(getKey(), (t, i) -> (Optional<? extends IUIComponentUserRelocatableExtension<?>>) i.getExtension(t.getKey())));

		public static INamespacePrefixedString getKey() {
			return KEY;
		}

		public static IRegistryObject<IExtensionType<INamespacePrefixedString, IUIComponentUserRelocatableExtension<?>, IUIComponent>> getTYPE() {
			return TYPE;
		}
	}
}
