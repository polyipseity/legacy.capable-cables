package $group__.client.ui.mvvm.views.domlike.components.minecraft.common;

import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.coredeprecated.structures.EnumCursor;
import $group__.client.ui.coredeprecated.structures.EnumUIMouseClickResult;
import $group__.client.ui.coredeprecated.structures.UIMouseButtonPressData;
import $group__.client.ui.mvvm.views.domlike.UIContainerDOMLikeComponentDOMLike;
import $group__.client.ui.structures.ShapeDescriptor;
import $group__.client.ui.utilities.DrawingUtilities;
import $group__.utilities.client.GLUtilities;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public abstract class UIButtonDOMLikeComponentDOMLike<S extends ShapeDescriptor<?>, D extends UIButtonDOMLikeComponentDOMLike.Data<?, ?>> extends UIContainerDOMLikeComponentDOMLike<S, D> {
	protected boolean keyPressing = false;

	public UIButtonDOMLikeComponentDOMLike(S shape, D data) { super(shape, data); }

	@Override
	public void render(final AffineTransformStack stack, Point2D mouse, float partialTicks) {
		AffineTransform transform = stack.getDelegated().peek();
		Shape transformed = transform.createTransformedShape(getShapeDescriptor().getShape());
		if (keyPressing || (isBeingDragged() && transformed.contains(mouse))) {
			DrawingUtilities.drawShape(transformed, true, data.colors.clicking, 0);
			DrawingUtilities.drawShape(transformed, false, data.colors.clickingBorder, 0);
		} else if (isBeingMouseHovered()) {
			DrawingUtilities.drawShape(transformed, true, data.colors.hovering, 0);
			DrawingUtilities.drawShape(transformed, false, data.colors.hoveringBorder, 0);
		} else {
			DrawingUtilities.drawShape(transformed, true, data.colors.base, 0);
			DrawingUtilities.drawShape(transformed, false, data.colors.baseBorder, 0);
		}
		super.render(stack, mouse, partialTicks);
	}

	@Override
	public void onMouseHovered(final AffineTransformStack stack, Point2D mouse) {
		super.onMouseHovered(stack, mouse);
		GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), MemoryUtil.NULL);
	}

	@Override
	public boolean onKeyPressed(int key, int scanCode, int modifiers) { return super.onKeyPressed(key, scanCode, modifiers) || (keyPressing = onButtonKeyboardPressed(key, scanCode, modifiers)); }

	protected abstract boolean onButtonKeyboardPressed(int key, int scanCode, int modifiers);

	@SuppressWarnings("ConstantConditions")
	@Override
	public boolean onKeyReleased(int key, int scanCode, int modifiers) { return super.onKeyReleased(key, scanCode, modifiers) || keyPressing && onButtonKeyboardReleased(key, scanCode, modifiers) && !(keyPressing = false); }

	@Override
	public EnumUIMouseClickResult onMouseClicked(final AffineTransformStack stack, UIMouseButtonPressData drag, Point2D mouse, int button) {
		EnumUIMouseClickResult ret = super.onMouseClicked(stack, drag, mouse, button);
		return ret.result ? ret : onButtonMousePressed(button) ? EnumUIMouseClickResult.DRAG : EnumUIMouseClickResult.PASS;
	}

	protected abstract boolean onButtonMousePressed(int button);

	@Override
	public boolean onMouseDragged(final AffineTransformStack stack, UIMouseButtonPressData drag, Point2D mouse, int button) { return super.onMouseDragged(stack, drag, mouse, button) || stack.getDelegated().peek().createTransformedShape(getShapeDescriptor().getShape()).contains(mouse) && onButtonMouseReleased(button); }

	@Override
	public void onMouseHover(final AffineTransformStack stack, Point2D mouse) { GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), EnumCursor.STANDARD_HAND_CURSOR.handle); }

	protected abstract boolean onButtonMouseReleased(int button);

	@Override
	public boolean onChangeFocus(boolean next) { return onChangeFocusWithThisFocusable(this, super::onChangeFocus, next); }

	protected abstract boolean onButtonKeyboardReleased(int key, int scanCode, int modifiers);

	@OnlyIn(Dist.CLIENT)
	public static class Data<E extends Events, C extends Data.ColorData> extends UIContainerDOMLikeComponentDOMLike.Data<E> {
		public C colors;

		public Data(E events, Supplier<Logger> logger, C colors) {
			super(events, logger);
			this.colors = colors;
		}

		@OnlyIn(Dist.CLIENT)
		public static class ColorData {
			public Color
					base = Color.DARK_GRAY, baseBorder = Color.DARK_GRAY,
					hovering = Color.GRAY, hoveringBorder = Color.GRAY,
					clicking = Color.LIGHT_GRAY, clickingBorder = Color.LIGHT_GRAY;

			public ColorData setBase(Color base) {
				this.base = base;
				return this;
			}

			public ColorData setBaseBorder(Color baseBorder) {
				this.baseBorder = baseBorder;
				return this;
			}

			public ColorData setHovering(Color hovering) {
				this.hovering = hovering;
				return this;
			}

			public ColorData setHoveringBorder(Color hoveringBorder) {
				this.hoveringBorder = hoveringBorder;
				return this;
			}

			public ColorData setClicking(Color clicking) {
				this.clicking = clicking;
				return this;
			}

			public ColorData setClickingBorder(Color clickingBorder) {
				this.clickingBorder = clickingBorder;
				return this;
			}
		}
	}
}
