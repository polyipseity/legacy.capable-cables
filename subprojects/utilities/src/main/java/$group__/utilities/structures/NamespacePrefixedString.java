package $group__.utilities.structures;

import $group__.utilities.ObjectUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;

import javax.annotation.concurrent.Immutable;

@Immutable
public class NamespacePrefixedString
		implements INamespacePrefixedString {
	protected final String namespace;
	protected final String path;

	public NamespacePrefixedString(String string) { this(INamespacePrefixedString.decompose(string)); }

	protected NamespacePrefixedString(String[] parts) { this(parts[0], parts[1]); }

	public NamespacePrefixedString(String namespace, String path) {
		this.namespace = namespace;
		this.path = path;
	}

	@Override
	public String getNamespace() { return namespace; }

	@Override
	public String getPath() { return path; }

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, OBJECT_VARIABLES); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, OBJECT_VARIABLES); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }
}
