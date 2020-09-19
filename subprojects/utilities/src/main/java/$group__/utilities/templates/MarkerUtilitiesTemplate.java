package $group__.utilities.templates;

import $group__.utilities.CapacityUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.collections.CacheUtilities;
import $group__.utilities.structures.Singleton;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public abstract class MarkerUtilitiesTemplate extends Singleton {
	@NonNls
	private static final String SEPARATOR = "-";
	private static final CacheBuilder<Object, Object> MARKERS_BUILDER = CacheUtilities.newCacheBuilderNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_LARGE).weakKeys();
	@NonNls
	protected final Marker markerUnmarked = getMarker("unmarked");
	@NonNls
	protected final Marker markerClass = getMarker("class");

	protected final String namespace;
	@NonNls
	protected final Marker markerStructure = getMarker("structure");
	protected final LoadingCache<Class<?>, Marker> classMarkers =
			getMarkersBuilder().build(CacheLoader.from(clazz -> {
				assert clazz != null;
				@SuppressWarnings("StringConcatenation") Marker ret = getMarker(clazz.getSimpleName() + '@' + Integer.toHexString(clazz.getName().hashCode()));
				getMarkerClass().add(ret);
				return ret;
			}));

	protected static CacheBuilder<Object, Object> getMarkersBuilder() { return MARKERS_BUILDER; }

	protected String getNamespacePrefixedString(String string) { return NamespaceUtilities.getNamespacePrefixedString(getSeparator(), getNamespace(), string); }

	protected MarkerUtilitiesTemplate(String namespace, Logger logger) {
		super(logger);
		this.namespace = namespace;
	}

	public final Marker getMarker(String string) { return MarkerFactory.getMarker(getNamespacePrefixedString(string)); }

	public static String getSeparator() { return SEPARATOR; }

	protected String getNamespace() { return namespace; }

	public Marker getClassMarker(Class<?> clazz) { return getClassMarkers().getUnchecked(clazz); }

	protected LoadingCache<Class<?>, Marker> getClassMarkers() { return classMarkers; }

	public Marker getMarkerClass() { return markerClass; }

	public Marker getMarkerUnmarked() { return markerUnmarked; }

	public Marker getMarkerStructure() { return markerStructure; }
}
