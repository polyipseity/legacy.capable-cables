package $group__.$modId__.client.gui;

import $group__.$modId__.client.gui.components.GuiResource;
import $group__.$modId__.client.gui.components.GuiTabs;
import $group__.$modId__.client.gui.coordinates.NumberRelativeDisplay.X;
import $group__.$modId__.client.gui.coordinates.NumberRelativeDisplay.Y;
import $group__.$modId__.client.gui.coordinates.XY;
import $group__.$modId__.client.gui.polygons.Rectangle;
import $group__.$modId__.client.gui.themes.EnumTheme;
import $group__.$modId__.client.gui.traits.IThemed;
import $group__.$modId__.common.registrables.items.ItemWrench;
import $group__.$modId__.traits.basic.ILogging;
import $group__.$modId__.utilities.helpers.specific.Colors;
import $group__.$modId__.utilities.variables.Globals;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

import static $group__.$modId__.client.gui.bases.GuiContainerBases.initGuiBase;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectArguments;

@SideOnly(Side.CLIENT)
public class GuiWrench extends GuiContainer implements IThemed<EnumTheme>, ILogging {

	/* SECTION variables */

	protected GuiTabsThemed<Number, List<GuiTabs.ITab<Number, ?>>, GuiTabs.ITab<Number, ?>, EnumTheme, ?> tabs;
	protected ItemStack stack;
	protected EnumTheme theme;
	protected Logger logger;


	/* SECTION constructors */

	public GuiWrench(Container container, ItemStack stack, Logger logger) { this(container, stack, EnumTheme.NONE, 0, logger); }

	public GuiWrench(Container container, ItemStack stack, EnumTheme theme, int open, Logger logger) {
		super(container);
		if (!(stack.getItem() instanceof ItemWrench)) throw rejectArguments(stack);
		this.stack = stack;
		this.theme = theme;
		this.logger = logger;

		tabs = new GuiTabsThemed<>(
				EnumTheme.NONE,
				0,
				logger,
				new GuiTabs.ITabThemed.Impl<>(
						new GuiRectangleThemedDrawable<>(
								new Rectangle<>(new XY<>(new X<>(0.1F), new Y<>(0.1F, -32), ), new XY<>(32, 32, )),
								new GuiResource<>(
										new Rectangle<>(new XY<>(new X<>(0.1F), new Y<>(0.1F, -32), ), new XY<>(64, 64, )),
										Globals.Client.Resources.GUI_WRENCH,
										Globals.Client.Resources.GUI_WRENCH_INFO,
										logger
										),
								EnumTheme.NONE,
								logger
								),
						new GuiRectangleThemed<>(
								new Rectangle<>(new XY<>(new X<>(0.1F), new Y<>(0.1F), ), new XY<>(new X<>(0.8F), new Y<>(0.8F), )),
								Colors.WHITE,
								EnumTheme.NONE,
								logger
								),
						EnumTheme.NONE,
						logger
						));
		tabs.setOpen(open);
		tabs.setTheme(theme);
	}


	/* SECTION getters & setters */

	public GuiTabsThemed<Number, List<GuiTabs.ITab<Number, ?>>, GuiTabs.ITab<Number, ?>, EnumTheme, ?> getTabs() { return tabs; }

	public void setTabs(GuiTabsThemed<Number, List<GuiTabs.ITab<Number, ?>>, GuiTabs.ITab<Number, ?>, EnumTheme, ?> tabs) { this.tabs = tabs; }

	public ItemStack getStack() { return stack; }

	public void setStack(ItemStack stack) { this.stack = stack; }

	@Override
	public Logger getLogger() { return logger; }

	@Override
	public void setLogger(Logger logger) { this.logger = logger; }

	@Override
	public EnumTheme getTheme() { return theme; }

	@Override
	public void setTheme(EnumTheme theme) {
		tabs.setTheme(theme);
		this.theme = theme;
	}


	/* SECTION methods */

	@Override
	public void initGui() {
		initGuiBase(this, t -> xSize = t, t -> ySize = t);
		super.initGui();
	}

	@Override
	public Optional<Rectangle.Immutable<Number, ?>> spec() { return tabs.spec(); }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) { tabs.draw(mc); }
}
