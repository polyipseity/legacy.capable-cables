package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.gui;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectArguments;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.requireRunOnceOnly;

public class GuiHandler implements IGuiHandler {
	private GuiHandler() { requireRunOnceOnly(); }

	public static final GuiHandler INSTANCE = new GuiHandler();
	protected final Map<Integer, IGuiHandler> guiMap = new HashMap<>();
	protected static final GuiHandlerFunctional DEFAULT = (side, ID, player, world, x, y, z) -> {
		throw rejectArguments(ID);
	};

	public int registerGui(IGuiHandler handler) {
		int r = guiMap.size();
		guiMap.put(r, handler);
		return r;
	}

	public int registerGui(GuiHandlerFunctional handler) { return registerGui((IGuiHandler) handler); }

	public Map<Integer, IGuiHandler> getGuiMap() { return ImmutableMap.copyOf(guiMap); }


	/** {@inheritDoc} */
	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { return guiMap.getOrDefault(ID, DEFAULT).getServerGuiElement(ID, player, world, x, y, z); }

	/** {@inheritDoc} */
	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { return guiMap.getOrDefault(ID, DEFAULT).getClientGuiElement(ID, player, world, x, y, z); }


	@FunctionalInterface
	public interface GuiHandlerFunctional extends IGuiHandler {
		@Nullable
		Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z);


		/** {@inheritDoc} */
		@Nullable
		@Override
		default Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { return getGuiElement(Side.SERVER, ID, player, world, x, y, z); }

		/** {@inheritDoc} */
		@Nullable
		@Override
		default Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { return getGuiElement(Side.CLIENT, ID, player, world, x, y, z); }
	}
}
