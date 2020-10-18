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

	private static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, ModConstants.getModId());

	private static final RegistryObject<Block> CABLE = getRegister().register("cable", BlockCable::new);

	@SuppressWarnings("unused")
	@Nullable
	private static final RegistryObject<Block> DEBUG_UI;

	static {
		DEBUG_UI = UIConstants.getBuildType().isDebug()
				? getRegister().register(UIDebugMinecraft.getPath(), UIDebugMinecraft::getBlockEntry)
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
