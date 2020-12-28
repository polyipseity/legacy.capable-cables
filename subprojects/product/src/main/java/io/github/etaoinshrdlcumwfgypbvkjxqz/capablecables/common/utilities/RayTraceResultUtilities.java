package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.utilities;

import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockRayTraceResult;

public enum RayTraceResultUtilities {
	;

	public static BlockRayTraceResult getBlockRayTraceResultFromItemUseContext(ItemUseContext context) {return getBlockRayTraceResultFromItemUseContext(context, false);}

	public static BlockRayTraceResult getBlockRayTraceResultFromItemUseContext(ItemUseContext context, boolean isInside) { return new BlockRayTraceResult(context.getHitVec(), context.getFace(), context.getPos(), false); }
}
