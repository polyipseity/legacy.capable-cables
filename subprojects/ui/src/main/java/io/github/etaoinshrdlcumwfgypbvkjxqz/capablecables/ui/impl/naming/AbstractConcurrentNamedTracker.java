package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.ConcurrentMap;

@ThreadSafe
public abstract class AbstractConcurrentNamedTracker<E extends INamed>
		extends AbstractNamedTracker<E> {
	@Override
	protected abstract ConcurrentMap<String, E> getData();
}
