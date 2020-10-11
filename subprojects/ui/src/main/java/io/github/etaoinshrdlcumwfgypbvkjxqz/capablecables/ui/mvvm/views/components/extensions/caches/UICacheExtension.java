package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.caches;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.IUICacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.IUICacheType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.UICacheRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus.UIComponentHierarchyChangedBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CleanerUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ConcurrencyUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheLoaderLoadedNullException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.AutoSubscribingCompositeDisposable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.FunctionalEventBusDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.SubscribeEventObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.registering.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.NonNls;
import sun.misc.Cleaner;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UICacheExtension
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IExtensionContainer<INamespacePrefixedString>, IExtensionContainer<INamespacePrefixedString>>
		implements IUICacheExtension {
	protected final Cache<INamespacePrefixedString, Object> cache =
			CacheBuilder.newBuilder()
					.initialCapacity(INITIAL_CAPACITY_SMALL)
					.expireAfterAccess(CacheUtilities.CACHE_EXPIRATION_ACCESS_DURATION, CacheUtilities.CACHE_EXPIRATION_ACCESS_TIME_UNIT)
					.concurrencyLevel(ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT).build();

	@UIExtensionConstructor(type = UIExtensionConstructor.EnumConstructorType.NO_ARGS)
	public UICacheExtension() {
		super(CastUtilities.castUnchecked(IExtensionContainer.class), CastUtilities.castUnchecked(IExtensionContainer.class)); // COMMENT should not matter in this case
	}

	@Override
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		getDelegated().invalidateAll();
	}

	@Override
	public Cache<INamespacePrefixedString, Object> getDelegated() { return cache; }

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IExtensionContainer<INamespacePrefixedString>> getType() { return IUICacheExtension.TYPE.getValue(); }

	public enum CacheUniversal {
		;

		@SuppressWarnings({"ThisEscapedInObjectConstruction", "unchecked", "RedundantSuppression", "AnonymousInnerClassMayBeStatic"})
		public static final Registry.RegistryObject<IUICacheType<IUIComponentManager<?>, IUIComponent>> MANAGER =
				UICacheRegistry.getInstance().registerApply(generateKey("manager"),
						key -> new AbstractUICacheType<IUIComponentManager<?>, IUIComponent>(key) {
							{
								OptionalWeakReference<? extends IUICacheType<?, IUIComponent>> thisRef =
										new OptionalWeakReference<>(this);
								Cleaner.create(CleanerUtilities.getCleanerReferent(this),
										new AutoSubscribingCompositeDisposable<>(UIEventBusEntryPoint.getEventBus(),
												new LoggingDisposableObserver<UIComponentHierarchyChangedBusEvent.Parent>(
														new FunctionalEventBusDisposableObserver<>(
																new SubscribeEventObject(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional().ifPresent(t ->
																				t.invalidate(event.getComponent()));
																}
														),
														UIConfiguration.getInstance().getLogger()
												) {}
										)::dispose);
							}

							@Override
							protected IUIComponentManager<?> load(IUIComponent container)
									throws CacheLoaderLoadedNullException {
								return IUIComponent.getYoungestParentInstanceOf(container, IUIComponentManager.class)
										.orElseThrow(CacheLoaderLoadedNullException::new);
							}
						});
		@SuppressWarnings({"ThisEscapedInObjectConstruction", "unchecked", "RedundantSuppression", "AnonymousInnerClassMayBeStatic"})
		public static final Registry.RegistryObject<IUICacheType<Integer, IUIComponent>> Z =
				UICacheRegistry.getInstance().registerApply(generateKey("z"),
						key -> new AbstractUICacheType<Integer, IUIComponent>(key) {
							{
								OptionalWeakReference<? extends IUICacheType<?, IUIComponent>> thisRef =
										new OptionalWeakReference<>(this);
								Cleaner.create(CleanerUtilities.getCleanerReferent(this),
										new AutoSubscribingCompositeDisposable<>(UIEventBusEntryPoint.getEventBus(),
												new LoggingDisposableObserver<UIComponentHierarchyChangedBusEvent.Parent>(
														new FunctionalEventBusDisposableObserver<>(
																new SubscribeEventObject(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional().ifPresent(t ->
																				t.invalidate(event.getComponent()));
																}
														),
														UIConfiguration.getInstance().getLogger()
												) {}
										)::dispose);
							}

							@Override
							public void invalidate(IUIComponent container) {
								super.invalidate(container);
								if (container instanceof IUIComponentContainer)
									StaticHolder.invalidateChildrenImpl((IUIComponentContainer) container, this);
							}

							@Override
							protected Integer load(IUIComponent container) {
								// COMMENT 0 counts container already
								return Iterators.size(new IUIComponent.ParentIterator(container.getParent().orElse(null)));
							}
						});

		private static INamespacePrefixedString generateKey(@NonNls String name) { return new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.DEFAULT_NAMESPACE, CacheUniversal.class.getName() + '.' + name); /* TODO make this a utility method perhaps */ }
	}
}
