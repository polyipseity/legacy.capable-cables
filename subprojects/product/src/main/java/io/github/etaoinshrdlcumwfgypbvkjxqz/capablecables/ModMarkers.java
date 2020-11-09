package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables;

import com.google.common.base.Suppliers;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.MarkersTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.slf4j.Marker;

import java.util.function.Supplier;

public final class ModMarkers extends MarkersTemplate {
	private static final Supplier<@Nonnull ModMarkers> INSTANCE = Suppliers.memoize(ModMarkers::new);

	private final Marker markerLifecycle = getMarker("lifecycle");
	private final Marker markerLifecycleClient = MarkersTemplate.addReferences(getMarkerLifecycle(), getMarker("client"));
	private final Marker markerLifecycleServer = MarkersTemplate.addReferences(getMarkerLifecycle(), getMarker("server"));
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

	private ModMarkers() { super(ModConstants.getModID()); }

	public static ModMarkers getInstance() { return INSTANCE.get(); }

	public Marker getRegistryMarker(IForgeRegistry<?> key) { return getRegistryMarkers().getUnchecked(key); }

	protected LoadingCache<IForgeRegistry<?>, Marker> getRegistryMarkers() { return registryMarkers; }

	public Marker getRegistryEntryMarker(IForgeRegistryEntry<?> key) { return getRegistryEntryMarkers().getUnchecked(key); }

	protected LoadingCache<IForgeRegistryEntry<?>, Marker> getRegistryEntryMarkers() { return registryEntryMarkers; }

	public Marker getMarkerLifecycle() { return markerLifecycle; }

	@OnlyIn(Dist.CLIENT)
	public Marker getMarkerLifecycleClient() {
		return markerLifecycleClient;
	}

	@OnlyIn(Dist.DEDICATED_SERVER)
	public Marker getMarkerLifecycleServer() {
		return markerLifecycleServer;
	}

	public Marker getMarkerRegistrable() { return markerRegistrable; }
}
