package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming;

import com.google.common.collect.MapMaker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

@ThreadSafe
public class ConcurrentConfigurableNamedTracker<E extends INamed>
		extends AbstractConcurrentNamedTracker<E> {
	private final ConcurrentMap<String, E> data;

	public ConcurrentConfigurableNamedTracker(Consumer<@Nonnull ? super MapMaker> configuration) {
		this.data = FunctionUtilities.accept(new MapMaker(), configuration).makeMap();
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected ConcurrentMap<String, E> getData() { return data; }
}
