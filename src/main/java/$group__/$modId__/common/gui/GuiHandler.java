package $group__.$modId__.common.gui;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;

public enum GuiHandler implements IGuiHandler {
	/* SECTION enums */
	INSTANCE;


	/* SECTION variables */

	protected final HashMap<Integer, IGuiHandler> guiMap = new HashMap<>();


	/* SECTION getters & setters */

	public Map<Integer, IGuiHandler> getGuiMap() { return ImmutableMap.copyOf(guiMap); }


	/* SECTION methods */

	public int registerGui(IGuiHandler handler) {
		int r = guiMap.size();
		guiMap.put(r, handler);
		return r;
	}

	public int registerGui(GuiHandlerFunctional handler) { return registerGui((IGuiHandler) handler); }


	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return guiMap.getOrDefault(id, DEFAULT).getServerGuiElement(id, player, world, x, y, z); }

	@Nullable
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return guiMap.getOrDefault(id, DEFAULT).getClientGuiElement(id, player, world, x, y, z); }


	/* SECTION static variables */

	protected static final GuiHandlerFunctional DEFAULT = (side, id, player, world, x, y, z) -> {
		throw rejectArguments(side, id, player, world, x, y, z);
	};


	/* SECTION static classes */

	@FunctionalInterface
	public interface GuiHandlerFunctional extends IGuiHandler {
		/* SECTION methods */

		Optional<Object> getGuiElement(Side side, int id, EntityPlayer player, World world, int x, int y, int z);


		@Nullable
		@Override
		default Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return unboxOptional(getGuiElement(Side.SERVER, id, player, world, x, y, z)); }

		@Nullable
		@Override
		default Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return unboxOptional(getGuiElement(Side.CLIENT, id, player, world, x, y, z)); }
	}
}
