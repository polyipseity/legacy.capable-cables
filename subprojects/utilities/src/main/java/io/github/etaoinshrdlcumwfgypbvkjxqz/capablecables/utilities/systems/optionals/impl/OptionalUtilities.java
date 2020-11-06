package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl;

import java.util.Optional;

public enum OptionalUtilities {
	;

	@SuppressWarnings("unchecked")
	public static <T> Optional<T> upcast(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<? extends T> instance) {
		/* COMMENT
		'Optional' is immutable,
		meaning that upcasting 'Optional' will not provide a way to make the value inside not of the original 'Optional' type.
		This should mean the upcast is safe.
		 */
		return (Optional<T>) instance;
	}
}
