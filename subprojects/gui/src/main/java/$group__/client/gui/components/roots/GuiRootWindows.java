package $group__.client.gui.components.roots;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiWindow;
import $group__.client.gui.components.backgrounds.GuiBackground;
import $group__.client.gui.traits.IGuiReRectangleHandler;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableList;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiRootWindows<C extends Container> extends GuiRoot<C> {
	protected List<GuiWindow> windows = new ArrayList<>(INITIAL_CAPACITY_2);

	public GuiRootWindows(ITextComponent title, @Nullable GuiBackground background) { this(title, background, null); }

	public GuiRootWindows(ITextComponent title, @Nullable GuiBackground background, @Nullable C container) { super(title, background, container); }

	@Override
	public void setRectangle(IGuiReRectangleHandler handler, GuiComponent invoker, java.awt.geom.Rectangle2D rectangle) {
		anchors.clear();
		constraints.clear();
		super.setRectangle(handler, invoker, rectangle);
	}

	@Override
	public void reRectangle(GuiComponent invoker, Rectangle2D rectangle) {
		super.reRectangle(invoker, rectangle);
		getWindows().forEach(w -> w.reRectangle(invoker));
	}

	protected void add(@Nullable GuiBackground background, GuiWindow... windows) {
		if (background != null) super.add(background);
		super.add(windows);
		getWindows().addAll(Arrays.asList(windows));
	}

	public void add(GuiWindow... windows) {
		add(null, windows);
	}

	@Override
	@Deprecated
	public void add(GuiComponent... components) {
		@Nullable GuiBackground background = null;
		GuiWindow[] windows = new GuiWindow[components.length];
		int i = 0;
		for (GuiComponent component : components) {
			if (component instanceof GuiBackground && background == null)
				background = (GuiBackground) component;
			else if (component instanceof GuiWindow)
				windows[i++] = (GuiWindow) component;
			else
				throw BecauseOf.illegalArgument("components", Arrays.toString(components), "component", component);
		}
		if (background == null)
			add(windows);
		else
			add(background, Arrays.copyOf(windows, windows.length - 1));
	}

	@Override
	public void remove(GuiComponent... components) {
		super.remove(components);
		List<GuiComponent> cl = Arrays.asList(components);
		getWindows().removeIf(cl::contains);
	}

	public ImmutableList<GuiWindow> getWindowsView() { return ImmutableList.copyOf(getWindows()); }

	protected List<GuiWindow> getWindows() { return windows; }
}
