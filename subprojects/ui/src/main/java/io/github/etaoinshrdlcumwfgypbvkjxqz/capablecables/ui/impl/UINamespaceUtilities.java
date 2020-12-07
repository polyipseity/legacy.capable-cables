package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import org.jetbrains.annotations.NonNls;

public enum UINamespaceUtilities {
	;

	public static final @NonNls String VIEW_BINDING_NAMESPACE = "view";
	public static final @NonNls String RENDERER_BINDING_NAMESPACE = "renderer";
	public static final @NonNls String INTERNAL_BINDING_NAMESPACE = "internal";
	public static final @NonNls String VIEW_BINDING_PREFIX = VIEW_BINDING_NAMESPACE + IIdentifier.StaticHolder.getSeparator();
	public static final @NonNls String RENDERER_BINDING_PREFIX = RENDERER_BINDING_NAMESPACE + IIdentifier.StaticHolder.getSeparator();

	public static @NonNls String getUniqueInternalBindingNamespace(IUIComponent component) {
		return getInternalBindingNamespace() + '-' + System.identityHashCode(component);
	}

	public static @NonNls String getInternalBindingNamespace() {
		return INTERNAL_BINDING_NAMESPACE;
	}

	public static @NonNls String getViewBindingNamespace() {
		return VIEW_BINDING_NAMESPACE;
	}

	public static @NonNls String getRendererBindingNamespace() {
		return RENDERER_BINDING_NAMESPACE;
	}

	public static @NonNls String getViewBindingPrefix() {
		return VIEW_BINDING_PREFIX;
	}

	public static @NonNls String getRendererBindingPrefix() {
		return RENDERER_BINDING_PREFIX;
	}
}
