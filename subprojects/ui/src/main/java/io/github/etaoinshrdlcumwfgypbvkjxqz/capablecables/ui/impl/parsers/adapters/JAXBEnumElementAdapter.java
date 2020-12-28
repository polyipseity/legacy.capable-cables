package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.EnumUtilities;
import jakarta.xml.bind.JAXBElement;

import java.util.function.Function;

public class JAXBEnumElementAdapter<L extends Enum<L>, R extends Enum<R>>
		extends JAXBAbstractElementAdapter<L, R> {
	private final Class<L> leftClazz;
	private final Class<R> rightClazz;
	private final Function<@Nonnull ? super L, @Nonnull ? extends JAXBElement<L>> elementWrapper;

	public JAXBEnumElementAdapter(Class<L> leftClazz, Class<R> rightClazz, Function<@Nonnull ? super L, @Nonnull ? extends JAXBElement<L>> elementWrapper) {
		assert EnumUtilities.areNamesCompatible(ImmutableList.of(leftClazz, rightClazz));

		this.leftClazz = leftClazz;
		this.rightClazz = rightClazz;
		this.elementWrapper = elementWrapper;
	}

	@Override
	@Deprecated
	public @Nonnull R leftToRight(@Nonnull JAXBElement<L> left) {
		return Enum.valueOf(getRightClazz(), left.getValue().name());
	}

	@Override
	@Deprecated
	public @Nonnull JAXBElement<L> rightToLeft(@Nonnull R right) {
		return getElementWrapper().apply(Enum.valueOf(getLeftClazz(), right.name()));
	}

	protected Function<@Nonnull ? super L, @Nonnull ? extends JAXBElement<L>> getElementWrapper() {
		return elementWrapper;
	}

	protected Class<L> getLeftClazz() {
		return leftClazz;
	}

	protected Class<R> getRightClazz() {
		return rightClazz;
	}
}
