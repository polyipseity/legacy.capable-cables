package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.utilities;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

public enum BlockUtilities {
	;

	public static BlockPos getPlacePosition(BlockRayTraceResult rtr) { return rtr.getPos().add(rtr.getFace().getDirectionVec()); }

	public static boolean checkNoEntityCollision(BlockState state, World world, BlockPos pos) {
		VoxelShape collision = state.getCollisionShape(world, pos);
		return world.checkNoEntityCollision(null, collision.withOffset(pos.getX(), pos.getY(), pos.getZ()));
	}
}
