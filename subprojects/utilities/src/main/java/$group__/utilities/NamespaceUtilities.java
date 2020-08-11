package $group__.utilities;

public enum NamespaceUtilities {
	;

	public static final String
			NAMESPACE_DEFAULT = "minecraft",
			NAMESPACE_SEPARATOR_DEFAULT = ":",
			NAMESPACE_DEFAULT_PREFIX = NAMESPACE_DEFAULT + NAMESPACE_SEPARATOR_DEFAULT;

	public static String getNamespacePrefixedString(String separator, String namespace, String string) { return namespace + separator + string; }
}
