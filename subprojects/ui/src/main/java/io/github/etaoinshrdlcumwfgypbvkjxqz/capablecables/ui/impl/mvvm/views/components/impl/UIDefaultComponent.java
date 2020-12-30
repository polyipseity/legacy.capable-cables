package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.AlwaysNull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIStructureLifecycleContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.EnumUILifecycleState;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.IUILifecycleStateTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.embed.IUIComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.naming.INamedTrackers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.descriptors.IShapeDescriptor;
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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CollectionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingActionConsumerSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.methods.IBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBindingActionConsumerSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods.ImmutableBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.OptionalUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
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
import java.util.function.Consumer;
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

	private static final IIdentifier PROPERTY_VISIBLE_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyVisible());
	private static final IIdentifier PROPERTY_ACTIVE_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyActive());
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	@Nullable
	private final String name;
	private final Map<IIdentifier, IUIPropertyMappingValue> mappings;
	private final ConcurrentMap<IIdentifier, IExtension<? extends IIdentifier, ?>> extensions = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
	private final ConcurrentMap<IIdentifier, IBindingMethodSource<? extends IUIEvent>> eventTargetBindingMethods = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
	private final IShapeDescriptor<?> shapeDescriptor;
	@UIProperty(PROPERTY_VISIBLE)
	private final IBindingField<Boolean> visible;
	@UIProperty(PROPERTY_ACTIVE)
	private final IBindingField<Boolean> active;
	private final AtomicBoolean modifyingShape = new AtomicBoolean();
	private final List<IUIComponentModifier> modifiers = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());
	private final IBindingActionConsumerSupplierHolder bindingActionConsumerSupplierHolder = new DefaultBindingActionConsumerSupplierHolder();
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

		Map<IIdentifier, ? extends IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.mappings = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);

		this.shapeDescriptor = new ProviderShapeDescriptor<>(suppressThisEscapedWarning(() -> this), arguments.getShapeDescriptor());

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(UIDefaultComponentRenderer.class));

		this.visible = IUIPropertyMappingValue.createBindingField(Boolean.class, ConstantValue.of(suppressBoxing(true)), this.mappings.get(getPropertyVisibleIdentifier()));
		this.active = IUIPropertyMappingValue.createBindingField(Boolean.class, ConstantValue.of(suppressBoxing(true)), this.mappings.get(getPropertyActiveIdentifier()));

		this.extensionsInitializer = new OneUseRunnable(() ->
				IExtensionContainer.addExtensionChecked(this, new UIDefaultCacheExtension()));
	}

	public static IIdentifier getPropertyVisibleIdentifier() { return PROPERTY_VISIBLE_IDENTIFIER; }

	public static IIdentifier getPropertyActiveIdentifier() { return PROPERTY_ACTIVE_IDENTIFIER; }

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
		boolean ret = EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getBusSubscriber()::onNext, () ->
						getShapeDescriptor().modify(action),
				new UIComponentModifyShapeDescriptorBusEvent(EnumHookStage.PRE, this),
				new UIComponentModifyShapeDescriptorBusEvent(EnumHookStage.POST, this));
		getModifyingShape().compareAndSet(true, false);
		return ret;
	}

	@Override
	public IUIComponent getContentComponent() {
		return this;
	}

	@Override
	public Optional<? extends IUIComponent> getParent() { return parent.getOptional(); }

	@Override
	public List<IUIComponent> getChildrenView() { return ImmutableList.copyOf(getChildren()); }

	@SuppressWarnings({"UnstableApiUsage", "rawtypes", "RedundantSuppression"})
	protected static @Immutable Spliterator<IUIComponent> getEmbedComponents(UIDefaultComponent instance) {
		return Streams.stream(instance.getComponentEmbeds()).unordered()
				.<IUIComponent>map(IUIComponentEmbed::getComponent)
				.spliterator();
	}

	public IBindingField<Boolean> getVisible() { return visible; }

	@SuppressWarnings({"AssignmentOrReturnOfFieldWithMutableType", "UnstableApiUsage", "rawtypes", "RedundantSuppression"})
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

	@Override
	public boolean dispatchEvent(IUIEvent event) {
		boolean ret = super.dispatchEvent(event);
		IIdentifier type = event.getType();
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
	public void transformChildren(AffineTransform transform) {
		Rectangle2D bounds = getShapeDescriptor().getShapeOutput().getBounds2D();
		transform.translate(bounds.getX(), bounds.getY());
	}

	@Override
	public boolean isModifyingShape() { return getModifyingShape().get(); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<IIdentifier, IBindingMethodSource<? extends IUIEvent>> getEventTargetBindingMethods() { return eventTargetBindingMethods; }

	protected Iterable<? extends IUIComponentEmbed<?>> getComponentEmbeds() {
		return ImmutableList.of();
	}

	@SuppressWarnings("AutoUnboxing")
	@Override
	public boolean isActive() { return getActive().getValue(); }

	@SuppressWarnings("AutoBoxing")
	@Override
	public void setActive(boolean active) { getActive().setValue(active); }

	@Override
	public boolean addChildren(Iterator<? extends IUIComponent> components) {
		return addChildrenImpl(this, (self, child) -> self.getChildren().size(), components);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static <T extends UIDefaultComponent, C extends IUIComponent> boolean addChildrenImpl(T instance,
	                                                                                             ToIntBiFunction<@Nonnull ? super T, @Nonnull ? super C> indexFunction,
	                                                                                             Iterator<? extends C> components) {
		return stripBool(Streams.stream(components)
				.filter(FunctionUtilities.notPredicate(instance.getChildren()::contains))
				.mapToInt(component -> padBool(instance.addChildAt(indexFunction.applyAsInt(instance, component), component)))
				.reduce(fBool(), PaddedBool::orBool));
	}

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
				.ifPresent(p -> p.removeChildren(Iterators.singletonIterator(component)));
		EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getBusSubscriber()::onNext, () -> {
					getChildren().add(index, component);
					component.onParentChange(null, this);
				},
				new UIAbstractComponentHierarchyChangeBusEvent.Parent(EnumHookStage.PRE, component, null, this),
				new UIAbstractComponentHierarchyChangeBusEvent.Parent(EnumHookStage.POST, component, null, this));
		IUIComponent.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh); // TODO relocation perhaps
		return true;
	}

	@Override
	public boolean removeChildren(Iterator<? extends IUIComponent> components) {
		@SuppressWarnings("UnstableApiUsage") boolean ret = stripBool(
				Streams.stream(components)
						.mapToInt(component -> {
							int index = getChildren().indexOf(component);
							if (index != -1) {
								EventBusUtilities.runWithPrePostHooks(UIEventBusEntryPoint.getBusSubscriber()::onNext, () -> {
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

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<IIdentifier, IUIPropertyMappingValue> getMappings() { return mappings; }

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

	protected IBindingActionConsumerSupplierHolder getBindingActionConsumerSupplierHolder() {
		return bindingActionConsumerSupplierHolder;
	}

	@Override
	public boolean clearChildren() {
		return removeChildren(
				getChildrenView().stream().unordered()
						.filter(FunctionUtilities.notPredicate(ImmutableSet.copyOf(Spliterators.iterator(getEmbedComponents(this)))::contains))
						.iterator()
		);
	}

	@Override
	public Optional<? extends IExtension<? extends IIdentifier, ?>> getExtension(IIdentifier key) { return IExtensionContainer.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<IIdentifier, IExtension<? extends IIdentifier, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<IIdentifier, IExtension<? extends IIdentifier, ?>> getExtensions() {
		extensionsInitializer.run();
		return extensions;
	}

	@Override
	public IUILifecycleStateTracker getLifecycleStateTracker() {
		return lifecycleStateTracker;
	}

	@Override
	public Map<IIdentifier, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

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

	@Override
	@Deprecated
	public Optional<? extends IExtension<? extends IIdentifier, ?>> addExtension(IExtension<? extends IIdentifier, ?> extension) {
		UIExtensionRegistry.getInstance().checkExtensionRegistered(extension);
		Optional<? extends IExtension<? extends IIdentifier, ?>> result = IExtensionContainer.addExtensionImpl(this, getExtensions(), extension);
		getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumer ->
				BindingUtilities.findAndInitializeBindings(bindingActionConsumer, Iterators.singletonIterator(extension)));
		return result;
	}

	@Override
	public Optional<? extends IExtension<? extends IIdentifier, ?>> removeExtension(IIdentifier key) {
		Optional<IExtension<? extends IIdentifier, ?>> result = IExtensionContainer.removeExtensionImpl(getExtensions(), key);
		BindingUtilities.findAndCleanupBindings(CollectionUtilities.iterate(result));
		return result;
	}

	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	protected void bind0(IUIStructureLifecycleContext context) {
		initializeBindings(context.getBindingActionConsumerSupplier());
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
					namedTrackers.add(IUIComponent.class, Iterators.singletonIterator(this));
					namedTrackers.add(IUIRendererContainer.class, rendererContainers.iterator());
					IUIView.getThemeStack(view).apply(rendererContainers.iterator());
				});
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		IUIComponent.super.initializeBindings(bindingActionConsumerSupplier);
		getBindingActionConsumerSupplierHolder().setValue(bindingActionConsumerSupplier);
		BindingUtilities.supplyBindingAction(bindingActionConsumerSupplier,
				() -> ImmutableBindingAction.bind(Iterables.concat(
						ImmutableList.of(getActive(), getVisible()),
						getEmbedBindings()
				)));
		BindingUtilities.initializeBindings(bindingActionConsumerSupplier, Iterators.singletonIterator(getRendererContainerContainer()));
		BindingUtilities.findAndInitializeBindings(bindingActionConsumerSupplier, getExtensions().values().iterator());
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

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		BindingUtilities.findAndCleanupBindings(getExtensions().values().iterator());
		BindingUtilities.cleanupBindings(Iterators.singletonIterator(getRendererContainerContainer()));
		getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumer -> {
			BindingUtilities.supplyBindingAction(bindingActionConsumer,
					() -> ImmutableBindingAction.unbind(Iterables.concat(
							ImmutableList.of(getActive(), getVisible()),
							getEmbedBindings()
					)));
			// COMMENT do not cleanup children, view component should do that via unbind
		});
		getBindingActionConsumerSupplierHolder().setValue(null);
		IUIComponent.super.cleanupBindings();
	}

	@OverridingMethodsMustInvokeSuper
	protected void initialize0(IUIComponentContext context) {
		setLastUpdateTimeInNanoseconds(null);
		setUpdateTimeDelta(null);
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
					namedTrackers.remove(IUIComponent.class, Iterators.singletonIterator(this));
					namedTrackers.remove(IUIRendererContainer.class, rendererContainers.iterator());
					UIDefaultingTheme.applyDefaultRenderers(rendererContainers.iterator());
				});
		cleanupBindings();
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IBinding<?>> getEmbedBindings() {
		return embedBindings;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public final void update(IUIComponentContext context) {
		long currentTimeInNanoseconds = context.getViewContext().getInputDevices().getTicker().read();
		setUpdateTimeDelta(suppressBoxing(
				currentTimeInNanoseconds -
						getLastUpdateTimeInNanoseconds()
								.orElse(currentTimeInNanoseconds) // COMMENT first update after initialize - no delta
		));
		update0(context);
		setLastUpdateTimeInNanoseconds(suppressBoxing(currentTimeInNanoseconds));
	}

	protected OptionalLong getLastUpdateTimeInNanoseconds() {
		return OptionalUtilities.ofLong(lastUpdateTimeInNanoseconds);
	}

	@OverridingMethodsMustInvokeSuper
	protected void update0(@SuppressWarnings("unused") IUIComponentContext context) {}

	protected void setLastUpdateTimeInNanoseconds(@Nullable Long lastUpdateTimeInNanoseconds) {
		this.lastUpdateTimeInNanoseconds = lastUpdateTimeInNanoseconds;
	}

	protected OptionalLong getUpdateTimeDelta() {
		return OptionalUtilities.ofLong(updateTimeDelta);
	}

	protected void setUpdateTimeDelta(@Nullable Long updateTimeDelta) {
		this.updateTimeDelta = updateTimeDelta;
	}
}
