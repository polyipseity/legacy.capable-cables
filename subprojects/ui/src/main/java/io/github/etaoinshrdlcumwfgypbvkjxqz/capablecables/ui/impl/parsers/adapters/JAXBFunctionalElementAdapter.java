package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import jakarta.xml.bind.JAXBElement;

import java.util.function.BiFunction;

public class JAXBFunctionalElementAdapter<L, R>
		extends JAXBAbstractElementAdapter<L, R> {
	private final BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super JAXBElement<L>, @Nonnull ? extends R> leftToRightFunction;
	private final BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super R, @Nonnull ? extends JAXBElement<L>> rightToLeftFunction;

	public JAXBFunctionalElementAdapter(BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super JAXBElement<L>, @Nonnull ? extends R> leftToRightFunction,
	                                    BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super R, @Nonnull ? extends JAXBElement<L>> rightToLeftFunction) {
		this.leftToRightFunction = leftToRightFunction;
		this.rightToLeftFunction = rightToLeftFunction;
	}

	@Override
	@Deprecated
	public @Nonnull R leftToRight(@Nonnull JAXBElement<L> left) {
		return getThreadLocalContext()
				.map(context -> getLeftToRightFunction().apply(context, left))
				.orElseThrow(IllegalStateException::new);
	}

	protected BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super JAXBElement<L>, @Nonnull ? extends R> getLeftToRightFunction() {
		return leftToRightFunction;
	}

	@Override
	@Deprecated
	public @Nonnull JAXBElement<L> rightToLeft(@Nonnull R right) {
		return getThreadLocalContext()
				.map(context -> getRightToLeftFunction().apply(context, right))
				.orElseThrow(IllegalStateException::new);
	}

	protected BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super R, @Nonnull ? extends JAXBElement<L>> getRightToLeftFunction() {
		return rightToLeftFunction;
	}
}
