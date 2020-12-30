package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.MaybeNullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.IDuplexFunction;

import java.io.Serializable;
import java.util.function.Function;

public class FunctionalDuplexFunction<L, R>
		implements IDuplexFunction<L, R>, Serializable {
	private static final long serialVersionUID = -7487772309945154280L;
	private final Function<? super L, ? extends R> leftToRightFunction;
	private final Function<? super R, ? extends L> rightToLeftFunction;

	protected FunctionalDuplexFunction(Function<? super L, ? extends R> leftToRightFunction,
	                                   Function<? super R, ? extends L> rightToLeftFunction) {
		this.leftToRightFunction = leftToRightFunction;
		this.rightToLeftFunction = rightToLeftFunction;
	}

	public static <L, R> FunctionalDuplexFunction<L, R> of(Function<? super L, ? extends R> leftToRightFunction,
	                                                       Function<? super R, ? extends L> rightToLeftFunction) {
		return new FunctionalDuplexFunction<>(leftToRightFunction, rightToLeftFunction);
	}

	@Override
	@MaybeNullable
	public R leftToRight(@MaybeNullable L left) { return getLeftToRightFunction().apply(left); }

	@Override
	@MaybeNullable
	public L rightToLeft(@MaybeNullable R right) { return getRightToLeftFunction().apply(right); }

	protected Function<? super R, ? extends L> getRightToLeftFunction() { return rightToLeftFunction; }

	protected Function<? super L, ? extends R> getLeftToRightFunction() { return leftToRightFunction; }
}
