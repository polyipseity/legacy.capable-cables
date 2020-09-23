package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentShapeAnchorController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeAnchorSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus.UIComponentBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.interactions.DefaultShapeAnchorController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CleanerUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.AutoSubscribingCompositeDisposable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
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
		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

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
										throw new ConcurrentModificationException(
												new LogMessageBuilder()
														.addMarkers(UIMarkers.getInstance()::getMarkerUIAnchor)
														.addKeyValue("this", this).addKeyValue("event", event).addKeyValue("controller", controller)
														.addMessages(() -> getResourceBundle().getString("anchor.others.cyclic"))
														.build()
										);
									getAnchoringAnchors().push(a);
									a.anchor(f);
									getAnchoringAnchors().pop();
								}));
					});
		}

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

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		protected void setAnchoringSelf(boolean anchoringSelf) { this.anchoringSelf = anchoringSelf; }
	}
}