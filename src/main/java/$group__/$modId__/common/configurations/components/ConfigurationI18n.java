package $group__.$modId__.common.configurations.components;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.traits.IMapDelegated;
import $group__.$modId__.traits.extensions.ICloneable;
import $group__.$modId__.utilities.helpers.Casts;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static $group__.$modId__.traits.basic.IImmutablizable.tryToImmutableUnboxed;
import static $group__.$modId__.traits.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.traits.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.traits.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.traits.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.toMapFromValues;
import static $group__.$modId__.utilities.helpers.specific.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public class ConfigurationI18n<M extends Map<String, ConfigurationI18n<?, ?>>, T extends ConfigurationI18n<M, T>> implements IMapDelegated<M, String, ConfigurationI18n<?, ?>, T> {
	/* SECTION variables */

	protected final ReentrantReadWriteLock parentLock = new ReentrantReadWriteLock();
	protected String
			id,
			name;
	protected WeakReference<ConfigurationI18n<?, ?>> parent;
	protected boolean parentNull;
	protected M children;


	/* SECTION constructors */

	public ConfigurationI18n(String id, String name, Iterable<? extends ConfigurationI18n<?, ?>> children) { this(id, name, Casts.<M>castUncheckedUnboxedNonnull(toMapFromValues(new TreeMap<>(), children, ConfigurationI18n::getName))); }

	public ConfigurationI18n(String id, String name, M children) { this(id, name, null, children); }

	public ConfigurationI18n(String id, String name, @Nullable ConfigurationI18n<?, ?> parent, M children) {
		this.id = id;
		this.name = name;
		this.children = children;

		ReentrantReadWriteLock.WriteLock plw = parentLock.writeLock();
		plw.lock();
		try {
			this.parent = new WeakReference<>(parent);
			parentNull = parent == null;
		} finally {
			plw.unlock();
		}
	}

	@SuppressWarnings("CopyConstructorMissesField")
	public ConfigurationI18n(ConfigurationI18n<? extends M, ?> copy) { this(copy.getId(), copy.getName(), unboxOptional(copy.getParent()), copy.getChildren()); }


	/* SECTION getters & setters */

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getId() { return id; }

	public void setId(String id) { this.id = id; }

	public Optional<? extends ConfigurationI18n<?, ?>> getParent() {
		ReentrantReadWriteLock.ReadLock plr = parentLock.readLock();
		plr.lock();
		try {
			return Optional.ofNullable(parent.get());
		} finally {
			plr.unlock();
		}
	}

	public void setParent(@Nullable ConfigurationI18n<?, ?> parent) {
		ReentrantReadWriteLock.WriteLock plw = parentLock.writeLock();
		plw.lock();
		try {
			this.parent = new WeakReference<>(parent);
			parentNull = parent == null;
		} finally {
			plw.unlock();
		}
	}

	public M getChildren() { return children; }

	public void setChildren(M children) { this.children = children; }

	@Override
	@Deprecated
	public M getMap() { return getChildren(); }

	@Override
	@Deprecated
	public void setMap(M delegate) { setChildren(delegate); }


	/* SECTION methods */

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }

	@Override
	@OverridingStatus(group = GROUP)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = GROUP)
	public final boolean equals(Object o) { return isEqual(this, o, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() { return ICloneable.clone(() -> super.clone(), LOGGER); }

	@Override
	@OverridingStatus(group = GROUP)
	public final String toString() { return getToStringString(this, super.toString()); }

	protected boolean isParentNull() {
		ReentrantReadWriteLock.ReadLock plr = parentLock.readLock();
		plr.lock();
		try {
			return parentNull;
		} finally {
			plr.unlock();
		}
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<M extends Map<String, ConfigurationI18n<?, ?>>, T extends Immutable<M, T>> extends ConfigurationI18n<M, T> implements IMapDelegated.IImmutable<M, String, ConfigurationI18n<?, ?>, T> {
		/* SECTION constructors */

		public Immutable(ConfigurationI18n<? extends M, ?> copy) { this(copy.getId(), copy.getName(), copy.getChildren()); }

		public Immutable(String id, String name, M children) { this(id, name, null, children); }

		public Immutable(String id, String name, @Nullable ConfigurationI18n<?, ?> parent, M children) { super(tryToImmutableUnboxedNonnull(id, ), tryToImmutableUnboxedNonnull(name, ), tryToImmutableUnboxed(parent, ), tryToImmutableUnboxedNonnull(children, )); }



		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setName(String name) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setId(String id) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setParent(@Nullable ConfigurationI18n<?, ?> parent) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setChildren(M children) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP)
		public final T toImmutable() { return IMapDelegated.IImmutable.super.toImmutable(); }

		@Override
		@OverridingStatus(group = GROUP)
		public final boolean isImmutable() { return IMapDelegated.IImmutable.super.isImmutable(); }
	}
}
