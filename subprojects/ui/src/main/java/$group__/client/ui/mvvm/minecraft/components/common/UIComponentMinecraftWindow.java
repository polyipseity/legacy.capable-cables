package $group__.client.ui.mvvm.minecraft.components.common;

import $group__.client.ui.events.bus.EventBusEntryPoint;
import $group__.client.ui.events.ui.UIEventListener;
import $group__.client.ui.mvvm.core.binding.IBindingField;
import $group__.client.ui.mvvm.core.binding.IHasBinding;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.structures.IUIConstraint;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.IUIReshapeExplicitly;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.extensions.caches.IUIExtensionCache;
import $group__.client.ui.mvvm.core.views.components.extensions.caches.UICacheShapeDescriptor;
import $group__.client.ui.mvvm.core.views.components.parsers.UIConstructor;
import $group__.client.ui.mvvm.core.views.components.parsers.UIProperty;
import $group__.client.ui.mvvm.core.views.events.IUIEvent;
import $group__.client.ui.mvvm.core.views.events.IUIEventFocus;
import $group__.client.ui.mvvm.core.views.events.IUIEventListener;
import $group__.client.ui.mvvm.core.views.events.IUIEventMouse;
import $group__.client.ui.mvvm.minecraft.core.views.IUIComponentMinecraft;
import $group__.client.ui.mvvm.structures.*;
import $group__.client.ui.mvvm.structures.ShapeDescriptor.Rectangular;
import $group__.client.ui.mvvm.views.components.UIComponentContainer;
import $group__.client.ui.mvvm.views.components.extensions.UIExtensionCache;
import $group__.client.ui.mvvm.views.events.bus.EventUIShapeDescriptor;
import $group__.client.ui.mvvm.views.events.ui.UIEventFocus;
import $group__.client.ui.mvvm.views.events.ui.UIEventMouse;
import $group__.client.ui.utilities.ReferenceUtilities;
import $group__.client.ui.utilities.UIObjectUtilities;
import $group__.client.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.automata.TransitionSystem;
import $group__.utilities.automata.core.ITransitionSystem;
import $group__.utilities.client.minecraft.GLUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.functions.IFunction4;
import $group__.utilities.interfaces.ICloneable;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.specific.ThrowableUtilities.Try;
import $group__.utilities.structures.Registry;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

