package $group__.ui.mvvm.views.components.extensions.caches;

import $group__.ui.UIConfiguration;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.extensions.caches.IUICacheExtension;
import $group__.ui.core.mvvm.views.components.extensions.caches.IUICacheType;
import $group__.ui.core.mvvm.views.components.extensions.caches.UICacheRegistry;
import $group__.ui.core.parsers.components.UIExtensionConstructor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.mvvm.views.events.bus.UIComponentHierarchyChangedBusEvent;
import $group__.utilities.CastUtilities;
import $group__.utilities.CleanerUtilities;
import $group__.utilities.ConcurrencyUtilities;
import $group__.utilities.collections.CacheLoaderLoadedNullException;
import $group__.utilities.collections.CacheUtilities;
import $group__.utilities.events.AutoSubscribingCompositeDisposable;
import $group__.utilities.events.FunctionalEventBusDisposableObserver;
import $group__.utilities.events.SubscribeEventObject;
import $group__.utilities.extensions.AbstractContainerAwareExtension;
import $group__.utilities.extensions.core.IExtensionContainer;
import $group__.utilities.extensions.core.IExtensionType;
import $group__.utilities.reactive.LoggingDisposableObserver;
import $group__.utilities.references.OptionalWeakReference;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.structures.Registry;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.NonNls;
import sun.misc.Cleaner;

import java.util.Optional;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

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

		@SuppressWarnings("ThisEscapedInObjectConstruction")
		public static final Registry.RegistryObject<IUICacheType<IUIComponentManager<?>, IUIComponent>> MANAGER =
				UICacheRegistry.getInstance().registerApply(generateKey("manager"),
						key -> new AbstractUICacheType<IUIComponentManager<?>, IUIComponent>(key) {
							{
								OptionalWeakReference<? extends IUICacheType<?, IUIComponent>> thisRef =
										new OptionalWeakReference<>(this);
								Cleaner.create(CleanerUtilities.getCleanerReferent(this),
										new AutoSubscribingCompositeDisposable<>(UIEventBusEntryPoint.getEventBus(),
												new LoggingDisposableObserver<>(
														new FunctionalEventBusDisposableObserver<UIComponentHierarchyChangedBusEvent.Parent>(
																new SubscribeEventObject(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional().ifPresent(t ->
																				t.invalidate(event.getComponent()));
																}
														),
														UIConfiguration.getInstance().getLogger()
												)
										)::dispose);
							}

							@Override
							protected IUIComponentManager<?> load(IUIComponent container)
									throws CacheLoaderLoadedNullException {
								return IUIComponent.getYoungestParentInstanceOf(container, IUIComponentManager.class)
										.orElseThrow(CacheLoaderLoadedNullException::new);
							}
						});
		@SuppressWarnings("ThisEscapedInObjectConstruction")
		public static final Registry.RegistryObject<IUICacheType<Integer, IUIComponent>> Z =
				UICacheRegistry.getInstance().registerApply(generateKey("z"),
						key -> new AbstractUICacheType<Integer, IUIComponent>(key) {
							{
								OptionalWeakReference<? extends IUICacheType<?, IUIComponent>> thisRef =
										new OptionalWeakReference<>(this);
								Cleaner.create(CleanerUtilities.getCleanerReferent(this),
										new AutoSubscribingCompositeDisposable<>(UIEventBusEntryPoint.getEventBus(),
												new LoggingDisposableObserver<>(
														new FunctionalEventBusDisposableObserver<UIComponentHierarchyChangedBusEvent.Parent>(
																new SubscribeEventObject(EventPriority.LOWEST, true),
																event -> {
																	if (event.getStage().isPost())
																		thisRef.getOptional().ifPresent(t ->
																				t.invalidate(event.getComponent()));
																}
														),
														UIConfiguration.getInstance().getLogger()
												)
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
								int ret = -1;
								for (Optional<? extends IUIComponent> parent = Optional.of(container);
								     parent.isPresent();
								     parent = parent.flatMap(IUIComponent::getParent))
									++ret;
								return ret;
							}
						});

		private static INamespacePrefixedString generateKey(@NonNls String name) { return new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.DEFAULT_NAMESPACE, CacheUniversal.class.getName() + '.' + name); /* TODO make this a utility method perhaps */ }
	}
}
