package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NonNls;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public enum NBTUtilities {
	;

	public static boolean setTagIfNotEmpty(CompoundNBT p, @NonNls CharSequence k, CompoundNBT v) {
		if (v.isEmpty())
			return false;
		p.put(k.toString(), v);
		return true;
	}

	@SuppressWarnings("UnusedReturnValue")
	public static <T> boolean setChildIfNotNull(CompoundNBT p, @NonNls CharSequence k, @Nullable T v, TriConsumer<? super CompoundNBT, ? super String, ? super T> c) {
		if (v == null) return false;
		c.accept(p, k.toString(), v);
		return true;
	}

	public static <T> Optional<T> readChildIfHasKey(@Nullable CompoundNBT p, @NonNls CharSequence key, Supplier<@Nonnull ? extends INBT> type,
	                                                BiFunction<@Nonnull ? super CompoundNBT, @Nonnull ? super String, @Nonnull ? extends T> f) {
		String key2 = key.toString();
		return Optional.ofNullable(p)
				.filter(pt -> pt.contains(key2, type.get().getId()))
				.map(pt -> f.apply(pt, key2));
	}

	public static Optional<CompoundNBT> returnTagIfNotEmpty(CompoundNBT p) { return Optional.of(p).filter(t -> !t.isEmpty()); }
}
