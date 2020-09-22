package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import org.jetbrains.annotations.NonNls;

import javax.annotation.concurrent.Immutable;

@Immutable
public class ImmutableNamespacePrefixedString
		implements INamespacePrefixedString {
	protected final String namespace;
	protected final String path;

	public ImmutableNamespacePrefixedString(@NonNls String string) { this(StaticHolder.decompose(string)); }

	protected ImmutableNamespacePrefixedString(String[] parts) { this(parts[0], parts[1]); }

	public ImmutableNamespacePrefixedString(@NonNls String namespace, @NonNls String path) {
		this.namespace = namespace;
		this.path = path;
	}

	@Override
	public @NonNls String getNamespace() { return namespace; }

	@Override
	public @NonNls String getPath() { return path; }

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, StaticHolder.getObjectVariables()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, StaticHolder.getObjectVariables()); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, StaticHolder.getObjectVariablesMap()); }
}
