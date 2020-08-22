package $group__.client.ui.structures.shapes.interactions;

import $group__.client.ui.core.structures.shapes.IShapeDescriptor;
import $group__.client.ui.core.structures.shapes.IUIAnchor;
import $group__.client.ui.core.structures.shapes.IUIAnchorSet;
import $group__.client.ui.events.bus.EventBusEntryPoint;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.views.events.bus.EventUIComponent;
import $group__.client.ui.structures.EnumUISide;
import $group__.utilities.ObjectUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.reactive.DisposableObserverAuto;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@Immutable
public final class UIAnchor implements IUIAnchor {
	public static final ImmutableList<Function<? super UIAnchor, ?>> OBJECT_VARIABLES = ObjectUtilities.extendsObjectVariables(IUIAnchor.OBJECT_VARIABLES,
			ImmutableList.of(UIAnchor::getContainer));
	public static final ImmutableMap<String, Function<? super UIAnchor, ?>> OBJECT_VARIABLES_MAP = ObjectUtilities.extendsObjectVariablesMap(OBJECT_VARIABLES, IUIAnchor.OBJECT_VARIABLES_MAP,
			ImmutableList.of("container"));
	protected final IUIComponent target;
	protected final EnumUISide originSide, targetSide;
	protected final double borderThickness;
	protected WeakReference<IUIAnchorSet<?>> container = new WeakReference<>(null);
	protected final AtomicBoolean anchoring = new AtomicBoolean();

	public UIAnchor(IUIComponent target, EnumUISide originSide, EnumUISide targetSide) { this(target, originSide, targetSide, 0); }

	public UIAnchor(IUIComponent target, EnumUISide originSide, EnumUISide targetSide, double borderThickness) {
		this.target = target;
		this.originSide = originSide;
		this.targetSide = targetSide;
		this.borderThickness = borderThickness;
	}

	@Override
	public boolean isAnchoring() { return getAnchoring().get(); }

	protected Optional<IUIAnchorSet<?>> getContainer() { return Optional.ofNullable(container.get()); }

	protected void setContainer(@Nullable IUIAnchorSet<?> container) { this.container = new WeakReference<>(container); }

	@Override
	public double getBorderThickness() { return borderThickness; }

	@Override
	public IUIComponent getTarget() { return target; }

	@Override
	public EnumUISide getOriginSide() { return originSide; }

	@Override
	public EnumUISide getTargetSide() { return targetSide; }

	protected final AtomicReference<ObserverEventUIShapeDescriptorModify> observerEventUIShapeDescriptorModify = new AtomicReference<>();

	@Override
	public void anchor(IShapeDescriptor<?> from)
			throws ConcurrentModificationException {
		Rectangle2D bounds = from.getShapeOutput().getBounds2D();
		Double targetValue =
				getOriginSide().getApplyBorder().apply(
						getTargetSide().getGetter().apply(getTarget().getShapeDescriptor().getShapeOutput().getBounds2D()),
						getBorderThickness());
		getOriginSide().getOpposite() // COMMENT set opposite side, avoid overshooting
				.ifPresent(so -> {
					Double oppositeTargetValue = getOriginSide().getApplyBorder().apply(targetValue, 1d);
					oppositeTargetValue = oppositeTargetValue < targetValue ?
							Math.min(oppositeTargetValue, so.getGetter().apply(bounds)) // COMMENT lesser means larger area
							: Math.max(oppositeTargetValue, so.getGetter().apply(bounds)); // COMMENT greater means larger area
					so.getSetter().accept(bounds, oppositeTargetValue);
				});
		getOriginSide().getSetter().accept(bounds, targetValue);
		if (getAnchoring().getAndSet(true)) {
			if (getAnchoring().compareAndSet(true, false))
				throw new ConcurrentModificationException("Anchor cycle involving: " + this);
		} else {
			if (from.isBeingModified())
				from.bound(bounds);
			else {
				from.modify(() -> {
					from.bound(bounds);
					return true;
				});
			}
			getAnchoring().compareAndSet(true, false);
		}
	}

	@Override
	public void onContainerRemoved() {
		setContainer(null);
		Optional.ofNullable(getObserverEventUIShapeDescriptorModify().getAndSet(null)).ifPresent(DisposableObserver::dispose);
	}

	protected AtomicReference<ObserverEventUIShapeDescriptorModify> getObserverEventUIShapeDescriptorModify() { return observerEventUIShapeDescriptorModify; }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }

	@Override
	public void onContainerAdded(IUIAnchorSet<?> container) {
		setContainer(container);
		EventBusEntryPoint.<EventUIComponent.ShapeDescriptorModify>getEventBus()
				.subscribe(getObserverEventUIShapeDescriptorModify().accumulateAndGet(new ObserverEventUIShapeDescriptorModify(), (p, n) -> {
					Optional.ofNullable(p).ifPresent(DisposableObserver::dispose);
					return n;
				}));
		Cleaner.create(container,
				this::onContainerRemoved); // TODO CLEANER not working
	}

	protected AtomicBoolean getAnchoring() { return anchoring; }

	protected class ObserverEventUIShapeDescriptorModify
			extends DisposableObserverAuto<EventUIComponent.ShapeDescriptorModify> {
		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(EventUIComponent.ShapeDescriptorModify event) {
			if (event.getStage() == EnumEventHookStage.POST && event.getComponent().equals(getTarget()))
				getContainer().flatMap(IUIAnchorSet::getFrom).ifPresent(UIAnchor.this::anchor);
		}
	}
}
