package $group__.client.ui.mvvm.core.views.components.extensions;

import $group__.client.ui.core.IShapeDescriptor;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.views.IUIReshapeExplicitly;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.structures.Registry;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.Optional;

public interface IUIExtensionComponentUserRelocatable<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends IUIExtension<ResourceLocation, IUIComponent>, IHasGenericClass.Extended<IUIComponent, E> {
	ResourceLocation KEY = new ResourceLocation(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, AREA_UI + ".component.user_relocatable");
	Registry.RegistryObject<IUIExtension.IType<ResourceLocation, IUIExtensionComponentUserRelocatable<?>, IUIComponent>> TYPE =
			RegExtension.INSTANCE.registerApply(KEY, k -> new IUIExtension.IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));

	Optional<? extends Shape> getRelocateShape();

	default boolean isRelocating() { return getRelocateData().isPresent(); }

	Optional<IRelocateData> getRelocateData();

	interface IRelocateData {
		Point2D getCursorPositionView();

		void handle(RectangularShape rectangular, Point2D cursorPosition);
	}
}