package $group__.common.registrable.tileentities;

import $group__.ui.UIConstants;
import $group__.ui.debug.UIDebugMinecraft;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static $group__.ModConstants.MOD_ID;

public enum TileEntityTypesThis {
	;

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<TileEntityType<?>> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.BUILD_TYPE.isDebug() ? TILE_ENTITIES.register(UIDebugMinecraft.PATH, UIDebugMinecraft::getTileEntityEntry) : null;
	}
}
