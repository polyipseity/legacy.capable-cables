package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftClientUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.EnumMinecraftUICoordinateSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.CoordinateSystemUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;
import java.awt.geom.Rectangle2D;

@OnlyIn(Dist.CLIENT)
public enum EnumCropMethod {
	GL_SCISSOR {
		@Override
		public void enable() {
			MinecraftOpenGLUtilities.Stacks.push("GL_SCISSOR_TEST",
					() -> GL11.glEnable(GL11.GL_SCISSOR_TEST),
					() -> GL11.glDisable(GL11.GL_SCISSOR_TEST));
		}

		@Override
		public void disable() {
			MinecraftOpenGLUtilities.Stacks.pop("GL_SCISSOR_TEST");
		}

		@Override
		public void crop(Shape shape, int z) {
			int[] oldBounds = new int[4];
			MinecraftOpenGLUtilities.State.getIntegerValue(GL11.GL_SCISSOR_BOX, oldBounds);
			Rectangle2D newBounds = shape.getBounds2D();
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
		}

		@Override
		public void unCrop(Shape shape) {
			MinecraftOpenGLUtilities.Stacks.pop("glScissor");
		}
	},
	STENCIL_BUFFER {
		@Override
		public void enable() {
			MinecraftOpenGLUtilities.Stacks.push("GL_STENCIL_TEST",
					() -> GL11.glEnable(GL11.GL_STENCIL_TEST), () -> GL11.glDisable(GL11.GL_STENCIL_TEST));
			MinecraftOpenGLUtilities.Stacks.push("stencilMask",
					() -> RenderSystem.stencilMask(MinecraftOpenGLUtilities.getGlMaskAllBits()), MinecraftOpenGLUtilities.Stacks.getStencilMaskFallback());
		}

		@Override
		public void disable() {
			MinecraftOpenGLUtilities.Stacks.pop("stencilMask");
			MinecraftOpenGLUtilities.Stacks.pop("GL_STENCIL_TEST");
		}

		@Override
		public void crop(Shape shape, int z) {
			int stencilRef = Math.floorMod(
					z,
					MathUtilities.pow2Int(MinecraftOpenGLUtilities.State.getInteger(GL11.GL_STENCIL_BITS))
			);

			MinecraftOpenGLUtilities.Stacks.push("stencilFunc",
					() -> RenderSystem.stencilFunc(GL11.GL_EQUAL, stencilRef, MinecraftOpenGLUtilities.getGlMaskAllBits()),
					MinecraftOpenGLUtilities.Stacks.getStencilFuncFallback());
			MinecraftOpenGLUtilities.Stacks.push("stencilOp",
					() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL14.GL_INCR_WRAP, GL14.GL_INCR_WRAP),
					MinecraftOpenGLUtilities.Stacks.getStencilOpFallback());
			MinecraftOpenGLUtilities.Stacks.push("colorMask",
					() -> RenderSystem.colorMask(false, false, false, false),
					MinecraftOpenGLUtilities.Stacks.getColorMaskFallback());

			MinecraftDrawingUtilities.drawShape(shape, true, Color.WHITE, 0);

			MinecraftOpenGLUtilities.Stacks.pop("colorMask");
			MinecraftOpenGLUtilities.Stacks.pop("stencilOp");
			MinecraftOpenGLUtilities.Stacks.pop("stencilFunc");

			MinecraftOpenGLUtilities.Stacks.push("stencilFunc",
					() -> RenderSystem.stencilFunc(GL11.GL_LESS, stencilRef, MinecraftOpenGLUtilities.getGlMaskAllBits()),
					MinecraftOpenGLUtilities.Stacks.getStencilFuncFallback());

			MinecraftOpenGLUtilities.Stacks.push("stencilOp",
					() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP),
					MinecraftOpenGLUtilities.Stacks.getStencilOpFallback());
		}

		@Override
		public void unCrop(Shape shape) {
			MinecraftOpenGLUtilities.Stacks.pop("stencilOp");

			MinecraftOpenGLUtilities.Stacks.push("stencilOp",
					() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_REPLACE, GL11.GL_REPLACE),
					MinecraftOpenGLUtilities.Stacks.getStencilOpFallback());
			MinecraftOpenGLUtilities.Stacks.push("colorMask",
					() -> RenderSystem.colorMask(false, false, false, false),
					MinecraftOpenGLUtilities.Stacks.getColorMaskFallback());

			MinecraftDrawingUtilities.drawShape(shape, true, Color.BLACK, 0);

			MinecraftOpenGLUtilities.Stacks.pop("colorMask");
			MinecraftOpenGLUtilities.Stacks.pop("stencilOp");

			MinecraftOpenGLUtilities.Stacks.pop("stencilFunc");
		}
	},
	;

	public static EnumCropMethod getBestMethod() {
		return MinecraftClientUtilities.getMinecraftNonnull().getFramebuffer().isStencilEnabled()
				? EnumCropMethod.STENCIL_BUFFER
				: EnumCropMethod.GL_SCISSOR;
	}

	public abstract void enable();

	public abstract void disable();

	public abstract void crop(Shape shape, int z);

	public abstract void unCrop(Shape shape);
}
