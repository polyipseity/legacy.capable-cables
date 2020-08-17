package $group__.common.registrables.tileentities;

import $group__.client.ui.ConstantsUI;
import $group__.client.ui.debug.UIDebugMinecraft;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static $group__.Constants.MOD_ID;

public enum TileEntityTypesThis {
	;

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<TileEntityType<?>> DEBUG_UI;

	static {
		DEBUG_UI = ConstantsUI.BUILD_TYPE.isDebug() ? TILE_ENTITIES.register(UIDebugMinecraft.PATH, UIDebugMinecraft::getTileEntityEntry) : null;
	}
}
