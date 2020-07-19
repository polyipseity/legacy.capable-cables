package $group__.client.gui.components.roots;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiWindow;
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

	public GuiRootWindows(ITextComponent title) {this(title, null);}

	public GuiRootWindows(ITextComponent title, @Nullable C container) { super(title, container); }

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

	public void add(GuiWindow... windows) {
		super.add(windows);
		getWindows().addAll(Arrays.asList(windows));
	}

	@Override
	@Deprecated
	public void add(GuiComponent... components) {
		for (GuiComponent component : components) {
			if (!(component instanceof GuiWindow))
				throw BecauseOf.illegalArgument("components", Arrays.toString(components), "component", component);
		}
		add(Arrays.copyOf(components, components.length, GuiWindow[].class));
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
