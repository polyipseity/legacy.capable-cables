package $group__.ui.mvvm.views.components;

import $group__.ui.core.binding.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.events.IUIEvent;
import $group__.ui.core.parsers.binding.UIProperty;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.events.ui.UIEventTarget;
import $group__.ui.mvvm.views.components.extensions.caches.UIExtensionCache;
import $group__.ui.mvvm.views.events.bus.EventUIComponent;
import $group__.ui.structures.shapes.descriptors.DelegatingShapeDescriptor;
import $group__.utilities.CastUtilities;
import $group__.utilities.binding.core.IBinderAction;
import $group__.utilities.binding.core.fields.IBindingField;
import $group__.utilities.binding.core.fields.IField;
import $group__.utilities.binding.core.methods.IBindingMethodSource;
import $group__.utilities.binding.methods.BindingMethodSource;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventBusUtilities;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.Subject;
import io.reactivex.rxjava3.subjects.UnicastSubject;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIComponent
		extends UIEventTarget
		implements IUIComponent {
	@NonNls
	public static final String PROPERTY_VISIBLE = INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "component.visible";
	@NonNls
	public static final String PROPERTY_ACTIVE = INamespacePrefixedString.StaticHolder.getDefaultPrefix() + "component.active";

	public static final INamespacePrefixedString PROPERTY_VISIBLE_LOCATION = new ImmutableNamespacePrefixedString(PROPERTY_VISIBLE);
	public static final INamespacePrefixedString PROPERTY_ACTIVE_LOCATION = new ImmutableNamespacePrefixedString(PROPERTY_ACTIVE);

	@Nullable
	protected final String id;
	protected final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	protected final Subject<IBinderAction> binderNotifierSubject = UnicastSubject.create();
	protected final ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> extensions = MapUtilities.newMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<INamespacePrefixedString, IBindingMethodSource<? extends IUIEvent>> eventTargetBindingMethods = MapUtilities.newMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	// todo add animation system
	// todo cache transform
	protected final IShapeDescriptor<?> shapeDescriptor;
	@UIProperty(PROPERTY_VISIBLE)
	protected final IBindingField<Boolean> visible;
	@UIProperty(PROPERTY_ACTIVE)
	protected final IBindingField<Boolean> active;
	protected WeakReference<IUIComponentContainer> parent = new WeakReference<>(null);
	protected final AtomicBoolean modifyingShape = new AtomicBoolean();

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIComponentConstructor(type = UIComponentConstructor.EnumConstructorType.MAPPINGS__ID__SHAPE_DESCRIPTOR)
	public UIComponent(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, @Nullable String id, IShapeDescriptor<?> shapeDescriptor) {
		this.mappings = MapUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);
		this.id = id;
		this.shapeDescriptor = new ComponentShapeDescriptor<>(shapeDescriptor);

		this.visible = IUIPropertyMappingValue.createBindingField(Boolean.class, false, true,
				this.mappings.get(PROPERTY_VISIBLE_LOCATION));
		this.active = IUIPropertyMappingValue.createBindingField(Boolean.class, false, true,
				this.mappings.get(PROPERTY_ACTIVE_LOCATION));

		StaticHolder.addExtensionChecked(this, new UIExtensionCache());
	}

	@Override
	public boolean modifyShape(Supplier<? extends Boolean> action) throws ConcurrentModificationException {
		getModifyingShape().compareAndSet(false, true);
		boolean ret = EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () ->
						getShapeDescriptor().modify(action),
				new EventUIComponent.ShapeDescriptorModify(EnumEventHookStage.PRE, this),
				new EventUIComponent.ShapeDescriptorModify(EnumEventHookStage.POST, this));
		getModifyingShape().compareAndSet(true, false);
		return ret;
	}

	protected AtomicBoolean getModifyingShape() { return modifyingShape; }

	@Override
	public boolean isModifyingShape() { return getModifyingShape().get(); }

	public class ComponentShapeDescriptor<S extends Shape>
			extends DelegatingShapeDescriptor<S> {
		protected ComponentShapeDescriptor(IShapeDescriptor<S> delegated) { super(delegated); }

		@Override
		public boolean modify(Supplier<? extends Boolean> action)
				throws ConcurrentModificationException {
			if (!getModifyingShape().get())
				throw new IllegalStateException("Use 'IShapeDescriptorProvider.modifyShape' instead");
			return super.modify(action);
		}
	}

	@Override
	public Optional<? extends String> getID() { return Optional.ofNullable(id); }

	@Override
	public Optional<? extends IUIComponentContainer> getParent() { return Optional.ofNullable(parent.get()); }

	@Override
	public boolean dispatchEvent(IUIEvent event) {
		boolean ret = super.dispatchEvent(event);
		INamespacePrefixedString type = event.getType();
		@Nullable IBindingMethodSource<? extends IUIEvent> method = getEventTargetBindingMethods().get(type);
		if (method == null) {
			method = new BindingMethodSource<>(event.getClass(),
					Optional.ofNullable(getMappings().get(type)).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));
			getEventTargetBindingMethods().put(type, method);
		}
		method.invoke(CastUtilities.castUnchecked(event)); // COMMENT should match
		return ret;
	}

	@Override
	public IShapeDescriptor<?> getShapeDescriptor() { return shapeDescriptor; }

	@Override
	public boolean isVisible() { return IField.StaticHolder.getValueNonnull(getVisible()); }

	public IBindingField<Boolean> getVisible() { return visible; }

	@Override
	public void setVisible(boolean visible) { getVisible().setValue(visible); }

	@Override
	public void onIndexMove(int previous, int next) {}

	@Override
	public void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next) { setParent(next); }

	protected void setParent(@Nullable IUIComponentContainer parent) { this.parent = new WeakReference<>(parent); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IBindingMethodSource<? extends IUIEvent>> getEventTargetBindingMethods() { return eventTargetBindingMethods; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappings() { return mappings; }

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> addExtension(IExtension<? extends INamespacePrefixedString, ?> extension) {
		return StaticHolder.addExtensionImpl(this, getExtensions(), extension.getType().getKey(), extension);
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) { return StaticHolder.removeExtensionImpl(getExtensions(), key); }

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return Optional.ofNullable(getExtensions().get(key)); }

	@Override
	public Iterable<? extends ObservableSource<IBinderAction>> getBinderNotifiers() { return Iterables.concat(ImmutableList.of(getBinderNotifierSubject()), IUIComponent.super.getBinderNotifiers()); }

	protected Subject<IBinderAction> getBinderNotifierSubject() { return binderNotifierSubject; }

	@Override
	public boolean isActive() { return IField.StaticHolder.getValueNonnull(getActive()); }

	public IBindingField<Boolean> getActive() { return active; }

	@Override
	public void setActive(boolean active) { getActive().setValue(active); }

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() { return extensions; }

	@Override
	public Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }
}
