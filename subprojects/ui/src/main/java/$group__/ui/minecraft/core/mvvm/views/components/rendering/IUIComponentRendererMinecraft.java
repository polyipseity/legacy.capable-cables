package $group__.ui.minecraft.core.mvvm.views.components.rendering;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRenderer;
import $group__.ui.minecraft.core.mvvm.views.EnumCropMethod;
import $group__.ui.mvvm.views.components.extensions.caches.UIExtensionCache;
import $group__.ui.utilities.UIObjectUtilities;
import $group__.ui.utilities.minecraft.CoordinateUtilities;
import $group__.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.client.minecraft.GLUtilities;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

@OnlyIn(Dist.CLIENT)
public interface IUIComponentRendererMinecraft<C extends IUIComponent>
		extends IUIComponentRenderer<C> {
	void render(C container, IAffineTransformStack stack, Point2D cursorPosition, double partialTicks, boolean pre);

	default void crop(C container, IAffineTransformStack stack, EnumCropMethod method, boolean push, Point2D mouse, double partialTicks) {
		IUIComponentRendererMinecraft.cropImpl(container, stack, method, push, mouse, partialTicks);
	}

	static void cropImpl(final IUIComponent container, IAffineTransformStack stack, EnumCropMethod method, boolean push, @SuppressWarnings("unused") Point2D mouse, @SuppressWarnings("unused") double partialTicks) {
		switch (method) {
			case STENCIL_BUFFER:
				if (push) {
					int stencilRef = Math.floorMod(UIExtensionCache.CacheUniversal.Z.getValue().get(container).orElseThrow(InternalError::new), (int) Math.pow(2, GLUtilities.GLStateUtilities.getInteger(GL11.GL_STENCIL_BITS)));

					GLUtilities.GLStacksUtilities.push("stencilFunc",
							() -> RenderSystem.stencilFunc(GL11.GL_EQUAL, stencilRef, GLUtilities.GL_MASK_ALL_BITS), GLUtilities.GLStacksUtilities.STENCIL_FUNC_FALLBACK);
					GLUtilities.GLStacksUtilities.push("stencilOp",
							() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL14.GL_INCR_WRAP, GL14.GL_INCR_WRAP), GLUtilities.GLStacksUtilities.STENCIL_OP_FALLBACK);
					GLUtilities.GLStacksUtilities.push("colorMask",
							() -> RenderSystem.colorMask(false, false, false, false), GLUtilities.GLStacksUtilities.COLOR_MASK_FALLBACK);

					DrawingUtilities.drawShape(stack.getDelegated().peek(), container.getShapeDescriptor().getShapeOutput(), true, Color.WHITE, 0);

					GLUtilities.GLStacksUtilities.pop("colorMask");
					GLUtilities.GLStacksUtilities.pop("stencilOp");
					GLUtilities.GLStacksUtilities.pop("stencilFunc");

					GLUtilities.GLStacksUtilities.push("stencilFunc",
							() -> RenderSystem.stencilFunc(GL11.GL_LESS, stencilRef, GLUtilities.GL_MASK_ALL_BITS), GLUtilities.GLStacksUtilities.STENCIL_FUNC_FALLBACK);

					GLUtilities.GLStacksUtilities.push("stencilOp",
							() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP), GLUtilities.GLStacksUtilities.STENCIL_OP_FALLBACK);
				} else {
					GLUtilities.GLStacksUtilities.pop("stencilOp");

					GLUtilities.GLStacksUtilities.push("stencilOp",
							() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_REPLACE, GL11.GL_REPLACE), GLUtilities.GLStacksUtilities.STENCIL_OP_FALLBACK);
					GLUtilities.GLStacksUtilities.push("colorMask",
							() -> RenderSystem.colorMask(false, false, false, false), GLUtilities.GLStacksUtilities.COLOR_MASK_FALLBACK);

					DrawingUtilities.drawShape(stack.getDelegated().peek(), container.getShapeDescriptor().getShapeOutput().getBounds2D(), true, Color.BLACK, 0);

					GLUtilities.GLStacksUtilities.pop("colorMask");
					GLUtilities.GLStacksUtilities.pop("stencilOp");

					GLUtilities.GLStacksUtilities.pop("stencilFunc");
				}
				break;
			case GL_SCISSOR:
				if (push) {
					int[] boundsBox = new int[4];
					GLUtilities.GLStateUtilities.getIntegerValue(GL11.GL_SCISSOR_BOX, boundsBox);
					UIObjectUtilities.acceptRectangular(
							CoordinateUtilities.toNativeRectangle(
									UIObjectUtilities.getRectangleExpanded(stack.getDelegated().peek().createTransformedShape(container.getShapeDescriptor().getShapeOutput().getBounds2D()).getBounds2D()))
									.createIntersection(new Rectangle2D.Double(boundsBox[0], boundsBox[1], boundsBox[2], boundsBox[3])),
							(x, y, w, h) -> GLUtilities.GLStacksUtilities.push("glScissor",
									() -> GLUtilities.GLStateUtilities.setIntegerValue(GL11.GL_SCISSOR_BOX, new int[]{x.intValue(), y.intValue(), w.intValue(), h.intValue()},
											(i, v) -> GL11.glScissor(v[0], v[1], v[2], v[3])), GLUtilities.GLStacksUtilities.GL_SCISSOR_FALLBACK));
				} else
					GLUtilities.GLStacksUtilities.pop("glScissor");
				break;
		}
	}
}
