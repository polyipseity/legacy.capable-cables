package $group__.utilities;

public enum NamespaceUtilities {
	;

	public static String getNamespacePrefixedString(String separator, String namespace, String string) { return namespace + separator + string; }
}
