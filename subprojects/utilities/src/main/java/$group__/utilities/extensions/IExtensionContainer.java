package $group__.utilities.extensions;

import $group__.utilities.*;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.templates.CommonConfigurationTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public interface IExtensionContainer<K> {
	@Deprecated
		// COMMENT use one of the checked version
	Optional<? extends IExtension<? extends K, ?>> addExtension(IExtension<? extends K, ?> extension);

	Optional<? extends IExtension<? extends K, ?>> removeExtension(K key);

	Optional<? extends IExtension<? extends K, ?>> getExtension(K key);

	Map<K, IExtension<? extends K, ?>> getExtensionsView();

	enum StaticHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

		@SuppressWarnings("UnusedReturnValue")
		public static <K, V extends IExtension<? extends K, ? super C>, C extends IExtensionContainer<K>> Optional<? extends IExtension<? extends K, ?>> addExtensionChecked(C container, V extension) { return container.addExtension(extension); }

		@SuppressWarnings("UnusedReturnValue")
		public static <K, V extends IExtension<? extends K, C> & IHasGenericClass.Extended<C, ? super E>, C extends IExtensionContainer<K>, E extends C> Optional<? extends IExtension<? extends K, ?>> addExtensionExtendedChecked(E container, V extension) { return container.addExtension(extension); }

		public static <K> Optional<? extends IExtension<? extends K, ?>> getExtensionImpl(Map<K, ? extends IExtension<? extends K, ?>> extensions, K key) { return Optional.ofNullable(extensions.get(key)); }

		@SuppressWarnings("unchecked")
		public static <K> Optional<? extends IExtension<? extends K, ?>> addExtensionImpl(IExtensionContainer<K> container, Map<K, ? super IExtension<? extends K, ?>> extensions, K key, IExtension<? extends K, ?> extension) {
			if (extension.getType().getKey() instanceof INamespacePrefixedString)
				AbstractExtensionRegistry.checkExtensionRegistered((IExtension<? extends INamespacePrefixedString, ?>) extension); // COMMENT checked
			if (!extension.getGenericClass().isInstance(container))
				throw ThrowableUtilities.logAndThrow(
						new IllegalArgumentException(
								new LogMessageBuilder()
										.addMarkers(UtilitiesMarkers.getInstance()::getMarkerExtension)
										.addKeyValue("container", container).addKeyValue("extensions", extensions).addKeyValue("key", key).addKeyValue("extension", extension)
										.addMessages(() -> getResourceBundle().getString("extension.add.impl.container.instance_of.fail"))
										.build()
						),
						UtilitiesConfiguration.getInstance().getLogger()
				);

			Optional<? extends IExtension<? extends K, ?>> ret = container.removeExtension(key);
			extensions.put(key, extension);
			extension.onExtensionAdded(CastUtilities.castUnchecked(container)); // COMMENT type checked above
			return ret;
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		public static <K, V extends IExtension<? extends K, ?>> Optional<V> removeExtensionImpl(Map<K, V> extensions, K key) {
			return Optional.ofNullable(extensions.remove(key)).filter(eo -> {
				eo.onExtensionRemoved();
				return true;
			});
		}
	}
}