// TODO responsibility of this classes may need to be delegated to the view model via some means
@OnlyIn(Dist.CLIENT)
public class UIComponentMinecraftWindow
		extends UIComponentContainer implements IUIReshapeExplicitly<ShapeDescriptor.Rectangular<?>>, IUIComponentMinecraft {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String
			PROPERTY_COLOR_BACKGROUND = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "window.colors.background",
			PROPERTY_COLOR_BORDER = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "window.colors.border",
			PROPERTY_COLOR_DRAGGING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "window.colors.dragging";
	// TODO make window scroll bars, maybe create a new component, and embed into this
	// TODO make value not hardcoded through themes
	public static final int
			WINDOW_RESHAPE_THICKNESS = 10, // COMMENT external
			WINDOW_DRAG_BAR_THICKNESS = 10, // COMMENT internal top
			WINDOW_VISIBLE_MINIMUM = 10;
	protected final List<IFunction4<? super IAffineTransformStack, ? super Point2D, ? super Double, ? super Boolean, ? extends Boolean>> scheduledActions = new LinkedList<>();
	protected final Set<Integer> activeDragButtons = new HashSet<>(CapacityUtilities.INITIAL_CAPACITY_TINY);
	protected ITransitionSystem<ICursorState, IUIEvent, ICursorTransitionSystemData> cursorTransitionSystem = TransitionSystem.getUninitialized();

	@UIProperty(PROPERTY_COLOR_BACKGROUND)
	protected final IBindingField<Color> colorBackground;
	@UIProperty(PROPERTY_COLOR_BORDER)
	protected final IBindingField<Color> colorBorder;
	@UIProperty(PROPERTY_COLOR_DRAGGING)
	protected final IBindingField<Color> colorDragging;
	@Nullable
	protected IWindowDragInfo windowDragInfo;

	@UIConstructor
	public UIComponentMinecraftWindow(Map<String, IUIPropertyMappingValue> propertyMapping) {
		super(propertyMapping);

		this.colorBackground = IHasBinding.createBindingField(Color.class,
				getPropertyMapping().get(PROPERTY_COLOR_BACKGROUND),
				s -> new Color(Integer.decode(s), true), Color.BLACK);
		this.colorBorder = IHasBinding.createBindingField(Color.class,
				getPropertyMapping().get(PROPERTY_COLOR_BORDER),
				s -> new Color(Integer.decode(s), true), Color.WHITE);
		this.colorDragging = IHasBinding.createBindingField(Color.class,
				getPropertyMapping().get(PROPERTY_COLOR_DRAGGING),
				s -> new Color(Integer.decode(s), true), Color.DARK_GRAY);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onCreated() {
		super.onCreated();
		// TODO add listeners
		addEventListener(UIEventFocus.TYPE_FOCUS_IN_POST, new UIEventListener.Functional<IUIEventFocus>(e ->
				getParent().orElseThrow(InternalError::new).moveChildToTop(this)), true);

		/* COMMENT
		used events:
		TYPE_MOUSE_ENTER
		TYPE_MOUSE_LEAVE
		TYPE_MOUSE_DOWN
		TYPE_MOUSE_UP
		 */
		IUIEventListener<IUIEventMouse> ctsListener = new UIEventListener.Functional<>(e -> {
			schedule((s, cp, pt, f) -> {
				if (f) {
					getCursorTransitionSystem().step(e, new ICursorTransitionSystemData.Impl(this, s, cp, pt, getWindowDragInfo().orElse(null), this::setWindowDragInfo, this::reshape));
					return true;
				}
				return false;
			});
			e.stopPropagation();
		});
		addEventListener(UIEventMouse.TYPE_MOUSE_ENTER, ICloneable.cloneUnchecked(ctsListener), false);
		addEventListener(UIEventMouse.TYPE_MOUSE_LEAVE, ICloneable.cloneUnchecked(ctsListener), false);
		addEventListener(UIEventMouse.TYPE_MOUSE_DOWN, ICloneable.cloneUnchecked(ctsListener), false);
		addEventListener(UIEventMouse.TYPE_MOUSE_UP, ICloneable.cloneUnchecked(ctsListener), false);

		setCursorTransitionSystem(new TransitionSystem<>(EnumCursorState.DEFAULT, null, ImmutableMap
				.<BiPredicate<? super ITransitionSystem<? extends ICursorState, ? extends IUIEvent, ? extends ICursorTransitionSystemData>, ? super ICursorTransitionSystemData>,
						Function<? super ICursorTransitionSystemData, ? extends ICursorState>>builder()
				.put((ts, d) -> ts.getState().equals(EnumCursorState.DEFAULT)
								&& ts.getInput()
								.filter(i -> i.getType().equals(UIEventMouse.TYPE_MOUSE_ENTER))
								.isPresent(),
						EnumCursorState.HOVERING::transitTo)
				.put((ts, d) -> ts.getState().equals(EnumCursorState.DEFAULT)
						&& !ts.getInput().isPresent(), d ->
						EnumCursorState.DEFAULT)

				.put((ts, d) -> ts.getState().equals(EnumCursorState.HOVERING)
								&& ts.getInput()
								.filter(i -> i.getType().equals(UIEventMouse.TYPE_MOUSE_LEAVE))
								.isPresent(),
						EnumCursorState.DEFAULT::transitTo)
				.put((ts, d) -> ts.getState().equals(EnumCursorState.HOVERING)
						&& !ts.getInput().isPresent(), d ->
						d.getStack().getDelegated().peek().createTransformedShape(d.getInstance().getShapeDescriptor().getShapeProcessed())
								.contains(d.getCursorPositionView()) ? EnumCursorState.HOVERING : EnumCursorState.DRAGGABLE)

				.put((ts, d) -> ts.getState().equals(EnumCursorState.DRAGGABLE)
								&& ts.getInput()
								.filter(i -> i.getType().equals(UIEventMouse.TYPE_MOUSE_LEAVE))
								.isPresent(),
						EnumCursorState.DEFAULT::transitTo)
				.put((ts, d) -> ts.getState().equals(EnumCursorState.DRAGGABLE)
								&& ts.getInput()
								.filter(i -> i.getType().equals(UIEventMouse.TYPE_MOUSE_DOWN))
								.filter(i -> shouldAndStartDrag(((IUIEventMouse) i).getData().getButton()))
								.isPresent(),
						EnumCursorState.DRAGGING::transitTo)
				.put((ts, d) -> ts.getState().equals(EnumCursorState.DRAGGABLE)
						&& !ts.getInput().isPresent(), d ->
						Try.call(() -> ReferenceUtilities.toRelativePoint(d.getStack().getDelegated().peek(), d.getCursorPositionView()), LOGGER)
								.filter(c -> {
									Shape s = d.getInstance().getShapeDescriptor().getShapeProcessed();
									if (!s.contains(c)) {
										EnumSet<EnumUISide> sides = EnumUISide.getSidesMouseOver(s.getBounds2D(), c);
										EnumCursor cursor;
										if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT)
												|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
											cursor = EnumCursor.EXTENSION_RESIZE_NW_SE_CURSOR;
										else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT)
												|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
											cursor = EnumCursor.EXTENSION_RESIZE_NE_SW_CURSOR;
										else if (sides.contains(EnumUISide.LEFT) || sides.contains(EnumUISide.RIGHT))
											cursor = EnumCursor.STANDARD_RESIZE_HORIZONTAL_CURSOR;
										else if (sides.contains(EnumUISide.UP) || sides.contains(EnumUISide.DOWN))
											cursor = EnumCursor.STANDARD_RESIZE_VERTICAL_CURSOR;
										else
											throw new InternalError();
										GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), cursor.getHandle());
										return true;
									} else
										return false;
								}).isPresent() ? EnumCursorState.DRAGGABLE : EnumCursorState.HOVERING.transitTo(d))

				.put((ts, d) -> ts.getState().equals(EnumCursorState.DRAGGING)
								&& ts.getInput()
								.filter(i -> i.getType().equals(UIEventMouse.TYPE_MOUSE_UP))
								.filter(i -> shouldAndStopDrag(((IUIEventMouse) i).getData().getButton()))
								.isPresent(),
						EnumCursorState.DRAGGABLE::transitTo)
				.put((ts, d) -> ts.getState().equals(EnumCursorState.DRAGGING)
						&& !ts.getInput().isPresent(), d -> {
					d.getWindowDragInfo().flatMap(IWindowDragInfo::getResizingBaseView).ifPresent(b -> {
						EnumSet<EnumUISide> sides = EnumUISide.getSidesMouseOver(new Rectangle2D.Double(b.getX(), b.getY(), 0, 0),
								d.getCursorPositionView());
						Optional<EnumCursor> cursor = Optional.empty();
						if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT)
								|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
							cursor = Optional.of(EnumCursor.EXTENSION_RESIZE_NW_SE_CURSOR);
						else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT)
								|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
							cursor = Optional.of(EnumCursor.EXTENSION_RESIZE_NE_SW_CURSOR);
						cursor.ifPresent(cs -> GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), cs.getHandle()));
					});
					return EnumCursorState.DRAGGING;
				})
				.build()));
	}

	@Override
	public boolean reshape(Function<? super Rectangular<?>, ? extends Boolean> action) throws ConcurrentModificationException {
		return getShapeDescriptor().modify(getShapeDescriptor(), action);
		// TODO resizing logic
	}

	//
	protected boolean shouldAndStartDrag(int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			getActiveDragButtons().add(button);
			return true;
		}
		return false;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Integer> getActiveDragButtons() { return activeDragButtons; }

	@Override
	protected ShapeDescriptor.Rectangular<?> createShapeDescriptor() {
		return new Rectangular<Rectangle2D>(getShapePlaceholderView(), new UIAnchorSet<>(this::getShapeDescriptor)) {
			@Override
			protected <T extends IShapeDescriptor<?>> boolean modify0(T self, Function<? super T, ? extends Boolean> action)
					throws ConcurrentModificationException {
				Optional<IUIConstraint> cs = CacheGuiWindow.CONSTRAINT.getValue().get(UIComponentMinecraftWindow.this);
				cs.ifPresent(c -> getConstraints().add(c));
				boolean ret = super.modify0(self, action);
				cs.ifPresent(c -> getConstraints().remove(c));
				return ret;
			}
		};
	}

	@Override
	public ShapeDescriptor.Rectangular<?> getShapeDescriptor() { return (Rectangular<?>) super.getShapeDescriptor(); }

	@Override
	public boolean contains(final IAffineTransformStack stack, Point2D point) {
		return getCursorTransitionSystem().getState().equals(EnumCursorState.DRAGGING) || stack.getDelegated().peek().createTransformedShape(CacheGuiWindow.RECTANGLE_CLICKABLE.getValue().get(this).orElseThrow(InternalError::new)).contains(point);
	}

	protected ITransitionSystem<ICursorState, IUIEvent, ICursorTransitionSystemData> getCursorTransitionSystem() { return cursorTransitionSystem; }

	protected void setCursorTransitionSystem(ITransitionSystem<ICursorState, IUIEvent, ICursorTransitionSystemData> cursorTransitionSystem) { this.cursorTransitionSystem = cursorTransitionSystem; }

	protected boolean shouldAndStopDrag(int button) { return getActiveDragButtons().remove(button); }

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	protected void onParentShapeDescriptorModified(EventUIShapeDescriptor.Modify event) {
		if (event.getStage() == EnumEventHookStage.POST && getParent().filter(p -> p.getShapeDescriptor().equals(event.getShapeDescriptor())).isPresent())
			IUIReshapeExplicitly.refresh(this);
	}

	@Override
	public void render(final IAffineTransformStack stack, Point2D cursorPosition, double partialTicks, boolean pre) {
		getScheduledActions().removeIf(a -> a.apply(stack, cursorPosition, partialTicks, pre));
		AffineTransform transform = stack.getDelegated().peek();
		if (pre) {
			DrawingUtilities.drawShape(transform, getShapeDescriptor().getShapeProcessed(), true, getColorBackground().getValue(), 0);
			DrawingUtilities.drawShape(transform, getShapeDescriptor().getShapeProcessed(), true, getColorBorder().getValue(), 0);
		} else {
			getWindowDragInfo().ifPresent(d -> {
				Rectangle2D r = (Rectangle2D) getShapeDescriptor().getShapeProcessed().getBounds2D().clone();
				d.handle(transform, r, cursorPosition);
				UIObjectUtilities.acceptRectangular(r, (x, y) -> (w, h) -> r.setRect(x, y, w - 1, h - 1));
				EnumCropMethod cropMethod = EnumCropMethod.getBestMethod();
				cropMethod.disable();
				DrawingUtilities.drawRectangle(transform, r, getColorDragging().getValue().getRGB(), 0);
				cropMethod.enable();
			});
		}
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IFunction4<? super IAffineTransformStack, ? super Point2D, ? super Double, ? super Boolean, ? extends Boolean>> getScheduledActions() { return scheduledActions; }

	public IBindingField<Color> getColorBackground() { return colorBackground; }

	public IBindingField<Color> getColorBorder() { return colorBorder; }

	protected Optional<IWindowDragInfo> getWindowDragInfo() { return Optional.ofNullable(windowDragInfo); }

	protected void setWindowDragInfo(@Nullable IWindowDragInfo windowDragInfo) { this.windowDragInfo = windowDragInfo; }

	public IBindingField<Color> getColorDragging() { return colorDragging; }

	@Override
	public void initialize(IAffineTransformStack stack) { EventBusEntryPoint.INSTANCE.register(this); }

	@Override
	public void schedule(IFunction4<? super IAffineTransformStack, ? super Point2D, ? super Double, ? super Boolean, ? extends Boolean> action) { getScheduledActions().add(action); }

	@Override
	public void crop(final IAffineTransformStack stack, EnumCropMethod method, boolean push, Point2D mouse, double partialTicks) { IUIComponentMinecraft.crop(this, stack, method, push, mouse, partialTicks); }

	@OnlyIn(Dist.CLIENT)
	protected enum EnumCursorState
			implements ICursorState {
		DEFAULT {
			@Override
			public ICursorState transitTo(ICursorTransitionSystemData data) {
				GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), MemoryUtil.NULL);
				return super.transitTo(data);
			}
		},
		HOVERING {
			@Override
			public ICursorState transitTo(ICursorTransitionSystemData data) {
				GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), MemoryUtil.NULL);
				return super.transitTo(data);
			}
		},
		DRAGGABLE {
			@Override
			public ICursorState transitTo(ICursorTransitionSystemData data) {
				data.getWindowDragInfo().ifPresent(d -> {
					Rectangle2D r = data.getInstance().getShapeDescriptor().getShapeProcessed().getBounds2D();
					d.handle(data.getStack().getDelegated().peek(), r, data.getCursorPositionView());
					data.reshape(s -> {
						s.getShapeRef().setFrame(r);
						return true;
					});
					data.setWindowDragInfo(null);
				});
				return super.transitTo(data);
			}
		},
		DRAGGING {
			@Override
			public ICursorState transitTo(ICursorTransitionSystemData data) {
				Point2D cp = data.getCursorPositionView();
				if (data.getStack().getDelegated().peek().createTransformedShape(CacheGuiWindow.RECTANGLE_DRAGGABLE.getValue().get(data.getInstance())
						.orElseThrow(InternalError::new)).contains(cp)) {
					data.setWindowDragInfo(new IWindowDragInfo.Impl(cp, IWindowDragInfo.EnumType.REPOSITION, null, null));
				} else {
					Rectangle2D spb = data.getInstance().getShapeDescriptor().getShapeProcessed().getBounds2D();
					EnumSet<EnumUISide> sides = EnumUISide.getSidesMouseOver(spb, cp);
					@Nullable Point2D base = null;
					if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT))
						base = new Point2D.Double(spb.getMaxX(), spb.getMaxY());
					else if (sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
						base = new Point2D.Double(spb.getX(), spb.getY());
					else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT))
						base = new Point2D.Double(spb.getX(), spb.getMaxY());
					else if (sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
						base = new Point2D.Double(spb.getMaxX(), spb.getY());
					data.setWindowDragInfo(new IWindowDragInfo.Impl(cp, IWindowDragInfo.EnumType.RESIZE, sides, base));
				}
				return super.transitTo(data);
			}
		},
		;
	}

	@Override
	public void removed(IAffineTransformStack stack) { EventBusEntryPoint.INSTANCE.unregister(this); }

	@Override
	public void transformChildren(final IAffineTransformStack stack) {
		super.transformChildren(stack);
		stack.getDelegated().peek().translate(0, WINDOW_DRAG_BAR_THICKNESS);
	}

	@OnlyIn(Dist.CLIENT)
	protected interface IWindowDragInfo {
		Point2D getCursorPositionView();

		EnumType getType();

		EnumSet<EnumUISide> getResizingSidesView();

		Optional<Point2D> getResizingBaseView();

		void handle(AffineTransform transform, Rectangle2D rectangle, Point2D mouse);

		@OnlyIn(Dist.CLIENT)
		enum EnumType {
			REPOSITION,
			RESIZE,
		}

		@OnlyIn(Dist.CLIENT)
		final class Impl
				implements IWindowDragInfo {
			protected final Point2D cursorPosition;
			protected final EnumType type;
			protected final EnumSet<EnumUISide> resizingSides;
			@Nullable
			protected final Point2D resizingBase;

			public Impl(Point2D cursorPosition, EnumType type, @Nullable EnumSet<EnumUISide> resizingSides, @Nullable Point2D resizingBase) {
				if (type == EnumType.RESIZE) {
					if (resizingSides == null)
						throw BecauseOf.illegalArgument("No sides specified", "type", type, "sides", null);
					if (resizingSides.contains(EnumUISide.HORIZONTAL) || resizingSides.contains(EnumUISide.VERTICAL))
						throw BecauseOf.illegalArgument("Invalid sides", "sides", resizingSides);
					resizingSides.forEach(s -> {
						if (resizingSides.contains(s.getOpposite()))
							throw BecauseOf.illegalArgument("Illegal sides combination", "sides", resizingSides);
					});
				} else {
					if (resizingSides != null || resizingBase != null)
						throw BecauseOf.illegalArgument("Too much data", "type", type, "sides", resizingSides, "base", resizingBase);
				}

				this.cursorPosition = (Point2D) cursorPosition.clone();
				this.type = type;
				this.resizingSides = Optional.ofNullable(resizingSides).map(EnumSet::clone).orElseGet(() -> EnumSet.noneOf(EnumUISide.class));
				this.resizingBase = (Point2D) Optional.ofNullable(resizingBase).map(Point2D::clone).orElse(null);
			}

			@Override
			public Point2D getCursorPositionView() { return (Point2D) getCursorPosition().clone(); }

			@Override
			public EnumType getType() { return type; }

			@Override
			public EnumSet<EnumUISide> getResizingSidesView() { return getResizingSides().clone(); }

			@Override
			public Optional<Point2D> getResizingBaseView() { return getResizingBase().map(t -> ((Point2D) t.clone())); }

			@Override
			public void handle(AffineTransform transform, Rectangle2D rectangle, Point2D mouse) {
				Point2D cp = getCursorPosition();
				switch (getType()) {
					case REPOSITION:
						rectangle.setRect(rectangle.getX() + (mouse.getX() - cp.getX()), rectangle.getY() + (mouse.getY() - cp.getY()),
								rectangle.getWidth(), rectangle.getHeight());
						break;
					case RESIZE:
						for (EnumUISide side : getResizingSides()) {
							EnumUIAxis axis = side.getAxis();
							side.getSetter().accept(rectangle, side.getGetter().apply(rectangle) + (axis.getCoordinate(mouse) - axis.getCoordinate(cp)));
						}
						break;
					default:
						throw new InternalError();
				}
			}

			protected Point2D getCursorPosition() { return cursorPosition; }

			@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
			protected EnumSet<EnumUISide> getResizingSides() { return resizingSides; }

			protected Optional<Point2D> getResizingBase() { return Optional.ofNullable(resizingBase); }
		}
	}

	@OnlyIn(Dist.CLIENT)
	protected interface ICursorState {
		@OverridingMethodsMustInvokeSuper
		default ICursorState transitTo(ICursorTransitionSystemData data) { return this; }
	}

	@OnlyIn(Dist.CLIENT)
	public enum CacheGuiWindow {
		;

		private static final Logger LOGGER = LogManager.getLogger();

		public static final Registry.RegistryObject<UIExtensionCache.IType<Rectangle2D, IUIComponent>> RECTANGLE_DRAGGABLE =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("rectangle_draggable"),
						k -> new UICacheShapeDescriptor<>(k,
								(t, i) -> UIExtensionCache.TYPE.getValue().get(i).flatMap(cache ->
										Try.call(() -> cache.getDelegated().get(t.getKey(),
												() -> {
													Rectangle2D bounds = i.getShapeDescriptor().getShapeProcessed().getBounds2D();
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
								(t, i) -> UIExtensionCache.TYPE.getValue().get(i).flatMap(cache -> Try.call(() -> cache.getDelegated().get(t.getKey(),
										() -> UIObjectUtilities.applyRectangular(i.getShapeDescriptor().getShapeProcessed().getBounds2D(), (x, y) -> (w, h) ->
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
		public static final Registry.RegistryObject<UIExtensionCache.IType<IUIConstraint, IUIComponent>> CONSTRAINT =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("constraint"),
						k -> new UICacheShapeDescriptor<>(k,
								(t, i) -> UIExtensionCache.TYPE.getValue().get(i)
										.flatMap(cache -> i.getManager()
												.map(m -> m.getShapeDescriptor().getShapeProcessed().getBounds2D())
												.flatMap(mb -> Try.call(() -> cache.getDelegated().get(t.getKey(), () ->
														new UIConstraint(
																new Rectangle2D.Double(0, 0, WINDOW_VISIBLE_MINIMUM, WINDOW_VISIBLE_MINIMUM),
																new Rectangle2D.Double(mb.getMaxX() - WINDOW_VISIBLE_MINIMUM, mb.getMaxY() - WINDOW_VISIBLE_MINIMUM,
																		IUIConstraint.CONSTRAINT_NULL_VALUE, IUIConstraint.CONSTRAINT_NULL_VALUE))), LOGGER)
														.map(CastUtilities::castUnchecked))),
								(t, i) -> IUIExtensionCache.IType.invalidate(i, t.getKey()),
								t -> {
									UICacheShapeDescriptor<IUIConstraint, IUIComponent> tc = (UICacheShapeDescriptor<IUIConstraint, IUIComponent>) t;
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

		private static ResourceLocation generateKey(String name) { return new ResourceLocation(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, "window." + name); }
	}

	@OnlyIn(Dist.CLIENT)
	protected interface ICursorTransitionSystemData {
		IUIComponent getInstance();

		IAffineTransformStack getStack();

		Point2D getCursorPositionView();

		double getPartialTicks();

		Optional<IWindowDragInfo> getWindowDragInfo();

		void setWindowDragInfo(@Nullable IWindowDragInfo windowDragInfo);

		boolean reshape(Function<? super Rectangular<?>, ? extends Boolean> action) throws ConcurrentModificationException;

		@OnlyIn(Dist.CLIENT)
		final class Impl
				implements ICursorTransitionSystemData {
			protected final IUIComponent instance;
			protected final IAffineTransformStack stack;
			protected final Point2D cursorPosition;
			protected final double partialTicks;
			@Nullable
			protected final IWindowDragInfo windowDragInfo;
			protected final Consumer<? super IWindowDragInfo> windowDragInfoSetter;
			protected final Function<? super Function<? super Rectangular<?>, ? extends Boolean>, ? extends Boolean> reshapeFunction;

			public Impl(IUIComponent instance, IAffineTransformStack stack, Point2D cursorPosition, double partialTicks, @Nullable IWindowDragInfo windowDragInfo, Consumer<? super IWindowDragInfo> windowDragInfoSetter, Function<? super Function<? super Rectangular<?>, ? extends Boolean>, ? extends Boolean> reshapeFunction) {
				this.instance = instance;
				this.stack = stack;
				this.cursorPosition = (Point2D) cursorPosition.clone();
				this.partialTicks = partialTicks;
				this.windowDragInfo = windowDragInfo;
				this.windowDragInfoSetter = windowDragInfoSetter;
				this.reshapeFunction = reshapeFunction;
			}

			@Override
			public IUIComponent getInstance() { return instance; }

			@Override
			public IAffineTransformStack getStack() { return stack; }

			@Override
			public Point2D getCursorPositionView() { return (Point2D) cursorPosition.clone(); }

			@Override
			public double getPartialTicks() { return partialTicks; }

			@Override
			public Optional<IWindowDragInfo> getWindowDragInfo() { return Optional.ofNullable(windowDragInfo); }

			@Override
			public void setWindowDragInfo(@Nullable IWindowDragInfo windowDragInfo) { getWindowDragInfoSetter().accept(windowDragInfo); }

			@Override
			public boolean reshape(Function<? super Rectangular<?>, ? extends Boolean> action) throws ConcurrentModificationException { return getReshapeFunction().apply(action); }

			protected Function<? super Function<? super Rectangular<?>, ? extends Boolean>, ? extends Boolean> getReshapeFunction() { return reshapeFunction; }

			protected Consumer<? super IWindowDragInfo> getWindowDragInfoSetter() { return windowDragInfoSetter; }
		}
	}
}
