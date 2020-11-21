package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

public enum UINamespaceUtilities {
	;

	public static final @NonNls String VIEW_BINDING_NAMESPACE = "view";
	public static final @NonNls String RENDERER_BINDING_NAMESPACE = "renderer";
	public static final @NonNls String VIEW_BINDING_PREFIX = VIEW_BINDING_NAMESPACE + INamespacePrefixedString.StaticHolder.getSeparator();
	public static final @NonNls String RENDERER_BINDING_PREFIX = RENDERER_BINDING_NAMESPACE + INamespacePrefixedString.StaticHolder.getSeparator();

	public static String getViewBindingNamespace() {
		return VIEW_BINDING_NAMESPACE;
	}

	public static String getRendererBindingNamespace() {
		return RENDERER_BINDING_NAMESPACE;
	}

	public static String getViewBindingPrefix() {
		return VIEW_BINDING_PREFIX;
	}

	public static String getRendererBindingPrefix() {
		return RENDERER_BINDING_PREFIX;
	}
}
