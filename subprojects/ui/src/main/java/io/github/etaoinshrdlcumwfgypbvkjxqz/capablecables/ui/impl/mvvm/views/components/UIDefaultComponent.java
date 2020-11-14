package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.annotations.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.annotations.ui.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIDefaultEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches.UIDefaultCacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractComponentHierarchyChangeBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIComponentModifyShapeDescriptorBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ProviderShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods.ImmutableBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

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
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private final BinderObserverSupplierHolder binderObserverSupplierHolder = new BinderObserverSupplierHolder();

	private final DelayedFieldInitializer<ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>>> extensionsInitializer;

	@UIComponentConstructor
	public UIDefaultComponent(UIComponentConstructor.IArguments arguments) {
		this.name = arguments.getName().orElse(null);

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.mappings = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);

		this.shapeDescriptor = new ProviderShapeDescriptor<>(suppressThisEscapedWarning(() -> this), arguments.getShapeDescriptor());

		this.visible = IUIPropertyMappingValue.createBindingField(Boolean.class, true,
				this.mappings.get(getPropertyVisibleLocation()));
		this.active = IUIPropertyMappingValue.createBindingField(Boolean.class, true,
				this.mappings.get(getPropertyActiveLocation()));

		this.extensionsInitializer = new DelayedFieldInitializer<>(field ->
				IExtensionContainer.addExtensionChecked(this, new UIDefaultCacheExtension()));
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

	private final List<IUIComponent> children = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());

	@Override
	public boolean isVisible() { return getVisible().getValue(); }

	protected AtomicBoolean getModifyingShape() { return modifyingShape; }

	public IBindingField<Boolean> getVisible() { return visible; }

	@Override
	public void setVisible(boolean visible) { getVisible().setValue(visible); }

	@Override
	public boolean isModifyingShape() { return getModifyingShape().get(); }

	private OptionalWeakReference<IUIComponent> parent = new OptionalWeakReference<>(null);

	@Override
	public Optional<? extends IUIComponent> getParent() { return parent.getOptional(); }

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
		getBinderObserverSupplierHolder().getBinderObserverSupplier().ifPresent(binderObserverSupplier ->
				BindingUtilities.findAndInitializeBindings(ImmutableList.of(extension), binderObserverSupplier));
		return result;
	}

	protected BinderObserverSupplierHolder getBinderObserverSupplierHolder() {
		return binderObserverSupplierHolder;
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) {
		Optional<IExtension<? extends INamespacePrefixedString, ?>> result = IExtensionContainer.removeExtensionImpl(getExtensions(), key);
		getBinderObserverSupplierHolder().getBinderObserverSupplier().ifPresent(binderObserverSupplier ->
				BindingUtilities.findAndCleanupBindings(result.map(ImmutableList::of).orElseGet(ImmutableList::of), binderObserverSupplier));
		return result;
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return IExtensionContainer.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() {
		return extensionsInitializer.apply(extensions);
	}

	@Override
	public List<IUIComponent> getChildrenView() { return ImmutableList.copyOf(getChildren()); }

	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void onParentChange(@Nullable IUIComponent previous, @Nullable IUIComponent next) {
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

	@Override
	public void transformChildren(AffineTransform transform) {
		Rectangle2D bounds = getShapeDescriptor().getShapeOutput().getBounds2D();
		transform.translate(bounds.getX(), bounds.getY());
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean addChildren(Iterable<? extends IUIComponent> components) {
		return Streams.stream(components)
				.map(component -> !getChildren().contains(component) && addChildAt(getChildren().size(), component))
				.reduce(false, Boolean::logicalOr);
	}

	@Override
	public boolean addChildAt(int index, IUIComponent component) {
		if (equals(component))
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerUIComponent)
							.addKeyValue("index", index).addKeyValue("component", component)
							.addMessages(() -> getResourceBundle().getString("children.add.self"))
							.build()
			);
		if (getChildren().contains(component))
			return moveChildTo(index, component);
		component.getParent()
				.ifPresent(p -> p.removeChildren(ImmutableList.of(component)));
		EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
					getChildren().add(index, component);
					component.onParentChange(null, this);
					getBinderObserverSupplierHolder().getBinderObserverSupplier().ifPresent(binderObserverSupplier ->
							BindingUtilities.initializeBindings(ImmutableList.of(component), binderObserverSupplier));
				},
				new UIAbstractComponentHierarchyChangeBusEvent.Parent(EnumHookStage.PRE, component, null, this),
				new UIAbstractComponentHierarchyChangeBusEvent.Parent(EnumHookStage.POST, component, null, this));
		IUIComponent.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh);
		return true;
	}

	@Override
	public boolean removeChildren(Iterable<? extends IUIComponent> components) {
		@SuppressWarnings("UnstableApiUsage") boolean ret = Streams.stream(components)
				.map(component -> {
					int index = getChildren().indexOf(component);
					if (index != -1) {
						EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
									getChildren().remove(component);
									component.onParentChange(this, null);
									getBinderObserverSupplierHolder().getBinderObserverSupplier().ifPresent(binderObserverSupplier ->
											BindingUtilities.cleanupBindings(ImmutableList.of(component), binderObserverSupplier));
								},
								new UIAbstractComponentHierarchyChangeBusEvent.Parent(EnumHookStage.PRE, component, this, null),
								new UIAbstractComponentHierarchyChangeBusEvent.Parent(EnumHookStage.POST, component, this, null));
						return true;
					}
					return false;
				})
				.reduce(false, Boolean::logicalOr);
		IUIComponent.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh);
		return ret;
	}

	@Override
	public boolean moveChildTo(int index, IUIComponent component) {
		int previous = getChildren().indexOf(component);
		if (previous == index || previous == -1)
			return false;
		getChildren().remove(previous);
		getChildren().add(index, component);
		return true;
	}

	@Override
	public boolean moveChildToTop(IUIComponent component) { return moveChildTo(getChildren().size() - 1, component); }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IUIComponent> getChildren() { return children; }

	protected void setParent(@Nullable IUIComponent parent) { this.parent = new OptionalWeakReference<>(parent); }

	@Override
	public Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIComponent.super.initializeBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setBinderObserverSupplier(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(getActive(), getVisible()));
		BindingUtilities.findAndInitializeBindings(getExtensions().values(), binderObserverSupplier);
		BindingUtilities.initializeBindings(getChildren(), binderObserverSupplier);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIComponent.super.cleanupBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setBinderObserverSupplier(null);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.unbind(getActive(), getVisible()));
		BindingUtilities.findAndCleanupBindings(getExtensions().values(), binderObserverSupplier);
		BindingUtilities.cleanupBindings(getChildren(), binderObserverSupplier);
	}
}
