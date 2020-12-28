package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import net.minecraftforge.eventbus.api.GenericEvent;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractBusEvent<T>
		extends GenericEvent<T>
		implements IExtensionContainer<Class<?>> {
	private final ConcurrentMap<Class<?>, IExtension<? extends Class<?>, ?>> extensions = MapBuilderUtilities.newMapMakerSingleThreaded()
			.weakKeys()
			.initialCapacity(CapacityUtilities.getInitialCapacityTiny())
			.makeMap();
	private final boolean hasGenericType;

	protected AbstractBusEvent(@Nullable Class<T> genericType) {
		super(genericType);
		this.hasGenericType = genericType != null;
	}

	@Override
	public Type getGenericType() {
		if (isHasGenericType())
			return super.getGenericType();
		throw new AbstractMethodError();
	}

	protected boolean isHasGenericType() { return hasGenericType; }

	@Override
	@Deprecated
	public Optional<? extends IExtension<? extends Class<?>, ?>> addExtension(IExtension<? extends Class<?>, ?> extension) {
		return IExtensionContainer.addExtensionImpl(this, getExtensions(), extension);
	}

	@Override
	public Optional<? extends IExtension<? extends Class<?>, ?>> removeExtension(Class<?> key) {
		return IExtensionContainer.removeExtensionImpl(getExtensions(), key);
	}

	@Override
	public Optional<? extends IExtension<? extends Class<?>, ?>> getExtension(Class<?> key) {
		return IExtensionContainer.getExtensionImpl(getExtensions(), key);
	}

	@Override
	public Map<Class<?>, IExtension<? extends Class<?>, ?>> getExtensionsView() {
		return ImmutableMap.copyOf(getExtensions());
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Class<?>, IExtension<? extends Class<?>, ?>> getExtensions() {
		return extensions;
	}
}
