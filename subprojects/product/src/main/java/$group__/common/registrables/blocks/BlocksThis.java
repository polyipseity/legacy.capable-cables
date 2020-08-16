package $group__.common.registrables.blocks;

import $group__.client.ui.ConstantsUI;
import $group__.client.ui.mvvm.minecraft.debug.UIDebug;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static $group__.Constants.MOD_ID;

public enum BlocksThis {
	;

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

	public static final RegistryObject<Block> CABLE = BLOCKS.register("cable", BlockCable::new);

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<Block> DEBUG_UI;

	static {
		DEBUG_UI = ConstantsUI.BUILD_TYPE.isDebug() ? BLOCKS.register(UIDebug.PATH, UIDebug::getBlockEntry) : null;
	}
}
