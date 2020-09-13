package $group__.common.registrables.blocks;

import $group__.ui.UIConstants;
import $group__.ui.debug.UIDebugMinecraft;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static $group__.ConstantsProduct.MOD_ID;

public enum BlocksThis {
	;

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

	public static final RegistryObject<Block> CABLE = BLOCKS.register("cable", BlockCable::new);

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<Block> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.BUILD_TYPE.isDebug() ? BLOCKS.register(UIDebugMinecraft.PATH, UIDebugMinecraft::getBlockEntry) : null;
	}
}
