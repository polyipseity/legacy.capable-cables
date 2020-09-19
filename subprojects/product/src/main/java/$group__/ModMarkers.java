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
import org.jetbrains.annotations.NonNls;
import org.slf4j.Marker;

public final class ModMarkers extends MarkerUtilitiesTemplate {
	private static final ModMarkers INSTANCE = Singleton.getSingletonInstance(ModMarkers.class, ModConfiguration.getInstance().getLogger());

	@NonNls
	private final Marker markerModLifecycle = getMarker("mod lifecycle");
	@NonNls
	private final Marker markerRegistrable = getMarker("registrable");
	private final LoadingCache<IForgeRegistry<?>, Marker> registryMarkers =
			MarkerUtilitiesTemplate.getMarkersBuilder().build(CacheLoader.from(registry -> {
				assert registry != null;
				Marker ret = getMarker(registry.getRegistryName().toString());
				getMarkerRegistrable().add(ret);
				return ret;
			}));
	private final LoadingCache<IForgeRegistryEntry<?>, Marker> registryEntryMarkers =
			MarkerUtilitiesTemplate.getMarkersBuilder().build(CacheLoader.from(registryEntry -> {
				assert registryEntry != null;
				Marker ret = getMarker(AssertionUtilities.assertNonnull(registryEntry.getRegistryName()).toString());
				getRegistryMarker(GameRegistry.findRegistry(CastUtilities.castUnchecked(registryEntry.getRegistryType()))).add(ret);
				return ret;
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
