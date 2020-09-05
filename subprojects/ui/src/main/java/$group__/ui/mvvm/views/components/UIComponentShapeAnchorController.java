package $group__.ui.mvvm.views.components;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentShapeAnchorController;
import $group__.ui.core.structures.shapes.interactions.IShapeAnchor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.mvvm.views.events.bus.EventUIComponent;
import $group__.ui.structures.shapes.interactions.ShapeAnchorController;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.reactive.DisposableObserverAuto;
import com.google.common.collect.ImmutableSet;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.*;

public class UIComponentShapeAnchorController
		extends ShapeAnchorController<IUIComponent>
		implements IUIComponentShapeAnchorController {
	protected final Disposable disposable;
	protected final Object cleanerRef = new Object();

	public UIComponentShapeAnchorController() {
		@SuppressWarnings("ThisEscapedInObjectConstruction")
		DisposableObserver<EventUIComponent.ShapeDescriptorModify> od = new ObserverEventUIShapeDescriptorModify(this);
		this.disposable = od;
		UIEventBusEntryPoint.<EventUIComponent.ShapeDescriptorModify>getEventBus().subscribe(od);

		Cleaner.create(getCleanerRef(), od::dispose);
	}

	protected final Object getCleanerRef() { return cleanerRef; }

	protected Disposable getDisposable() { return disposable; }

	protected static class ObserverEventUIShapeDescriptorModify
			extends DisposableObserverAuto<EventUIComponent.ShapeDescriptorModify> {
		@Nullable
		protected static final IShapeAnchor SELF_ANCHOR = null;
		protected final WeakReference<UIComponentShapeAnchorController> controller;
		protected final Deque<IShapeAnchor> anchoringAnchors = new ArrayDeque<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);

		protected ObserverEventUIShapeDescriptorModify(UIComponentShapeAnchorController controller) { this.controller = new WeakReference<>(controller); }

		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(EventUIComponent.ShapeDescriptorModify event) {
			if (event.getStage() == EnumEventHookStage.POST && (getAnchoringAnchors().isEmpty() || !Objects.equals(getAnchoringAnchors().peek(), SELF_ANCHOR)))
				getController()
						.ifPresent(ctr -> {
							getAnchoringAnchors().push(SELF_ANCHOR);
							anchorSelf(event, ctr);
							getAnchoringAnchors().pop();
							anchorOthers(event, ctr);
						});
		}

		protected void anchorOthers(EventUIComponent.ShapeDescriptorModify event, UIComponentShapeAnchorController controller) {
			controller.getSubscribersMap().getOrDefault(event.getComponent(), ImmutableSet.of()).stream().unordered()
					.forEach(a -> a.getContainer()
							.map(ac -> controller.getAnchorSetsInverse().get(ac))
							.ifPresent(f -> {
								if (getAnchoringAnchors().contains(a))
									throw new ConcurrentModificationException("Anchor cycle:" + System.lineSeparator()
											+ getAnchoringAnchors());
								getAnchoringAnchors().push(a);
								a.anchor(f);
								getAnchoringAnchors().pop();
							}));
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Deque<IShapeAnchor> getAnchoringAnchors() { return anchoringAnchors; }

		protected Optional<? extends UIComponentShapeAnchorController> getController() { return Optional.ofNullable(controller.get()); }

		protected void anchorSelf(EventUIComponent.ShapeDescriptorModify event, UIComponentShapeAnchorController controller) {
			controller.getAnchorSet(event.getComponent())
					.anchor(event.getComponent());
		}
	}
}
