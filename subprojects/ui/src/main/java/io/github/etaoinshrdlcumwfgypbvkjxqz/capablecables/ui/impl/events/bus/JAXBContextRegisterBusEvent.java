package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.AbstractBusEvent;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public class JAXBContextRegisterBusEvent
		extends AbstractBusEvent<Void> {
	private final Set<Class<?>> classesToBeBound = Collections.newSetFromMap(
			MapBuilderUtilities.newMapMakerNormalThreaded().weakKeys().initialCapacity(CapacityUtilities.getInitialCapacityMedium()).makeMap());

	public JAXBContextRegisterBusEvent() {
		super(Void.class);
	}

	@SuppressWarnings("UnusedReturnValue")
	public boolean addClassesToBeBound(Iterator<? extends Class<?>> classes) {
		return Iterators.addAll(getClassesToBeBound(), classes);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Class<?>> getClassesToBeBound() { return classesToBeBound; }

	public ImmutableSet<Class<?>> getClassesToBeBoundView() { return ImmutableSet.copyOf(getClassesToBeBound()); }
}
