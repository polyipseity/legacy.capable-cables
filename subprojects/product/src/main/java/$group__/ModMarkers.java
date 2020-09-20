package $group__;

import $group__.utilities.AssertionUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.structures.Singleton;
import $group__.utilities.templates.MarkerUtilitiesTemplate;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.slf4j.Marker;

public final class ModMarkers extends MarkerUtilitiesTemplate {
	private static final ModMarkers INSTANCE = Singleton.getSingletonInstance(ModMarkers.class, ModConfiguration.getInstance().getLogger());

	private final Marker markerModLifecycle = getMarker("mod lifecycle");
	private final Marker markerRegistrable = getMarker("registrable");
	private final LoadingCache<IForgeRegistry<?>, Marker> registryMarkers =
			MarkerUtilitiesTemplate.getMarkersBuilder().build(CacheLoader.from(registry ->
					addReferences(getMarker(AssertionUtilities.assertNonnull(registry).getRegistryName().toString()),
							getMarkerRegistrable())));
	private final LoadingCache<IForgeRegistryEntry<?>, Marker> registryEntryMarkers =
			MarkerUtilitiesTemplate.getMarkersBuilder().build(CacheLoader.from(registryEntry -> {
				assert registryEntry != null;
				return addReferences(getMarker(AssertionUtilities.assertNonnull(registryEntry.getRegistryName()).toString()),
						getRegistryMarker(GameRegistry.findRegistry(CastUtilities.castUnchecked(registryEntry.getRegistryType()))));
			}));

	private ModMarkers() { super(ModConstants.MOD_ID, ModConfiguration.getInstance().getLogger()); }

	public static ModMarkers getInstance() { return INSTANCE; }

	public Marker getRegistryMarker(IForgeRegistry<?> key) { return getRegistryMarkers().getUnchecked(key); }

	protected LoadingCache<IForgeRegistry<?>, Marker> getRegistryMarkers() { return registryMarkers; }

	public Marker getRegistryEntryMarker(IForgeRegistryEntry<?> key) { return getRegistryEntryMarkers().getUnchecked(key); }

	protected LoadingCache<IForgeRegistryEntry<?>, Marker> getRegistryEntryMarkers() { return registryEntryMarkers; }

	public Marker getMarkerModLifecycle() { return markerModLifecycle; }

	public Marker getMarkerRegistrable() { return markerRegistrable; }
}
