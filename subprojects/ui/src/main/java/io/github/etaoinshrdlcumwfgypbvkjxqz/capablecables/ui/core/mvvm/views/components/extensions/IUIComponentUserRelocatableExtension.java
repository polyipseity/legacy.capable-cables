package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.registering.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.Optional;

public interface IUIComponentUserRelocatableExtension<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends IUIExtension<INamespacePrefixedString, IUIComponent>, IHasGenericClass.Extended<IUIComponent, E>, IUIRendererContainerContainer<IUIComponentUserRelocatableExtension.IRelocatingRenderer> {

	Optional<? extends Shape> getRelocateShape();

	boolean isRelocating();

	Optional<? extends IRelocateData> getRelocateData();

	interface IRelocateData {
		Point2D getPreviousPointView();

		<R extends RectangularShape> R handle(Point2D point,
		                                      RectangularShape source,
		                                      R destination);
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
		Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIComponentUserRelocatableExtension<?>, IUIComponent>> TYPE =
				UIExtensionRegistry.getInstance().registerApply(getKEY(), k -> new ImmutableExtensionType<>(k, (t, i) -> (Optional<? extends IUIComponentUserRelocatableExtension<?>>) i.getExtension(t.getKey())));

		public static INamespacePrefixedString getKEY() {
			return KEY;
		}

		public static Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIComponentUserRelocatableExtension<?>, IUIComponent>> getTYPE() {
			return TYPE;
		}
	}
}
