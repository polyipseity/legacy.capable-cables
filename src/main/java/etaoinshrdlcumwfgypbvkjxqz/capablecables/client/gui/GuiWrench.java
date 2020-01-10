package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.GuiContainerDefault;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.GuiTabs;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.GuiTabsThemed;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components.GuiRectangleThemed;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components.GuiRectangleThemedDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components.GuiResource;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.Frame;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IThemed;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.IThemed.EnumTheme;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.NumberRelativeDisplay.X;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.NumberRelativeDisplay.Y;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.XY;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.ItemWrench;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Colors;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.variables.References;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectArguments;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public final class GuiWrench extends GuiContainerDefault<Number> implements IThemed<EnumTheme> {
	protected static final GuiTabsThemed<?, ?> TABS = new GuiTabsThemed<>(
			0,
			EnumTheme.NONE,
			new GuiTabs.ITabThemed.Impl<>(
					new GuiRectangleThemedDrawable<>(
							new Rectangle<>(new XY<>(new X<>(0.3F), new Y<>(0.3F)), new XY<>(16, 16)),
							EnumTheme.NONE,
							new GuiResource<>(
									new Rectangle<>(new XY<>(new X<>(0.3F), new Y<>(0.3F)), new XY<>(16, 16)),
									Frame.Immutable.getNone(Function.identity()),
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


	protected GuiTabsThemed.Immutable<Number, ?> tabs;
	protected ItemStack stack;

	protected EnumTheme theme;


	@SuppressWarnings("unchecked")
	public GuiWrench(Container container, int open, EnumTheme theme, ItemStack stack) {
		super(container);
		if (!(stack.getItem() instanceof ItemWrench)) throw rejectArguments(stack);
		this.theme = theme;
		this.stack = stack;

		GuiTabsThemed<?, ?> tabs = TABS.clone();
		tabs.setOpen(open);
		tabs.setTheme(theme);
		this.tabs = (GuiTabsThemed.Immutable<Number, ?>) tabs.toImmutable();
	}

	public GuiWrench(Container container, ItemStack stack) { this(container, 0, EnumTheme.NONE, stack); }


	public GuiTabsThemed<Number, ?> getTabs() { return tabs; }

	public void setTabs(GuiTabsThemed<Number, ?> tabs) { throw rejectUnsupportedOperation(); }

	public ItemStack getStack() { return stack; }

	public void setStack(ItemStack stack) { throw rejectUnsupportedOperation(); }


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTheme(EnumTheme theme) { this.theme = theme; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnumTheme getTheme() { return theme; }


	/** {@inheritDoc} */
	@Override
	public Rectangle<Number, ?> specification() {
		return tabs.specification();
	}


	/** {@inheritDoc} */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) { tabs.draw(mc); }
}
