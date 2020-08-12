package $group__.client.ui.mvvm.minecraft.extensions;

import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.minecraft.core.extensions.IUIExtensionMinecraft;
import $group__.client.ui.mvvm.views.components.extensions.UIExtensionCache;
import $group__.client.ui.utilities.UIObjectUtilities;
import $group__.client.ui.utilities.minecraft.CoordinateUtilities;
import $group__.client.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.client.minecraft.GLUtilities;
import $group__.utilities.extensions.ExtensionContainerAware;
import $group__.utilities.structures.Registry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

@OnlyIn(Dist.CLIENT)
public abstract class UIExtensionMinecraft<C extends IUIComponent>
		extends ExtensionContainerAware<C>
		implements IUIExtensionMinecraft<C> {
	public static final Registry.RegistryObject<IType<IUIExtensionMinecraft<IUIComponent>, IUIComponent>> TYPE =
			IUIExtension.RegUIExtension.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));

	public UIExtensionMinecraft(Class<C> genericClass) { super(genericClass); }

	@Override
	public void render(final IAffineTransformStack stack, Point2D cursorPosition, double partialTicks) {
		getContainer().ifPresent(c -> {
			if (c instanceof IUIComponentContainer) {
				IUIComponentContainer cc = (IUIComponentContainer) c;
				IUIComponentContainer.runWithStackTransformed(cc, stack, () -> {
					if (Minecraft.getInstance().getFramebuffer().isStencilEnabled()) {
						GLUtilities.GLStacksUtilities.push("GL_STENCIL_TEST",
								() -> GL11.glEnable(GL11.GL_STENCIL_TEST), () -> GL11.glDisable(GL11.GL_STENCIL_TEST));
						GLUtilities.GLStacksUtilities.push("stencilMask",
								() -> RenderSystem.stencilMask(GLUtilities.GL_MASK_ALL_BITS), GLUtilities.GLStacksUtilities.STENCIL_MASK_FALLBACK);
						cc.getChildrenView().forEach(ccc -> {
							if (ccc.isVisible()) {
								TYPE.getValue().get(ccc).ifPresent(e -> {
									e.crop(stack, EnumCropMethod.STENCIL_BUFFER, cursorPosition, partialTicks, true);
									e.render(stack, cursorPosition, partialTicks);
									e.crop(stack, EnumCropMethod.STENCIL_BUFFER, cursorPosition, partialTicks, false);
								});
							}
						});
						GLUtilities.GLStacksUtilities.pop("stencilMask");
						GLUtilities.GLStacksUtilities.pop("GL_STENCIL_TEST");
					} else {
						GLUtilities.GLStacksUtilities.push("GL_SCISSOR_TEST",
								() -> GL11.glEnable(GL11.GL_SCISSOR_TEST),
								() -> GL11.glDisable(GL11.GL_SCISSOR_TEST));
						cc.getChildrenView().forEach(ccc -> {
							if (ccc.isVisible()) {
								TYPE.getValue().get(ccc).ifPresent(e -> {
									e.crop(stack, EnumCropMethod.GL_SCISSOR, cursorPosition, partialTicks, true);
									e.render(stack, cursorPosition, partialTicks);
									e.crop(stack, EnumCropMethod.GL_SCISSOR, cursorPosition, partialTicks, false);
								});
							}
						});
						GLUtilities.GLStacksUtilities.pop("GL_SCISSOR_TEST");
					}
				});
			}
		});
	}

	@Override
	public void crop(final IAffineTransformStack stack, EnumCropMethod method, Point2D mouse, double partialTicks, boolean write) {
		getContainer().ifPresent(c -> {
			switch (method) {
				case STENCIL_BUFFER:
					if (write) {
						int stencilRef = Math.floorMod(UIExtensionCache.CacheUniversal.Z.getValue().get(c).orElseThrow(InternalError::new), (int) Math.pow(2, GLUtilities.GLStateUtilities.getInteger(GL11.GL_STENCIL_BITS)));

						GLUtilities.GLStacksUtilities.push("stencilFunc",
								() -> RenderSystem.stencilFunc(GL11.GL_EQUAL, stencilRef, GLUtilities.GL_MASK_ALL_BITS), GLUtilities.GLStacksUtilities.STENCIL_FUNC_FALLBACK);
						GLUtilities.GLStacksUtilities.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL14.GL_INCR_WRAP, GL14.GL_INCR_WRAP), GLUtilities.GLStacksUtilities.STENCIL_OP_FALLBACK);
						GLUtilities.GLStacksUtilities.push("colorMask",
								() -> RenderSystem.colorMask(false, false, false, false), GLUtilities.GLStacksUtilities.COLOR_MASK_FALLBACK);

						DrawingUtilities.drawShape(stack.getDelegated().peek(), c.getShapeDescriptor().getShapeProcessed(), true, Color.WHITE, 0);

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

						DrawingUtilities.drawShape(stack.getDelegated().peek(), c.getShapeDescriptor().getShapeProcessed().getBounds2D(), true, Color.BLACK, 0);

						GLUtilities.GLStacksUtilities.pop("colorMask");
						GLUtilities.GLStacksUtilities.pop("stencilOp");

						GLUtilities.GLStacksUtilities.pop("stencilFunc");
					}
					break;
				case GL_SCISSOR:
					if (write) {
						int[] boundsBox = new int[4];
						GLUtilities.GLStateUtilities.getIntegerValue(GL11.GL_SCISSOR_BOX, boundsBox);
						UIObjectUtilities.acceptRectangular(
								CoordinateUtilities.toNativeRectangle(
										UIObjectUtilities.getRectangleExpanded(stack.getDelegated().peek().createTransformedShape(c.getShapeDescriptor().getShapeProcessed().getBounds2D()).getBounds2D()))
										.createIntersection(new Rectangle2D.Double(boundsBox[0], boundsBox[1], boundsBox[2], boundsBox[3])),
								(x, y) -> (w, h) -> GLUtilities.GLStacksUtilities.push("glScissor",
										() -> GLUtilities.GLStateUtilities.setIntegerValue(GL11.GL_SCISSOR_BOX, new int[]{x.intValue(), y.intValue(), w.intValue(), h.intValue()},
												(i, v) -> GL11.glScissor(v[0], v[1], v[2], v[3])), GLUtilities.GLStacksUtilities.GL_SCISSOR_FALLBACK));
					} else
						GLUtilities.GLStacksUtilities.pop("glScissor");
					break;
			}
		});
	}

	@Override
	public void initialize(final IAffineTransformStack stack) {
		getContainer().ifPresent(c -> {
			if (c instanceof IUIComponentContainer) {
				IUIComponentContainer cc = (IUIComponentContainer) c;
				IUIComponentContainer.runWithStackTransformed(cc, stack, () ->
						cc.getChildrenView().forEach(ccc ->
								UIExtensionMinecraft.TYPE.getValue().get(ccc).ifPresent(e -> e.initialize(stack))));
			}
		});
	}

	@Override
	public void tick(final IAffineTransformStack stack) {
		getContainer().ifPresent(c -> {
			if (c instanceof IUIComponentContainer) {
				IUIComponentContainer cc = (IUIComponentContainer) c;
				IUIComponentContainer.runWithStackTransformed(cc, stack, () ->
						cc.getChildrenView().forEach(ccc ->
								UIExtensionMinecraft.TYPE.getValue().get(ccc).ifPresent(e -> e.tick(stack))));
			}
		});
	}

	@Override
	public void removed(final IAffineTransformStack stack) {
		getContainer().ifPresent(c -> {
			if (c instanceof IUIComponentContainer) {
				IUIComponentContainer cc = (IUIComponentContainer) c;
				IUIComponentContainer.runWithStackTransformed(cc, stack, () ->
						cc.getChildrenView().forEach(ccc ->
								UIExtensionMinecraft.TYPE.getValue().get(ccc).ifPresent(e -> e.removed(stack))));
			}
		});
	}

	@Override
	public IType<?, ?> getType() { return TYPE.getValue(); }
}
