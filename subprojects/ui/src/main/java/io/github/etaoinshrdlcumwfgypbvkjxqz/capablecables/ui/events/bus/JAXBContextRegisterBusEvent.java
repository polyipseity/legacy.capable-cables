package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.AbstractBusEvent;

import java.util.Collections;
import java.util.Set;

public class JAXBContextRegisterBusEvent
		extends AbstractBusEvent<Void> {
	private final Set<Class<?>> classesToBeBound = Collections.newSetFromMap(
			MapBuilderUtilities.newMapMakerNormalThreaded().weakKeys().initialCapacity(CapacityUtilities.getInitialCapacityMedium()).makeMap());

	public JAXBContextRegisterBusEvent() {
		super(Void.class);
	}

	public boolean addClassesToBeBound(Class<?>... classes) { return addClassesToBeBound(ImmutableList.copyOf(classes)); }

	public boolean addClassesToBeBound(Iterable<? extends Class<?>> classes) { return Iterables.addAll(getClassesToBeBound(), classes); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Class<?>> getClassesToBeBound() { return classesToBeBound; }

	public ImmutableSet<Class<?>> getClassesToBeBoundView() { return ImmutableSet.copyOf(getClassesToBeBound()); }
}
