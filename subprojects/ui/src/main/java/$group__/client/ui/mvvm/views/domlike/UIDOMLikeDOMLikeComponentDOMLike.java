package $group__.client.ui.mvvm.views.domlike;

import $group__.client.ui.coredeprecated.IUIExtension;
import $group__.client.ui.coredeprecated.events.EventUIComponentController;
import $group__.client.ui.coredeprecated.events.EventUIComponentStateChanged;
import $group__.client.ui.coredeprecated.events.EventUIComponentView;
import $group__.client.ui.coredeprecated.extensions.UIExtensionCache;
import $group__.client.ui.coredeprecated.extensions.UIExtensionCache.CacheUniversal;
import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.coredeprecated.structures.UIKeyboardKeyPressData;
import $group__.client.ui.coredeprecated.structures.UIMouseButtonPressData;
import $group__.client.ui.coredeprecated.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentContainerDOMLike;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentManagerDOMLike;
import $group__.client.ui.mvvm.views.domlike.events.UIEventTargetDOMLike;
import $group__.client.ui.utilities.CoordinateUtilities;
import $group__.client.ui.utilities.DrawingUtilities;
import $group__.client.ui.utilities.UIObjectUtilities;
import $group__.utilities.client.GLUtilities;
import $group__.utilities.client.GLUtilities.GLStacksUtilities;
import $group__.utilities.client.GLUtilities.GLStateUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventUtilities;
import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

