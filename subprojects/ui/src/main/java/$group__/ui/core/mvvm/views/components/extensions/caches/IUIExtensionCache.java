package $group__.ui.core.mvvm.views.components.extensions.caches;

import $group__.ui.UIConfiguration;
import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.utilities.CastUtilities;
import $group__.utilities.PreconditionUtilities;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.Registry;
import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.observers.DisposableObserver;
import sun.misc.Cleaner;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IUIExtensionCache
		extends IUIExtension<INamespacePrefixedString, IExtensionContainer<INamespacePrefixedString>> {
	INamespacePrefixedString KEY = new NamespacePrefixedString(INamespacePrefixedString.DEFAULT_NAMESPACE, AREA_UI + ".cache");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IUIExtension.IType<INamespacePrefixedString, IUIExtensionCache, IExtensionContainer<INamespacePrefixedString>>> TYPE =
			RegExtension.INSTANCE.registerApply(KEY, k -> new IUIExtension.IType.Impl<>(k, (t, i) -> (Optional<? extends IUIExtensionCache>) i.getExtension(t.getKey())));

	Cache<INamespacePrefixedString, Object> getDelegated();

	interface IType<V, I extends IExtensionContainer<INamespacePrefixedString>> {
		static void invalidateChildrenImpl(IUIComponentContainer instance, IType<?, ? super IUIComponent> type) {
			instance.getChildrenView().stream().unordered()
					.forEach(type::invalidate);
		}

		default void invalidate(I instance) { invalidateImpl(instance, getKey()); }

		static void invalidateImpl(IExtensionContainer<INamespacePrefixedString> instance, INamespacePrefixedString key) {
			IUIExtensionCache.TYPE.getValue().get(instance)
					.ifPresent(c ->
							c.getDelegated().invalidate(key));
		}

		Optional<? extends V> get(I instance);

		INamespacePrefixedString getKey();

		class Impl<V, I extends IExtensionContainer<INamespacePrefixedString>> implements IType<V, I> {
			protected final INamespacePrefixedString key;
			protected final BiFunction<? super IType<V, I>, ? super I, ? extends Optional<? extends V>> getter;
			protected final BiConsumer<? super IType<V, I>, ? super I> invalidator;
			protected final List<? extends DisposableObserver<?>> eventListeners;
			protected final Object cleanerRef = new Object();

			@SuppressWarnings("ThisEscapedInObjectConstruction")
			public Impl(INamespacePrefixedString key,
			            BiFunction<? super IType<V, I>, ? super I, ? extends Optional<? extends V>> getter,
			            BiConsumer<? super IType<V, I>, ? super I> invalidator,
			            Function<? super IType<V, I>, ? extends List<? extends DisposableObserver<?>>> eventListenersFunction) {
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
			public Optional<? extends V> get(I instance) { return getGetter().apply(this, instance).map(Function.identity()); }

			@Override
			public void invalidate(I instance) { getInvalidator().accept(this, instance); }

			@Override
			public INamespacePrefixedString getKey() { return key; }

			protected BiConsumer<? super IType<V, I>, ? super I> getInvalidator() { return invalidator; }

			protected BiFunction<? super IType<V, I>, ? super I, ? extends Optional<? extends V>> getGetter() { return getter; }
		}
	}

	final class RegUICache extends Registry<INamespacePrefixedString, IType<?, ?>> {
		public static final RegUICache INSTANCE = new RegUICache();

		protected RegUICache() {
			super(true, UIConfiguration.INSTANCE.getLogger());
			PreconditionUtilities.requireRunOnceOnly(UIConfiguration.INSTANCE.getLogger());
		}
	}
}
