package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.client.ui.events.bus.UIEventBusEntryPoint;
import $group__.client.ui.events.ui.UIEventTarget;
import $group__.client.ui.mvvm.binding.BindingMethodSource;
import $group__.client.ui.mvvm.core.binding.IBinderAction;
import $group__.client.ui.mvvm.core.binding.IBindingField;
import $group__.client.ui.mvvm.core.binding.IBindingMethod;
import $group__.client.ui.mvvm.core.binding.IHasBinding;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import $group__.client.ui.mvvm.core.views.components.parsers.UIProperty;
import $group__.client.ui.mvvm.core.views.events.IUIEvent;
import $group__.client.ui.mvvm.views.components.extensions.caches.UIExtensionCache;
import $group__.client.ui.mvvm.views.events.bus.EventUIComponent;
import $group__.client.ui.structures.shapes.descriptors.DelegatingShapeDescriptor;
import $group__.client.ui.utilities.BindingUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.MapUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventUtilities;
import $group__.utilities.extensions.IExtensionContainer;
import com.google.common.collect.ImmutableMap;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.Subject;
import io.reactivex.rxjava3.subjects.UnicastSubject;
import net.minecraft.util.ResourceLocation;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIComponent
		extends UIEventTarget
		implements IUIComponent {
	public static final String PROPERTY_VISIBLE = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "component.visible";
	public static final String PROPERTY_ACTIVE = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "component.active";

	public static final ResourceLocation PROPERTY_VISIBLE_LOCATION = new ResourceLocation(PROPERTY_VISIBLE);
	public static final ResourceLocation PROPERTY_ACTIVE_LOCATION = new ResourceLocation(PROPERTY_ACTIVE);

	@Nullable
	protected final String id;
	protected final Map<ResourceLocation, IUIPropertyMappingValue> propertyMapping;
	protected final Subject<IBinderAction> binderNotifierSubject = UnicastSubject.create();
	protected final ConcurrentMap<ResourceLocation, IUIExtension<? extends ResourceLocation, ? super IUIComponent>> extensions = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<ResourceLocation, IBindingMethod.ISource<?>> eventTargetBindingMethods = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	// todo add animation system
	// todo add name
	// todo cache transform
	protected final IShapeDescriptor<?> shapeDescriptor;
	@UIProperty(PROPERTY_VISIBLE)
	protected final IBindingField<Boolean> visible;
	@UIProperty(PROPERTY_ACTIVE)
	protected final IBindingField<Boolean> active;
	protected WeakReference<IUIComponentContainer> parent = new WeakReference<>(null);
	protected final AtomicBoolean modifyingShape = new AtomicBoolean();

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	public UIComponent(IShapeDescriptor<?> shapeDescriptor, Map<ResourceLocation, IUIPropertyMappingValue> propertyMapping) {
		this.shapeDescriptor = new ComponentShapeDescriptor<>(shapeDescriptor);
		this.propertyMapping = new HashMap<>(propertyMapping);

		this.id = Optional.ofNullable(this.propertyMapping.get(PROPERTY_ID_LOCATION))
				.flatMap(IUIPropertyMappingValue::getDefaultValue)
				.map(Node::getNodeValue)
				.orElse(null);

		this.visible = IHasBinding.createBindingField(Boolean.class, this.propertyMapping.get(PROPERTY_VISIBLE_LOCATION), BindingUtilities.Deserializers::deserializeBoolean, true);
		this.active = IHasBinding.createBindingField(Boolean.class, this.propertyMapping.get(PROPERTY_ACTIVE_LOCATION), BindingUtilities.Deserializers::deserializeBoolean, true);

		IExtensionContainer.addExtensionSafe(this, new UIExtensionCache());
	}

	@Override
	public boolean modifyShape(Supplier<? extends Boolean> action) throws ConcurrentModificationException {
		getModifyingShape().compareAndSet(false, true);
		boolean ret = EventUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () ->
						getShapeDescriptor().modify(action),
				new EventUIComponent.ShapeDescriptorModify(EnumEventHookStage.PRE, this),
				new EventUIComponent.ShapeDescriptorModify(EnumEventHookStage.POST, this));
		getModifyingShape().compareAndSet(true, false);
		return ret;
	}

	protected AtomicBoolean getModifyingShape() { return modifyingShape; }

	@Override
	public boolean isModifyingShape() { return getModifyingShape().get(); }

	protected class ComponentShapeDescriptor<S extends Shape>
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
	public Optional<String> getID() { return Optional.ofNullable(id); }

	@Override
	public Map<ResourceLocation, IUIPropertyMappingValue> getPropertyMappingView() { return ImmutableMap.copyOf(getPropertyMapping()); }

	@Override
	public Optional<IUIComponentContainer> getParent() { return Optional.ofNullable(parent.get()); }

	@Override
	public IShapeDescriptor<?> getShapeDescriptor() { return shapeDescriptor; }

	@Override
	public boolean isVisible() { return getVisible().getValue(); }

	public IBindingField<Boolean> getVisible() { return visible; }

	@Override
	public void setVisible(boolean visible) { getVisible().setValue(visible); }

	@Override
	public void onIndexMove(int previous, int next) {}

	@Override
	public void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next) { setParent(next); }

	protected void setParent(@Nullable IUIComponentContainer parent) { this.parent = new WeakReference<>(parent); }

	@Override
	public Optional<IUIExtension<? extends ResourceLocation, ? super IUIComponent>> addExtension(IUIExtension<? extends ResourceLocation, ? super IUIComponent> extension) { return IExtensionContainer.addExtension(this, getExtensions(), extension.getType().getKey(), extension); }

	@Override
	public Optional<IUIExtension<? extends ResourceLocation, ? super IUIComponent>> removeExtension(ResourceLocation key) { return IExtensionContainer.removeExtension(getExtensions(), key); }

	@Override
	public Optional<IUIExtension<? extends ResourceLocation, ? super IUIComponent>> getExtension(ResourceLocation key) { return Optional.ofNullable(getExtensions().get(key)); }

	@Override
	public Map<ResourceLocation, IUIExtension<? extends ResourceLocation, ? super IUIComponent>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<ResourceLocation, IUIExtension<? extends ResourceLocation, ? super IUIComponent>> getExtensions() { return extensions; }

	@Override
	public Consumer<Supplier<? extends Observer<? super IBinderAction>>> getBinderSubscriber() { return s -> getBinderNotifierSubject().subscribe(s.get()); }

	protected Subject<IBinderAction> getBinderNotifierSubject() { return binderNotifierSubject; }

	@Override
	public boolean dispatchEvent(IUIEvent event) {
		boolean ret = super.dispatchEvent(event);
		ResourceLocation type = event.getType();
		CastUtilities.<IBindingMethod.ISource<? extends IUIEvent>>castUnchecked( // COMMENT should match
				Optional.<IBindingMethod.ISource<?>>ofNullable(getEventTargetBindingMethods().get(type))
						.orElseGet(() -> {
							IBindingMethod.ISource<? extends IUIEvent> r = new BindingMethodSource<>(event.getClass(),
									Optional.ofNullable(getPropertyMapping().get(type)).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));
							getEventTargetBindingMethods().put(type, r);
							return r;
						})).invoke(CastUtilities.castUnchecked(event)); // COMMENT should match
		return ret;
	}

	@Override
	public boolean isActive() { return getActive().getValue(); }

	public IBindingField<Boolean> getActive() { return active; }

	@Override
	public void setActive(boolean active) { getActive().setValue(active); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<ResourceLocation, IUIPropertyMappingValue> getPropertyMapping() { return propertyMapping; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<ResourceLocation, IBindingMethod.ISource<?>> getEventTargetBindingMethods() { return eventTargetBindingMethods; }
}
