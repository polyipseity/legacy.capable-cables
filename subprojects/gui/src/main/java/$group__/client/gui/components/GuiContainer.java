package $group__.client.gui.components;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayList;
import java.util.List;

import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;

@OnlyIn(Dist.CLIENT)
public class GuiContainer extends GuiComponent implements INestedGuiEventHandler {
	protected List<GuiComponent> children = new ArrayList<>(INITIAL_CAPACITY_2);
	protected boolean dragging = false;
	@Nullable
	protected IGuiEventListener focused = null;

	@Override
	public List<? extends IGuiEventListener> children() { return children; }

	@Override
	public boolean isDragging() { return dragging; }

	@Override
	public void setDragging(boolean dragging) { this.dragging = dragging; }

	@Nullable
	@Override
	public IGuiEventListener getFocused() { return focused; }

	@Override
	public void setFocused(@Nullable IGuiEventListener focused) { this.focused = focused; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void render(int mouseX, int mouseY, float partialTicks) { children.forEach(c -> c.render(mouseX, mouseY, partialTicks)); }

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void onInitialize() { children.forEach(GuiComponent::onInitialize); }

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void onResize() {
		super.onResize();
		children.forEach(GuiComponent::onResize);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void onTick() { children.forEach(GuiComponent::onTick); }

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void onClose() { children.forEach(GuiComponent::onClose); }

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void onRemoved() { children.forEach(GuiComponent::onRemoved); }
}
