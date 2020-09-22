package $group__.ui.minecraft.core.mvvm.views.components.rendering;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRenderer;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.ui.minecraft.core.mvvm.views.EnumCropMethod;
import $group__.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import $group__.ui.mvvm.views.components.extensions.caches.UICacheExtension;
import $group__.ui.utilities.UIObjectUtilities;
import $group__.ui.utilities.minecraft.CoordinateUtilities;
import $group__.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.minecraft.client.GLUtilities;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

@OnlyIn(Dist.CLIENT)
public interface IUIComponentRendererMinecraft<C extends IUIComponent & IUIComponentMinecraft>
		extends IUIComponentRenderer<C> {
	void render(IUIComponentContext context, C container, EnumRenderStage stage, double partialTicks);

	default void crop(IUIComponentContext context, C container, EnumCropStage stage, EnumCropMethod method, double partialTicks) {
		IUIComponentRendererMinecraft.cropImpl(context, container, stage, method, partialTicks);
	}

	static void cropImpl(IUIComponentContext context, final IUIComponent container, EnumCropStage stage, EnumCropMethod method, @SuppressWarnings("unused") double partialTicks) {
		AffineTransform transform = context.getTransformStack().element();
		switch (method) {
			case STENCIL_BUFFER:
				switch (stage) {
					case CROP:
						int stencilRef = Math.floorMod(UICacheExtension.CacheUniversal.Z.getValue().get(container).orElseThrow(InternalError::new), (int) Math.pow(2, GLUtilities.State.getInteger(GL11.GL_STENCIL_BITS)));

						GLUtilities.Stacks.push("stencilFunc",
								() -> RenderSystem.stencilFunc(GL11.GL_EQUAL, stencilRef, GLUtilities.GL_MASK_ALL_BITS), GLUtilities.Stacks.STENCIL_FUNC_FALLBACK);
						GLUtilities.Stacks.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL14.GL_INCR_WRAP, GL14.GL_INCR_WRAP), GLUtilities.Stacks.STENCIL_OP_FALLBACK);
						GLUtilities.Stacks.push("colorMask",
								() -> RenderSystem.colorMask(false, false, false, false), GLUtilities.Stacks.COLOR_MASK_FALLBACK);

						DrawingUtilities.drawShape(transform, container.getShapeDescriptor().getShapeOutput(), true, Color.WHITE, 0);

						GLUtilities.Stacks.pop("colorMask");
						GLUtilities.Stacks.pop("stencilOp");
						GLUtilities.Stacks.pop("stencilFunc");

						GLUtilities.Stacks.push("stencilFunc",
								() -> RenderSystem.stencilFunc(GL11.GL_LESS, stencilRef, GLUtilities.GL_MASK_ALL_BITS), GLUtilities.Stacks.STENCIL_FUNC_FALLBACK);

						GLUtilities.Stacks.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP), GLUtilities.Stacks.STENCIL_OP_FALLBACK);
						break;
					case UN_CROP:
						GLUtilities.Stacks.pop("stencilOp");

						GLUtilities.Stacks.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_REPLACE, GL11.GL_REPLACE), GLUtilities.Stacks.STENCIL_OP_FALLBACK);
						GLUtilities.Stacks.push("colorMask",
								() -> RenderSystem.colorMask(false, false, false, false), GLUtilities.Stacks.COLOR_MASK_FALLBACK);

						DrawingUtilities.drawShape(transform, container.getShapeDescriptor().getShapeOutput().getBounds2D(), true, Color.BLACK, 0);

						GLUtilities.Stacks.pop("colorMask");
						GLUtilities.Stacks.pop("stencilOp");

						GLUtilities.Stacks.pop("stencilFunc");
						break;
					default:
						throw new InternalError();
				}
				break;
			case GL_SCISSOR:
				switch (stage) {
					case CROP:
						int[] boundsBox = new int[4];
						GLUtilities.State.getIntegerValue(GL11.GL_SCISSOR_BOX, boundsBox);
						UIObjectUtilities.acceptRectangular(
								CoordinateUtilities.toNativeRectangle(
										UIObjectUtilities.getRectangleExpanded(transform.createTransformedShape(container.getShapeDescriptor().getShapeOutput().getBounds2D()).getBounds2D()))
										.createIntersection(new Rectangle2D.Double(boundsBox[0], boundsBox[1], boundsBox[2], boundsBox[3])),
								(x, y, w, h) -> GLUtilities.Stacks.push("glScissor",
										() -> GLUtilities.State.setIntegerValue(GL11.GL_SCISSOR_BOX, new int[]{x.intValue(), y.intValue(), w.intValue(), h.intValue()},
												(i, v) -> GL11.glScissor(v[0], v[1], v[2], v[3])), GLUtilities.Stacks.GL_SCISSOR_FALLBACK));
						break;
					case UN_CROP:
						GLUtilities.Stacks.pop("glScissor");
						break;
					default:
						throw new InternalError();
				}
				break;
		}
	}

	@OnlyIn(Dist.CLIENT)
	enum EnumRenderStage {
		PRE_CHILDREN,
		POST_CHILDREN,
	}

	@OnlyIn(Dist.CLIENT)
	enum EnumCropStage {
		CROP,
		UN_CROP,
	}
}
