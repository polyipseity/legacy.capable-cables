package $group__.client.ui.mvvm.binding;

import $group__.client.ui.mvvm.core.binding.IBinder;
import $group__.client.ui.mvvm.core.binding.IBindingField;
import $group__.client.ui.mvvm.core.binding.IBindingMethod;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.specific.MapUtilities;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraft.util.ResourceLocation;
import sun.misc.Cleaner;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Binder implements IBinder {
	protected final ConcurrentMap<ResourceLocation, FieldBinding<?>> fieldBindings = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<ResourceLocation, MethodBinding<?>> methodBindings = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap();

	@Override
	public boolean bindFields(Iterable<IBindingField<?>> fields) {
		final boolean[] ret = {false};
		IBinder.sortAndTrimBindings(fields).asMap().forEach((s, fs) -> ret[0] |= getFieldBinding(s).add(fs));
		return ret[0];
	}

	@Override
	public boolean bindMethods(Iterable<IBindingMethod<?>> methods) {
		final boolean[] ret = {false};
		IBinder.sortAndTrimBindings(methods).asMap().forEach((s, ms) -> ret[0] |= getMethodBinding(s).add(ms));
		return ret[0];
	}

	@Override
	public boolean unbindFields(Iterable<IBindingField<?>> fields) {
		final boolean[] ret = {false};
		IBinder.sortAndTrimBindings(fields).asMap().forEach((s, fs) -> {
			FieldBinding<?> b = getFieldBinding(s);
			ret[0] |= b.remove(fs);
			if (b.isEmpty())
				getFieldBindings().remove(b.getBindingKey());
		});
		return ret[0];
	}

	@Override
	public boolean unbindMethods(Iterable<IBindingMethod<?>> methods) {
		final boolean[] ret = {false};
		IBinder.sortAndTrimBindings(methods).asMap().forEach((s, ms) -> {
			MethodBinding<?> b = getMethodBinding(s);
			ret[0] |= b.remove(ms);
			if (b.isEmpty())
				getMethodBindings().remove(b.getBindingKey());
		});
		return ret[0];
	}

	@Override
	public boolean unbindAllFields() {
		final boolean[] ret = {false};
		getFieldBindings().forEach((s, b) -> ret[0] |= b.removeAll());
		getFieldBindings().clear();
		return ret[0];
	}

	@Override
	public boolean unbindAllMethods() {
		final boolean[] ret = {false};
		getMethodBindings().forEach((s, b) -> ret[0] |= b.removeAll());
		getMethodBindings().clear();
		return ret[0];
	}

	protected MethodBinding<?> getMethodBinding(ResourceLocation bindingKey) { return getMethodBindings().computeIfAbsent(bindingKey, MethodBinding::new); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<ResourceLocation, MethodBinding<?>> getMethodBindings() { return methodBindings; }

	protected FieldBinding<?> getFieldBinding(ResourceLocation bindingKey) { return getFieldBindings().computeIfAbsent(bindingKey, FieldBinding::new); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<ResourceLocation, FieldBinding<?>> getFieldBindings() { return fieldBindings; }

	protected static class FieldBinding<T> {
		protected final ResourceLocation bindingKey;
		protected final Map<IBindingField<T>, Disposable> fields = new HashMap<>(CapacityUtilities.INITIAL_CAPACITY_TINY);
		protected final AtomicBoolean isSource = new AtomicBoolean();

		public FieldBinding(ResourceLocation bindingKey) {
			this.bindingKey = bindingKey;
			{
				final Map<IBindingField<T>, Disposable> fieldsCopy = getFields();
				// TODO see if the lambda will have an implicit ref to this
				Cleaner.create(this, () -> fieldsCopy.values().forEach(Disposable::dispose));
			}
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		public Map<IBindingField<T>, Disposable> getFields() { return fields; }

		public boolean add(Iterable<IBindingField<?>> fields) {
			final boolean[] ret = {false};
			IBinder.<T, IBindingField<T>, IBindingField<?>>checkAndCastBindings(getBindingKey(),
					getFields().keySet().stream().findAny().map(IHasGenericClass::getGenericClass).orElse(null),
					fields)
					.forEach(f -> {
						if (!getFields().containsKey(f)) {
							DisposableObserver<T> d = IBindingField.createSynchronizationObserver(f, getFields().keySet(), getIsSource());
							getFields().put(f, d);
							f.getField().getNotifier().subscribe(d);
							ret[0] = true;
						}
					});
			return ret[0];
		}

		public ResourceLocation getBindingKey() { return bindingKey; }

		public AtomicBoolean getIsSource() { return isSource; }

		public boolean remove(Iterable<IBindingField<?>> fields) {
			final boolean[] ret = {false};
			IBinder.<T, IBindingField<T>, IBindingField<?>>checkAndCastBindings(getBindingKey(),
					getFields().keySet().stream().findAny().map(IHasGenericClass::getGenericClass).orElse(null),
					fields)
					.forEach(f -> {
						if (getFields().containsKey(f)) {
							ret[0] |= Optional.ofNullable(getFields().remove(f)).filter(d -> {
								d.dispose();
								return true;
							}).isPresent();
						}
					});
			return ret[0];
		}

		public boolean removeAll() {
			if (isEmpty())
				return false;
			getFields().values().forEach(Disposable::dispose);
			getFields().clear();
			return true;
		}

		public boolean isEmpty() { return getFields().isEmpty(); }
	}

	protected static class MethodBinding<T> {
		protected final ResourceLocation bindingKey;
		protected final Map<IBindingMethod.ISource<T>, Disposable> sources = new HashMap<>(CapacityUtilities.INITIAL_CAPACITY_TINY);
		protected final Set<IBindingMethod.IDestination<T>> destinations = new HashSet<>(CapacityUtilities.INITIAL_CAPACITY_TINY);

		public MethodBinding(ResourceLocation bindingKey) {
			this.bindingKey = bindingKey;
			{
				Map<IBindingMethod.ISource<T>, Disposable> sourcesCopy = getSources();
				// TODO see if the lambda will have an implicit ref to this
				Cleaner.create(this, () -> sourcesCopy.values().forEach(Disposable::dispose));
			}
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Map<IBindingMethod.ISource<T>, Disposable> getSources() { return sources; }

		@SuppressWarnings("SuspiciousMethodCalls")
		public boolean add(Iterable<IBindingMethod<?>> methods) {
			final boolean[] ret = {false};
			IBinder.<T, IBindingMethod<T>, IBindingMethod<?>>checkAndCastBindings(getBindingKey(),
					getSources().keySet().stream().findAny().map(IHasGenericClass::getGenericClass).orElse(null),
					methods)
					.forEach(m -> {
						switch (m.getType()) {
							case SOURCE:
								if (!getSources().containsKey(m)) {
									DisposableObserver<T> d = IBindingMethod.ISource.createDelegatingObserver(getDestinations());
									IBindingMethod.ISource<T> s = (IBindingMethod.ISource<T>) m;
									s.getNotifier().subscribe(d);
									getSources().put(s, d);
									ret[0] = true;
								}
								break;
							case DESTINATION:
								ret[0] |= getDestinations().add((IBindingMethod.IDestination<T>) m);
								break;
							default:
								throw new InternalError();
						}
					});
			return ret[0];
		}

		public ResourceLocation getBindingKey() { return bindingKey; }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Set<IBindingMethod.IDestination<T>> getDestinations() { return destinations; }

		@SuppressWarnings("SuspiciousMethodCalls")
		public boolean remove(Iterable<IBindingMethod<?>> methods) {
			final boolean[] ret = {false};
			IBinder.<T, IBindingMethod<T>, IBindingMethod<?>>checkAndCastBindings(getBindingKey(),
					getSources().keySet().stream().findAny().map(IHasGenericClass::getGenericClass).orElse(null),
					methods)
					.forEach(m -> {
						switch (m.getType()) {
							case SOURCE:
								ret[0] |= Optional.ofNullable(getSources().remove(m)).filter(d -> {
									d.dispose();
									return true;
								}).isPresent();
								break;
							case DESTINATION:
								ret[0] |= getDestinations().remove(m);
								break;
							default:
								throw new InternalError();
						}
					});
			return ret[0];
		}

		public boolean removeAll() {
			if (isEmpty())
				return false;
			getSources().values().forEach(Disposable::dispose);
			getSources().clear();
			getDestinations().clear();
			return true;
		}

		public boolean isEmpty() { return getSources().isEmpty() && getDestinations().isEmpty(); }
	}
}
