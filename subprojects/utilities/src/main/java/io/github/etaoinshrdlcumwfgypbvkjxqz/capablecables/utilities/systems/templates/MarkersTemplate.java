package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.NamespaceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.StackTraceUtilities;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Arrays;

public abstract class MarkersTemplate {
	@NonNls
	public static final String SEPARATOR = "-";
	private static final CacheBuilder<Object, Object> MARKERS_BUILDER = CacheUtilities.newCacheBuilderNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacityLarge()).weakKeys();
	@NonNls
	private final Marker markerUnmarked;
	@NonNls
	private final Marker markerClass;

	private final String namespace;
	@NonNls
	private final Marker markerStructure;
	private final LoadingCache<Class<?>, Marker> classMarkers;

	protected MarkersTemplate(CharSequence namespace) {
		this.namespace = namespace.toString();
		this.markerUnmarked = getMarker("unmarked");
		this.markerClass = getMarker("class");
		this.markerStructure = getMarker("structure");
		this.classMarkers = getMarkersBuilder().build(CacheLoader.from(clazz -> {
			assert clazz != null;
			return addReferences(getMarker(clazz.getSimpleName() + '@' + Integer.toHexString(clazz.getName().hashCode())),
					getMarkerClass());
		}));
	}

	public final Marker getMarker(@NonNls CharSequence string) { return MarkerFactory.getMarker(getNamespacePrefixedString(string)); }

	protected static CacheBuilder<Object, Object> getMarkersBuilder() { return MARKERS_BUILDER; }

	public static Marker addReferences(Marker marker, Marker... references) {
		Arrays.stream(references).forEachOrdered(marker::add);
		return marker;
	}

	public Marker getMarkerClass() { return markerClass; }

	protected String getNamespacePrefixedString(CharSequence string) { return NamespaceUtilities.getNamespacePrefixedString(getSeparator(), getNamespace(), string); }

	public static String getSeparator() { return SEPARATOR; }

	protected String getNamespace() { return namespace; }

	public Marker getClassMarker() { return getClassMarkers().getUnchecked(StackTraceUtilities.getCallerClass()); }

	protected LoadingCache<Class<?>, Marker> getClassMarkers() { return classMarkers; }

	public Marker getClassMarker(Class<?> clazz) { return getClassMarkers().getUnchecked(clazz); }

	public Marker getMarkerUnmarked() { return markerUnmarked; }

	public Marker getMarkerStructure() { return markerStructure; }
}
