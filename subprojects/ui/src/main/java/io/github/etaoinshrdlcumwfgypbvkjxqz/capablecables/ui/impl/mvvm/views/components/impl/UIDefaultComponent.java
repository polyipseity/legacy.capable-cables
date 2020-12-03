package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.AlwaysNull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIStructureLifecycleContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles.EnumUILifecycleState;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles.IUILifecycleStateTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.embed.IUIComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamedTrackers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIDefaultEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.lifecycles.UIDefaultLifecycleStateTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.lifecycles.UILifecycleUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches.UIDefaultCacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractComponentHierarchyChangeBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIComponentModifyShapeDescriptorBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ProviderShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming.UIDefaultingTheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods.IBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods.ImmutableBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.OptionalUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;
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
import java.util.function.ToIntBiFunction;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressBoxing;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool.*;

public class UIDefaultComponent
		extends UIDefaultEventTarget
		implements IUIComponent {
	@NonNls
	public static final String PROPERTY_VISIBLE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.default.visible";
	@NonNls
	public static final String PROPERTY_ACTIVE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.default.active";

	private static final INamespacePrefixedString PROPERTY_VISIBLE_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyVisible());
	private static final INamespacePrefixedString PROPERTY_ACTIVE_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyActive());
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
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
	private final IBinderObserverSupplierHolder binderObserverSupplierHolder = new DefaultBinderObserverSupplierHolder();
	private final IUILifecycleStateTracker lifecycleStateTracker = new UIDefaultLifecycleStateTracker();
	private final List<IBinding<?>> embedBindings = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());

	private final IUIRendererContainerContainer<IUIComponentRenderer<?>> rendererContainerContainer;

	private final Runnable extensionsInitializer;
	private final List<IUIComponent> children = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());
	private OptionalWeakReference<IUIComponent> parent = OptionalWeakReference.of(null);

	private @Nullable Long lastUpdateTimeInNanoseconds; // COMMENT treat it as nanoseconds even though the ticker may not return the actual time
	private @Nullable Long updateTimeDelta;

	@UIComponentConstructor
	public UIDefaultComponent(IUIComponentArguments arguments) {
		this.name = arguments.getName()
				.filter(name -> {
					if (name.isEmpty()) // COMMENT we are not allowing empty names, reserving it for other purposes
						throw new IllegalArgumentException();
					return true;
				})
				.orElse(null);

		Map<INamespacePrefixedString, ? extends IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.mappings = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);

		this.shapeDescriptor = new ProviderShapeDescriptor<>(suppressThisEscapedWarning(() -> this), arguments.getShapeDescriptor());

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(UIDefaultComponentRenderer.class));

		this.visible = IUIPropertyMappingValue.createBindingField(Boolean.class, ConstantValue.of(suppressBoxing(true)), this.mappings.get(getPropertyVisibleLocation()));
		this.active = IUIPropertyMappingValue.createBindingField(Boolean.class, ConstantValue.of(suppressBoxing(true)), this.mappings.get(getPropertyActiveLocation()));

		this.extensionsInitializer = new OneUseRunnable(() ->
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

	@Override
	public Optional<? extends IUIComponent> getParent() { return parent.getOptional(); }

	@Override
	public List<IUIComponent> getChildrenView() { return ImmutableList.copyOf(getChildren()); }

	@SuppressWarnings("UnstableApiUsage")
	public static <T extends UIDefaultComponent, C extends IUIComponent> boolean addChildrenImpl(T instance,
	                                                                                             ToIntBiFunction<@Nonnull ? super T, @Nonnull ? super C> indexFunction,
	                                                                                             Iterable<? extends C> components) {
		return stripBool(Streams.stream(components)
				.filter(FunctionUtilities.notPredicate(instance.getChildren()::contains))
				.mapToInt(component -> padBool(instance.addChildAt(indexFunction.applyAsInt(instance, component), component)))
				.reduce(fBool(), PaddedBool::orBool));
	}

	public IBindingField<Boolean> getVisible() { return visible; }

	@SuppressWarnings({"AssignmentOrReturnOfFieldWithMutableType", "UnstableApiUsage"})
	protected List<IUIComponent> getChildren() {
		Streams.stream(getComponentEmbeds()) // COMMENT should be sequential
				.map(IUIComponentEmbed::getEmbedInitializer)
				.forEachOrdered(Runnable::run);
		return children;
	}

	@SuppressWarnings("AutoUnboxing")
	@Override
	public boolean isVisible() { return getVisible().getValue(); }

	@SuppressWarnings("AutoBoxing")
	@Override
	public void setVisible(boolean visible) { getVisible().setValue(visible); }

	@Override
	public void onParentChange(@Nullable IUIComponent previous, @Nullable IUIComponent next) {
		setParent(next);
	}

	protected void setParent(@Nullable IUIComponent parent) { this.parent = OptionalWeakReference.of(parent); }

	protected AtomicBoolean getModifyingShape() { return modifyingShape; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IBindingMethodSource<? extends IUIEvent>> getEventTargetBindingMethods() { return eventTargetBindingMethods; }

	@Override
	public void transformChildren(AffineTransform transform) {
		Rectangle2D bounds = getShapeDescriptor().getShapeOutput().getBounds2D();
		transform.translate(bounds.getX(), bounds.getY());
	}

	@Override
	public boolean isModifyingShape() { return getModifyingShape().get(); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappings() { return mappings; }

	@Override
	public boolean addChildren(Iterable<? extends IUIComponent> components) {
		return addChildrenImpl(this, (self, child) -> self.getChildren().size(), components);
	}

	@SuppressWarnings("AutoUnboxing")
	@Override
	public boolean isActive() { return getActive().getValue(); }

	@SuppressWarnings("AutoBoxing")
	@Override
	public void setActive(boolean active) { getActive().setValue(active); }

	@Override
	public boolean addChildAt(int index, IUIComponent component) {
		if (equals(component))
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerUIComponent)
							.addKeyValue("index", suppressBoxing(index)).addKeyValue("component", component)
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
				},
				new UIAbstractComponentHierarchyChangeBusEvent.Parent(EnumHookStage.PRE, component, null, this),
				new UIAbstractComponentHierarchyChangeBusEvent.Parent(EnumHookStage.POST, component, null, this));
		IUIComponent.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh); // TODO relocation perhaps
		return true;
	}

	protected IBinderObserverSupplierHolder getBinderObserverSupplierHolder() {
		return binderObserverSupplierHolder;
	}

	@Override
	public boolean removeChildren(Iterable<? extends IUIComponent> components) {
		@SuppressWarnings("UnstableApiUsage") boolean ret = stripBool(
				Streams.stream(components)
						.mapToInt(component -> {
							int index = getChildren().indexOf(component);
							if (index != -1) {
								EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
											getChildren().remove(component);
											component.onParentChange(this, null);
										},
										new UIAbstractComponentHierarchyChangeBusEvent.Parent(EnumHookStage.PRE, component, this, null),
										new UIAbstractComponentHierarchyChangeBusEvent.Parent(EnumHookStage.POST, component, this, null));
								return tBool();
							}
							return fBool();
						})
						.reduce(fBool(), PaddedBool::orBool)
		);
		IUIComponent.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh); // TODO relocation perhaps
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
	public Shape getAbsoluteShape()
			throws IllegalStateException {
		try (IUIComponentContext context = IUIComponent.createContextTo(this)) {
			return IUIComponent.getContextualShape(context, this);
		}
	}

	public IBindingField<Boolean> getActive() { return active; }

	@Override
	public boolean moveChildToTop(IUIComponent component) { return moveChildTo(getChildren().size() - 1, component); }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public IShapeDescriptor<?> getShapeDescriptor() { return shapeDescriptor; }

	@Override
	public boolean dispatchEvent(IUIEvent event) {
		boolean ret = super.dispatchEvent(event);
		INamespacePrefixedString type = event.getType();
		@Nullable IBindingMethodSource<? extends IUIEvent> method = getEventTargetBindingMethods().get(type);
		if (method == null) {
			method = ImmutableBindingMethodSource.of(event.getClass(),
					Optional.ofNullable(getMappings().get(type)).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));
			getEventTargetBindingMethods().put(type, method);
		}
		method.invoke(CastUtilities.castUnchecked(event)); // COMMENT should match
		return ret;
	}

	@Override
	public List<? extends IUIComponentModifier> getModifiersView() { return ImmutableList.copyOf(getModifiers()); }

	@Override
	public boolean addModifier(IUIComponentModifier modifier) {
		boolean ret;
		if (!modifier.getTargetComponent()
				.filter(previousTargetComponent -> !previousTargetComponent.removeModifier(modifier))
				.isPresent()) {
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
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.findAndInitializeBindings(binderObserverSupplier, ImmutableList.of(extension)));
		return result;
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) {
		Optional<IExtension<? extends INamespacePrefixedString, ?>> result = IExtensionContainer.removeExtensionImpl(getExtensions(), key);
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.findAndCleanupBindings(result.map(ImmutableList::of).orElseGet(ImmutableList::of)));
		return result;
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return IExtensionContainer.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() {
		extensionsInitializer.run();
		return extensions;
	}

	@Override
	public IUILifecycleStateTracker getLifecycleStateTracker() {
		return lifecycleStateTracker;
	}

	@Override
	public Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public final void bind(IUIStructureLifecycleContext context) {
		UILifecycleUtilities.addStateIdempotent(this, EnumUILifecycleState.BOUND, context, true, context1 -> {
			IUIComponent.super.bind(context1);
			bind0(context1);
		});
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	@OverridingMethodsMustInvokeSuper
	public final void unbind(@AlwaysNull @Nullable Void context) {
		UILifecycleUtilities.removeStateIdempotent(this, EnumUILifecycleState.BOUND, context, context1 -> {
			unbind0(context1);
			IUIComponent.super.unbind(context1);
		});
	}

	@OverridingMethodsMustInvokeSuper
	protected void unbind0(@SuppressWarnings("unused") @AlwaysNull @Nullable Void context) {}

	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	protected void bind0(IUIStructureLifecycleContext context) {
		initializeBindings(context.getBinderObserverSupplier());
		getManager()
				.flatMap(IUIComponentManager::getView)
				.ifPresent(view -> {
					@SuppressWarnings("UnstableApiUsage")
					Iterable<? extends IUIRendererContainer<?>> rendererContainers =
							Iterables.concat(ImmutableSet.of(getRendererContainer()),
									getExtensions().values().stream().unordered()
											.filter(IUIRendererContainerContainer.class::isInstance)
											.<IUIRendererContainerContainer<?>>map(IUIRendererContainerContainer.class::cast)
											.map(IUIRendererContainerContainer::getRendererContainer)
											.collect(ImmutableList.toImmutableList()));

					INamedTrackers namedTrackers = IUIView.getNamedTrackers(view);
					namedTrackers.add(IUIComponent.class, this);
					namedTrackers.addAll(IUIRendererContainer.class, rendererContainers);
					IUIView.getThemeStack(view).applyAll(rendererContainers);
				});
	}

	protected Iterable<? extends IUIComponentEmbed<?>> getComponentEmbeds() {
		return ImmutableSet.of();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIComponent.super.initializeBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setValue(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(Iterables.concat(
						ImmutableList.of(getActive(), getVisible()),
						getEmbedBindings()
				)));
		BindingUtilities.initializeBindings(binderObserverSupplier, ImmutableSet.of(getRendererContainerContainer()));
		BindingUtilities.findAndInitializeBindings(binderObserverSupplier, getExtensions().values());
		// COMMENT do not init children, view component should do that via bind
	}

	@Override
	public IUIRendererContainer<? extends IUIComponentRenderer<?>> getRendererContainer() {
		return getRendererContainerContainer().getRendererContainer();
	}

	protected IUIRendererContainerContainer<IUIComponentRenderer<?>> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public final void initialize(IUIComponentContext context) {
		UILifecycleUtilities.addStateIdempotent(this, EnumUILifecycleState.INITIALIZED, context, false, context1 -> {
			IUIComponent.super.initialize(context1);
			initialize0(context1);
		});
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public final void cleanup(IUIComponentContext context) {
		UILifecycleUtilities.removeStateIdempotent(this, EnumUILifecycleState.INITIALIZED, context, context1 -> {
			cleanup0(context1);
			IUIComponent.super.cleanup(context1);
		});
	}

	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	protected void cleanup0(IUIComponentContext context) {
		getManager()
				.flatMap(IUIComponentManager::getView)
				.ifPresent(view -> {
					@SuppressWarnings("UnstableApiUsage")
					Iterable<? extends IUIRendererContainer<?>> rendererContainers =
							Iterables.concat(ImmutableSet.of(getRendererContainer()),
									getExtensions().values().stream().unordered()
											.filter(IUIRendererContainerContainer.class::isInstance)
											.<IUIRendererContainerContainer<?>>map(IUIRendererContainerContainer.class::cast)
											.map(IUIRendererContainerContainer::getRendererContainer)
											.collect(ImmutableList.toImmutableList()));

					INamedTrackers namedTrackers = IUIView.getNamedTrackers(view);
					namedTrackers.remove(IUIComponent.class, this);
					namedTrackers.removeAll(IUIRendererContainer.class, rendererContainers);
					UIDefaultingTheme.applyDefaultRenderers(rendererContainers);
				});
		cleanupBindings();
	}

	@OverridingMethodsMustInvokeSuper
	protected void initialize0(IUIComponentContext context) {
		setLastUpdateTimeInNanoseconds(null);
		setUpdateTimeDelta(null);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier -> {
			BindingUtilities.findAndCleanupBindings(getExtensions().values());
			BindingUtilities.cleanupBindings(ImmutableSet.of(getRendererContainerContainer()));
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
					() -> ImmutableBinderAction.unbind(Iterables.concat(
							ImmutableList.of(getActive(), getVisible()),
							getEmbedBindings()
					)));
			// COMMENT do not cleanup children, view component should do that via unbind
		});
		getBinderObserverSupplierHolder().setValue(null);
		IUIComponent.super.cleanupBindings();
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IBinding<?>> getEmbedBindings() {
		return embedBindings;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void update(ITicker ticker) {
		long currentTimeInNanoseconds = ticker.read();
		setUpdateTimeDelta(suppressBoxing(
				currentTimeInNanoseconds -
						getLastUpdateTimeInNanoseconds()
								.orElse(currentTimeInNanoseconds) // COMMENT first update after initialize - no delta
		));
		setLastUpdateTimeInNanoseconds(suppressBoxing(currentTimeInNanoseconds));
	}

	private OptionalLong getLastUpdateTimeInNanoseconds() {
		return OptionalUtilities.ofLong(lastUpdateTimeInNanoseconds); // COMMENT for internal use ONLY
	}

	private void setLastUpdateTimeInNanoseconds(@Nullable Long lastUpdateTimeInNanoseconds) {
		this.lastUpdateTimeInNanoseconds = lastUpdateTimeInNanoseconds;
	}

	protected OptionalLong getUpdateTimeDelta() {
		return OptionalUtilities.ofLong(updateTimeDelta);
	}

	protected void setUpdateTimeDelta(@Nullable Long updateTimeDelta) {
		this.updateTimeDelta = updateTimeDelta;
	}
}
