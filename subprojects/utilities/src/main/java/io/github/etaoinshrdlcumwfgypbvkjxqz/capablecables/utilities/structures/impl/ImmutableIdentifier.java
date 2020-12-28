package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import org.jetbrains.annotations.NonNls;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ImmutableIdentifier
		extends AbstractIdentifier {
	private final String namespace;
	private final String name;

	private ImmutableIdentifier(@NonNls CharSequence namespace, @NonNls CharSequence name) {
		this.namespace = namespace.toString();
		this.name = name.toString();
	}

	public static ImmutableIdentifier of(@NonNls CharSequence string) {
		return ofInterning(string, false, false);
	}

	public static ImmutableIdentifier ofInterning(@NonNls CharSequence string, boolean internNamespace, boolean internName) {
		ITuple2<String, String> decomposed = IIdentifier.decompose(string);
		return ofInterning(decomposed.getLeft(), decomposed.getRight(), internNamespace, internName);
	}

	public static ImmutableIdentifier ofInterning(@NonNls CharSequence namespace, @NonNls CharSequence name, boolean internNamespace, boolean internName) {
		/* COMMENT
		Many instances of this type are created with the same strings commonly, so we will intern them.

		Heap instead of PermGen has been used for the intern table since Java 7, so memory is much less of an issue.
		The default string pool size was increased significantly during Java 7,
		so the intern performance should be okay.  It can be adjusted by the user if needed.

		It will also help with 'equals' of this type, as two equal strings will return true on the identity check,
		which is the best case scenario, instead of checking the whole string, which is the worst case scenario.
		*/
		return new ImmutableIdentifier(internNamespace ? namespace.toString().intern() : namespace,
				internName ? name.toString().intern() : name);
	}

	public static ImmutableIdentifier of(@NonNls CharSequence namespace, @NonNls CharSequence name) {
		return ofInterning(namespace, name, false, false);
	}

	public static ImmutableIdentifier ofInterning(@NonNls CharSequence namespace, @NonNls CharSequence name) {
		return ofInterning(namespace, name, true, true);
	}

	public static ImmutableIdentifier ofInterning(@NonNls CharSequence string) {
		return ofInterning(string, true, true);
	}

	@Override
	public @NonNls String getNamespace() { return namespace; }

	@Override
	public @NonNls String getName() { return name; }
}
