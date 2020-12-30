package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.MaybeNullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionalDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.ReversedDuplexFunction;

import java.util.function.Function;

public interface IDuplexFunction<L, R> {
	default <NL> IDuplexFunction<NL, R> chainLeft(IDuplexFunction<NL, L> function) {
		return FunctionalDuplexFunction.of(
				asLeftToRightFunction(function).andThen(asLeftToRightFunction(this)),
				asRightToLeftFunction(this).andThen(asRightToLeftFunction(function))
		);
	}

	static <L, R> Function<L, R> asLeftToRightFunction(IDuplexFunction<? super L, ? extends R> instance) {
		return instance::leftToRight;
	}

	static <L, R> Function<R, L> asRightToLeftFunction(IDuplexFunction<? extends L, ? super R> instance) {
		return instance::rightToLeft;
	}

	@MaybeNullable
	R leftToRight(@MaybeNullable L left);

	@MaybeNullable
	L rightToLeft(@MaybeNullable R right);

	default <NR> IDuplexFunction<L, NR> chainRight(IDuplexFunction<R, NR> function) {
		return FunctionalDuplexFunction.of(
				asLeftToRightFunction(this).andThen(asLeftToRightFunction(function)),
				asRightToLeftFunction(function).andThen(asRightToLeftFunction(this))
		);
	}

	default IDuplexFunction<R, L> swap() {
		return ReversedDuplexFunction.of(this);
	}
}
