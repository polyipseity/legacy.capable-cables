package $group__.client.gui.components;

import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.structures.EnumGuiMouseClickResult;
import $group__.client.gui.structures.EnumGuiState;
import $group__.client.gui.structures.GuiDragInfo;
import $group__.client.gui.utilities.GuiUtilities.ObjectUtilities;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.function.Function;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public abstract class GuiButton extends GuiContainer {
	@SuppressWarnings("CanBeFinal")
	protected ColorData colors;

	public GuiButton(Shape shape, ColorData colors) {
		super(shape);
		this.colors = colors;
	}

	protected abstract boolean onButtonClicked(int button);

	@Override
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		if (EnumGuiState.READY.isReachedBy(getState())) {
			AffineTransform transform = stack.delegated.peek();
			Shape transformed = transform.createTransformedShape(getShape());
			if (isBeingDragged() && transformed.contains(mouse)) {
				ObjectUtilities.drawShape(transformed, true, colors.clicked, 0);
				ObjectUtilities.drawShape(transformed, false, colors.clickedBorder, 0);
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
		return EnumGuiMouseClickResult.DRAG;
	}

	@Override
	public boolean onMouseDragged(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) { return onButtonClicked(button); }

	@OnlyIn(CLIENT)
	public static class ColorData {
		public Color
				base = Color.DARK_GRAY,
				baseBorder = Color.DARK_GRAY,
				clicked = Color.GRAY,
				clickedBorder = Color.GRAY;

		public ColorData setBase(Color base) {
			this.base = base;
			return this;
		}

		public ColorData setBaseBorder(Color baseBorder) {
			this.baseBorder = baseBorder;
			return this;
		}

		public ColorData setClicked(Color clicked) {
			this.clicked = clicked;
			return this;
		}

		public ColorData setClickedBorder(Color clickedBorder) {
			this.clickedBorder = clickedBorder;
			return this;
		}
	}

	@OnlyIn(CLIENT)
	public static class Functional extends GuiButton {
		@SuppressWarnings("CanBeFinal")
		protected Function<Integer, Boolean> function;

		public Functional(Shape shape, ColorData colors, Function<Integer, Boolean> function) {
			super(shape, colors);
			this.function = function;
		}

		@Override
		protected boolean onButtonClicked(int button) { return function.apply(button); }
	}
}
