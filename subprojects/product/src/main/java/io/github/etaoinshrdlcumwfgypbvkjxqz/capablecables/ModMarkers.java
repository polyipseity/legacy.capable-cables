package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables;

import com.google.common.base.Suppliers;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.MarkersTemplate;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.slf4j.Marker;

import java.util.function.Supplier;

public final class ModMarkers extends MarkersTemplate {
	private static final Supplier<ModMarkers> INSTANCE = Suppliers.memoize(ModMarkers::new);

	private final Marker markerModLifecycle = getMarker("mod lifecycle");
	private final Marker markerRegistrable = getMarker("registrable");
	private final LoadingCache<IForgeRegistry<?>, Marker> registryMarkers =
			MarkersTemplate.getMarkersBuilder().build(CacheLoader.from(registry ->
					addReferences(getMarker(AssertionUtilities.assertNonnull(registry).getRegistryName().toString()),
							getMarkerRegistrable())));
	private final LoadingCache<IForgeRegistryEntry<?>, Marker> registryEntryMarkers =
			MarkersTemplate.getMarkersBuilder().build(CacheLoader.from(registryEntry -> {
				assert registryEntry != null;
				return addReferences(getMarker(AssertionUtilities.assertNonnull(registryEntry.getRegistryName()).toString()),
						getRegistryMarker(GameRegistry.findRegistry(CastUtilities.castUnchecked(registryEntry.getRegistryType()))));
			}));

	private ModMarkers() { super(ModConstants.getModId()); }

	public static ModMarkers getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	public Marker getRegistryMarker(IForgeRegistry<?> key) { return getRegistryMarkers().getUnchecked(key); }

	protected LoadingCache<IForgeRegistry<?>, Marker> getRegistryMarkers() { return registryMarkers; }

	public Marker getRegistryEntryMarker(IForgeRegistryEntry<?> key) { return getRegistryEntryMarkers().getUnchecked(key); }

	protected LoadingCache<IForgeRegistryEntry<?>, Marker> getRegistryEntryMarkers() { return registryEntryMarkers; }

	public Marker getMarkerModLifecycle() { return markerModLifecycle; }

	public Marker getMarkerRegistrable() { return markerRegistrable; }
}
