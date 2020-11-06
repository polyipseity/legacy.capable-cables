package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import org.slf4j.Marker;

import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public abstract class ConfigurationTemplate<D> {
	private static final Supplier<@Nonnull Marker> CLASS_MARKER = Suppliers.memoize(() -> UtilitiesMarkers.getInstance().getClassMarker(ConfigurationTemplate.class));
	private final AtomicBoolean configured = new AtomicBoolean();

	protected ConfigurationTemplate() {}

	@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "UnusedReturnValue"})
	public static <D> boolean configureSafe(ConfigurationTemplate<D> self, Supplier<@Nonnull ? extends D> data) {
		synchronized (self) {
			if (!self.isConfigured()) {
				self.configure(data.get());
				return true;
			}
		}
		return false;
	}

	public boolean isConfigured() { return getConfigured().get(); }

	public final void configure(D data) {
		if (getConfigured().getAndSet(true)) {
			// COMMENT this should only be invoked after all the configurations needed to make this call are ready
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(ConfigurationTemplate::getClassMarker)
							.addKeyValue("this", this).addKeyValue("data", data)
							.addMessages(() -> StaticHolder.getResourceBundle().getString("configure.done.already"))
							.build()
			);
		}
		configure0(data);
	}

	protected AtomicBoolean getConfigured() { return configured; }

	protected abstract void configure0(D data);

	public static Marker getClassMarker() { return AssertionUtilities.assertNonnull(CLASS_MARKER.get()); }

	public enum StaticHolder {
		;

		private static final Supplier<@Nonnull ResourceBundle> RESOURCE_BUNDLE = Suppliers.memoize(() -> CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance()));

		protected static ResourceBundle getResourceBundle() { return AssertionUtilities.assertNonnull(RESOURCE_BUNDLE.get()); }
	}
}
