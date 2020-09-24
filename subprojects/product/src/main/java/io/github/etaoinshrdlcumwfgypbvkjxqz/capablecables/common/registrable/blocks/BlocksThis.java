package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.blocks;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug.UIDebugMinecraft;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public enum BlocksThis {
	;

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModConstants.MOD_ID);

	public static final RegistryObject<Block> CABLE = BLOCKS.register("cable", BlockCable::new);

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<Block> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.BUILD_TYPE.isDebug() ? BLOCKS.register(UIDebugMinecraft.getPath(), UIDebugMinecraft::getBlockEntry) : null;
	}
}
