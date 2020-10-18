package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class AbstractContainerAwareExtension<K, C extends IExtensionContainer<? super K>, E extends C>
		extends IHasGenericClass.Extended.Impl<C, E>
		implements IExtension<K, C> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	private OptionalWeakReference<E> container = new OptionalWeakReference<>(null);

	public AbstractContainerAwareExtension(Class<C> genericClass, Class<E> containerClass) { super(genericClass, containerClass); }

	@SuppressWarnings("unchecked")
	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(C container) {
		if (!getExtendedClass().isInstance(container))
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UtilitiesMarkers.getInstance()::getMarkerExtension)
							.addKeyValue("container", container)
							.addMessages(() -> getResourceBundle().getString("this.added.container.instance_of.fail"))
							.build()
			);
		setContainer((E) container); // COMMENT checked
		Cleaner.create(container,
				this::onExtensionRemoved);
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() { setContainer(null); }

	protected Optional<? extends E> getContainer() { return container.getOptional(); }

	protected void setContainer(@Nullable E container) { this.container = new OptionalWeakReference<>(container); }
}
