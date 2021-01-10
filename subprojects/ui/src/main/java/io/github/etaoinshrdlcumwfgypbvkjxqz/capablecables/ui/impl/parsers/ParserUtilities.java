package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBImmutableAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.def.IThrowableHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import java.io.IOException;
import java.io.InputStream;

public enum ParserUtilities {
	;

	public static Object parseJAXBResource(JAXBContext context,
	                                       Class<?> resourceContext, String resourceName,
	                                       IThrowableHandler<? super IOException> throwableHandler) {
		InputStream is = AssertionUtilities.assertNonnull(resourceContext.getResourceAsStream(resourceName));
		try {
			try {
				return context.createUnmarshaller().unmarshal(is);
			} catch (JAXBException e) {
				throw ThrowableUtilities.propagate(e);
			}
		} finally {
			ThrowableUtilities.runQuietly(is::close, IOException.class, throwableHandler);
		}
	}

	public static Object transformJAXBResource(IJAXBAdapterRegistry registry, Object parsed) {
		return IJAXBAdapterRegistry.adaptFromJAXB(
				JAXBImmutableAdapterContext.of(UIDefaultComponentSchemaHolder.getAdapterRegistry()),
				parsed);
	}
}
