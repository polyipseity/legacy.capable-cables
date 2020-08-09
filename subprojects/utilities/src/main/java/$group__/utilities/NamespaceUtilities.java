package $group__.utilities;

public enum NamespaceUtilities {
	;

	public static final String NAMESPACE_DEFAULT = "minecraft";
	public static final String NAMESPACE_SEPARATOR_DEFAULT = ":";

	public static String getNamespacePrefixedString(String separator, String namespace, String string) { return namespace + separator + string; }
}
