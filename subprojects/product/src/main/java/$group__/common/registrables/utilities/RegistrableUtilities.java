package $group__.common.registrables.utilities;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public enum RegistrableUtilities {
	;

	public enum BlockUtilities {
		;

		public static BlockPos getPlacePosition(BlockRayTraceResult rtr) { return rtr.getPos().add(rtr.getFace().getDirectionVec()); }

		public static boolean checkNoEntityCollision(BlockState state, World world, BlockPos pos) {
			VoxelShape collision = state.getCollisionShape(world, pos);
			return world.checkNoEntityCollision(null, collision.withOffset(pos.getX(), pos.getY(), pos.getZ()));
		}
	}

	public enum RayTraceResultUtilities {
		;

		public static BlockRayTraceResult getBlockRayTraceResultFromItemUseContext(ItemUseContext context) {return getBlockRayTraceResultFromItemUseContext(context, false);}

		public static BlockRayTraceResult getBlockRayTraceResultFromItemUseContext(ItemUseContext context, boolean isInside) { return new BlockRayTraceResult(context.getHitVec(), context.getFace(), context.getPos(), false); }
	}

	public enum NBTUtilities {
		;

		public static boolean setTagIfNotEmpty(CompoundNBT p, String k, CompoundNBT v) {
			if (v.size() == 0) return false;
			p.put(k, v);
			return true;
		}

		public static <T> boolean setChildIfNotNull(CompoundNBT p, String k, @Nullable T v, TriConsumer<CompoundNBT, String, ? super T> c) {
			if (v == null) return false;
			c.accept(p, k, v);
			return true;
		}

		public static <T> Optional<T> readChildIfHasKey(@Nullable CompoundNBT p, String key, Supplier<? extends INBT> type,
		                                                BiFunction<CompoundNBT, String, T> f) {
			if (p != null && p.contains(key, type.get().getId())) return Optional.of(f.apply(p, key));
			return Optional.empty();
		}

		public static Optional<CompoundNBT> returnTagIfNotEmpty(CompoundNBT p) {
			return p.isEmpty() ? Optional.empty() : Optional.of(p);
		}
	}
}
