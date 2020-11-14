package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.IUICacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.IUICacheType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.UICacheRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.annotations.ui.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractComponentHierarchyChangeBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheLoaderLoadedNullException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.AutoSubscribingCompositeDisposable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.FunctionalEventBusDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.ImmutableSubscribeEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import net.minecraftforge.eventbus.api.EventPriority;
import sun.misc.Cleaner;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UIDefaultCacheExtension
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IExtensionContainer<INamespacePrefixedString>, IExtensionContainer<INamespacePrefixedString>>
		implements IUICacheExtension {
	private static final UIExtensionConstructor.IArguments DEFAULT_ARGUMENTS = new UIExtensionConstructor.ImmutableArguments(ImmutableMap.of(), IExtensionContainer.class);
	private final Cache<INamespacePrefixedString, Object> cache =
			CacheBuilder.newBuilder()
					.initialCapacity(CapacityUtilities.getInitialCapacitySmall())
					.expireAfterAccess(CacheUtilities.getCacheExpirationAccessDuration(), CacheUtilities.getCacheExpirationAccessTimeUnit())
					.concurrencyLevel(ConcurrencyUtilities.getSingleThreadThreadCount()).build();

	public UIDefaultCacheExtension() { this(getDefaultArguments()); }

	@UIExtensionConstructor
	public UIDefaultCacheExtension(@SuppressWarnings("unused") UIExtensionConstructor.IArguments arguments) {
		super(CastUtilities.castUnchecked(IExtensionContainer.class)); // COMMENT should not matter in this case
	}

	protected static UIExtensionConstructor.IArguments getDefaultArguments() { return DEFAULT_ARGUMENTS; }

	@Override
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		getDelegate().invalidateAll();
	}

	@Override
	public Cache<INamespacePrefixedString, Object> getDelegate() { return cache; }

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IExtensionContainer<INamespacePrefixedString>> getType() { return StaticHolder.getType().getValue(); }

	public enum CacheUniversal {
		;

		@SuppressWarnings({"unchecked", "RedundantSuppression", "AnonymousInnerClassMayBeStatic"})
		private static final IRegistryObject<IUICacheType<IUIComponentManager<?>, IUIComponent>> MANAGER =
				AssertionUtilities.assertNonnull(FunctionUtilities.apply(IUICacheType.generateKey("manager"),
						key -> UICacheRegistry.getInstance().register(key,
								new UIAbstractCacheType<IUIComponentManager<?>, IUIComponent>(key) {
									{
										OptionalWeakReference<? extends IUICacheType<?, IUIComponent>> thisRef =
												new OptionalWeakReference<>(suppressThisEscapedWarning(() -> this));
										Cleaner.create(CleanerUtilities.getCleanerReferent(suppressThisEscapedWarning(() -> this)),
												new AutoSubscribingCompositeDisposable<>(UIEventBusEntryPoint.getEventBus(),
														new LoggingDisposableObserver<UIAbstractComponentHierarchyChangeBusEvent.Parent>(
																new FunctionalEventBusDisposableObserver<>(
																		new ImmutableSubscribeEvent(EventPriority.LOWEST, true),
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
								})));
		@SuppressWarnings({"unchecked", "RedundantSuppression", "AnonymousInnerClassMayBeStatic"})
		private static final IRegistryObject<IUICacheType<Integer, IUIComponent>> Z =
				AssertionUtilities.assertNonnull(FunctionUtilities.apply(IUICacheType.generateKey("z"),
						key -> UICacheRegistry.getInstance().register(key,
								new UIAbstractCacheType<Integer, IUIComponent>(key) {
									{
										OptionalWeakReference<? extends IUICacheType<?, IUIComponent>> thisRef =
												new OptionalWeakReference<>(suppressThisEscapedWarning(() -> this));
										Cleaner.create(CleanerUtilities.getCleanerReferent(suppressThisEscapedWarning(() -> this)),
												new AutoSubscribingCompositeDisposable<>(UIEventBusEntryPoint.getEventBus(),
														new LoggingDisposableObserver<UIAbstractComponentHierarchyChangeBusEvent.Parent>(
																new FunctionalEventBusDisposableObserver<>(
																		new ImmutableSubscribeEvent(EventPriority.LOWEST, true),
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
									IUICacheType.invalidateChildrenImpl((IUIComponentContainer) container, this);
							}

									@Override
									protected Integer load(IUIComponent container) {
										// COMMENT 0 counts container already
										return Iterators.size(new IUIComponent.ParentIterator(container.getParent().orElse(null)));
									}
								})));

		public static IRegistryObject<IUICacheType<IUIComponentManager<?>, IUIComponent>> getManager() {
			return MANAGER;
		}

		public static IRegistryObject<IUICacheType<Integer, IUIComponent>> getZ() {
			return Z;
		}
	}
}
