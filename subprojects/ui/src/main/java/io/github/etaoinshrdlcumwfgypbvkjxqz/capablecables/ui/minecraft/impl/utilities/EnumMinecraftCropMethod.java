package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.utilities;

import com.google.common.math.DoubleMath;
import com.google.common.primitives.Ints;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MathUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.UncheckedAutoCloseable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftClientUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.PrimitiveUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.math.RoundingMode;

@OnlyIn(Dist.CLIENT)
// TODO this class could use some improvements, such as better methods
public enum EnumMinecraftCropMethod {
	GL_SCISSOR {
		@Override
		public void enable() {
			GL11C.glEnable(GL11C.GL_SCISSOR_TEST);
		}

		@Override
		public void disable() {
			GL11C.glDisable(GL11C.GL_SCISSOR_TEST);
		}

		@Override
		public void setCrop(Shape shape, int z) {
			Rectangle2D shapeBounds = shape.getBounds2D();
			GL11C.glScissor(
					Ints.saturatedCast(DoubleMath.roundToLong(PrimitiveUtilities.toIntegerSaturated(shapeBounds.getX()), RoundingMode.DOWN)),
					Ints.saturatedCast(DoubleMath.roundToLong(PrimitiveUtilities.toIntegerSaturated(shapeBounds.getY()), RoundingMode.DOWN)),
					Ints.saturatedCast(DoubleMath.roundToLong(PrimitiveUtilities.toIntegerSaturated(shapeBounds.getWidth()), RoundingMode.UP)),
					Ints.saturatedCast(DoubleMath.roundToLong(PrimitiveUtilities.toIntegerSaturated(shapeBounds.getHeight()), RoundingMode.UP))
			);
		}

		@Override
		public void clearCrop() {
			GL11C.glScissor(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
		}
	},
	STENCIL_BUFFER {
		@Override
		public void enable() {
			GL11C.glEnable(GL11C.GL_STENCIL_TEST);
		}

		@Override
		public void disable() {
			GL11C.glDisable(GL11C.GL_STENCIL_TEST);
		}

		@Override
		public void setCrop(Shape shape, int z) {
			int stencilZ = Math.floorMod(
					z,
					MathUtilities.pow2Int(GL11C.glGetInteger(GL11.GL_STENCIL_BITS))
			);

			RenderSystem.stencilFunc(GL11C.GL_ALWAYS, stencilZ, MinecraftOpenGLUtilities.getGlMaskAllBits());
			RenderSystem.stencilOp(GL11C.GL_KEEP, GL11C.GL_REPLACE, GL11C.GL_REPLACE);

			RenderSystem.colorMask(false, false, false, false);
			MinecraftDrawingUtilities.drawShape(shape, true, Color.WHITE, 0);
			RenderSystem.colorMask(true, true, true, true);

			RenderSystem.stencilFunc(GL11C.GL_EQUAL, stencilZ, MinecraftOpenGLUtilities.getGlMaskAllBits());
		}

		@Override
		public void clearCrop() {
			RenderSystem.clearStencil(0);
		}
	},
	;

	private final UncheckedAutoCloseable disabler = this::disable;

	public static EnumMinecraftCropMethod getBestMethod() {
		return MinecraftClientUtilities.getMinecraftNonnull().getFramebuffer().isStencilEnabled()
				? EnumMinecraftCropMethod.STENCIL_BUFFER
				: EnumMinecraftCropMethod.GL_SCISSOR;
	}

	public abstract void enable();

	public abstract void disable();

	public UncheckedAutoCloseable use() {
		enable();
		return getDisabler();
	}

	protected UncheckedAutoCloseable getDisabler() {
		return disabler;
	}

	public abstract void setCrop(Shape shape, int z);

	public abstract void clearCrop();
}
