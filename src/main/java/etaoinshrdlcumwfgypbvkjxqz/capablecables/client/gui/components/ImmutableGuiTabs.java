package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.components;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.concurrent.Immutable;
import java.util.List;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;

@SideOnly(Side.CLIENT)
@Immutable
public class ImmutableGuiTabs extends GuiTabs {
    public ImmutableGuiTabs(int open, List<ITab> tabs) { super(open, tabs); }
    public ImmutableGuiTabs(int open, ITab... tabs) { super(open, ImmutableList.copyOf(tabs)); }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOpen(int open) { throw rejectUnsupportedOperation(); }
}
