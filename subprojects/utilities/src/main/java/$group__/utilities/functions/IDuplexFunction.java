package $group__.utilities.functions;

import java.io.Serializable;
import java.util.function.Function;

public interface IDuplexFunction<L, R> {
	default <NL> IDuplexFunction<NL, R> chainLeft(IDuplexFunction<NL, L> function) {
		return new Functional<>(
				function.asLeftToRightFunction().andThen(asLeftToRightFunction()),
				asRightToLeftFunction().andThen(function.asRightToLeftFunction())
		);
	}

	default Function<L, R> asLeftToRightFunction() { return this::leftToRight; }

	default Function<R, L> asRightToLeftFunction() { return this::rightToLeft; }

	R leftToRight(L left);

	L rightToLeft(R right);

	default <NR> IDuplexFunction<L, NR> chainRight(IDuplexFunction<R, NR> function) {
		return new Functional<>(
				asLeftToRightFunction().andThen(function.asLeftToRightFunction()),
				function.asRightToLeftFunction().andThen(asRightToLeftFunction())
		);
	}

	default IDuplexFunction<R, L> reverse() { return Reverse.of(this); }

	class Functional<L, R>
			implements IDuplexFunction<L, R>, Serializable {
		private static final long serialVersionUID = -7487772309945154280L;
		protected final Function<? super L, ? extends R> leftToRightFunction;
		protected final Function<? super R, ? extends L> rightToLeftFunction;

		public Functional(Function<? super L, ? extends R> leftToRightFunction, Function<? super R, ? extends L> rightToLeftFunction) {
			this.leftToRightFunction = leftToRightFunction;
			this.rightToLeftFunction = rightToLeftFunction;
		}

		@Override
		public Function<L, R> asLeftToRightFunction() { return getLeftToRightFunction()::apply; }

		@Override
		public Function<R, L> asRightToLeftFunction() { return getRightToLeftFunction()::apply; }

		@Override
		public R leftToRight(L left) { return getLeftToRightFunction().apply(left); }

		@Override
		public L rightToLeft(R right) { return getRightToLeftFunction().apply(right); }

		protected Function<? super R, ? extends L> getRightToLeftFunction() { return rightToLeftFunction; }

		protected Function<? super L, ? extends R> getLeftToRightFunction() { return leftToRightFunction; }
	}

	class Reverse<L, R>
			implements IDuplexFunction<L, R>, Serializable {
		private static final long serialVersionUID = 8742593880687842971L;
		protected final IDuplexFunction<R, L> reverse;

		protected Reverse(IDuplexFunction<R, L> reverse) {
			this.reverse = reverse;
		}

		public static <L, R> IDuplexFunction<L, R> of(IDuplexFunction<R, L> reverse) {
			if (reverse instanceof Reverse)
				return ((Reverse<R, L>) reverse).getReverse();
			return new Reverse<>(reverse);
		}

		protected IDuplexFunction<R, L> getReverse() { return reverse; }

		@Override
		public R leftToRight(L left) { return getReverse().rightToLeft(left); }

		@Override
		public L rightToLeft(R right) { return getReverse().leftToRight(right); }

		@Override
		public IDuplexFunction<R, L> reverse() { return getReverse(); }
	}
}
