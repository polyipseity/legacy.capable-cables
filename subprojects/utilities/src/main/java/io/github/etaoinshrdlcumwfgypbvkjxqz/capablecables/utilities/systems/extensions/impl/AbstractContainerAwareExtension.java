package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class AbstractContainerAwareExtension<K, C extends IExtensionContainer<? super K>, CE extends C>
		implements IExtension<K, C> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<? extends CE> typeToken;
	private OptionalWeakReference<CE> container = new OptionalWeakReference<>(null);

	@SuppressWarnings("UnstableApiUsage")
	public AbstractContainerAwareExtension(Class<CE> type) {
		this.typeToken = TypeToken.of(type);
	}

	@SuppressWarnings({"UnstableApiUsage", "unchecked"})
	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(C container) {
		if (!getTypeToken().getRawType().isInstance(container))
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UtilitiesMarkers.getInstance()::getMarkerExtension)
							.addKeyValue("container", container)
							.addMessages(() -> getResourceBundle().getString("this.added.container.instance_of.fail"))
							.build()
			);
		setContainer((CE) container); // COMMENT checked
		Cleaner.create(container,
				this::onExtensionRemoved);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<? extends CE> getTypeToken() {
		return typeToken;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() { setContainer(null); }

	protected Optional<? extends CE> getContainer() { return container.getOptional(); }

	protected void setContainer(@Nullable CE container) { this.container = new OptionalWeakReference<>(container); }
}
