package $group__.utilities.specific;

import $group__.utilities.Assertions;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public enum Optionals {
	/* MARK empty */;


	@Nullable
	public static <O, T> T optional(@Nullable O optional, Function<? super O, ? extends T> whenPresent, Supplier<?
			extends T> whenAbsent) { return optional == null ? whenAbsent.get() : whenPresent.apply(optional); }

	public static <O, T> T optionalNonnull(@Nullable O optional, Function<? super O, ? extends T> whenPresent,
	                                       Supplier<? extends T> whenAbsent) {
		return Assertions.assertNonnull(optional(optional,
				whenPresent, whenAbsent));
	}


	@Nullable
	public static <T> T unboxOptional(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<? extends T> o) { return o.orElse(null); }

	public static <T> T unboxOptionalNonnull(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<?
			extends T> o) { return Assertions.assertNonnull(unboxOptional(o)); }
}