@OnlyIn(Dist.CLIENT)
public class UIDOMLikeDOMLikeComponentDOMLike extends UIEventTargetDOMLike
		implements IUIComponentDOMLike {
	// todo add animation system
	// todo add name
	// todo cache transform
	private static final Rectangle2D SHAPE_PLACEHOLDER = new Rectangle2D.Double(0, 0, 1, 1);
	protected final ConcurrentMap<ResourceLocation, IUIExtension> extensions = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected WeakReference<IUIComponentContainerDOMLike> parent = new WeakReference<>(null);
		@Nullable
		protected IShapeDescriptor<?, ?> shapeDescriptor;
		protected boolean visible = true;
		protected boolean active = true;

	public UIDOMLikeDOMLikeComponentDOMLike() { addExtension(new UIExtensionCache()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<ResourceLocation, IUIExtension> getExtensions() { return extensions; }

	public static Rectangle2D getShapePlaceholderView() { return (Rectangle2D) SHAPE_PLACEHOLDER.clone(); }

	@Override
	public Optional<IUIExtension> getExtension(ResourceLocation key) { return Optional.ofNullable(getExtensions().get(key)); }	@Override
	public boolean contains(final AffineTransformStack stack, Point2D point) { return stack.getDelegated().peek().createTransformedShape(getShapeDescriptor().getShapeProcessed()).contains(point); }

	@Override
	public Optional<IUIExtension> addExtension(IUIExtension extension) {
		IUIExtension.Reg.checkExtensionRegistered(extension);
		Optional<IUIExtension> ret = Optional.ofNullable(getExtensions().put(extension.getType().getKey(), extension)).filter(eo -> {
			eo.onExtensionRemove();
			return true;
		});
		extension.onExtensionRemove();
		return ret;
	}	@Override
	public void onParentChange(@Nullable IUIComponentContainerDOMLike previous, @Nullable IUIComponentContainerDOMLike next) { parent = new WeakReference<>(next); }

	@Override
	public Optional<IUIExtension> removeExtension(ResourceLocation key) {
		return Optional.ofNullable(getExtensions().remove(key)).filter(eo -> {
			eo.onExtensionRemove();
			return true;
		});
	}	@Override
	public void onIndexMove(int previous, int next) {}
	
	@Override
	public ImmutableMap<ResourceLocation, IUIExtension> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }


		@Override
		public IShapeDescriptor<?, ?> getShapeDescriptor() { return Objects.requireNonNull(shapeDescriptor); }

		@Override
		public boolean isVisible() { return visible; }

		@Override
		public boolean setVisible(final AffineTransformStack stack, boolean visible) {
			boolean previous = isVisible();
			if (previous != visible) {
				return EventUtilities.callWithPrePostHooks(() -> setVisible0(visible),
						new EventUIComponentStateChanged.Visible(EnumEventHookStage.PRE, this, stack, previous, visible),
						new EventUIComponentStateChanged.Visible(EnumEventHookStage.POST, this, stack, previous, visible));
			}
			return false;
		}

		protected boolean setVisible0(boolean visible) {
			this.visible = visible;
			return true;
		}

		@Override
		public void crop(final AffineTransformStack stack, EnumCropMethod method, Point2D mouse, float partialTicks, boolean write) {
			switch (method) {
				case STENCIL_BUFFER:
					if (write) {
						int stencilRef = Math.floorMod(CacheUniversal.Z.getValue().get(this).orElseThrow(InternalError::new), (int) Math.pow(2, GLStateUtilities.getInteger(GL11.GL_STENCIL_BITS)));

						GLStacksUtilities.push("stencilFunc",
								() -> RenderSystem.stencilFunc(GL11.GL_EQUAL, stencilRef, GLUtilities.GL_MASK_ALL_BITS), GLStacksUtilities.STENCIL_FUNC_FALLBACK);
						GLStacksUtilities.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL14.GL_INCR_WRAP, GL14.GL_INCR_WRAP), GLStacksUtilities.STENCIL_OP_FALLBACK);
						GLStacksUtilities.push("colorMask",
								() -> RenderSystem.colorMask(false, false, false, false), GLStacksUtilities.COLOR_MASK_FALLBACK);

						DrawingUtilities.drawShape(stack.getDelegated().peek(), getShapeDescriptor().getShapeProcessed(), true, Color.WHITE, 0);

						GLStacksUtilities.pop("colorMask");
						GLStacksUtilities.pop("stencilOp");
						GLStacksUtilities.pop("stencilFunc");

						GLStacksUtilities.push("stencilFunc",
								() -> RenderSystem.stencilFunc(GL11.GL_LESS, stencilRef, GLUtilities.GL_MASK_ALL_BITS), GLStacksUtilities.STENCIL_FUNC_FALLBACK);

						GLStacksUtilities.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP), GLStacksUtilities.STENCIL_OP_FALLBACK);
					} else {
						GLStacksUtilities.pop("stencilOp");

						GLStacksUtilities.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_REPLACE, GL11.GL_REPLACE), GLStacksUtilities.STENCIL_OP_FALLBACK);
						GLStacksUtilities.push("colorMask",
								() -> RenderSystem.colorMask(false, false, false, false), GLStacksUtilities.COLOR_MASK_FALLBACK);

						DrawingUtilities.drawShape(stack.getDelegated().peek(), getShapeDescriptor().getShapeProcessed().getBounds2D(), true, Color.BLACK, 0);

						GLStacksUtilities.pop("colorMask");
						GLStacksUtilities.pop("stencilOp");

						GLStacksUtilities.pop("stencilFunc");
					}
					break;
				case GL_SCISSOR:
					if (write) {
						int[] boundsBox = new int[4];
						GLStateUtilities.getIntegerValue(GL11.GL_SCISSOR_BOX, boundsBox);
						UIObjectUtilities.acceptRectangular(
								CoordinateUtilities.toNativeRectangle(
										UIObjectUtilities.getRectangleExpanded(stack.getDelegated().peek().createTransformedShape(getShapeDescriptor().getShapeProcessed().getBounds2D()).getBounds2D()))
										.createIntersection(new Rectangle2D.Double(boundsBox[0], boundsBox[1], boundsBox[2], boundsBox[3])),
								(x, y) -> (w, h) -> GLStacksUtilities.push("glScissor",
										() -> GLStateUtilities.setIntegerValue(GL11.GL_SCISSOR_BOX, new int[]{x.intValue(), y.intValue(), w.intValue(), h.intValue()},
												(i, v) -> GL11.glScissor(v[0], v[1], v[2], v[3])), GLStacksUtilities.GL_SCISSOR_FALLBACK));
					} else
						GLStacksUtilities.pop("glScissor");
					break;
			}
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean render(final AffineTransformStack stack, Point2D mouse, float partialTicks) {
			return EventUtilities.callWithPrePostHooks(() -> render0(stack, mouse, partialTicks),
					new EventUIComponentView.Render(EnumEventHookStage.PRE, this, stack, mouse, partialTicks),
					new EventUIComponentView.Render(EnumEventHookStage.POST, this, stack, mouse, partialTicks));
		}

		public boolean render0(final AffineTransformStack stack, Point2D mouse, float partialTicks) { return false; }
		

		@Override
		public boolean isActive() { return active; }

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean setActive(final AffineTransformStack stack, boolean active) {
			boolean previous = isActive();
			if (previous != active) {
				return EventUtilities.callWithPrePostHooks(() -> setActive0(stack, active),
						new EventUIComponentStateChanged.Active(EnumEventHookStage.PRE, this, stack, previous, active),
						new EventUIComponentStateChanged.Active(EnumEventHookStage.POST, this, stack, previous, active));
			}
			return false;
		}

		protected boolean setActive0(final AffineTransformStack stack, boolean active) {
			if (!active) {
				AffineTransform peek = stack.getDelegated().peek();
				stack.getDelegated().pop();
				getParent().ifPresent(p -> p.setFocus(stack, null));
				stack.getDelegated().push(peek);
				getManager().ifPresent(mag -> {
					AffineTransformStack s = mag.getCleanTransformStack();
					IUIComponentDOMLike.getKeyboardKeysBeingPressedForComponent(this).forEach(pk ->
							mag.onKeyboardKeyPress(s, EnumStage.END,
									pk.getData()));
					Point2D cursor = GLUtilities.getCursorPos();
					IUIComponentDOMLike.getMouseButtonsBeingPressedForComponent(this).forEach(pm ->
							mag.onMouseButtonPress(s, EnumStage.END,
									new UIMouseButtonPressData(cursor, pm.getData().getButton())));
				});
			}
			this.active = active;
			return true;
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean onMouseMove(final AffineTransformStack stack, Point2D mouse) {
			return EventUtilities.callWithPrePostHooks(() -> onMouseMove0(stack, mouse),
					new EventUIComponentController.Mouse.Move(EnumEventHookStage.PRE, this, stack, mouse),
					new EventUIComponentController.Mouse.Move(EnumEventHookStage.POST, this, stack, mouse));
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean onMouseHover(final AffineTransformStack stack, EnumStage stage, Point2D mouse) {
			return EventUtilities.callWithPrePostHooks(() -> onMouseHover0(stack, stage, mouse),
					new EventUIComponentController.Mouse.Hover(EnumEventHookStage.PRE, this, stack, stage, mouse),
					new EventUIComponentController.Mouse.Hover(EnumEventHookStage.POST, this, stack, stage, mouse));
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean onMouseButtonPress(final AffineTransformStack stack, EnumStage stage, UIMouseButtonPressData data) {
			return EventUtilities.callWithPrePostHooks(() -> onMouseButtonPress0(stack, stage, data),
					new EventUIComponentController.Mouse.Click(EnumEventHookStage.PRE, this, stack, stage, data),
					new EventUIComponentController.Mouse.Click(EnumEventHookStage.POST, this, stack, stage, data));
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean onMouseScroll(final AffineTransformStack stack, Point2D mouse, double delta) {
			return EventUtilities.callWithPrePostHooks(() -> onMouseScroll0(stack, mouse, delta),
					new EventUIComponentController.Mouse.Scroll(EnumEventHookStage.PRE, this, stack, mouse, delta),
					new EventUIComponentController.Mouse.Scroll(EnumEventHookStage.POST, this, stack, mouse, delta));
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean onKeyboardKeyPress(final AffineTransformStack stack, EnumStage stage, UIKeyboardKeyPressData data) {
			return EventUtilities.callWithPrePostHooks(() -> onKeyboardKeyPress0(stack, stage, data),
					new EventUIComponentController.Keyboard.Press(EnumEventHookStage.PRE, this, stack, stage, data),
					new EventUIComponentController.Keyboard.Press(EnumEventHookStage.POST, this, stack, stage, data));
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean onKeyboardCharType(final AffineTransformStack stack, char codePoint, int modifiers) {
			return EventUtilities.callWithPrePostHooks(() -> onKeyboardCharType0(stack, codePoint, modifiers),
					new EventUIComponentController.Keyboard.Type(EnumEventHookStage.PRE, this, stack, codePoint, modifiers),
					new EventUIComponentController.Keyboard.Type(EnumEventHookStage.POST, this, stack, codePoint, modifiers));
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean onFocus(final AffineTransformStack stack, EnumStage stage) {
			return EventUtilities.callWithPrePostHooks(() -> onFocus0(stack, stage),
					new EventUIComponentController.Focus(EnumEventHookStage.PRE, this, stack, stage),
					new EventUIComponentController.Focus(EnumEventHookStage.POST, this, stack, stage));
		}

		@Override
		public boolean changeFocus(final AffineTransformStack stack, boolean next) { return false; }

		public boolean onMouseMove0(final AffineTransformStack stack, Point2D mouse) { return false; }

		public boolean onMouseHover0(final AffineTransformStack stack, EnumStage stage, Point2D mouse) { return false; }

		public boolean onMouseButtonPress0(final AffineTransformStack stack, EnumStage stage, UIMouseButtonPressData data) { return false; }

		public boolean onMouseScroll0(final AffineTransformStack stack, Point2D mouse, double delta) { return false; }

		public boolean onKeyboardKeyPress0(final AffineTransformStack stack, EnumStage stage, UIKeyboardKeyPressData data) { return false; }

		public boolean onKeyboardCharType0(final AffineTransformStack stack, char codePoint, int modifiers) { return false; }

		public boolean onFocus0(final AffineTransformStack stack, EnumStage stage) { return false; }



	@Override
	public Optional<IUIComponentContainerDOMLike> getParent() { return Optional.ofNullable(parent.get()); }

	@Override
	public Optional<IUIComponentManagerDOMLike> getManager() { return CacheUniversal.MANAGER.getValue().get(this); }
}
