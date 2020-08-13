package $group__.common.registrables.tileentities;

import $group__.client.ui.mvvm.minecraft.debug.UIComponentDebug;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static $group__.Constants.MOD_ID;

public enum TileEntityTypesThis {
	;

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

	// todo add debug flag
	@SuppressWarnings("unused")
	private static final RegistryObject<TileEntityType<?>> DEBUG_GUI_COMPONENT = TILE_ENTITIES.register(UIComponentDebug.PATH, UIComponentDebug::getTileEntityEntry);
}
