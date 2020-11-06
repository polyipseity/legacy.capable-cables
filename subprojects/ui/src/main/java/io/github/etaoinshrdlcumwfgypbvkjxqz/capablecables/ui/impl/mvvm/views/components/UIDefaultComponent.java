package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIDefaultEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches.UIDefaultCacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIComponentModifyShapeDescriptorBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ProviderShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods.ImmutableBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class UIDefaultComponent
		extends UIDefaultEventTarget
		implements IUIComponent {
	@NonNls
	public static final String PROPERTY_VISIBLE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.default.visible";
	@NonNls
	public static final String PROPERTY_ACTIVE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.default.active";

	private static final INamespacePrefixedString PROPERTY_VISIBLE_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyVisible());
	private static final INamespacePrefixedString PROPERTY_ACTIVE_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyActive());

	@Nullable
	private final String name;
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private final ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> extensions = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
	private final ConcurrentMap<INamespacePrefixedString, IBindingMethodSource<? extends IUIEvent>> eventTargetBindingMethods = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
	private final IShapeDescriptor<?> shapeDescriptor;
	@UIProperty(PROPERTY_VISIBLE)
	private final IBindingField<Boolean> visible;
	@UIProperty(PROPERTY_ACTIVE)
	private final IBindingField<Boolean> active;
	private final AtomicBoolean modifyingShape = new AtomicBoolean();
	private final List<IUIComponentModifier> modifiers = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());
	private OptionalWeakReference<IUIComponentContainer> parent = new OptionalWeakReference<>(null);
	@Nullable
	private Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier;

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIComponentConstructor
	public UIDefaultComponent(UIComponentConstructor.IArguments arguments) {
		this.name = arguments.getName().orElse(null);

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.mappings = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);

		this.shapeDescriptor = new ProviderShapeDescriptor<>(this, arguments.getShapeDescriptor());

		this.visible = IUIPropertyMappingValue.createBindingField(Boolean.class, true,
				this.mappings.get(getPropertyVisibleLocation()));
		this.active = IUIPropertyMappingValue.createBindingField(Boolean.class, true,
				this.mappings.get(getPropertyActiveLocation()));

		IExtensionContainer.addExtensionChecked(this, new UIDefaultCacheExtension());
	}

	public static INamespacePrefixedString getPropertyVisibleLocation() { return PROPERTY_VISIBLE_LOCATION; }

	public static INamespacePrefixedString getPropertyActiveLocation() { return PROPERTY_ACTIVE_LOCATION; }

	public static String getPropertyVisible() {
		return PROPERTY_VISIBLE;
	}

	public static String getPropertyActive() {
		return PROPERTY_ACTIVE;
	}

	@Override
	public Optional<? extends String> getName() { return Optional.ofNullable(name); }

	@Override
	public boolean modifyShape(BooleanSupplier action) throws ConcurrentModificationException {
		getModifyingShape().compareAndSet(false, true);
		boolean ret = EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () ->
						getShapeDescriptor().modify(action),
				new UIComponentModifyShapeDescriptorBusEvent(EnumHookStage.PRE, this),
				new UIComponentModifyShapeDescriptorBusEvent(EnumHookStage.POST, this));
		getModifyingShape().compareAndSet(true, false);
		return ret;
	}

	@Override
	public Optional<? extends IUIComponentContainer> getParent() { return parent.getOptional(); }

	@Override
	public boolean isVisible() { return getVisible().getValue(); }

	protected AtomicBoolean getModifyingShape() { return modifyingShape; }

	public IBindingField<Boolean> getVisible() { return visible; }

	@Override
	public void setVisible(boolean visible) { getVisible().setValue(visible); }

	@Override
	public boolean isModifyingShape() { return getModifyingShape().get(); }

	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next) {
		// COMMENT no need to concern that the view is not present yet as setting the manager of the view adds all components automatically
		getManager()
				.flatMap(IUIComponentManager::getView)
				.map(IUIView::getNamedTrackers)
				.ifPresent(trackers -> trackers.remove(IUIComponent.class, this));
		setParent(next);
		getManager()
				.flatMap(IUIComponentManager::getView)
				.map(IUIView::getNamedTrackers)
				.ifPresent(trackers -> trackers.add(IUIComponent.class, this));
	}

	protected void setParent(@Nullable IUIComponentContainer parent) { this.parent = new OptionalWeakReference<>(parent); }

	@Override
	public Shape getAbsoluteShape()
			throws IllegalStateException {
		try (IUIComponentContext context = IUIComponent.createContextTo(this)) {
			return IUIComponent.getContextualShape(context, this);
		}
	}

	@Override
	public boolean dispatchEvent(IUIEvent event) {
		boolean ret = super.dispatchEvent(event);
		INamespacePrefixedString type = event.getType();
		@Nullable IBindingMethodSource<? extends IUIEvent> method = getEventTargetBindingMethods().get(type);
		if (method == null) {
			method = new ImmutableBindingMethodSource<>(event.getClass(),
					Optional.ofNullable(getMappings().get(type)).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));
			getEventTargetBindingMethods().put(type, method);
		}
		method.invoke(CastUtilities.castUnchecked(event)); // COMMENT should match
		return ret;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IBindingMethodSource<? extends IUIEvent>> getEventTargetBindingMethods() { return eventTargetBindingMethods; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappings() { return mappings; }

	@Override
	public boolean isActive() { return getActive().getValue(); }

	public IBindingField<Boolean> getActive() { return active; }

	@Override
	public IShapeDescriptor<?> getShapeDescriptor() { return shapeDescriptor; }

	@Override
	public void setActive(boolean active) { getActive().setValue(active); }

	@Override
	public List<? extends IUIComponentModifier> getModifiersView() { return ImmutableList.copyOf(getModifiers()); }

	@Override
	public boolean addModifier(IUIComponentModifier modifier) {
		boolean ret;
		if (modifier.getTargetComponent()
				.map(previousTargetComponent -> previousTargetComponent.removeModifier(modifier))
				.orElse(true)) {
			AssertionUtilities.assertTrue(getModifiers().add(modifier));
			modifier.setTargetComponent(this);
			ret = true;
		} else
			ret = false;
		assertModifierPresence(this, modifier, ret);
		return ret;
	}

	@Override
	public boolean removeModifier(IUIComponentModifier modifier) {
		boolean ret;
		assertModifierUnique(this, modifier);
		if (getModifiers().remove(modifier)) {
			modifier.setTargetComponent(null);
			ret = true;
		} else
			ret = false;
		assertModifierPresence(this, modifier, false);
		return ret;
	}

	@Override
	public boolean moveModifierToTop(IUIComponentModifier modifier) {
		boolean ret;
		assertModifierUnique(this, modifier);
		if (getModifiers().remove(modifier)) {
			AssertionUtilities.assertTrue(getModifiers().add(modifier));
			ret = true;
		} else
			ret = false;
		assertModifierPresence(this, modifier, ret);
		return ret;
	}

	@Override
	public void initialize(IUIComponentContext context) {}

	@Override
	public void removed(IUIComponentContext context) {}

	@Override
	public boolean containsPoint(IUIComponentContext context, Point2D point) {
		return IUIComponent.getContextualShape(context, this).contains(point);
	}

	protected static void assertModifierUnique(UIDefaultComponent self, IUIComponentModifier modifier) {
		assert self.getModifiers().indexOf(modifier) == self.getModifiers().lastIndexOf(modifier);
	}

	protected static void assertModifierPresence(UIDefaultComponent self, IUIComponentModifier modifier, boolean present) {
		assert self.getModifiers().contains(modifier) == present;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IUIComponentModifier> getModifiers() { return modifiers; }

	@Override
	@Deprecated
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> addExtension(IExtension<? extends INamespacePrefixedString, ?> extension) {
		UIExtensionRegistry.getInstance().checkExtensionRegistered(extension);
		Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> result = IExtensionContainer.addExtensionImpl(this, getExtensions(), extension);
		getBinderObserverSupplier().ifPresent(binderObserverSupplier ->
				BindingUtilities.findAndInitializeBindings(ImmutableList.of(extension), binderObserverSupplier));
		return result;
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) {
		Optional<IExtension<? extends INamespacePrefixedString, ?>> result = IExtensionContainer.removeExtensionImpl(getExtensions(), key);
		getBinderObserverSupplier().ifPresent(binderObserverSupplier ->
				BindingUtilities.findAndCleanupBindings(result.map(ImmutableList::of).orElseGet(ImmutableList::of), binderObserverSupplier));
		return result;
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return IExtensionContainer.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() { return extensions; }

	protected Optional<? extends Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>>> getBinderObserverSupplier() { return Optional.ofNullable(binderObserverSupplier); }

	protected void setBinderObserverSupplier(@Nullable Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) { this.binderObserverSupplier = binderObserverSupplier; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIComponent.super.initializeBindings(binderObserverSupplier);
		setBinderObserverSupplier(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(getActive(), getVisible()));
		BindingUtilities.findAndInitializeBindings(getExtensions().values(), binderObserverSupplier);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIComponent.super.cleanupBindings(binderObserverSupplier);
		setBinderObserverSupplier(null);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.unbind(getActive(), getVisible()));
		BindingUtilities.findAndCleanupBindings(getExtensions().values(), binderObserverSupplier);
	}

	@Override
	public Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }


}
