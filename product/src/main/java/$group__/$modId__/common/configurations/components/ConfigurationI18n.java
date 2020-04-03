package $group__.$modId__.common.configurations.components;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.logging.ILogging;
import $group__.$modId__.logging.ILoggingUser;
import $group__.$modId__.utilities.concurrent.IMutatorImmutablizable;
import $group__.$modId__.utilities.concurrent.IMutatorUser;
import $group__.$modId__.utilities.extensions.ICloneable;
import $group__.$modId__.utilities.extensions.IStructure;
import $group__.$modId__.utilities.extensions.delegated.IMapDelegated;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;

import static $group__.$modId__.utilities.Constants.PACKAGE;
import static $group__.$modId__.utilities.concurrent.IMutator.trySetNonnull;
import static $group__.$modId__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.$modId__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

public class ConfigurationI18n<T extends ConfigurationI18n<T, M>, M extends Map<String, ConfigurationI18n<?, ?>>> implements IStructure<T, T>, IMapDelegated<M, String, ConfigurationI18n<?, ?>>, IMutatorUser<IMutatorImmutablizable<?, ?>>, ILoggingUser<ILogging<Logger>, Logger> {
	/* SECTION variables */

	protected String
			id,
			name;
	protected WeakReference<ConfigurationI18n<?, ?>> parent;
	protected M children;

	protected IMutatorImmutablizable<?, ?> mutator;
	protected ILogging<Logger> logging;


	/* SECTION constructors */

	public ConfigurationI18n(String id, String name, M children, IMutatorImmutablizable<?, ?> mutator,
	                         ILogging<Logger> logging) { this(id, name, null, children, mutator, logging); }

	public ConfigurationI18n(String id, String name, @Nullable ConfigurationI18n<?, ?> parent, M children,
	                         IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		this.mutator = trySetNonnull(mutator, mutator, true);
		this.logging = trySetNonnull(getMutator(), logging, true);
		this.id = trySetNonnull(getMutator(), id, true);
		this.name = trySetNonnull(getMutator(), name, true);
		this.parent = trySetNonnull(getMutator(), new WeakReference<>(parent), true);
		this.children = trySetNonnull(getMutator(), children, true);
	}

	public ConfigurationI18n(ConfigurationI18n<?, ? extends M> copy) { this(copy, copy.getMutator()); }


	protected ConfigurationI18n(ConfigurationI18n<?, ? extends M> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getId(), copy.getName(), copy.getParent(), copy.getChildren(), mutator, copy.getLogging()); }


	/* SECTION getters & setters */

	public String getName() { return name; }

	public void setName(String name) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetName(name)); }

	public boolean trySetName(String name) { return trySet(t -> this.name = t, name); }

	public Optional<String> tryGetName() { return Optional.of(getName()); }

	public String getId() { return id; }

	public void setId(String id) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetId(id)); }

	public boolean trySetId(String id) { return trySet(t -> this.id = t, id); }

	public Optional<String> tryGetId() { return Optional.of(getId()); }

	@Nullable
	public ConfigurationI18n<?, ?> getParent() { return parent.get(); }

	public void setParent(ConfigurationI18n<?, ?> parent) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetParent(parent)); }

	public boolean trySetParent(ConfigurationI18n<?, ?> parent) {
		return trySet(t -> this.parent =
				new WeakReference<>(t), parent);
	}

	public Optional<ConfigurationI18n<?, ?>> tryGetParent() { return Optional.ofNullable(getParent()); }

	public M getChildren() { return children; }

	public void setChildren(M children) throws UnsupportedOperationException { rejectUnsupportedOperationIf(trySet(t -> this.children = t, children)); }

	@Override
	@Deprecated
	public M getMap() { return getChildren(); }

	@Override
	@Deprecated
	public void setMap(M delegate) throws UnsupportedOperationException { setChildren(delegate); }


	@Override
	public IMutatorImmutablizable<?, ?> getMutator() { return mutator; }

	@Override
	public boolean trySetMutator(IMutatorImmutablizable<?, ?> mutator) {
		return trySet(t -> this.mutator = t,
				mutator);
	}

	@Override
	public ILogging<Logger> getLogging() { return logging; }

	@Override
	public boolean trySetLogging(ILogging<Logger> logging) { return trySet(t -> this.logging = t, logging); }


	/* SECTION methods */

	@Override
	public T toImmutable() {
		return castUncheckedUnboxedNonnull(new ConfigurationI18n<>(this,
				IMutatorImmutablizable.of(getMutator().toImmutable())));
	}

	@Override
	public boolean isImmutable() { return getMutator().isImmutable(); }


	@Override
	@OverridingStatus(group = PACKAGE)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = PACKAGE)
	public final boolean equals(Object o) { return areEqual(this, o, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = PACKAGE, when = When.MAYBE)
	public T clone() { return ICloneable.clone(() -> super.clone(), getLogger()); }

	@Override
	@OverridingStatus(group = PACKAGE)
	public final String toString() { return getToStringString(this, super.toString()); }
}
