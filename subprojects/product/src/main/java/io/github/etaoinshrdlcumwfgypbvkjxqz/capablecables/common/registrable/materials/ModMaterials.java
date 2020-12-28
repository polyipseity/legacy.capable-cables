package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.materials;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;

public enum ModMaterials {
	;

	private static final Material PETRIFIED_SKY = new Material(MaterialColor.PURPLE, false, true, true, true, false, false, false, PushReaction.NORMAL);

	public static Material getPetrifiedSky() {
		return PETRIFIED_SKY;
	}
}
