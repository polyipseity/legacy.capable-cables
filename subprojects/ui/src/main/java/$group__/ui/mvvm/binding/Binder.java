package $group__.ui.mvvm.binding;

import $group__.ui.core.mvvm.binding.BindingTransformerNotFoundException;
import $group__.ui.core.mvvm.binding.IBinder;
import $group__.ui.core.mvvm.binding.IBindingField;
import $group__.ui.core.mvvm.binding.IBindingMethod;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.MapUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.interfaces.IValue;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import sun.misc.Cleaner;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class Binder implements IBinder {
	protected final ConcurrentMap<INamespacePrefixedString, FieldBinding> fieldBindings = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<INamespacePrefixedString, MethodBinding> methodBindings = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> fieldTransformers = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> methodTransformers = MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap();

	@Override
	public boolean bindFields(Iterable<? extends IBindingField<?>> fields)
			throws BindingTransformerNotFoundException {
		final boolean[] ret = {false};
		IBinder.sortAndTrimBindings(fields).asMap().forEach((s, fs) -> ret[0] |= getFieldBinding(s).add(fs));
		return ret[0];
	}

	@Override
	public boolean bindMethods(Iterable<? extends IBindingMethod<?>> methods) {
		final boolean[] ret = {false};
		IBinder.sortAndTrimBindings(methods).asMap().forEach((s, ms) -> ret[0] |= getMethodBinding(s).add(ms));
		return ret[0];
	}

	@Override
	public boolean unbindFields(Iterable<? extends IBindingField<?>> fields) {
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
	public boolean unbindMethods(Iterable<? extends IBindingMethod<?>> methods) {
		final boolean[] ret = {false};
		IBinder.sortAndTrimBindings(methods).asMap().forEach((s, ms) -> {
			MethodBinding b = getMethodBinding(s);
			ret[0] |= b.remove(ms);
			if (b.isEmpty())
				getMethodBindings().remove(b.getBindingKey());
		});
		return ret[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, R> Optional<? extends Function<T, R>> addFieldTransformer(Function<T, R> transformer) {
		Optional<Class<?>>[] types = IBinder.resolveFunctionTypes(transformer);
		if (!types[0].isPresent() || !types[1].isPresent())
			return Optional.of(transformer);
		return IBinder.putTransformer(getFieldTransformers(), (Class<T>) types[0].get(), (Class<R>) types[1].get(), transformer); // COMMENT should be of correct types
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, R> Optional<? extends Function<T, R>> addMethodTransformer(Function<T, R> transformer) {
		Optional<Class<?>>[] types = IBinder.resolveFunctionTypes(transformer);
		if (!types[0].isPresent() || !types[1].isPresent())
			return Optional.of(transformer);
		return IBinder.putTransformer(getMethodTransformers(), (Class<T>) types[0].get(), (Class<R>) types[1].get(), transformer); // COMMENT should be of correct types
	}

	@Override
	public <T, R> Optional<? extends Function<T, R>> removeFieldTransformer(Class<T> from, Class<R> to) { return IBinder.removeTransformer(getFieldTransformers(), from, to); }

	@Override
	public <T, R> Optional<? extends Function<T, R>> removeMethodTransformer(Class<T> from, Class<R> to) { return IBinder.removeTransformer(getMethodTransformers(), from, to); }

	protected MethodBinding getMethodBinding(INamespacePrefixedString bindingKey) { return getMethodBindings().computeIfAbsent(bindingKey, k -> new MethodBinding(k, getMethodTransformers())); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, MethodBinding> getMethodBindings() { return methodBindings; }

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

	protected FieldBinding getFieldBinding(INamespacePrefixedString bindingKey) { return getFieldBindings().computeIfAbsent(bindingKey, k -> new FieldBinding(k, getFieldTransformers())); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, FieldBinding> getFieldBindings() { return fieldBindings; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> getFieldTransformers() { return fieldTransformers; }

	protected static class FieldBinding {
		protected final INamespacePrefixedString bindingKey;
		protected final Map<IBindingField<?>, Disposable> fields = new HashMap<>(CapacityUtilities.INITIAL_CAPACITY_TINY);
		protected final ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> transformers;
		protected final AtomicBoolean isSource = new AtomicBoolean(true);
		protected final Object cleanerRef = new Object();

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		public FieldBinding(INamespacePrefixedString bindingKey, ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> transformers) {
			this.bindingKey = bindingKey;
			this.transformers = transformers; // COMMENT intended to be modified
			@SuppressWarnings("UnnecessaryLocalVariable") Map<IBindingField<?>, Disposable> fieldsRef = fields;
			Cleaner.create(getCleanerRef(), () ->
					fieldsRef.values().forEach(Disposable::dispose));
		}

		protected final Object getCleanerRef() { return cleanerRef; }

		public boolean add(Iterable<? extends IBindingField<?>> fields)
				throws BindingTransformerNotFoundException {
			final boolean[] ret = {false};
			fields.forEach(f -> {
				if (!getFields().containsKey(f)) {
					getFields().keySet().stream().unordered()
							.findAny()
							.ifPresent(fc ->
									f.setValue(CastUtilities.castUncheckedNullable( // COMMENT should be of the right type
											IBinder.transform(getTransformers(),
													CastUtilities.castUncheckedNullable(fc.getValue().orElse(null)), // COMMENT should be always safe
													fc.getGenericClass(),
													f.getGenericClass()))));
					DisposableObserver<? extends IValue<?>> d = IBindingField.createSynchronizationObserver(f, getFields().keySet(), getTransformers(), getIsSource());
					getFields().put(f, d);
					f.getField().getNotifier().subscribe(CastUtilities.castUnchecked(d)); // COMMENT should be of the same type
					ret[0] = true;
				}
			});
			return ret[0];
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		public Map<IBindingField<?>, Disposable> getFields() { return fields; }

		public INamespacePrefixedString getBindingKey() { return bindingKey; }

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

		public boolean remove(Iterable<? extends IBindingField<?>> fields) {
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
		protected final INamespacePrefixedString bindingKey;
		protected final Map<IBindingMethod.Source<?>, Disposable> sources = new HashMap<>(CapacityUtilities.INITIAL_CAPACITY_TINY);
		protected final Set<IBindingMethod.Destination<?>> destinations = new HashSet<>(CapacityUtilities.INITIAL_CAPACITY_TINY);
		protected final ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> transformers;
		protected final Object cleanerRef = new Object();

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		public MethodBinding(INamespacePrefixedString bindingKey, ConcurrentMap<Class<?>, Map<Class<?>, Function<?, ?>>> transformers) {
			this.bindingKey = bindingKey;
			this.transformers = transformers; // COMMENT intended to be modified
			@SuppressWarnings("UnnecessaryLocalVariable") Map<IBindingMethod.Source<?>, Disposable> sourcesRef = sources;
			Cleaner.create(getCleanerRef(), () ->
					sourcesRef.values().forEach(Disposable::dispose));
		}

		protected final Object getCleanerRef() { return cleanerRef; }

		@SuppressWarnings("SuspiciousMethodCalls")
		public boolean add(Iterable<? extends IBindingMethod<?>> methods) {
			final boolean[] ret = {false};

			methods.forEach(m -> {
				switch (m.getType()) {
					case SOURCE:
						if (!getSources().containsKey(m)) {
							IBindingMethod.Source<?> s = (IBindingMethod.Source<?>) m;
							DisposableObserver<?> d = IBindingMethod.Source.createDelegatingObserver(s, getDestinations(), getTransformers());
							s.getNotifier().subscribe(CastUtilities.castUnchecked(d)); // COMMENT should be of the same type
							getSources().put(s, d);
							ret[0] = true;
						}
						break;
					case DESTINATION:
						ret[0] |= getDestinations().add((IBindingMethod.Destination<?>) m);
						break;
					default:
						throw new InternalError();
				}
			});
			return ret[0];
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Map<IBindingMethod.Source<?>, Disposable> getSources() { return sources; }

		public INamespacePrefixedString getBindingKey() { return bindingKey; }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Set<IBindingMethod.Destination<?>> getDestinations() { return destinations; }

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
		public boolean remove(Iterable<? extends IBindingMethod<?>> methods) {
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
