package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.MaybeNullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.IDuplexFunction;

import java.io.Serializable;

public class ReversedDuplexFunction<L, R>
		implements IDuplexFunction<L, R>, Serializable {
	private static final long serialVersionUID = 8742593880687842971L;
	private final IDuplexFunction<R, L> reverse;

	protected ReversedDuplexFunction(IDuplexFunction<R, L> reverse) {
		this.reverse = reverse;
	}

	public static <L, R> IDuplexFunction<L, R> of(IDuplexFunction<R, L> reverse) {
		if (reverse instanceof ReversedDuplexFunction)
			return ((ReversedDuplexFunction<R, L>) reverse).getReverse();
		return new ReversedDuplexFunction<>(reverse);
	}

	protected IDuplexFunction<R, L> getReverse() { return reverse; }

	@Override
	@MaybeNullable
	public R leftToRight(@MaybeNullable L left) { return getReverse().rightToLeft(left); }

	@Override
	@MaybeNullable
	public L rightToLeft(@MaybeNullable R right) { return getReverse().leftToRight(right); }

	@Override
	public IDuplexFunction<R, L> swap() {
		return getReverse();
	}
}
