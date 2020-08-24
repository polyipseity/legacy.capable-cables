package $group__.ui.mvvm.views.components.parsers.dom;

import $group__.ui.core.mvvm.views.components.parsers.xml.IUIDOMParser;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.MapUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import com.google.common.collect.ImmutableMap;
import org.w3c.dom.Document;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public abstract class UIDOMParser<T>
		extends IHasGenericClass.Impl<T>
		implements IUIDOMParser<T> {
	protected final String mainNamespaceURI;
	protected final ConcurrentMap<String, Schema> schemaMap =
			MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_TINY).makeMap();

	public UIDOMParser(Class<T> genericClass, String mainNamespaceURI, Schema mainSchema) {
		super(genericClass);
		this.mainNamespaceURI = mainNamespaceURI;
		schemaMap.put(mainNamespaceURI, mainSchema);
	}

	@Override
	public Map<String, Schema> getSchemaMapView() { return ImmutableMap.copyOf(getSchemaMap()); }

	@Override
	public String getMainNamespaceURI() { return mainNamespaceURI; }

	@Override
	public Optional<? extends Schema> putSchema(String namespaceURI, Schema schema) { return Optional.ofNullable(getSchemaMap().put(namespaceURI, schema)); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<String, Schema> getSchemaMap() { return schemaMap; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void parse(Document resource)
			throws Throwable {
		DOMSource ds = new DOMSource(resource);
		for (Schema s : getSchemaMap().values())
			s.newValidator().validate(ds);
	}
}
