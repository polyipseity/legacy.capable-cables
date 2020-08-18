package $group__.client.ui.mvvm.minecraft.components.common;

import $group__.client.ui.core.IShapeDescriptor;
import $group__.client.ui.core.IUIConstraint;
import $group__.client.ui.events.bus.EventBusEntryPoint;
import $group__.client.ui.events.ui.UIEventListener;
import $group__.client.ui.mvvm.core.binding.IBindingField;
import $group__.client.ui.mvvm.core.binding.IHasBinding;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.IUIReshapeExplicitly;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.extensions.caches.IUIExtensionCache;
import $group__.client.ui.mvvm.core.views.components.parsers.UIConstructor;
import $group__.client.ui.mvvm.core.views.components.parsers.UIProperty;
import $group__.client.ui.mvvm.core.views.events.IUIEventFocus;
import $group__.client.ui.mvvm.minecraft.core.views.IUIComponentMinecraft;
import $group__.client.ui.mvvm.views.components.UIComponentContainer;
import $group__.client.ui.mvvm.views.components.extensions.caches.UICacheShapeDescriptor;
import $group__.client.ui.mvvm.views.components.extensions.caches.UIExtensionCache;
import $group__.client.ui.mvvm.views.events.bus.EventUIShapeDescriptor;
import $group__.client.ui.mvvm.views.events.ui.UIEventFocus;
import $group__.client.ui.structures.ShapeDescriptor;
import $group__.client.ui.structures.ShapeDescriptor.Rectangular;
import $group__.client.ui.structures.UIConstraint;
import $group__.client.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.reactive.DisposableObserverAuto;
import $group__.utilities.structures.Registry;
import com.google.common.collect.ImmutableList;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

