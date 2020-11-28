package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ImmutableNamespacePrefixedString
		implements INamespacePrefixedString {
	private final String namespace;
	private final String string;

	private ImmutableNamespacePrefixedString(@NonNls CharSequence namespace, @NonNls CharSequence string) {
		/* COMMENT
		Many instances of this type are created with the same strings commonly, so we will intern them.

		Heap instead of PermGen has been used for the intern table since Java 7, so memory is much less of an issue.
		The default string pool size was increased significantly during Java 7,
		so the intern performance should be okay.  It can be adjusted by the user if needed.

		It will also help with 'equals' of this type, as two equal strings will return true on the identity check,
		which is the best case scenario, instead of checking the whole string, which is the worst case scenario.
		 */
		this.namespace = namespace.toString().intern();
		this.string = string.toString().intern();
	}

	public static ImmutableNamespacePrefixedString of(@NonNls CharSequence string) {
		String[] decomposed = INamespacePrefixedString.decompose(string);
		return of(decomposed[0], decomposed[1]);
	}

	public static ImmutableNamespacePrefixedString of(@NonNls CharSequence namespace, @NonNls CharSequence string) {
		return new ImmutableNamespacePrefixedString(namespace, string);
	}

	@Override
	public @NonNls String getNamespace() { return namespace; }

	@Override
	public @NonNls String getString() { return string; }

	@Override
	public int hashCode() { return ObjectUtilities.hashCodeImpl(this, StaticHolder.getObjectVariableMap().values()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equalsImpl(this, obj, INamespacePrefixedString.class, true, StaticHolder.getObjectVariableMap().values()); }

	@Override
	public String toString() { return ObjectUtilities.toStringImpl(this, StaticHolder.getObjectVariableMap()); }
}
