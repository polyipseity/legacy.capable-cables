package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.utilities;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftClientUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

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
	},
	STENCIL_BUFFER {
		@Override
		public void enable() {
			MinecraftOpenGLUtilities.Stacks.push("GL_STENCIL_TEST",
					() -> GL11.glEnable(GL11.GL_STENCIL_TEST), () -> GL11.glDisable(GL11.GL_STENCIL_TEST));
			MinecraftOpenGLUtilities.Stacks.push("stencilMask",
					() -> RenderSystem.stencilMask(MinecraftOpenGLUtilities.GL_MASK_ALL_BITS), MinecraftOpenGLUtilities.Stacks.STENCIL_MASK_FALLBACK);
		}

		@Override
		public void disable() {
			MinecraftOpenGLUtilities.Stacks.pop("stencilMask");
			MinecraftOpenGLUtilities.Stacks.pop("GL_STENCIL_TEST");
		}
	},
	;

	public static EnumCropMethod getBestMethod() {
		return MinecraftClientUtilities.getMinecraftNonnull().getFramebuffer().isStencilEnabled() ?
				EnumCropMethod.STENCIL_BUFFER : EnumCropMethod.GL_SCISSOR;
	}

	public abstract void enable();

	public abstract void disable();
}
