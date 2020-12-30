package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBElementAdapter;
import jakarta.xml.bind.JAXBElement;

public abstract class JAXBAbstractElementAdapter<L, R>
		extends JAXBAbstractAdapter<JAXBElement<L>, R>
		implements IJAXBElementAdapter<L, R> {}
