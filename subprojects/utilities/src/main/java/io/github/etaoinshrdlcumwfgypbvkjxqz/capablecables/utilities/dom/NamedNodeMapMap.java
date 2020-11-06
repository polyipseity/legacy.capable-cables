package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dom;

import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import org.jetbrains.annotations.NonNls;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Operations are not thread-safe.
 */
public class NamedNodeMapMap
		extends AbstractDelegatingObject<NamedNodeMap>
		implements Map<String, Node> {
	private final NodeListList asList;

	public NamedNodeMapMap(NamedNodeMap delegate) {
		super(delegate);
		this.asList = new NodeListList(new NamedNodeMapNodeList(delegate));
	}

	public static NamedNodeMapMap wrap(NamedNodeMap delegated) { return new NamedNodeMapMap(delegated); }

	protected static class EntryThis
			implements Entry<String, Node> {
		private final Map<String, Node> parent;
		@Nullable
		private final String key;

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

	@Override
	public int size() { return getDelegate().getLength(); }

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
					return hasNamespaceURI(ss) ? getDelegate().getNamedItemNS(ss[0], ss[1]) : getDelegate().getNamedItem(s);
				}).orElse(null);
	}

	@SuppressWarnings("CallToSuspiciousStringMethod")
	@Override
	public Node put(@Nullable String key, @Nullable Node value) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		String[] ss = splitString(key);
		if (hasNamespaceURI(ss)) {
			if (ss[0].equals(value.getNamespaceURI()) && ss[1].equals(value.getLocalName()))
				return getDelegate().setNamedItemNS(value);
			throw new UnsupportedOperationException();
		}
		if (key.equals(value.getLocalName()))
			return getDelegate().setNamedItem(value);
		throw new UnsupportedOperationException();
	}

	@Nullable
	@Override
	public Node remove(Object key) {
		if (containsKey(key)) {
			String s = (String) key;
			String[] ss = splitString(s);
			return ss.length == 2 ? getDelegate().removeNamedItemNS(ss[0], ss[1]) : getDelegate().removeNamedItem(s);
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Node> m) { m.forEach(this::put); }

	@Override
	public void clear() {
		while (size() != 0) {
			String k = constructKey(AssertionUtilities.assertNonnull(getDelegate().item(0)));
			String[] ks = splitString(k);
			if (hasNamespaceURI(ks))
				remove(ks[0], ks[1]);
			else
				remove(k);
		}
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public @Nonnull Set<String> keySet() {
		return getAsList().stream().unordered()
				.map(NamedNodeMapMap::constructKey)
				.collect(ImmutableSet.toImmutableSet());
	}

	@Override
	public @Nonnull Collection<Node> values() { return getAsList(); }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public @Nonnull Set<Entry<String, Node>> entrySet() { return keySet().stream().unordered().map(k -> new EntryThis(this, k)).collect(ImmutableSet.toImmutableSet()); }

	protected static String[] splitString(String string) { return string.split(Pattern.quote(":")); }

	protected static boolean hasNamespaceURI(String[] sections) { return sections.length == 2; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected NodeListList getAsList() { return asList; }

	@SuppressWarnings("RedundantLambdaParameterType")
	protected static @NonNls String constructKey(Node node) {
		return Optional.ofNullable(node.getNamespaceURI())
				.map((@NonNls String ns) -> ns + ':')
				.orElse("")
				+ Optional.ofNullable(node.getLocalName())
				.orElse("");
	}
}
