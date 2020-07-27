package $group__.client.gui.components;

import $group__.client.gui.structures.*;
import $group__.client.gui.utilities.GLUtilities;
import $group__.client.gui.utilities.GuiUtilities.ObjectUtilities;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.function.BiFunction;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public abstract class GuiButton extends GuiContainer {
	@SuppressWarnings("CanBeFinal")
	public ColorData colors;

	public GuiButton(Shape shape, ColorData colors, Logger logger) {
		super(shape, logger);
		this.colors = colors;
	}

	protected abstract boolean onButtonClicked(int button);

	protected abstract boolean onButtonActivated(int button);

	@Override
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		if (EnumGuiState.READY.isReachedBy(getState())) {
			AffineTransform transform = stack.delegated.peek();
			Shape transformed = transform.createTransformedShape(getShape());
			if (isBeingDragged() && transformed.contains(mouse)) {
				ObjectUtilities.drawShape(transformed, true, colors.clicking, 0);
				ObjectUtilities.drawShape(transformed, false, colors.clickingBorder, 0);
			} else if (isBeingHovered()) {
				ObjectUtilities.drawShape(transformed, true, colors.hovering, 0);
				ObjectUtilities.drawShape(transformed, false, colors.hoveringBorder, 0);
			} else {
				ObjectUtilities.drawShape(transformed, true, colors.base, 0);
				ObjectUtilities.drawShape(transformed, false, colors.baseBorder, 0);
			}
			super.render(stack, mouse, partialTicks);
		}
	}

	@Override
	public EnumGuiMouseClickResult onMouseClicked(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		EnumGuiMouseClickResult ret = super.onMouseClicked(stack, drag, mouse, button);
		if (ret.result) return ret;
		return onButtonClicked(button) ? EnumGuiMouseClickResult.DRAG : EnumGuiMouseClickResult.PASS;
	}

	@Override
	public boolean onMouseDragged(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		if (super.onMouseDragged(stack, drag, mouse, button)) return true;
		return stack.delegated.peek().createTransformedShape(getShape()).contains(mouse) && onButtonActivated(button);
	}

	@Override
	public void onMouseHover(AffineTransformStack stack, Point2D mouse) { GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), EnumCursor.STANDARD_HAND_CURSOR.handle); }

	@Override
	public void onMouseHovered(AffineTransformStack stack, Point2D mouse) {
		super.onMouseHovered(stack, mouse);
		GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), MemoryUtil.NULL);
	}

	@OnlyIn(CLIENT)
	@SuppressWarnings("UnusedReturnValue")
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

	@OnlyIn(CLIENT)
	public static class Functional extends GuiButton {
		@SuppressWarnings("CanBeFinal")
		protected BiFunction<GuiButton.Functional, Integer, Boolean> filter, activator;

		public Functional(Shape shape, ColorData colors, BiFunction<GuiButton.Functional, Integer, Boolean> filter, BiFunction<GuiButton.Functional, Integer, Boolean> activator, Logger logger) {
			super(shape, colors, logger);
			this.filter = filter;
			this.activator = activator;
		}

		@Override
		protected boolean onButtonClicked(int button) { return filter.apply(this, button); }

		@Override
		protected boolean onButtonActivated(int button) { return activator.apply(this, button); }
	}
}
