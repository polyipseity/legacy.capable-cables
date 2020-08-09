package $group__.client.ui.coredeprecated.events;

import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.coredeprecated.structures.UIKeyboardKeyPressData;
import $group__.client.ui.coredeprecated.structures.UIMouseButtonPressData;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public abstract class EventUIComponentController extends EventUIComponent.Outbound {
	protected final AffineTransformStack stack;

	protected EventUIComponentController(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack) {
		super(stage, component);
		this.stack = stack;
	}

	@OnlyIn(Dist.CLIENT)
	public static abstract class Mouse extends EventUIComponentController {
		protected Mouse(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack) { super(stage, component, stack); }

		@OnlyIn(Dist.CLIENT)
		public static class Move extends Mouse {
			protected final Point2D mouse;

			public Move(EnumEventHookStage stage, IUIComponentDOMLike component,
			            AffineTransformStack stack, Point2D mouse) {
				super(stage, component, stack);
				this.mouse = mouse;
			}

			public Point2D getMouseView() { return (Point2D) mouse.clone(); }
		}

		@OnlyIn(Dist.CLIENT)
		public static class Hover extends Mouse {
			protected final IUIComponentDOMLike.EnumStage actionStage;
			protected final Point2D mouse;

			public Hover(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack,
			             IUIComponentDOMLike.EnumStage actionStage, Point2D mouse) {
				super(stage, component, stack);
				this.actionStage = actionStage;
				this.mouse = mouse;
			}

			public IUIComponentDOMLike.EnumStage getActionStage() { return actionStage; }

			public Point2D getMouseView() { return (Point2D) mouse.clone(); }
		}

		@OnlyIn(Dist.CLIENT)
		public static class Click extends Mouse {
			protected final IUIComponentDOMLike.EnumStage actionStage;
			protected final UIMouseButtonPressData data;

			public Click(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack,
			             IUIComponentDOMLike.EnumStage actionStage, UIMouseButtonPressData data) {
				super(stage, component, stack);
				this.actionStage = actionStage;
				this.data = data;
			}

			public IUIComponentDOMLike.EnumStage getActionStage() { return actionStage; }

			public UIMouseButtonPressData getData() { return data; }
		}

		@OnlyIn(Dist.CLIENT)
		public static class Scroll extends Mouse {
			protected final Point2D mouse;
			protected final double delta;

			public Scroll(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack,
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
		protected Keyboard(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack) { super(stage, component, stack); }

		@OnlyIn(Dist.CLIENT)
		public static class Press extends Keyboard {
			protected final IUIComponentDOMLike.EnumStage actionStage;
			protected final UIKeyboardKeyPressData data;

			public Press(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack,
			             IUIComponentDOMLike.EnumStage actionStage, UIKeyboardKeyPressData data) {
				super(stage, component, stack);
				this.actionStage = actionStage;
				this.data = data;
			}

			public IUIComponentDOMLike.EnumStage getActionStage() { return actionStage; }

			public UIKeyboardKeyPressData getData() { return data; }
		}

		@OnlyIn(Dist.CLIENT)
		public static class Type extends Keyboard {
			protected final char codePoint;
			protected final int modifiers;

			public Type(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack,
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
		protected final IUIComponentDOMLike.EnumStage actionStage;

		public Focus(EnumEventHookStage stage, IUIComponentDOMLike component, final AffineTransformStack stack,
		             IUIComponentDOMLike.EnumStage actionStage) {
			super(stage, component, stack);
			this.actionStage = actionStage;
		}

		public IUIComponentDOMLike.EnumStage getActionStage() { return actionStage; }
	}
}
