package $group__.client.ui.mvvm.minecraft.core.views;

import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.views.components.extensions.caches.UIExtensionCache;
import $group__.client.ui.utilities.UIObjectUtilities;
import $group__.client.ui.utilities.minecraft.CoordinateUtilities;
import $group__.client.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.client.minecraft.GLUtilities;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
@OnlyIn(Dist.CLIENT)
public interface IUIComponentMinecraft {
	static void crop(final IUIComponent self, IAffineTransformStack stack, EnumCropMethod method, boolean push, @SuppressWarnings("unused") Point2D mouse, @SuppressWarnings("unused") double partialTicks) {
		switch (method) {
			case STENCIL_BUFFER:
				if (push) {
					int stencilRef = Math.floorMod(UIExtensionCache.CacheUniversal.Z.getValue().get(self).orElseThrow(InternalError::new), (int) Math.pow(2, GLUtilities.GLStateUtilities.getInteger(GL11.GL_STENCIL_BITS)));

					GLUtilities.GLStacksUtilities.push("stencilFunc",
							() -> RenderSystem.stencilFunc(GL11.GL_EQUAL, stencilRef, GLUtilities.GL_MASK_ALL_BITS), GLUtilities.GLStacksUtilities.STENCIL_FUNC_FALLBACK);
					GLUtilities.GLStacksUtilities.push("stencilOp",
							() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL14.GL_INCR_WRAP, GL14.GL_INCR_WRAP), GLUtilities.GLStacksUtilities.STENCIL_OP_FALLBACK);
					GLUtilities.GLStacksUtilities.push("colorMask",
							() -> RenderSystem.colorMask(false, false, false, false), GLUtilities.GLStacksUtilities.COLOR_MASK_FALLBACK);

					DrawingUtilities.drawShape(stack.getDelegated().peek(), self.getShapeDescriptor().getShapeOutput(), true, Color.WHITE, 0);

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

					DrawingUtilities.drawShape(stack.getDelegated().peek(), self.getShapeDescriptor().getShapeOutput().getBounds2D(), true, Color.BLACK, 0);

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
									UIObjectUtilities.getRectangleExpanded(stack.getDelegated().peek().createTransformedShape(self.getShapeDescriptor().getShapeOutput().getBounds2D()).getBounds2D()))
									.createIntersection(new Rectangle2D.Double(boundsBox[0], boundsBox[1], boundsBox[2], boundsBox[3])),
							(x, y, w, h) -> GLUtilities.GLStacksUtilities.push("glScissor",
									() -> GLUtilities.GLStateUtilities.setIntegerValue(GL11.GL_SCISSOR_BOX, new int[]{x.intValue(), y.intValue(), w.intValue(), h.intValue()},
											(i, v) -> GL11.glScissor(v[0], v[1], v[2], v[3])), GLUtilities.GLStacksUtilities.GL_SCISSOR_FALLBACK));
				} else
					GLUtilities.GLStacksUtilities.pop("glScissor");
				break;
		}
	}

	default void render(IAffineTransformStack stack, Point2D cursorPosition, double partialTicks, boolean pre) {}

	void crop(IAffineTransformStack stack, EnumCropMethod method, boolean push, Point2D mouse, double partialTicks);

	default void initialize(IAffineTransformStack stack) {}

	default void tick(IAffineTransformStack stack) {}

	default void removed(IAffineTransformStack stack) {}

	@OnlyIn(Dist.CLIENT)
	enum EnumCropMethod {
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
				if (true) // TODO
					return;
				GLUtilities.GLStacksUtilities.push("GL_STENCIL_TEST",
						() -> GL11.glEnable(GL11.GL_STENCIL_TEST), () -> GL11.glDisable(GL11.GL_STENCIL_TEST));
				GLUtilities.GLStacksUtilities.push("stencilMask",
						() -> RenderSystem.stencilMask(GLUtilities.GL_MASK_ALL_BITS), GLUtilities.GLStacksUtilities.STENCIL_MASK_FALLBACK);
			}

			@Override
			public void disable() {
				if (true) // TODO
					return;
				GLUtilities.GLStacksUtilities.pop("stencilMask");
				GLUtilities.GLStacksUtilities.pop("GL_STENCIL_TEST");
			}
		},
		;

		public static EnumCropMethod getBestMethod() {
			return Minecraft.getInstance().getFramebuffer().isStencilEnabled() ?
					IUIComponentMinecraft.EnumCropMethod.STENCIL_BUFFER : IUIComponentMinecraft.EnumCropMethod.GL_SCISSOR;
		}

		public abstract void enable();

		public abstract void disable();
	}
}
