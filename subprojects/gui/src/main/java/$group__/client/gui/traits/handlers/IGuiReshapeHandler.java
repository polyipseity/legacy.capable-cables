package $group__.client.gui.traits.handlers;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.structures.ShapeDescriptor;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public interface IGuiReshapeHandler<S extends ShapeDescriptor<?>> {
	void reshape(GuiComponent<?, ?> invoker, Consumer<? super S> transformer);

	default <TS extends ShapeDescriptor<?>> void reshape(GuiComponent<?, ?> invoker, GuiComponent<TS, ?> target, Consumer<? super TS> transformer) {
		target.transformShape(this, invoker, transformer);
		reshape(invoker);
	}

	void reshape(GuiComponent<?, ?> invoker);
}
