package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;

import java.util.function.Supplier;

public class JAXBIdentityObjectAdapter<LR>
		extends JAXBAbstractObjectAdapter<LR, LR> {
	private static final Supplier<JAXBIdentityObjectAdapter<?>> INSTANCE = Suppliers.memoize(JAXBIdentityObjectAdapter::new);

	@SuppressWarnings("unchecked")
	public static <T> JAXBIdentityObjectAdapter<T> getInstance() {
		return (JAXBIdentityObjectAdapter<T>) INSTANCE.get(); // COMMENT safe, accepts anything and produces the same thing
	}

	@Override
	@Deprecated
	public @Nonnull LR leftToRight(@Nonnull LR left) {
		return left;
	}

	@Override
	@Deprecated
	public @Nonnull LR rightToLeft(@Nonnull LR right) {
		return right;
	}
}
