package $group__.client.gui;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.ref.WeakReference;

@OnlyIn(Dist.CLIENT)
public abstract class GuiComponent implements IRenderable, IGuiEventListener {
	@Nullable
	protected WeakReference<GuiRoot> root;
	@Nullable
	protected WeakReference<GuiContainer> parent;
	protected Rectangle rectangle;

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {

	}
}
