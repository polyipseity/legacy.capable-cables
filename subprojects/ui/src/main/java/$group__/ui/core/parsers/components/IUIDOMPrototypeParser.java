package $group__.ui.core.parsers.components;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.mvvm.structures.UIPropertyMappingValue;
import $group__.utilities.DOMUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public interface IUIDOMPrototypeParser<T>
		extends IUIDOMParser<T> {

	static String getClassFromMaybeAlias(Map<String, String> aliases, String maybeAlias) { return aliases.getOrDefault(maybeAlias, maybeAlias); }

	static void initializeMapping(Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping, Node node, @Nullable String namespaceURI) {
		DOMUtilities.getChildrenByTagNameNS(node, namespaceURI, "property").forEach(p ->
				mapping.put(
						DOMUtilities.getAttributeValue(p, "key").map(NamespacePrefixedString::new).orElseThrow(InternalError::new),
						new UIPropertyMappingValue(p.getFirstChild(), null)));
		DOMUtilities.getChildrenByTagNameNS(node, namespaceURI, "binding").forEach(p ->
				mapping.put(
						DOMUtilities.getAttributeValue(p, "key").map(NamespacePrefixedString::new).orElseThrow(InternalError::new),
						new UIPropertyMappingValue(
								p.getFirstChild(),
								DOMUtilities.getAttributeValue(p, "binding").map(NamespacePrefixedString::new).orElse(null))));
	}

	Map<INamespacePrefixedString, BiFunction<? super IUIDOMPrototypeParser<?>, ? super Node, ? extends IGeneralPrototype>> getVisitorsView();

	Optional<? extends BiFunction<? super IUIDOMPrototypeParser<?>, ? super Node, ? extends IGeneralPrototype>> putVisitor(INamespacePrefixedString localName,
	                                                                                                                       BiFunction<? super IUIDOMPrototypeParser<?>, ? super Node, ? extends IGeneralPrototype> visitor);

	Map<String, String> getAliasesView();
}
