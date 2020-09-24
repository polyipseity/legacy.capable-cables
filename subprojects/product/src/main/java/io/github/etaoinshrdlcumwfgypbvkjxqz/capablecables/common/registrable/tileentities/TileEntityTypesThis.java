package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.tileentities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug.UIDebugMinecraft;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public enum TileEntityTypesThis {
	;

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ModConstants.MOD_ID);

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<TileEntityType<?>> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.BUILD_TYPE.isDebug() ? TILE_ENTITIES.register(UIDebugMinecraft.getPath(), UIDebugMinecraft::getTileEntityEntry) : null;
	}
}
