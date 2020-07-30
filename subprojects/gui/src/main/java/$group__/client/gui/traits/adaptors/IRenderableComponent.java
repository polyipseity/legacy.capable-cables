package $group__.client.gui.traits.adaptors;

import $group__.annotations.OverridingStatus;
import $group__.client.gui.ConstantsGui;
import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.structures.EnumGuiState;
import net.minecraft.client.gui.IRenderable;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.meta.When;
import java.awt.geom.Point2D;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@SuppressWarnings("SpellCheckingInspection")
@OnlyIn(CLIENT)
public interface IRenderableComponent {
	void renderPre(AffineTransformStack stack, Point2D mouse, float partialTicks);

	void render(AffineTransformStack stack, Point2D mouse, float partialTicks);

	////////// Deprecated //////////

	@OnlyIn(CLIENT)
	interface IAdapter extends IScreenAdapter, IRenderableComponent, IRenderable {
		@Override
		@Deprecated
		@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
		default void render(int mouseX, int mouseY, float partialTicks) {
			Point2D mouse = new Point2D.Double(mouseX, mouseY);
			if (getRoot().data.visible && EnumGuiState.READY.isReachedBy(getRoot().data.getState())) {
				renderPre(getTransformStack(), mouse, partialTicks);
				render(getTransformStack(), mouse, partialTicks);
			}
			getRoot().onMouseHovering(getTransformStack(), mouse);
		}
	}
}
