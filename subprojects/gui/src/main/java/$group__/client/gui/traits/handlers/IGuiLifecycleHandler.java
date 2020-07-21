package $group__.client.gui.traits.handlers;

import $group__.client.gui.components.GuiComponent;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public interface IGuiLifecycleHandler {
	default boolean isTemporary() { return false; }

	void initialize(GuiComponent invoker);

	void tick(GuiComponent invoker);

	void close(GuiComponent invoker);

	void destroy(GuiComponent invoker);

	class Initializer implements IGuiLifecycleHandler {
		protected final GuiComponent component;

		public Initializer(GuiComponent component) { this.component = component; }

		@Override
		public boolean isTemporary() { return true; }

		@Override
		public void initialize(GuiComponent invoker) { component.onInitialize(this, invoker); }

		@Override
		@Deprecated
		public void tick(GuiComponent invoker) throws UnsupportedOperationException { throw BecauseOf.unsupportedOperation(); }

		@Override
		@Deprecated
		public void close(GuiComponent invoker) throws UnsupportedOperationException { throw BecauseOf.unsupportedOperation(); }

		@Override
		@Deprecated
		public void destroy(GuiComponent invoker) throws UnsupportedOperationException { throw BecauseOf.unsupportedOperation(); }
	}
}
