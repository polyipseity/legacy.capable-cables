package $group__.ui.minecraft.core.mvvm.views;

import $group__.utilities.minecraft.client.ClientUtilities;
import $group__.utilities.minecraft.client.GLUtilities;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public enum EnumCropMethod {
	GL_SCISSOR {
		@Override
		public void enable() {
			GLUtilities.Stacks.push("GL_SCISSOR_TEST",
					() -> GL11.glEnable(GL11.GL_SCISSOR_TEST),
					() -> GL11.glDisable(GL11.GL_SCISSOR_TEST));
		}

		@Override
		public void disable() {
			GLUtilities.Stacks.pop("GL_SCISSOR_TEST");
		}
	},
	STENCIL_BUFFER {
		@Override
		public void enable() {
			GLUtilities.Stacks.push("GL_STENCIL_TEST",
					() -> GL11.glEnable(GL11.GL_STENCIL_TEST), () -> GL11.glDisable(GL11.GL_STENCIL_TEST));
			GLUtilities.Stacks.push("stencilMask",
					() -> RenderSystem.stencilMask(GLUtilities.GL_MASK_ALL_BITS), GLUtilities.Stacks.STENCIL_MASK_FALLBACK);
		}

		@Override
		public void disable() {
			GLUtilities.Stacks.pop("stencilMask");
			GLUtilities.Stacks.pop("GL_STENCIL_TEST");
		}
	},
	;

	public static EnumCropMethod getBestMethod() {
		return ClientUtilities.getMinecraftNonnull().getFramebuffer().isStencilEnabled() ?
				EnumCropMethod.STENCIL_BUFFER : EnumCropMethod.GL_SCISSOR;
	}

	public abstract void enable();

	public abstract void disable();
}
