package $group__.$modId__.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static $group__.$modId__.utilities.helpers.MapsExtension.SINGLE_THREAD_MAP_MAKER;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;

public enum GuiHandler implements IGuiHandler {
	/* SECTION enums */
	INSTANCE;


	/* SECTION static variables */

	public static final ConcurrentMap<Integer, IGuiHandler> GUI_MAP = SINGLE_THREAD_MAP_MAKER.makeMap();
	protected static final GuiHandlerFunctional DEFAULT = (side, id, player, world, x, y, z) -> {
		throw rejectArguments(side, id, player, world, x, y, z);
	};


	/* SECTION methods */

	public int registerGui(GuiHandlerFunctional handler) { return registerGui((IGuiHandler) handler); }

	public int registerGui(IGuiHandler handler) {
		int r = GUI_MAP.size();
		GUI_MAP.put(r, handler);
		return r;
	}


	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return GUI_MAP.getOrDefault(id, DEFAULT).getServerGuiElement(id, player, world, x, y, z); }

	@Nullable
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return GUI_MAP.getOrDefault(id, DEFAULT).getClientGuiElement(id, player, world, x, y, z); }


	/* SECTION static classes */

	@FunctionalInterface
	public interface GuiHandlerFunctional extends IGuiHandler {
		/* SECTION methods */

		@Nullable
		@Override
		default Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return unboxOptional(getGuiElement(Side.SERVER, id, player, world, x, y, z)); }

		@Nullable
		@Override
		default Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return unboxOptional(getGuiElement(Side.CLIENT, id, player, world, x, y, z)); }


		Optional<Object> getGuiElement(Side side, int id, EntityPlayer player, World world, int x, int y, int z);
	}
}
