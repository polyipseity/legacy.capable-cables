package $group__.ui.core.parsers.components;

import $group__.ui.core.parsers.IUIResourceParser;
import org.w3c.dom.Document;

import javax.annotation.Nullable;
import javax.xml.validation.Schema;
import java.util.Map;
import java.util.Optional;

public interface IUIDOMParser<T>
		extends IUIResourceParser<T, Document> {
	default Schema getMainSchema() {
		@Nullable Schema ret = getSchemaMapView().get(getMainNamespaceURI());
		assert ret != null;
		return ret;
	}

	Map<String, Schema> getSchemaMapView();

	String getMainNamespaceURI();

	Optional<? extends Schema> putSchema(String namespaceURI, Schema schema);
}
