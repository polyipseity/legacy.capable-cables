package $group__.ui.mvvm.views.components;

import $group__.ui.UIConfiguration;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentShapeAnchorController;
import $group__.ui.core.structures.shapes.interactions.IShapeAnchor;
import $group__.ui.core.structures.shapes.interactions.IShapeAnchorSet;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.mvvm.views.events.bus.UIComponentBusEvent;
import $group__.ui.structures.shapes.interactions.DefaultShapeAnchorController;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CleanerUtilities;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.events.AutoSubscribingCompositeDisposable;
import $group__.utilities.reactive.LoggingDisposableObserver;
import $group__.utilities.references.OptionalWeakReference;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import sun.misc.Cleaner;

import java.util.*;
import java.util.stream.BaseStream;

public class DefaultUIComponentShapeAnchorController
		extends DefaultShapeAnchorController<IUIComponent>
		implements IUIComponentShapeAnchorController {

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	public DefaultUIComponentShapeAnchorController() {
		Cleaner.create(CleanerUtilities.getCleanerReferent(this),
				new AutoSubscribingCompositeDisposable<>(UIEventBusEntryPoint.getEventBus(),
						new ModifyShapeDescriptorObserver(this, UIConfiguration.getInstance().getLogger())
				)::dispose);
	}

	protected static class ModifyShapeDescriptorObserver
			extends LoggingDisposableObserver<UIComponentBusEvent.ModifyShapeDescriptor> {
		protected final OptionalWeakReference<DefaultUIComponentShapeAnchorController> owner;
		protected final Deque<IShapeAnchor> anchoringAnchors = new ArrayDeque<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);
		protected boolean anchoringSelf = false;

		protected ModifyShapeDescriptorObserver(DefaultUIComponentShapeAnchorController owner, Logger logger) {
			super(logger);
			this.owner = new OptionalWeakReference<>(owner);
		}

		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(UIComponentBusEvent.ModifyShapeDescriptor event) {
			super.onNext(event);
			if (event.getStage().isPost() && !isAnchoringSelf())
				getOwner()
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

		protected Optional<? extends DefaultUIComponentShapeAnchorController> getOwner() { return owner.getOptional(); }

		protected void anchorSelf(UIComponentBusEvent.ModifyShapeDescriptor event, DefaultUIComponentShapeAnchorController controller) {
			Optional.ofNullable(controller.getAnchorSets().getIfPresent(event.getComponent()))
					.ifPresent(as -> as.anchor(event.getComponent()));
		}

		protected void anchorOthers(UIComponentBusEvent.ModifyShapeDescriptor event, DefaultUIComponentShapeAnchorController controller) {
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
