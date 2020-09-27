package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import net.minecraftforge.eventbus.api.Event;

import java.util.Collections;
import java.util.Set;

public class JAXBContextRegisterEvent
		extends Event {
	private final Set<Class<?>> classesToBeBound = Collections.newSetFromMap(
			MapBuilderUtilities.newMapMakerNormalThreaded().weakKeys().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap());

	public boolean addClassesToBeBound(Class<?>... classes) { return addClassesToBeBound(ImmutableList.copyOf(classes)); }

	public boolean addClassesToBeBound(Iterable<? extends Class<?>> classes) { return Iterables.addAll(getClassesToBeBound(), classes); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Class<?>> getClassesToBeBound() { return classesToBeBound; }
}
