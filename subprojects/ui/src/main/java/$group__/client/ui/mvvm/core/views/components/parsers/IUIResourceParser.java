package $group__.client.ui.mvvm.core.views.components.parsers;

import $group__.client.ui.mvvm.core.views.components.IUIComponentManager;
import $group__.utilities.interfaces.IHasGenericClass;
import org.xml.sax.SAXException;

import java.io.IOException;

public interface IUIResourceParser<T extends IUIComponentManager, R> extends IHasGenericClass<T> {
	void parseResource(R resource) throws IOException, SAXException, ClassNotFoundException;

	void reset();

	T createUI();
}
