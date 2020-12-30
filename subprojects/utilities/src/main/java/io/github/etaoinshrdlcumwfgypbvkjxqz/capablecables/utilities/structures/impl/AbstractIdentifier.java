package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;

public abstract class AbstractIdentifier
		implements IIdentifier {
	@Override
	public int hashCode() {
		return ObjectUtilities.hashCodeImpl(this, StaticHolder.getObjectVariableMap().values().iterator());
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) {
		return ObjectUtilities.equalsImpl(this, obj, IIdentifier.class, true, StaticHolder.getObjectVariableMap().values().iterator());
	}

	@Override
	public @Nonnull String toString() {
		return IIdentifier.asString(this);
	}

	@Override
	public int length() {
		return getNamespace().length() + 1 + getName().length();
	}

	@Override
	public char charAt(int index) {
		String namespace = getNamespace();
		int namespaceLength = namespace.length();
		if (index < namespaceLength)
			return namespace.charAt(index);
		index -= namespaceLength;
		if (index < 1)
			return StaticHolder.getSeparatorChar();
		--index;
		return getName().charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return toString().subSequence(start, end); // COMMENT will need to append strings anyway not using toString
	}
}
