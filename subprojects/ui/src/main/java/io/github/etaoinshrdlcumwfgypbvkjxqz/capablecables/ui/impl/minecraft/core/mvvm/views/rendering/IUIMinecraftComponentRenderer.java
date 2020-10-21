package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.EnumCropMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches.UIDefaultCacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.EnumMinecraftUICoordinateSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.CoordinateSystemUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

@OnlyIn(Dist.CLIENT)
public interface IUIMinecraftComponentRenderer<C extends IUIComponent & IUIComponentMinecraft>
		extends IUIComponentRenderer<C> {
	void render(IUIComponentContext context, C container, EnumRenderStage stage, double partialTicks);

	default void crop(IUIComponentContext context, C container, EnumCropStage stage, EnumCropMethod method, double partialTicks) {
		IUIMinecraftComponentRenderer.cropImpl(context, container, stage, method, partialTicks);
	}

	static void cropImpl(IUIComponentContext componentContext, final IUIComponent container, EnumCropStage stage, EnumCropMethod method, @SuppressWarnings("unused") double partialTicks) {
		AffineTransform transform = IUIComponentContext.getCurrentTransform(componentContext);
		switch (method) {
			case STENCIL_BUFFER:
				switch (stage) {
					case CROP:
						int stencilRef = Math.floorMod(
								UIDefaultCacheExtension.CacheUniversal.getZ().getValue().get(container).orElseThrow(InternalError::new),
								MathUtilities.pow2Int(MinecraftOpenGLUtilities.State.getInteger(GL11.GL_STENCIL_BITS))
						);

						MinecraftOpenGLUtilities.Stacks.push("stencilFunc",
								() -> RenderSystem.stencilFunc(GL11.GL_EQUAL, stencilRef, MinecraftOpenGLUtilities.getGlMaskAllBits()), MinecraftOpenGLUtilities.Stacks.getStencilFuncFallback());
						MinecraftOpenGLUtilities.Stacks.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL14.GL_INCR_WRAP, GL14.GL_INCR_WRAP), MinecraftOpenGLUtilities.Stacks.getStencilOpFallback());
						MinecraftOpenGLUtilities.Stacks.push("colorMask",
								() -> RenderSystem.colorMask(false, false, false, false), MinecraftOpenGLUtilities.Stacks.getColorMaskFallback());

						MinecraftDrawingUtilities.drawShape(transform, container.getShapeDescriptor().getShapeOutput(), true, Color.WHITE, 0);

						MinecraftOpenGLUtilities.Stacks.pop("colorMask");
						MinecraftOpenGLUtilities.Stacks.pop("stencilOp");
						MinecraftOpenGLUtilities.Stacks.pop("stencilFunc");

						MinecraftOpenGLUtilities.Stacks.push("stencilFunc",
								() -> RenderSystem.stencilFunc(GL11.GL_LESS, stencilRef, MinecraftOpenGLUtilities.getGlMaskAllBits()), MinecraftOpenGLUtilities.Stacks.getStencilFuncFallback());

						MinecraftOpenGLUtilities.Stacks.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP), MinecraftOpenGLUtilities.Stacks.getStencilOpFallback());
						break;
					case UN_CROP:
						MinecraftOpenGLUtilities.Stacks.pop("stencilOp");

						MinecraftOpenGLUtilities.Stacks.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_REPLACE, GL11.GL_REPLACE), MinecraftOpenGLUtilities.Stacks.getStencilOpFallback());
						MinecraftOpenGLUtilities.Stacks.push("colorMask",
								() -> RenderSystem.colorMask(false, false, false, false), MinecraftOpenGLUtilities.Stacks.getColorMaskFallback());

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
						Rectangle2D newBounds = IUIComponent.getContextualShape(componentContext, container).getBounds2D();
						UIObjectUtilities.acceptRectangularShape(
								CoordinateSystemUtilities.convertRectangularShape(
										UIObjectUtilities.floorRectangularShape(newBounds, newBounds),
										newBounds,
										EnumMinecraftUICoordinateSystem.SCALED, EnumMinecraftUICoordinateSystem.NATIVE
								).createIntersection(new Rectangle2D.Double(oldBounds[0], oldBounds[1], oldBounds[2], oldBounds[3])),
								(x, y, w, h) -> MinecraftOpenGLUtilities.Stacks.push("glScissor",
										() -> {
											assert x != null;
											assert y != null;
											assert w != null;
											assert h != null;
											MinecraftOpenGLUtilities.State.setIntegerValue(GL11.GL_SCISSOR_BOX, new int[]{x.intValue(), y.intValue(), w.intValue(), h.intValue()},
													(i, v) -> {
														assert v != null;
														GL11.glScissor(v[0], v[1], v[2], v[3]);
													});
										}, MinecraftOpenGLUtilities.Stacks.getGlScissorFallback()));
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