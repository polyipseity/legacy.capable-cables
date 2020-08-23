package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.core.structures.shapes.interactions.IShapeAnchor;
import $group__.client.ui.events.bus.EventBusEntryPoint;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentShapeAnchorController;
import $group__.client.ui.mvvm.views.events.bus.EventUIComponent;
import $group__.client.ui.structures.shapes.interactions.ShapeAnchorController;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.reactive.DisposableObserverAuto;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

public class UIComponentShapeAnchorController
		extends ShapeAnchorController<IUIComponent>
		implements IUIComponentShapeAnchorController {
	protected final Disposable disposable;
	protected final Object cleanerRef = new Object();

	public UIComponentShapeAnchorController() {
		@SuppressWarnings("ThisEscapedInObjectConstruction")
		DisposableObserver<EventUIComponent.ShapeDescriptorModify> od = new ObserverEventUIShapeDescriptorModify(this);
		this.disposable = od;
		EventBusEntryPoint.<EventUIComponent.ShapeDescriptorModify>getEventBus().subscribe(od);

		Cleaner.create(getCleanerRef(), od::dispose);
	}

	protected final Object getCleanerRef() { return cleanerRef; }

	protected Disposable getDisposable() { return disposable; }

	protected static class ObserverEventUIShapeDescriptorModify
			extends DisposableObserverAuto<EventUIComponent.ShapeDescriptorModify> {
		@Nullable
		protected static final IShapeAnchor SELF_ANCHOR = null;
		protected final WeakReference<UIComponentShapeAnchorController> controller;
		protected final Stack<IShapeAnchor> anchoringAnchors = new Stack<>();

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

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Stack<IShapeAnchor> getAnchoringAnchors() { return anchoringAnchors; }

		protected Optional<? extends UIComponentShapeAnchorController> getController() { return Optional.ofNullable(controller.get()); }

		protected void anchorSelf(EventUIComponent.ShapeDescriptorModify event, UIComponentShapeAnchorController controller) {
			controller.getAnchorSet(event.getComponent())
					.anchor(event.getComponent());
		}

		protected void anchorOthers(EventUIComponent.ShapeDescriptorModify event, UIComponentShapeAnchorController controller) {
			Optional.ofNullable(controller.getSubscribersMap().get(event.getComponent()))
					.ifPresent(as ->
							as.forEach(a -> a.getContainer()
									.map(ac -> controller.getAnchorSets().inverse().get(ac))
									.map(Reference::get)
									.ifPresent(f -> {
										if (getAnchoringAnchors().contains(a))
											throw new ConcurrentModificationException("Anchor cycle:" + System.lineSeparator()
													+ getAnchoringAnchors());
										getAnchoringAnchors().push(a).anchor(f);
										getAnchoringAnchors().pop();
									})));
		}
	}
}
