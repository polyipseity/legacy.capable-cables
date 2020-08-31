package $group__.utilities.structures;

import $group__.utilities.AssertionUtilities;
import $group__.utilities.CastUtilities;
import com.google.common.collect.ImmutableSet;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Operations are not thread-safe.
 */
public class NamedNodeMapMap
		implements Map<String, Node> {
	protected final NamedNodeMap delegated;
	protected final NodeListList delegatedList = NodeListList.wrap(new NodeList() {
		@Override
		public Node item(int index) {
			return getDelegated().item(index);
		}

		@Override
		public int getLength() {
			return getDelegated().getLength();
		}
	});

	protected NamedNodeMapMap(NamedNodeMap delegated) { this.delegated = delegated; }

	public static NamedNodeMapMap wrap(NamedNodeMap delegated) { return new NamedNodeMapMap(delegated); }

	protected static class EntryThis
			implements Entry<String, Node> {
		protected final Map<String, Node> parent;
		@Nullable
		protected final String key;

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		public EntryThis(Map<String, Node> parent, @Nullable String key) {
			this.parent = parent;
			this.key = key;
		}

		@Nullable
		@Override
		public String getKey() { return key; }

		@Nullable
		@Override
		public Node getValue() { return getParent().get(getKey()); }

		@Nullable
		@Override
		public Node setValue(Node value) { return getParent().put(getKey(), value); }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Map<String, Node> getParent() { return parent; }
	}

	protected NamedNodeMap getDelegated() { return delegated; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected NodeListList getDelegatedList() { return delegatedList; }

	@Override
	public int size() { return getDelegated().getLength(); }

	@Override
	public boolean isEmpty() { return size() == 0; }

	@Override
	public boolean containsKey(@Nullable Object key) { return get(key) != null; }

	@Override
	public boolean containsValue(@Nullable Object value) { return value instanceof Node && containsKey(constructKey((Node) value)); }

	@Nullable
	@Override
	public Node get(@Nullable Object key) {
		return CastUtilities.castChecked(String.class, key)
				.map(s -> {
					String[] ss = splitString(s);
					return hasNamespaceURI(ss) ? getDelegated().getNamedItemNS(ss[0], ss[1]) : getDelegated().getNamedItem(s);
				}).orElse(null);
	}

	@Override
	public Node put(@Nullable String key, @Nullable Node value) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		String[] ss = splitString(key);
		if (hasNamespaceURI(ss)) {
			if (ss[0].equals(value.getNamespaceURI()) && ss[1].equals(value.getLocalName()))
				return getDelegated().setNamedItemNS(value);
			throw new UnsupportedOperationException();
		}
		if (key.equals(value.getLocalName()))
			return getDelegated().setNamedItem(value);
		throw new UnsupportedOperationException();
	}

	@Nullable
	@Override
	public Node remove(Object key) {
		if (containsKey(key)) {
			String s = (String) key;
			String[] ss = splitString(s);
			return ss.length == 2 ? getDelegated().removeNamedItemNS(ss[0], ss[1]) : getDelegated().removeNamedItem(s);
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Node> m) { m.forEach(this::put); }

	@Override
	public void clear() {
		while (size() != 0) {
			String k = constructKey(AssertionUtilities.assertNonnull(getDelegatedList().get(0)));
			String[] ks = splitString(k);
			if (hasNamespaceURI(ks))
				remove(ks[0], ks[1]);
			else
				remove(k);
		}
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public Set<String> keySet() { return getDelegatedList().stream().unordered().map(NamedNodeMapMap::constructKey).collect(ImmutableSet.toImmutableSet()); }

	@Override
	public Collection<Node> values() { return getDelegatedList(); }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public Set<Entry<String, Node>> entrySet() { return keySet().stream().unordered().map(k -> new EntryThis(this, k)).collect(ImmutableSet.toImmutableSet()); }

	protected static String[] splitString(String string) { return string.split(Pattern.quote(":")); }

	protected static boolean hasNamespaceURI(String[] sections) { return sections.length == 2; }

	protected static String constructKey(Node node) {
		return Optional.ofNullable(node.getNamespaceURI())
				.map(ns -> ns + ':')
				.orElse("")
				+ Optional.ofNullable(node.getLocalName())
				.orElse("");
	}
}
