package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import jakarta.xml.bind.JAXBElement;

import javax.annotation.Nullable;
import java.util.Optional;

public enum JAXBUtilities {
	;

	public static <T> Optional<T> getActualValueOptional(JAXBElement<T> element) { return Optional.ofNullable(getActualValue(element)); }

	@Nullable
	public static <T> T getActualValue(JAXBElement<T> element) { return element.isNil() ? null : element.getValue(); }
}
