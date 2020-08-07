package $group__.client.gui.core;

import $group__.client.gui.core.structures.AffineTransformStack;
import $group__.client.gui.core.structures.EnumGuiState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

// TODO remove
@OnlyIn(Dist.CLIENT)
public interface IGuiModel<C extends IGuiComponent<?, ?, ?>> {
	Optional<C> getComponent();

	boolean initialize(final AffineTransformStack stack);

	boolean tick(final AffineTransformStack stack);

	boolean close(final AffineTransformStack stack);

	EnumGuiState getState();
}
