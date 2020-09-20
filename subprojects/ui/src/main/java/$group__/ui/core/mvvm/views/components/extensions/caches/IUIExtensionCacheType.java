package $group__.ui.core.mvvm.views.components.extensions.caches;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.utilities.CastUtilities;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.structures.INamespacePrefixedString;
import com.google.common.collect.ImmutableList;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.observers.DisposableObserver;
import sun.misc.Cleaner;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IUIExtensionCacheType<V, I extends IExtensionContainer<INamespacePrefixedString>> {
	static void invalidateChildrenImpl(IUIComponentContainer instance, IUIExtensionCacheType<?, ? super IUIComponent> type) {
		instance.getChildrenView().stream().unordered()
				.forEach(type::invalidate);
	}

	default void invalidate(I instance) { invalidateImpl(instance, getKey()); }

	static void invalidateImpl(IExtensionContainer<INamespacePrefixedString> instance, INamespacePrefixedString key) {
		IUIExtensionCache.TYPE.getValue().get(instance)
				.ifPresent(c ->
						c.getDelegated().invalidate(key));
	}

	INamespacePrefixedString getKey();

	Optional<? extends V> get(I instance);

	class Impl<V, I extends IExtensionContainer<INamespacePrefixedString>> implements IUIExtensionCacheType<V, I> {
		protected final INamespacePrefixedString key;
		protected final BiFunction<? super IUIExtensionCacheType<V, I>, ? super I, ? extends Optional<? extends V>> getter;
		protected final BiConsumer<? super IUIExtensionCacheType<V, I>, ? super I> invalidator;
		protected final List<? extends DisposableObserver<?>> eventListeners;
		protected final Object cleanerRef = new Object();

		@SuppressWarnings("ThisEscapedInObjectConstruction")
		public Impl(INamespacePrefixedString key,
		            BiFunction<? super IUIExtensionCacheType<V, I>, ? super I, ? extends Optional<? extends V>> getter,
		            BiConsumer<? super IUIExtensionCacheType<V, I>, ? super I> invalidator,
		            Function<? super IUIExtensionCacheType<V, I>, ? extends List<? extends DisposableObserver<?>>> eventListenersFunction) {
			this.key = key;
			this.getter = getter;
			this.invalidator = invalidator;
			this.eventListeners = ImmutableList.copyOf(eventListenersFunction.apply(this));

			eventListeners.stream().unordered()
					.map(CastUtilities::<Observer<? super Object>>castUnchecked) // COMMENT we do not care about the event type
					.forEach(UIEventBusEntryPoint.getEventBus()::subscribe);
			@SuppressWarnings("UnnecessaryLocalVariable") List<? extends DisposableObserver<?>> eventListenersRef = eventListeners;
			Cleaner.create(getCleanerRef(), () ->
					eventListenersRef.stream().unordered().forEach(DisposableObserver::dispose));
		}

		protected final Object getCleanerRef() { return cleanerRef; }

		protected List<? extends DisposableObserver<?>> getEventListeners() { return eventListeners; }

		@Override
		public void invalidate(I instance) { getInvalidator().accept(this, instance); }

		@Override
		public Optional<? extends V> get(I instance) { return getGetter().apply(this, instance).map(Function.identity()); }

		@Override
		public INamespacePrefixedString getKey() { return key; }

		protected BiFunction<? super IUIExtensionCacheType<V, I>, ? super I, ? extends Optional<? extends V>> getGetter() { return getter; }

		protected BiConsumer<? super IUIExtensionCacheType<V, I>, ? super I> getInvalidator() { return invalidator; }
	}
}
