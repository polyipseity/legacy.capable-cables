package $group__.ui.mvvm.views.components;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentShapeAnchorController;
import $group__.ui.core.structures.shapes.interactions.IShapeAnchor;
import $group__.ui.core.structures.shapes.interactions.IShapeAnchorSet;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.mvvm.views.events.bus.EventUIComponent;
import $group__.ui.structures.shapes.interactions.ShapeAnchorController;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.reactive.DisposableObserverAuto;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sun.misc.Cleaner;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.BaseStream;

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
		protected final WeakReference<UIComponentShapeAnchorController> controller;
		protected final Deque<IShapeAnchor> anchoringAnchors = new ArrayDeque<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);
		protected boolean anchoringSelf = false;

		protected ObserverEventUIShapeDescriptorModify(UIComponentShapeAnchorController controller) { this.controller = new WeakReference<>(controller); }

		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(EventUIComponent.ShapeDescriptorModify event) {
			if (event.getStage().isPost() && !isAnchoringSelf())
				getController()
						.ifPresent(ctr -> {
							setAnchoringSelf(true);
							anchorSelf(event, ctr);
							setAnchoringSelf(false);
							anchorOthers(event, ctr);
						});
		}

		protected boolean isAnchoringSelf() { return anchoringSelf; }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Deque<IShapeAnchor> getAnchoringAnchors() { return anchoringAnchors; }

		protected Optional<? extends UIComponentShapeAnchorController> getController() { return Optional.ofNullable(controller.get()); }

		protected void anchorSelf(EventUIComponent.ShapeDescriptorModify event, UIComponentShapeAnchorController controller) {
			Optional.ofNullable(controller.getAnchorSets().getIfPresent(event.getComponent()))
					.ifPresent(as -> as.anchor(event.getComponent()));
		}

		protected void anchorOthers(EventUIComponent.ShapeDescriptorModify event, UIComponentShapeAnchorController controller) {
			Optional.ofNullable(controller.getSubscribersMap().getIfPresent(event.getComponent()))
					.map(Collection::stream)
					.map(BaseStream::unordered)
					.ifPresent(s -> {
						Map<IShapeAnchorSet, IUIComponent> inverse = MapUtilities.inverse(controller.getAnchorSets().asMap());
						s.forEach(a -> a.getContainer()
								.map(inverse::get)
								.ifPresent(f -> {
									if (getAnchoringAnchors().contains(a))
										throw new ConcurrentModificationException("Anchor cycle:" + System.lineSeparator()
												+ getAnchoringAnchors());
									getAnchoringAnchors().push(a);
									a.anchor(f);
									getAnchoringAnchors().pop();
								}));
					});
		}

		protected void setAnchoringSelf(boolean anchoringSelf) { this.anchoringSelf = anchoringSelf; }
	}
}
