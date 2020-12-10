package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import org.jetbrains.annotations.NonNls;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ImmutableIdentifier
		extends AbstractIdentifier {
	private final String namespace;
	private final String string;

	private ImmutableIdentifier(@NonNls CharSequence namespace, @NonNls CharSequence string) {
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

	public static ImmutableIdentifier of(@NonNls CharSequence string) {
		ITuple2<String, String> decomposed = IIdentifier.decompose(string);
		return of(decomposed.getLeft(), decomposed.getRight());
	}

	public static ImmutableIdentifier of(@NonNls CharSequence namespace, @NonNls CharSequence string) {
		return new ImmutableIdentifier(namespace, string);
	}

	@Override
	public @NonNls String getNamespace() { return namespace; }

	@Override
	public @NonNls String getName() { return string; }
}
