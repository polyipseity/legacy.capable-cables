package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.utilities.EnumCropMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.utilities.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.caches.UICacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.EnumMinecraftUICoordinateSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.CoordinateSystemUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.UIObjectUtilities;
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

	static void cropImpl(IUIComponentContext componentContext, final IUIComponent container, EnumCropStage stage, EnumCropMethod method, @SuppressWarnings("unused") double partialTicks) {
		AffineTransform transform = IUIComponentContext.StaticHolder.getCurrentTransform(componentContext);
		switch (method) {
			case STENCIL_BUFFER:
				switch (stage) {
					case CROP:
						int stencilRef = Math.floorMod(UICacheExtension.CacheUniversal.Z.getValue().get(container).orElseThrow(InternalError::new), (int) Math.pow(2, MinecraftOpenGLUtilities.State.getInteger(GL11.GL_STENCIL_BITS)));

						MinecraftOpenGLUtilities.Stacks.push("stencilFunc",
								() -> RenderSystem.stencilFunc(GL11.GL_EQUAL, stencilRef, MinecraftOpenGLUtilities.GL_MASK_ALL_BITS), MinecraftOpenGLUtilities.Stacks.STENCIL_FUNC_FALLBACK);
						MinecraftOpenGLUtilities.Stacks.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL14.GL_INCR_WRAP, GL14.GL_INCR_WRAP), MinecraftOpenGLUtilities.Stacks.STENCIL_OP_FALLBACK);
						MinecraftOpenGLUtilities.Stacks.push("colorMask",
								() -> RenderSystem.colorMask(false, false, false, false), MinecraftOpenGLUtilities.Stacks.COLOR_MASK_FALLBACK);

						MinecraftDrawingUtilities.drawShape(transform, container.getShapeDescriptor().getShapeOutput(), true, Color.WHITE, 0);

						MinecraftOpenGLUtilities.Stacks.pop("colorMask");
						MinecraftOpenGLUtilities.Stacks.pop("stencilOp");
						MinecraftOpenGLUtilities.Stacks.pop("stencilFunc");

						MinecraftOpenGLUtilities.Stacks.push("stencilFunc",
								() -> RenderSystem.stencilFunc(GL11.GL_LESS, stencilRef, MinecraftOpenGLUtilities.GL_MASK_ALL_BITS), MinecraftOpenGLUtilities.Stacks.STENCIL_FUNC_FALLBACK);

						MinecraftOpenGLUtilities.Stacks.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP), MinecraftOpenGLUtilities.Stacks.STENCIL_OP_FALLBACK);
						break;
					case UN_CROP:
						MinecraftOpenGLUtilities.Stacks.pop("stencilOp");

						MinecraftOpenGLUtilities.Stacks.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_REPLACE, GL11.GL_REPLACE), MinecraftOpenGLUtilities.Stacks.STENCIL_OP_FALLBACK);
						MinecraftOpenGLUtilities.Stacks.push("colorMask",
								() -> RenderSystem.colorMask(false, false, false, false), MinecraftOpenGLUtilities.Stacks.COLOR_MASK_FALLBACK);

						MinecraftDrawingUtilities.drawShape(transform, container.getShapeDescriptor().getShapeOutput().getBounds2D(), true, Color.BLACK, 0);

						MinecraftOpenGLUtilities.Stacks.pop("colorMask");
						MinecraftOpenGLUtilities.Stacks.pop("stencilOp");

						MinecraftOpenGLUtilities.Stacks.pop("stencilFunc");
						break;
					default:
						throw new InternalError();
				}
				break;
			case GL_SCISSOR:
				switch (stage) {
					case CROP:
						int[] oldBounds = new int[4];
						MinecraftOpenGLUtilities.State.getIntegerValue(GL11.GL_SCISSOR_BOX, oldBounds);
						Rectangle2D newBounds = IUIComponent.StaticHolder.getContextualShape(componentContext, container).getBounds2D();
						UIObjectUtilities.acceptRectangularShape(
								CoordinateSystemUtilities.convertRectangularShape(
										UIObjectUtilities.floorRectangularShape(newBounds, newBounds),
										newBounds,
										EnumMinecraftUICoordinateSystem.SCALED, EnumMinecraftUICoordinateSystem.NATIVE
								).createIntersection(new Rectangle2D.Double(oldBounds[0], oldBounds[1], oldBounds[2], oldBounds[3])),
								(x, y, w, h) -> MinecraftOpenGLUtilities.Stacks.push("glScissor",
										() -> MinecraftOpenGLUtilities.State.setIntegerValue(GL11.GL_SCISSOR_BOX, new int[]{x.intValue(), y.intValue(), w.intValue(), h.intValue()},
												(i, v) -> GL11.glScissor(v[0], v[1], v[2], v[3])), MinecraftOpenGLUtilities.Stacks.GL_SCISSOR_FALLBACK));
						break;
					case UN_CROP:
						MinecraftOpenGLUtilities.Stacks.pop("glScissor");
						break;
					default:
						throw new InternalError();
				}
				break;
			default:
				throw new AssertionError();
		}
	}

	@OnlyIn(Dist.CLIENT)
	enum EnumRenderStage {
		PRE_CHILDREN,
		POST_CHILDREN,
		;

		public boolean isPreChildren() { return this == PRE_CHILDREN; }

		public boolean isPostChildren() { return this == POST_CHILDREN; }
	}

	@OnlyIn(Dist.CLIENT)
	enum EnumCropStage {
		CROP,
		UN_CROP,
		;
	}
}
