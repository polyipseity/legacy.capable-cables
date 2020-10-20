package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.blocks;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug.UIMinecraftDebug;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public enum ModBlocks {
	;

	private static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, ModConstants.getModID());

	private static final RegistryObject<Block> CABLE = getRegister().register("cable", CableBlock::new);

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<Block> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.getBuildType().isDebug()
				? getRegister().register(UIMinecraftDebug.getPath(), UIMinecraftDebug::getBlockEntry)
				: null;
	}

	public static DeferredRegister<Block> getRegister() {
		return REGISTER;
	}

	public static RegistryObject<Block> getCable() {
		return CABLE;
	}

	@SuppressWarnings("unused")
	@Nullable
	private static RegistryObject<Block> getDebugUI() {
		return DEBUG_UI;
	}
}
