package $group__.client.ui.mvvm.binding;

import $group__.client.ui.mvvm.core.binding.IBinder;
import $group__.client.ui.mvvm.core.binding.IBindingField;
import $group__.client.ui.mvvm.core.binding.IBindingMethod;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.MapUtilities;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraft.util.ResourceLocation;
import sun.misc.Cleaner;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class Binder implements IBinder {
	protected final ConcurrentMap<ResourceLocation, FieldBinding> fieldBindings = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<ResourceLocation, MethodBinding> methodBindings = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> fieldTransformers = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> methodTransformers = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap();

	@Override
	public boolean unbindFields(Iterable<IBindingField<?>> fields) {
		final boolean[] ret = {false};
		IBinder.sortAndTrimBindings(fields).asMap().forEach((s, fs) -> {
			FieldBinding b = getFieldBinding(s);
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
			MethodBinding b = getMethodBinding(s);
			ret[0] |= b.remove(ms);
			if (b.isEmpty())
				getMethodBindings().remove(b.getBindingKey());
		});
		return ret[0];
	}

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

	@SuppressWarnings("unchecked")
	@Override
	public <T, R> Optional<Function<T, R>> addFieldTransformer(Function<T, R> transformer) {
		Optional<Class<?>>[] types = IBinder.resolveFunctionTypes(transformer);
		if (!types[0].isPresent() || !types[1].isPresent())
			return Optional.of(transformer);
		return IBinder.putTransformer(getFieldTransformers(), (Class<T>) types[0].get(), (Class<R>) types[1].get(), transformer); // COMMENT should be of correct types
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, R> Optional<Function<T, R>> addMethodTransformer(Function<T, R> transformer) {
		Optional<Class<?>>[] types = IBinder.resolveFunctionTypes(transformer);
		if (!types[0].isPresent() || !types[1].isPresent())
			return Optional.of(transformer);
		return IBinder.putTransformer(getMethodTransformers(), (Class<T>) types[0].get(), (Class<R>) types[1].get(), transformer); // COMMENT should be of correct types
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, R> Optional<Function<T, R>> removeFieldTransformer(Function<T, R> transformer) {
		Optional<Class<?>>[] types = IBinder.resolveFunctionTypes(transformer);
		if (!types[0].isPresent() || !types[1].isPresent())
			return Optional.of(transformer);
		return IBinder.removeTransformer(getFieldTransformers(), (Class<T>) types[0].get(), (Class<R>) types[1].get()); // COMMENT should be of correct types
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, R> Optional<Function<T, R>> removeMethodTransformer(Function<T, R> transformer) {
		Optional<Class<?>>[] types = IBinder.resolveFunctionTypes(transformer);
		if (!types[0].isPresent() || !types[1].isPresent())
			return Optional.of(transformer);
		return IBinder.removeTransformer(getMethodTransformers(), (Class<T>) types[0].get(), (Class<R>) types[1].get()); // COMMENT should be of correct types
	}

	protected MethodBinding getMethodBinding(ResourceLocation bindingKey) { return getMethodBindings().computeIfAbsent(bindingKey, k -> new MethodBinding(k, getMethodTransformers())); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<ResourceLocation, MethodBinding> getMethodBindings() { return methodBindings; }

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

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> getMethodTransformers() { return methodTransformers; }

	protected FieldBinding getFieldBinding(ResourceLocation bindingKey) { return getFieldBindings().computeIfAbsent(bindingKey, k -> new FieldBinding(k, getFieldTransformers())); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<ResourceLocation, FieldBinding> getFieldBindings() { return fieldBindings; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> getFieldTransformers() { return fieldTransformers; }

	protected static class FieldBinding {
		protected final ResourceLocation bindingKey;
		protected final Map<IBindingField<?>, Disposable> fields = new HashMap<>(CapacityUtilities.INITIAL_CAPACITY_TINY);
		protected final ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> transformers;
		protected final AtomicBoolean isSource = new AtomicBoolean();
		protected final Object cleanerRef = new Object();

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		public FieldBinding(ResourceLocation bindingKey, ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> transformers) {
			this.bindingKey = bindingKey;
			this.transformers = transformers; // COMMENT intended to be modified
			@SuppressWarnings("UnnecessaryLocalVariable") Map<IBindingField<?>, Disposable> fieldsRef = fields;
			Cleaner.create(cleanerRef, () ->
					fieldsRef.values().forEach(Disposable::dispose));
		}

		public boolean add(Iterable<IBindingField<?>> fields) {
			final boolean[] ret = {false};
			fields.forEach(f -> {
				if (!getFields().containsKey(f)) {
					DisposableObserver<?> d = IBindingField.createSynchronizationObserver(f, getFields().keySet(), getTransformers(), getIsSource());
					getFields().put(f, d);
					f.getField().getNotifier().subscribe(CastUtilities.castUnchecked(d)); // COMMENT should be of the same type
					ret[0] = true;
				}
			});
			return ret[0];
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		public Map<IBindingField<?>, Disposable> getFields() { return fields; }

		public ResourceLocation getBindingKey() { return bindingKey; }

		public AtomicBoolean getIsSource() { return isSource; }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> getTransformers() { return transformers; }

		public boolean removeAll() {
			if (isEmpty())
				return false;
			getFields().values().forEach(Disposable::dispose);
			getFields().clear();
			return true;
		}

		public boolean isEmpty() { return getFields().isEmpty(); }

		public boolean remove(Iterable<IBindingField<?>> fields) {
			final boolean[] ret = {false};
			fields.forEach(f -> {
				if (getFields().containsKey(f)) {
					ret[0] |= Optional.ofNullable(getFields().remove(f)).filter(d -> {
						d.dispose();
						return true;
					}).isPresent();
				}
			});
			return ret[0];
		}
	}

	protected static class MethodBinding {
		protected final ResourceLocation bindingKey;
		protected final Map<IBindingMethod.ISource<?>, Disposable> sources = new HashMap<>(CapacityUtilities.INITIAL_CAPACITY_TINY);
		protected final Set<IBindingMethod.IDestination<?>> destinations = new HashSet<>(CapacityUtilities.INITIAL_CAPACITY_TINY);
		protected final ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> transformers;
		protected final Object cleanerRef = new Object();

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		public MethodBinding(ResourceLocation bindingKey, ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> transformers) {
			this.bindingKey = bindingKey;
			this.transformers = transformers; // COMMENT intended to be modified
			@SuppressWarnings("UnnecessaryLocalVariable") Map<IBindingMethod.ISource<?>, Disposable> sourcesRef = sources;
			Cleaner.create(cleanerRef, ()
					-> sourcesRef.values().forEach(Disposable::dispose));
		}

		@SuppressWarnings("SuspiciousMethodCalls")
		public boolean add(Iterable<IBindingMethod<?>> methods) {
			final boolean[] ret = {false};

			methods.forEach(m -> {
				switch (m.getType()) {
					case SOURCE:
						if (!getSources().containsKey(m)) {
							IBindingMethod.ISource<?> s = (IBindingMethod.ISource<?>) m;
							DisposableObserver<?> d = IBindingMethod.ISource.createDelegatingObserver(s, getDestinations(), getTransformers());
							s.getNotifier().subscribe(CastUtilities.castUnchecked(d)); // COMMENT should be of the same type
							getSources().put(s, d);
							ret[0] = true;
						}
						break;
					case DESTINATION:
						ret[0] |= getDestinations().add((IBindingMethod.IDestination<?>) m);
						break;
					default:
						throw new InternalError();
				}
			});
			return ret[0];
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Map<IBindingMethod.ISource<?>, Disposable> getSources() { return sources; }

		public ResourceLocation getBindingKey() { return bindingKey; }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Set<IBindingMethod.IDestination<?>> getDestinations() { return destinations; }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> getTransformers() { return transformers; }

		public boolean removeAll() {
			if (isEmpty())
				return false;
			getSources().values().forEach(Disposable::dispose);
			getSources().clear();
			getDestinations().clear();
			return true;
		}

		public boolean isEmpty() { return getSources().isEmpty() && getDestinations().isEmpty(); }

		@SuppressWarnings("SuspiciousMethodCalls")
		public boolean remove(Iterable<IBindingMethod<?>> methods) {
			final boolean[] ret = {false};
			methods.forEach(m -> {
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
	}
}
