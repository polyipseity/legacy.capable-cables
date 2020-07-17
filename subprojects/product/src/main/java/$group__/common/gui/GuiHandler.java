package $group__.common.gui;

import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static $group__.utilities.helpers.specific.Maps.MAP_MAKER_SINGLE_THREAD;
import static $group__.utilities.helpers.specific.Optionals.unboxOptional;

public enum GuiHandler implements IGuiHandler {
	INSTANCE;

	public static final ConcurrentMap<Integer, IGuiHandler> GUI_MAP = MAP_MAKER_SINGLE_THREAD.makeMap();
	protected static final GuiHandlerFunctional DEFAULT = (distribution, id, player, world, x, y, z) -> {
		throw BecauseOf.illegalArgument("id", id);
	};


	public int registerGui(GuiHandlerFunctional handler) { return registerGui((IGuiHandler) handler); }

	public int registerGui(IGuiHandler handler) {
		int r = GUI_MAP.size();
		GUI_MAP.put(r, handler);
		return r;
	}


	@Nullable
	@Override
	public Object getServerGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) { return GUI_MAP.getOrDefault(id, DEFAULT).getServerGuiElement(id, player, world, x, y, z); }

	@Nullable
	@Override
	public Object getClientGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) { return GUI_MAP.getOrDefault(id, DEFAULT).getClientGuiElement(id, player, world, x, y, z); }


	@FunctionalInterface
	public interface GuiHandlerFunctional extends IGuiHandler {
		Optional<Object> tryGetGuiElement(Dist distribution, int id, PlayerEntity player, World world, int x, int y, int z);

		@Nullable
		@Override
		default Object getServerGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) { return unboxOptional(tryGetGuiElement(Dist.DEDICATED_SERVER, id, player, world, x, y, z)); }

		@Nullable
		@Override
		default Object getClientGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) { return unboxOptional(tryGetGuiElement(Dist.CLIENT, id, player, world, x, y, z)); }
	}
}
