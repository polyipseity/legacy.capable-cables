package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.GuiTabsThemed;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IThemed;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.ItemWrench;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectArguments;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
public class GuiWrench extends GuiScreen implements IThemed<IThemed.Theme> {
    protected GuiTabsThemed<Integer> tabs;
    protected ItemStack stack;
    public GuiWrench(int open, Theme theme, ItemStack stack) {
        if (!(stack.getItem() instanceof ItemWrench)) throw rejectArguments(stack);
        this.theme = theme;
        this.stack = stack;
        /* tabs = new GuiTabsThemed.Immutable<>(open, this.theme,
                new GuiTabs.ITab.Impl.Immutable<>(
                        new GuiRectangleThemedDrawable.Immutable<>(),
                        new GuiRectangleThemed.Immutable<>()
                )
        ); */
    }
    public GuiWrench(ItemStack stack) { this(0, Theme.NONE, stack); }

    public void setTabs(GuiTabsThemed<Integer> tabs) { throw rejectUnsupportedOperation(); }
    public GuiTabsThemed<Integer> getTabs() { return tabs; }
    public void setStack(ItemStack stack) { throw rejectUnsupportedOperation(); }
    public ItemStack getStack() { return stack; }

    protected Theme theme;
    /** {@inheritDoc} */
    @Override
    public void setTheme(Theme theme) { this.theme = theme; }
    /** {@inheritDoc} */
    @Override
    public Theme getTheme() { return theme; }
}
