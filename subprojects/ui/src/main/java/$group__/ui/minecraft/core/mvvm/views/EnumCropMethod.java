package $group__.ui.minecraft.core.mvvm.views;

import $group__.utilities.client.minecraft.GLUtilities;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public enum EnumCropMethod {
	GL_SCISSOR {
		@Override
		public void enable() {
			GLUtilities.GLStacksUtilities.push("GL_SCISSOR_TEST",
					() -> GL11.glEnable(GL11.GL_SCISSOR_TEST),
					() -> GL11.glDisable(GL11.GL_SCISSOR_TEST));
		}

		@Override
		public void disable() {
			GLUtilities.GLStacksUtilities.pop("GL_SCISSOR_TEST");
		}
	},
	STENCIL_BUFFER {
		@Override
		public void enable() {
			GLUtilities.GLStacksUtilities.push("GL_STENCIL_TEST",
					() -> GL11.glEnable(GL11.GL_STENCIL_TEST), () -> GL11.glDisable(GL11.GL_STENCIL_TEST));
			GLUtilities.GLStacksUtilities.push("stencilMask",
					() -> RenderSystem.stencilMask(GLUtilities.GL_MASK_ALL_BITS), GLUtilities.GLStacksUtilities.STENCIL_MASK_FALLBACK);
		}

		@Override
		public void disable() {
			GLUtilities.GLStacksUtilities.pop("stencilMask");
			GLUtilities.GLStacksUtilities.pop("GL_STENCIL_TEST");
		}
	},
	;

	public static EnumCropMethod getBestMethod() {
		return Minecraft.getInstance().getFramebuffer().isStencilEnabled() ?
				EnumCropMethod.STENCIL_BUFFER : EnumCropMethod.GL_SCISSOR;
	}

	public abstract void enable();

	public abstract void disable();
}
