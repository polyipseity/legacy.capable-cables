package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapterContext;

import java.util.function.BiFunction;

public class JAXBFunctionalObjectAdapter<L, R>
		extends JAXBAbstractObjectAdapter<L, R> {
	private final BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super L, @Nonnull ? extends R> leftToRightFunction;
	private final BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super R, @Nonnull ? extends L> rightToLeftFunction;

	public JAXBFunctionalObjectAdapter(BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super L, @Nonnull ? extends R> leftToRightFunction,
	                                   BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super R, @Nonnull ? extends L> rightToLeftFunction) {
		this.leftToRightFunction = leftToRightFunction;
		this.rightToLeftFunction = rightToLeftFunction;
	}

	@Override
	@Deprecated
	public @Nonnull R leftToRight(@Nonnull L left) {
		return getThreadLocalContext()
				.map(context -> getLeftToRightFunction().apply(context, left))
				.orElseThrow(IllegalStateException::new);
	}

	protected BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super L, @Nonnull ? extends R> getLeftToRightFunction() {
		return leftToRightFunction;
	}

	@Override
	@Deprecated
	public @Nonnull L rightToLeft(@Nonnull R right) {
		return getThreadLocalContext()
				.map(context -> getRightToLeftFunction().apply(context, right))
				.orElseThrow(IllegalStateException::new);
	}

	protected BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super R, @Nonnull ? extends L> getRightToLeftFunction() {
		return rightToLeftFunction;
	}
}
