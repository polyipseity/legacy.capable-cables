package $group__.client.gui.core;

import $group__.client.gui.core.structures.AffineTransformStack;
import $group__.client.gui.core.structures.IShapeDescriptor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface IGuiView<C extends IGuiComponent<?, ?, ?>, SD extends IShapeDescriptor<?, ?>> {
	SD getShapeDescriptor();

	Optional<C> getComponent();

	boolean isVisible();

	boolean setVisible(final AffineTransformStack stack, boolean visible);

	void crop(final AffineTransformStack stack, EnumCropMethod method, Point2D mouse, float partialTicks, boolean write);

	boolean render(final AffineTransformStack stack, Point2D mouse, float partialTicks);

	enum EnumCropMethod {
		GL_SCISSOR,
		STENCIL_BUFFER,
	}
}
