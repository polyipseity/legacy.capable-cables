package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.blocks;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.materials.ModMaterials;
import net.minecraft.block.Block;

public class CableBlock extends Block {
	protected CableBlock() {
		super(Properties.create(ModMaterials.getPetrifiedSky()));
	}
}
