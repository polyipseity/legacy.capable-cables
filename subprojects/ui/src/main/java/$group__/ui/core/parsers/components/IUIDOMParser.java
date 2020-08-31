package $group__.ui.core.parsers.components;

import $group__.ui.core.parsers.IUIResourceParser;
import $group__.utilities.AssertionUtilities;
import org.w3c.dom.Document;

import javax.xml.validation.Schema;
import java.util.Map;
import java.util.Optional;

public interface IUIDOMParser<T>
		extends IUIResourceParser<T, Document> {
	default Schema getMainSchema() { return AssertionUtilities.assertNonnull(getSchemaMapView().get(getMainNamespaceURI())); }

	Map<String, Schema> getSchemaMapView();

	String getMainNamespaceURI();

	Optional<? extends Schema> putSchema(String namespaceURI, Schema schema);
}
