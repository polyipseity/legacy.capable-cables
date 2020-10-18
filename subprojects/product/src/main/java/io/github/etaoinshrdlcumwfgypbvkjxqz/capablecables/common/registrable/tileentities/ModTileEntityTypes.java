package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.tileentities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug.UIMinecraftDebug;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public enum ModTileEntityTypes {
	;

	private static final DeferredRegister<TileEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ModConstants.getModId());

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<TileEntityType<?>> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.getBuildType().isDebug() ? getRegister().register(UIMinecraftDebug.getPath(), UIMinecraftDebug::getTileEntityEntry) : null;
	}

	public static DeferredRegister<TileEntityType<?>> getRegister() {
		return REGISTER;
	}

	@SuppressWarnings("unused")
	@Nullable
	private static RegistryObject<TileEntityType<?>> getDebugUI() {
		return DEBUG_UI;
	}
}