// TODO responsibility of this classes may need to be delegated to the view model via some means
@OnlyIn(Dist.CLIENT)
public class UIComponentMinecraftWindow
		extends UIComponentContainer
		implements IUIReshapeExplicitly<IShapeDescriptor<? extends RectangularShape>>, IUIComponentMinecraft {
	public static final String PROPERTY_COLOR_BACKGROUND = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "window.colors.background";
	public static final String PROPERTY_COLOR_BORDER = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "window.colors.border";
	// TODO make window scroll bars, maybe create a new component, and embed into this
	// TODO make value not hardcoded through themes
	public static final int
			WINDOW_DRAG_BAR_THICKNESS = 10, // COMMENT internal top
			WINDOW_VISIBLE_MINIMUM = 10;

	@UIProperty(PROPERTY_COLOR_BACKGROUND)
	protected final IBindingField<Color> colorBackground;
	@UIProperty(PROPERTY_COLOR_BORDER)
	protected final IBindingField<Color> colorBorder;

	@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
	@UIConstructor
	public UIComponentMinecraftWindow(Map<String, IUIPropertyMappingValue> propertyMapping) {
		super(propertyMapping);

		this.colorBackground = IHasBinding.createBindingField(Color.class,
				getPropertyMapping().get(PROPERTY_COLOR_BACKGROUND),
				s -> new Color(Integer.decode(s), true), Color.BLACK);
		this.colorBorder = IHasBinding.createBindingField(Color.class,
				getPropertyMapping().get(PROPERTY_COLOR_BORDER),
				s -> new Color(Integer.decode(s), true), Color.WHITE);

		addEventListener(UIEventFocus.TYPE_FOCUS_IN_POST, new UIEventListener.Functional<IUIEventFocus>(e ->
				getParent().orElseThrow(InternalError::new).moveChildToTop(this)), true);
	}

	@Override
	public boolean reshape(Function<? super IShapeDescriptor<? extends RectangularShape>, ? extends Boolean> action) throws ConcurrentModificationException {
		return getShapeDescriptor().modify(() -> action.apply(getShapeDescriptor()));
		// TODO resizing logic
	}

	@Override
	protected ShapeDescriptor.Rectangular<?> createShapeDescriptor() {
		return new Rectangular<Rectangle2D>(getShapePlaceholderView()) {
			@Override
			protected boolean modify0(Supplier<? extends Boolean> action)
					throws ConcurrentModificationException {
				Optional<IUIConstraint> cs = CacheGuiWindow.CONSTRAINT.getValue().get(UIComponentMinecraftWindow.this);
				cs.ifPresent(c -> getConstraints().add(c));
				boolean ret = super.modify0(action);
				cs.ifPresent(c -> getConstraints().remove(c));
				return ret;
			}
		};
	}

	@Override
	public ShapeDescriptor.Rectangular<?> getShapeDescriptor() { return (Rectangular<?>) super.getShapeDescriptor(); }

	protected final AtomicReference<ObserverEventUIShapeDescriptorModify> observerEventUIShapeDescriptorModify = new AtomicReference<>();

	@Override
	public void initialize(IAffineTransformStack stack) {
		EventBusEntryPoint.<EventUIShapeDescriptor.Modify>getEventBus()
				.subscribe(getObserverEventUIShapeDescriptorModify().accumulateAndGet(new ObserverEventUIShapeDescriptorModify(), (p, n) -> {
					Optional.ofNullable(p).ifPresent(DisposableObserver::dispose);
					return n;
				}));
	}

	protected AtomicReference<ObserverEventUIShapeDescriptorModify> getObserverEventUIShapeDescriptorModify() { return observerEventUIShapeDescriptorModify; }

	@Override
	public void render(IAffineTransformStack stack, Point2D cursorPosition, double partialTicks, boolean pre) {
		AffineTransform transform = stack.getDelegated().peek();
		if (pre) {
			DrawingUtilities.drawShape(transform, getShapeDescriptor().getShapeOutput(), true, getColorBackground().getValue(), 0);
			DrawingUtilities.drawShape(transform, getShapeDescriptor().getShapeOutput(), true, getColorBorder().getValue(), 0);
		}
	}

	public IBindingField<Color> getColorBackground() { return colorBackground; }

	public IBindingField<Color> getColorBorder() { return colorBorder; }

	@Override
	public void crop(IAffineTransformStack stack, EnumCropMethod method, boolean push, Point2D mouse, double partialTicks) { IUIComponentMinecraft.crop(this, stack, method, push, mouse, partialTicks); }

	@Override
	public void removed(IAffineTransformStack stack) {
		Optional.ofNullable(getObserverEventUIShapeDescriptorModify().getAndSet(null)).ifPresent(DisposableObserver::dispose);
	}

	@Override
	public void transformChildren(IAffineTransformStack stack) {
		super.transformChildren(stack);
		stack.getDelegated().peek().translate(0, WINDOW_DRAG_BAR_THICKNESS); // TODO move
	}

	@OnlyIn(Dist.CLIENT)
	public enum CacheGuiWindow {
		;

		private static final Logger LOGGER = LogManager.getLogger();

		/*
		TODO move cache
		public static final Registry.RegistryObject<UIExtensionCache.IType<Rectangle2D, IUIComponent>> RECTANGLE_DRAGGABLE =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("rectangle_draggable"),
						k -> new UICacheShapeDescriptor<>(k,
								(t, i) -> IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache ->
										Try.call(() -> cache.getDelegated().get(t.getKey(),
												() -> {
													Rectangle2D bounds = i.getShapeDescriptor().getShapeOutput().getBounds2D();
													return new Rectangle2D.Double(
															bounds.getX(),
															bounds.getY(),
															bounds.getWidth(),
															WINDOW_DRAG_BAR_THICKNESS);
												}), LOGGER).map(CastUtilities::castUnchecked)),
								(t, i) -> IUIExtensionCache.IType.invalidate(i, t.getKey()),
								t -> {
									UICacheShapeDescriptor<Rectangle2D, IUIComponent> tc = (UICacheShapeDescriptor<Rectangle2D, IUIComponent>) t;
									return new Object() {
										@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
										protected void onShapeDescriptorReshape(EventUIShapeDescriptor.Modify event) {
											if (event.getStage() == EnumEventHookStage.POST) {
												UICacheShapeDescriptor.getInstanceFromShapeDescriptor(tc.getInstancesView(), event.getShapeDescriptor())
														.ifPresent(tc::invalidate);
											}
										}
									};
								}));
		public static final Registry.RegistryObject<UIExtensionCache.IType<Rectangle2D, IUIComponent>> RECTANGLE_CLICKABLE =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("rectangle_clickable"),
						k -> new UICacheShapeDescriptor<>(k,
								(t, i) -> IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache -> Try.call(() -> cache.getDelegated().get(t.getKey(),
										() -> UIObjectUtilities.applyRectangular(i.getShapeDescriptor().getShapeOutput().getBounds2D(), (x, y, w, h) ->
												new Rectangle2D.Double(x - WINDOW_RESHAPE_THICKNESS, y - WINDOW_RESHAPE_THICKNESS, w + (WINDOW_RESHAPE_THICKNESS << 1), h + (WINDOW_RESHAPE_THICKNESS << 1)))), LOGGER).map(CastUtilities::castUnchecked)),
								(t, i) -> IUIExtensionCache.IType.invalidate(i, t.getKey()),
								t -> {
									UICacheShapeDescriptor<Rectangle2D, IUIComponent> tc = (UICacheShapeDescriptor<Rectangle2D, IUIComponent>) t;
									return new Object() {
										@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
										protected void onShapeDescriptorReshape(EventUIShapeDescriptor.Modify event) {
											if (event.getStage() == EnumEventHookStage.POST) {
												UICacheShapeDescriptor.getInstanceFromShapeDescriptor(tc.getInstancesView(), event.getShapeDescriptor())
														.ifPresent(tc::invalidate);
											}
										}
									};
								}));

		 */
		public static final Registry.RegistryObject<UIExtensionCache.IType<IUIConstraint, IUIComponent>> CONSTRAINT =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("constraint"),
						k -> new UICacheShapeDescriptor<>(k,
								(t, i) -> IUIExtensionCache.TYPE.getValue().get(i)
										.flatMap(cache -> i.getManager()
												.map(m -> m.getShapeDescriptor().getShapeOutput().getBounds2D())
												.flatMap(mb -> Try.call(() -> cache.getDelegated().get(t.getKey(), () ->
														new UIConstraint(
																new Rectangle2D.Double(0, 0, WINDOW_VISIBLE_MINIMUM, WINDOW_VISIBLE_MINIMUM),
																new Rectangle2D.Double(mb.getMaxX() - WINDOW_VISIBLE_MINIMUM, mb.getMaxY() - WINDOW_VISIBLE_MINIMUM,
																		IUIConstraint.CONSTRAINT_NULL_VALUE, IUIConstraint.CONSTRAINT_NULL_VALUE))), LOGGER)
														.map(CastUtilities::castUnchecked))),
								(t, i) -> IUIExtensionCache.IType.invalidate(i, t.getKey()),
								t -> {
									UICacheShapeDescriptor<IUIConstraint, IUIComponent> tc = (UICacheShapeDescriptor<IUIConstraint, IUIComponent>) t;
									return ImmutableList.of(new DisposableObserverAuto<EventUIShapeDescriptor.Modify>() {
										@Override
										@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
										public void onNext(EventUIShapeDescriptor.Modify event) {
											if (event.getStage() == EnumEventHookStage.POST) {
												UICacheShapeDescriptor.getInstanceFromShapeDescriptor(tc.getInstancesView(), event.getShapeDescriptor())
														.ifPresent(tc::invalidate);
											}
										}
									});
								}));

		private static ResourceLocation generateKey(@SuppressWarnings("SameParameterValue") String name) { return new ResourceLocation(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, "window." + name); }
	}

	protected class ObserverEventUIShapeDescriptorModify
			extends DisposableObserverAuto<EventUIShapeDescriptor.Modify> {
		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(EventUIShapeDescriptor.Modify event) {
			if (event.getStage() == EnumEventHookStage.POST && getParent().filter(p -> p.getShapeDescriptor().equals(event.getShapeDescriptor())).isPresent())
				IUIReshapeExplicitly.refresh(UIComponentMinecraftWindow.this);
		}
	}
}
