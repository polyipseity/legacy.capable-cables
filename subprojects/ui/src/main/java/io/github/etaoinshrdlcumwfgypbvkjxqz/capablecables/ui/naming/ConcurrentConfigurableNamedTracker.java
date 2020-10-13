package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.naming;

import com.google.common.collect.MapMaker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

@ThreadSafe
public class ConcurrentConfigurableNamedTracker<E extends INamed>
		extends AbstractConcurrentNamedTracker<E> {
	private final ConcurrentMap<String, E> data;

	public ConcurrentConfigurableNamedTracker(Consumer<? super MapMaker> configuration) {
		MapMaker dataBuilder = new MapMaker();
		configuration.accept(dataBuilder);
		this.data = dataBuilder.makeMap();
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected ConcurrentMap<String, E> getData() { return data; }
}
