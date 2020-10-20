package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.IUIExtensibleParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;

import java.util.concurrent.ConcurrentMap;

public abstract class UIAbstractExtensibleParser<T, R, P, H, HD>
		extends UIAbstractParser<T, R, P>
		implements IUIExtensibleParser<T, R, H, HD> {
	private final ConcurrentMap<HD, H> handlers =
			MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();

	@Override
	public void addHandler(HD discriminator, H handler) { getHandlers().put(discriminator, handler); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<HD, H> getHandlers() { return handlers; }
}
