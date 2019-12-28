package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.components;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IDrawable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.IGuiThemed;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SideOnly(Side.CLIENT)
public class GuiTabs extends Gui implements IDrawable, IGuiThemed<IGuiThemed.Theme> {
    protected final List<ITab> tabs;
    protected int open;

    public GuiTabs(int open, List<ITab> tabs) {
        this.open = open;
        this.tabs = tabs;
    }
    public GuiTabs(int open, ITab... tabs) { this(open, Arrays.asList(tabs)); }

    public List<ITab> getTabs() { return tabs; }
    public void setOpen(int open) { this.open = open; }
    public int getOpen() { return open; }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() {
        AtomicInteger i = new AtomicInteger();
        tabs.forEach(t -> {
            if (i.getAndIncrement() == open) t.draw();
            else t.drawClosed();
        });
    }

    /* IGuiThemed */
    private Theme theme = Theme.NONE;
    /**
     * {@inheritDoc}
     */
    @Override
    public void setTheme(Theme theme) { this.theme = theme; }
    /**
     * {@inheritDoc}
     */
    @Override
    public Theme getTheme() { return theme; }

    public interface ITab extends IDrawable {
        void drawClosed();

        abstract class Impl implements ITab {
            protected IDrawable content;
            protected IDrawable access;
            public Impl(IDrawable content, IDrawable access) {
                this.content = content;
                this.access = access;
            }
            @SuppressWarnings("EmptyMethod")
            protected void merge() {}

            /**
             * {@inheritDoc}
             */
            @Override
            public void draw() {
                access.draw();
                content.draw();
                merge();
            }
            /**
             * {@inheritDoc}
             */
            @Override
            public void drawClosed() { access.draw(); }
        }
    }
}
