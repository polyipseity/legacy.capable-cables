package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.ICompatibilitySupplier;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractParsedSupplier<P, T>
		implements ICompatibilitySupplier<T> {
	private final Supplier<P> lazyParsed = Suppliers.memoize(this::parse);

	protected abstract P parse();

	@Override
	public final T get() {
		return transform(getLazyParsed().get());
	}

	protected abstract T transform(P parsed);

	protected Supplier<? extends P> getLazyParsed() {
		return lazyParsed;
	}

	public static class Functional<P, T>
			extends AbstractParsedSupplier<P, T> {
		private final Supplier<? extends P> parser;
		private final Function<? super P, ? extends T> transformer;

		protected Functional(Supplier<? extends P> parser, Function<P, T> transformer) {
			this.parser = parser;
			this.transformer = transformer;
		}

		public static <P, T> Functional<P, T> of(Supplier<? extends P> parser, Function<P, T> transformer) {
			return new Functional<>(parser, transformer);
		}

		@Override
		protected P parse() {
			return getParser().get();
		}

		@Override
		protected T transform(P parsed) {
			return getTransformer().apply(parsed);
		}

		protected Function<? super P, ? extends T> getTransformer() {
			return transformer;
		}

		protected Supplier<? extends P> getParser() {
			return parser;
		}
	}
}
