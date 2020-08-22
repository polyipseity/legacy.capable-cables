package $group__.client.ui.mvvm.core.views.components.extensions;

import $group__.client.ui.core.structures.shapes.IShapeDescriptor;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.views.IUIReshapeExplicitly;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.structures.EnumUISide;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.structures.Registry;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.EnumSet;
import java.util.Optional;

public interface IUIExtensionComponentUserResizable<E extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends IUIExtension<ResourceLocation, IUIComponent>, IHasGenericClass.Extended<IUIComponent, E> {
	ResourceLocation KEY = new ResourceLocation(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, AREA_UI + ".component.user_resizable");
	Registry.RegistryObject<IUIExtension.IType<ResourceLocation, IUIExtensionComponentUserResizable<?>, IUIComponent>> TYPE =
			RegExtension.INSTANCE.registerApply(KEY, k -> new IUIExtension.IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));

	Optional<? extends Shape> getResizeShape();

	default boolean isResizing() { return getResizeData().isPresent(); }

	Optional<? extends IResizeData> getResizeData();

	interface IResizeData {
		Point2D getCursorPositionView();

		EnumSet<EnumUISide> getSidesView();

		Optional<Point2D> getBaseView();

		long getInitialCursorHandle();

		void handle(RectangularShape rectangular, Point2D cursorPosition);
	}
}
