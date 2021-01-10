package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.blocks;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug.UIMinecraftDebug;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.impl.minecraft.UIMinecraftDesignerBootstrap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.compile.EnumBuildType;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public enum ModBlocks {
	;

	private static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, ModConstants.getModID());

	private static final RegistryObject<Block> CABLE = getRegister().register("cable", CableBlock::new);

	private static final @Nullable RegistryObject<Block> UI_DESIGNER;
	private static final @Nullable RegistryObject<Block> DEBUG_UI;

	static {
		if (UIConstants.getBuildType() == EnumBuildType.DEBUG) {
			UI_DESIGNER = getRegister().register(UIMinecraftDesignerBootstrap.getPath(), UIMinecraftDesignerBootstrap::getBlockEntry);
			DEBUG_UI = getRegister().register(UIMinecraftDebug.getPath(), UIMinecraftDebug::getBlockEntry);
		} else {
			UI_DESIGNER = null;
			DEBUG_UI = null;
		}
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

	@SuppressWarnings("unused")
	@Nullable
	private static RegistryObject<Block> getUIDesigner() {
		return UI_DESIGNER;
	}
}
