package $group__.client.ui.coredeprecated.events;

import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.mvvm.structures.IUIDataKeyboardKeyPress;
import $group__.client.ui.mvvm.structures.IUIDataMouseButtonClick;
import $group__.client.ui.mvvm.views.components.IUIComponent;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public abstract class EventUIComponentController extends EventUIComponent.Outbound {
	protected final AffineTransformStack stack;

	protected EventUIComponentController(EnumEventHookStage stage, IUIComponent component, final AffineTransformStack stack) {
		super(stage, component);
		this.stack = stack;
	}

	@OnlyIn(Dist.CLIENT)
	public static abstract class Mouse extends EventUIComponentController {
		protected Mouse(EnumEventHookStage stage, IUIComponent component, final AffineTransformStack stack) { super(stage, component, stack); }

		@OnlyIn(Dist.CLIENT)
		public static class Move extends Mouse {
			protected final Point2D mouse;

			public Move(EnumEventHookStage stage, IUIComponent component,
			            AffineTransformStack stack, Point2D mouse) {
				super(stage, component, stack);
				this.mouse = mouse;
			}

			public Point2D getMouseView() { return (Point2D) mouse.clone(); }
		}

		@OnlyIn(Dist.CLIENT)
		public static class Hover extends Mouse {
			protected final IUIComponent.EnumStage actionStage;
			protected final Point2D mouse;

			public Hover(EnumEventHookStage stage, IUIComponent component, final AffineTransformStack stack,
			             IUIComponent.EnumStage actionStage, Point2D mouse) {
				super(stage, component, stack);
				this.actionStage = actionStage;
				this.mouse = mouse;
			}

			public IUIComponent.EnumStage getActionStage() { return actionStage; }

			public Point2D getMouseView() { return (Point2D) mouse.clone(); }
		}

		@OnlyIn(Dist.CLIENT)
		public static class Click extends Mouse {
			protected final IUIComponent.EnumStage actionStage;
			protected final IUIDataMouseButtonClick data;

			public Click(EnumEventHookStage stage, IUIComponent component, final AffineTransformStack stack,
			             IUIComponent.EnumStage actionStage, IUIDataMouseButtonClick data) {
				super(stage, component, stack);
				this.actionStage = actionStage;
				this.data = data;
			}

			public IUIComponent.EnumStage getActionStage() { return actionStage; }

			public IUIDataMouseButtonClick getData() { return data; }
		}

		@OnlyIn(Dist.CLIENT)
		public static class Scroll extends Mouse {
			protected final Point2D mouse;
			protected final double delta;

			public Scroll(EnumEventHookStage stage, IUIComponent component, final AffineTransformStack stack,
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
	public static abstract class Keyboard extends EventUIComponentController {
		protected Keyboard(EnumEventHookStage stage, IUIComponent component, final AffineTransformStack stack) { super(stage, component, stack); }

		@OnlyIn(Dist.CLIENT)
		public static class Press extends Keyboard {
			protected final IUIComponent.EnumStage actionStage;
			protected final IUIDataKeyboardKeyPress data;

			public Press(EnumEventHookStage stage, IUIComponent component, final AffineTransformStack stack,
			             IUIComponent.EnumStage actionStage, IUIDataKeyboardKeyPress data) {
				super(stage, component, stack);
				this.actionStage = actionStage;
				this.data = data;
			}

			public IUIComponent.EnumStage getActionStage() { return actionStage; }

			public IUIDataKeyboardKeyPress getData() { return data; }
		}

		@OnlyIn(Dist.CLIENT)
		public static class Type extends Keyboard {
			protected final char codePoint;
			protected final int modifiers;

			public Type(EnumEventHookStage stage, IUIComponent component, final AffineTransformStack stack,
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
	public static class Focus extends EventUIComponentController {
		protected final IUIComponent.EnumStage actionStage;

		public Focus(EnumEventHookStage stage, IUIComponent component, final AffineTransformStack stack,
		             IUIComponent.EnumStage actionStage) {
			super(stage, component, stack);
			this.actionStage = actionStage;
		}

		public IUIComponent.EnumStage getActionStage() { return actionStage; }
	}
}
