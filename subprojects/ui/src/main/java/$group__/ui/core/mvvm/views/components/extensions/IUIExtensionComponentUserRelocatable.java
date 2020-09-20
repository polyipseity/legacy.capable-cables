package $group__.ui.core.mvvm.views.components.extensions;

import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.core.mvvm.views.IUIReshapeExplicitly;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.utilities.extensions.AbstractExtensionRegistry;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.structures.Registry;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.Optional;

public interface IUIExtensionComponentUserRelocatable<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends IUIExtension<INamespacePrefixedString, IUIComponent>, IHasGenericClass.Extended<IUIComponent, E> {
	INamespacePrefixedString KEY = new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.getDefaultNamespace(), AREA_UI + ".component.user_relocatable");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IUIExtension.IType<INamespacePrefixedString, IUIExtensionComponentUserRelocatable<?>, IUIComponent>> TYPE =
			AbstractExtensionRegistry.INSTANCE.registerApply(KEY, k -> new IUIExtension.IType.Impl<>(k, (t, i) -> (Optional<? extends IUIExtensionComponentUserRelocatable<?>>) i.getExtension(t.getKey())));

	Optional<? extends Shape> getRelocateShape();

	default boolean isRelocating() { return getRelocateData().isPresent(); }

	Optional<? extends IRelocateData> getRelocateData();

	interface IRelocateData {
		Point2D getCursorPositionView();

		void handle(RectangularShape rectangular, Point2D cursorPosition);
	}
}
