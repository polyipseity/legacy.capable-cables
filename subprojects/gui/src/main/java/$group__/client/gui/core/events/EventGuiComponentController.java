package $group__.client.gui.core.events;

import $group__.client.gui.core.IGuiComponent;
import $group__.client.gui.core.IGuiController;
import $group__.client.gui.core.structures.AffineTransformStack;
import $group__.client.gui.core.structures.GuiKeyboardKeyPressData;
import $group__.client.gui.core.structures.GuiMouseButtonPressData;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public abstract class EventGuiComponentController extends EventGuiComponent.Outbound {
	protected final AffineTransformStack stack;

	protected EventGuiComponentController(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack) {
		super(stage, component);
		this.stack = stack;
	}

	@OnlyIn(Dist.CLIENT)
	public static abstract class Mouse extends EventGuiComponentController {
		protected Mouse(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack) { super(stage, component, stack); }

		@OnlyIn(Dist.CLIENT)
		public static class Move extends Mouse {
			protected final Point2D mouse;

			public Move(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component,
			            AffineTransformStack stack, Point2D mouse) {
				super(stage, component, stack);
				this.mouse = mouse;
			}

			public Point2D getMouseView() { return (Point2D) mouse.clone(); }
		}

		@OnlyIn(Dist.CLIENT)
		public static class Hover extends Mouse {
			protected final IGuiController.EnumStage actionStage;
			protected final Point2D mouse;

			public Hover(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack,
			             IGuiController.EnumStage actionStage, Point2D mouse) {
				super(stage, component, stack);
				this.actionStage = actionStage;
				this.mouse = mouse;
			}

			public IGuiController.EnumStage getActionStage() { return actionStage; }

			public Point2D getMouseView() { return (Point2D) mouse.clone(); }
		}

		@OnlyIn(Dist.CLIENT)
		public static class Click extends Mouse {
			protected final IGuiController.EnumStage actionStage;
			protected final GuiMouseButtonPressData data;

			public Click(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack,
			             IGuiController.EnumStage actionStage, GuiMouseButtonPressData data) {
				super(stage, component, stack);
				this.actionStage = actionStage;
				this.data = data;
			}

			public IGuiController.EnumStage getActionStage() { return actionStage; }

			public GuiMouseButtonPressData getData() { return data; }
		}

		@OnlyIn(Dist.CLIENT)
		public static class Scroll extends Mouse {
			protected final Point2D mouse;
			protected final double delta;

			public Scroll(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack,
			              Point2D mouse, double delta) {
				super(stage, component, stack);
				this.mouse = mouse;
				this.delta = delta;
			}

			public Point2D getMouseView() { return (Point2D) mouse.clone(); }

			public double getDelta() { return delta; }
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static abstract class Keyboard extends EventGuiComponentController {
		protected Keyboard(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack) { super(stage, component, stack); }

		@OnlyIn(Dist.CLIENT)
		public static class Press extends Keyboard {
			protected final IGuiController.EnumStage actionStage;
			protected final GuiKeyboardKeyPressData data;

			public Press(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack,
			             IGuiController.EnumStage actionStage, GuiKeyboardKeyPressData data) {
				super(stage, component, stack);
				this.actionStage = actionStage;
				this.data = data;
			}

			public IGuiController.EnumStage getActionStage() { return actionStage; }

			public GuiKeyboardKeyPressData getData() { return data; }
		}

		@OnlyIn(Dist.CLIENT)
		public static class Type extends Keyboard {
			protected final char codePoint;
			protected final int modifiers;

			public Type(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack,
			            char codePoint, int modifiers) {
				super(stage, component, stack);
				this.codePoint = codePoint;
				this.modifiers = modifiers;
			}

			public char getCodePoint() { return codePoint; }

			public int getModifiers() { return modifiers; }
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Focus extends EventGuiComponentController {
		protected final IGuiController.EnumStage actionStage;

		public Focus(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, final AffineTransformStack stack,
		             IGuiController.EnumStage actionStage) {
			super(stage, component, stack);
			this.actionStage = actionStage;
		}

		public IGuiController.EnumStage getActionStage() { return actionStage; }
	}
}
