package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.templates.GuiContainerDefault;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.templates.components.*;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.Frame;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IThemed;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.IThemed.EnumTheme;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.NumberRelativeDisplay.X;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.NumberRelativeDisplay.Y;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.XY;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.common.registrable.items.ItemWrench;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Colors;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.References;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectArguments;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class GuiWrench extends GuiContainerDefault<Number> implements IThemed<EnumTheme> {
	/* SECTION static variables */

	protected static final GuiTabsThemed<?, ?, ?> TABS = new GuiTabsThemed<>(
			EnumTheme.NONE,
			0,
			new GuiTabs.ITabThemed.Impl<>(
					new GuiRectangleThemedDrawable<>(
							new Rectangle<>(new XY<>(new X<>(0.3F), new Y<>(0.3F)), new XY<>(16, 16)),
							EnumTheme.NONE,
							new GuiResource<>(
									new Rectangle<>(new XY<>(new X<>(0.3F), new Y<>(0.3F)), new XY<>(16, 16)),
									new Frame<>(0, 0, 0, 0),
									References.ResourceLocations.GUI_WRENCH,
									new Rectangle<>(new XY<>(256 - 16, 0), new XY<>(16, 16))
							)
					),
					new GuiRectangleThemed<>(
							new Rectangle<>(new XY<>(new X<>(0.3F, 16), new Y<>(0.3F, 16)), new XY<>(new X<>(0.4F, -16), new Y<>(0.4F, -16))),
							Colors.WHITE,
							EnumTheme.NONE
					),
					EnumTheme.NONE
			)
	);


	/* SECTION variables */

	protected GuiTabsThemed.Immutable<Number, EnumTheme, ?> tabs;
	protected ItemStack stack;
	protected EnumTheme theme;


	/* SECTION constructors */

	public GuiWrench(Container container, ItemStack stack, EnumTheme theme, int open) {
		super(container);
		if (!(stack.getItem() instanceof ItemWrench)) throw rejectArguments(stack);
		this.stack = stack;
		this.theme = theme;

		GuiTabsThemed<Number, EnumTheme, ?> tabs = castUnchecked(TABS.clone());
		tabs.setOpen(open);
		tabs.setTheme(theme);
		this.tabs = castUnchecked(tabs.toImmutable());
	}

	public GuiWrench(Container container, ItemStack stack) { this(container, stack, EnumTheme.NONE, 0); }


	/* SECTION getters & setters */

	public GuiTabsThemed<Number, EnumTheme, ?> getTabs() { return tabs; }

	public void setTabs(GuiTabsThemed<Number, EnumTheme, ?> tabs) { throw rejectUnsupportedOperation(); }

	public ItemStack getStack() { return stack; }

	public void setStack(ItemStack stack) { throw rejectUnsupportedOperation(); }


	/** {@inheritDoc} */
	@Override
	public void setTheme(EnumTheme theme) {
		tabs.setTheme(theme);
		this.theme = theme;
	}

	/** {@inheritDoc} */
	@Override
	public EnumTheme getTheme() { return theme; }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public Rectangle<Number, ?> specification() {
		return tabs.specification();
	}


	/** {@inheritDoc} */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) { tabs.draw(mc); }
}
