package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.extensions;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.IIntersection;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObject;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;

public interface IUIComponentUserRelocatableExtension<C extends IUIComponent>
		extends IUIExtension<IIdentifier, IUIComponent>, IUIRendererContainerContainer<IUIComponentUserRelocatableExtension.IRelocatingRenderer> {
	@SuppressWarnings("UnstableApiUsage")
	@Override
	TypeToken<? extends C> getTypeToken();

	Optional<? extends Shape> getRelocateShape();

	boolean isRelocating();

	Optional<? extends IRelocateData> getRelocateData();

	enum StaticHolder {
		;

		private static final IIdentifier KEY = ImmutableIdentifier.ofInterning(IUIExtension.StaticHolder.getDefaultNamespace(), "component.user_relocatable");
		@SuppressWarnings("unchecked")
		private static final
		IRegistryObject<IExtensionType<IIdentifier, IUIComponentUserRelocatableExtension<?>, IUIComponent>> TYPE =
				UIExtensionRegistry.getInstance().register(getKey(), new ImmutableExtensionType<>(getKey(), (t, i) -> (Optional<? extends IUIComponentUserRelocatableExtension<?>>) i.getExtension(t.getKey())));

		public static IIdentifier getKey() {
			return KEY;
		}

		public static IRegistryObject<IExtensionType<IIdentifier, IUIComponentUserRelocatableExtension<?>, IUIComponent>> getTYPE() {
			return TYPE;
		}
	}

	interface IRelocateData {
		Optional<? extends IIntersection<? extends IUIComponent, ? extends IUIReshapeExplicitly<?>>> getTargetComponent();

		Point2D getPreviousPointView();

		Optional<? extends Shape> handle(Point2D point);
	}

	interface IRelocatingRenderer
			extends IUIRenderer<IUIComponentUserRelocatableExtension<?>> {
		void render(IUIComponentContext context, IRelocateData data);
	}
}
