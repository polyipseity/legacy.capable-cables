package $group__.client.ui.mvvm.structures;

import $group__.client.ui.events.bus.EventBusEntryPoint;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.structures.IUIAnchor;
import $group__.client.ui.mvvm.core.structures.IUIAnchorSet;
import $group__.client.ui.mvvm.views.events.bus.EventUIShapeDescriptor;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@Immutable
public final class UIAnchor implements IUIAnchor {
	public static final ImmutableList<Function<? super UIAnchor, ?>> OBJECT_VARIABLES = ObjectUtilities.extendsObjectVariables(IUIAnchor.OBJECT_VARIABLES,
			ImmutableList.of(UIAnchor::getContainer));
	public static final ImmutableMap<String, Function<? super UIAnchor, ?>> OBJECT_VARIABLES_MAP = ObjectUtilities.extendsObjectVariablesMap(OBJECT_VARIABLES, IUIAnchor.OBJECT_VARIABLES_MAP,
			ImmutableList.of("container"));
	protected final IShapeDescriptor<?> to;
	protected final EnumUISide fromSide, toSide;
	protected final double borderThickness;
	protected WeakReference<IUIAnchorSet<?>> container = new WeakReference<>(null);

	public UIAnchor(IShapeDescriptor<?> to, EnumUISide fromSide, EnumUISide toSide) { this(to, fromSide, toSide, 0); }

	public UIAnchor(IShapeDescriptor<?> to, EnumUISide fromSide, EnumUISide toSide, double borderThickness) {
		this.to = to;
		this.fromSide = fromSide;
		this.toSide = toSide;
		this.borderThickness = borderThickness;
	}

	@Override
	public IShapeDescriptor<?> getTo() { return to; }

	protected Optional<IUIAnchorSet<?>> getContainer() { return Optional.ofNullable(container.get()); }

	protected void setContainer(@Nullable IUIAnchorSet<?> container) { this.container = new WeakReference<>(container); }

	@Override
	public double getBorderThickness() { return borderThickness; }

	@Override
	public void anchor(IShapeDescriptor<?> from) {
		Rectangle2D bounds = from.getShapeOutput().getBounds2D();
		getFromSide().getSetter().accept(bounds,
				getFromSide().getApplyBorder().apply(
						getToSide().getGetter().apply(getTo().getShapeOutput().getBounds2D()),
						getBorderThickness()));
		try {
			from.modify(() -> {
				from.bound(bounds);
				return true;
			});
		} catch (ConcurrentModificationException ex) {
			throw new ConcurrentModificationException("Anchor cycle involving: " + this, ex);
		}
	}

	@Override
	public EnumUISide getToSide() { return toSide; }

	@Override
	public EnumUISide getFromSide() { return fromSide; }

	protected final AtomicReference<ObserverEventUIShapeDescriptorModify> observerEventUIShapeDescriptorModify = new AtomicReference<>();

	@Override
	public void onContainerAdded(IUIAnchorSet<?> container) {
		setContainer(container);
		EventBusEntryPoint.<EventUIShapeDescriptor.Modify>getEventBus()
				.subscribe(getObserverEventUIShapeDescriptorModify().accumulateAndGet(new ObserverEventUIShapeDescriptorModify(), (p, n) -> {
					Optional.ofNullable(p).ifPresent(DisposableObserver::dispose);
					return n;
				}));

		Cleaner.create(container, this::onContainerRemoved);
		container.getFrom()
				.ifPresent(this::anchor);
	}

	@Override
	public void onContainerRemoved() {
		setContainer(null);
		Optional.ofNullable(getObserverEventUIShapeDescriptorModify().getAndSet(null)).ifPresent(DisposableObserver::dispose);
	}

	protected AtomicReference<ObserverEventUIShapeDescriptorModify> getObserverEventUIShapeDescriptorModify() { return observerEventUIShapeDescriptorModify; }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }

	protected class ObserverEventUIShapeDescriptorModify
			extends DisposableObserverAuto<EventUIShapeDescriptor.Modify> {
		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(EventUIShapeDescriptor.Modify event) {
			if (event.getStage() == EnumEventHookStage.POST && event.getShapeDescriptor().equals(getTo()))
				getContainer().flatMap(IUIAnchorSet::getFrom).ifPresent(UIAnchor.this::anchor);
		}
	}
}
