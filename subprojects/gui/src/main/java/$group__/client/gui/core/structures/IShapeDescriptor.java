package $group__.client.gui.core.structures;

import $group__.client.gui.core.IGuiView;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static $group__.client.gui.core.structures.IGuiConstraint.CONSTRAINT_NULL_VALUE;
import static $group__.client.gui.core.structures.IGuiConstraint.Constants.getConstraintNullRectangleView;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
@OnlyIn(Dist.CLIENT)
public interface IShapeDescriptor<S extends Shape, A extends IGuiAnchorSet<?>> {
	static void checkIsBeingModified(IShapeDescriptor<?, ?> shapeDescriptor) throws IllegalStateException {
		if (!shapeDescriptor.isBeingModified())
			throw new IllegalStateException("Not marked as being modified");
	}

	boolean isBeingModified();

	Shape getShapeProcessed();

	Optional<IGuiView<?, ?>> getView();

	List<IGuiConstraint> getConstraintsView();

	List<IGuiConstraint> getConstraintsRef()
			throws IllegalStateException;

	A getAnchorSet();

	<T extends IShapeDescriptor<?, ?>> boolean modify(T self, Function<? super T, Boolean> action)
			throws ConcurrentModificationException;

	boolean setShape(S shape)
			throws IllegalStateException;

	S getShapeRef()
			throws IllegalStateException;

	boolean bound(Rectangle2D bounds)
			throws IllegalStateException;

	boolean transform(AffineTransform transform)
			throws IllegalStateException;

	@OnlyIn(Dist.CLIENT)
	enum Constants {
		;

		private static final GuiConstraint CONSTRAINT_MINIMUM = new GuiConstraint(new Rectangle2D.Double(CONSTRAINT_NULL_VALUE, CONSTRAINT_NULL_VALUE, 1, 1), getConstraintNullRectangleView());

		public static IGuiConstraint getConstraintMinimumView() { return CONSTRAINT_MINIMUM.copy(); }
	}
}
