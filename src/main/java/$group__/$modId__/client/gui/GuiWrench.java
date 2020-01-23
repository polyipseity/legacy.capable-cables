package $group__.$modId__.client.gui;

import $group__.$modId__.client.gui.templates.GuiContainerDefault;
import $group__.$modId__.client.gui.templates.components.*;
import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.client.gui.utilities.constructs.IThemed.EnumTheme;
import $group__.$modId__.client.gui.utilities.constructs.NumberRelativeDisplay.X;
import $group__.$modId__.client.gui.utilities.constructs.NumberRelativeDisplay.Y;
import $group__.$modId__.client.gui.utilities.constructs.XY;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.common.registrable.items.ItemWrench;
import $group__.$modId__.utilities.helpers.Colors;
import $group__.$modId__.utilities.variables.References;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class GuiWrench extends GuiContainerDefault<Number> implements IThemed<EnumTheme> {
	/* SECTION variables */

	protected GuiTabsThemed<Number, EnumTheme, ?> tabs;
	protected ItemStack stack;
	protected EnumTheme theme;


	/* SECTION constructors */

	public GuiWrench(Container container, ItemStack stack, EnumTheme theme, int open) {
		super(container);
		if (!(stack.getItem() instanceof ItemWrench)) throw rejectArguments(stack);
		this.stack = stack;
		this.theme = theme;

		GuiTabsThemed<Number, EnumTheme, ?> tabs = TABS.clone();
		tabs.setOpen(open);
		tabs.setTheme(theme);
		this.tabs = tabs;
	}

	public GuiWrench(Container container, ItemStack stack) { this(container, stack, EnumTheme.NONE, 0); }


	/* SECTION getters & setters */

	public GuiTabsThemed<Number, EnumTheme, ?> getTabs() { return tabs; }

	@SuppressWarnings("unused")
	public void setTabs(GuiTabsThemed<Number, EnumTheme, ?> tabs) { throw rejectUnsupportedOperation(); }

	public ItemStack getStack() { return stack; }

	@SuppressWarnings("unused")
	public void setStack(ItemStack stack) { throw rejectUnsupportedOperation(); }


	@Override
	public void setTheme(EnumTheme theme) {
		tabs.setTheme(theme);
		this.theme = theme;
	}

	@Override
	public EnumTheme getTheme() { return theme; }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public Rectangle<Number, ?> spec() {
		return tabs.spec();
	}


	/** {@inheritDoc} */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) { tabs.draw(mc); }


	/* SECTION static variables */

	protected static final GuiTabsThemed<Number, EnumTheme, ?> TABS = new GuiTabsThemed<>(
			EnumTheme.NONE,
			0,
			new GuiTabs.ITabThemed.Impl<>(
					new GuiRectangleThemedDrawable<>(
							new Rectangle<>(new XY<>(new X<>(0.1F), new Y<>(0.1F, -16)), new XY<>(16, 16)),
							EnumTheme.NONE,
							new GuiResource<>(
									new Rectangle<>(new XY<>(new X<>(0.1F), new Y<>(0.1F, -16)), new XY<>(16, 16)),
									References.Client.Resources.GUI_WRENCH,
									References.Client.Resources.GUI_WRENCH_INFO
							)
					),
					new GuiRectangleThemed<>(
							new Rectangle<>(new XY<>(new X<>(0.1F), new Y<>(0.1F)), new XY<>(new X<>(0.8F), new Y<>(0.8F))),
							Colors.WHITE,
							EnumTheme.NONE
					),
					EnumTheme.NONE
			)
	);
}
