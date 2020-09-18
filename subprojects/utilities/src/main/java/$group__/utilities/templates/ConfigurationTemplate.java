package $group__.utilities.templates;

import $group__.utilities.structures.Singleton;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public abstract class ConfigurationTemplate<D> extends Singleton {
	protected final AtomicBoolean configured = new AtomicBoolean();

	protected ConfigurationTemplate(Logger logger) { super(logger); }

	@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "UnusedReturnValue"})
	public static <D> boolean configureSafe(ConfigurationTemplate<D> self, Supplier<? extends D> data) {
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
		if (getConfigured().getAndSet(true))
			throw new IllegalStateException("Setup already done"); // TODO
		configure0(data);
	}

	protected AtomicBoolean getConfigured() { return configured; }

	protected abstract void configure0(D data);
}
