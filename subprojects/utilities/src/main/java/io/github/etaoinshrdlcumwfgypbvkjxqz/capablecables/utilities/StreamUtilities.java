package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum StreamUtilities {
	;
	// COMMENT from http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html
	public static final int THRESHOLD_PARALLEL = 10000;

	@SuppressWarnings("UnstableApiUsage")
	public static <T> Stream<T> streamSmart(Iterable<T> iterable, int cost) {
		return CastUtilities.castChecked(CastUtilities.<Class<Collection<T>>>castUnchecked(Collection.class), iterable)
				.map(c -> streamSmart(c.size(), cost, c::stream, c::parallelStream))
				.orElseGet(() -> streamSmart(1, cost, () -> Streams.stream(iterable), () -> Streams.stream(iterable).parallel()));
	}

	// COMMENT from http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html
	public static <T extends Stream<?>> T streamSmart(int size, int cost, Supplier<@Nonnull ? extends T> sequential, Supplier<?
			extends T> parallel) { return AssertionUtilities.assertNonnull(size * cost >= getThresholdParallel() ? parallel.get() : sequential.get()); }

	public static int getThresholdParallel() {
		return THRESHOLD_PARALLEL;
	}
}
