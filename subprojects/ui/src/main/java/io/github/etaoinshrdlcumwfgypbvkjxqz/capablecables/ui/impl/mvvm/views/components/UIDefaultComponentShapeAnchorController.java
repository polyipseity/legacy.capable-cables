package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentShapeAnchorController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeAnchorSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractComponentHierarchyChangeBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIComponentModifyShapeDescriptorBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.DefaultShapeAnchorController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.AutoSubscribingDisposable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl.DelegatingSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl.ReactiveUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import sun.misc.Cleaner;

import java.util.*;
import java.util.stream.BaseStream;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UIDefaultComponentShapeAnchorController
		extends DefaultShapeAnchorController<IUIComponent>
		implements IUIComponentShapeAnchorController {

	public UIDefaultComponentShapeAnchorController() {
		Cleaner.create(suppressThisEscapedWarning(() -> this),
				AutoSubscribingDisposable.of(UIEventBusEntryPoint.getBusPublisher(),
						ImmutableList.of(
								ModifyShapeDescriptorSubscriber.ofDecorated(suppressThisEscapedWarning(() -> this), UIConfiguration.getInstance().getLogger()),
								ComponentHierarchyParentChangeSubscriber.ofDecorated(suppressThisEscapedWarning(() -> this), UIConfiguration.getInstance().getLogger())
						)
				)::dispose);
	}

	public static class ComponentHierarchyParentChangeSubscriber
			extends DelegatingSubscriber<UIAbstractComponentHierarchyChangeBusEvent.Parent> {
		private final OptionalWeakReference<UIDefaultComponentShapeAnchorController> owner;

		protected ComponentHierarchyParentChangeSubscriber(Subscriber<? super UIAbstractComponentHierarchyChangeBusEvent.Parent> delegate, UIDefaultComponentShapeAnchorController owner) {
			super(delegate);
			this.owner = OptionalWeakReference.of(owner);
		}

		public static DisposableSubscriber<UIAbstractComponentHierarchyChangeBusEvent.Parent> ofDecorated(UIDefaultComponentShapeAnchorController owner, Logger logger) {
			return ReactiveUtilities.decorateAsListener(delegate -> new ComponentHierarchyParentChangeSubscriber(delegate, owner), logger);
		}

		@SuppressWarnings("UnstableApiUsage")
		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(UIAbstractComponentHierarchyChangeBusEvent.@Nonnull Parent event) {
			super.onNext(event);
			if (event.getStage() == EnumHookStage.POST && !event.getNext().isPresent())
				getOwner().ifPresent(owner -> {
					IUIComponent component = event.getComponent();

					// COMMENT remove component as origin
					Optional.ofNullable(owner.getAnchorSets().getIfPresent(component))
							.map(IShapeAnchorSet::getAnchorsView)
							.map(Map::values)
							.ifPresent(anchors -> owner.removeAnchors(component, anchors));

					// COMMENT remove component as target
					ImmutableMap<IShapeAnchorSet, IUIComponent> anchorSetsInverse =
							MapUtilities.inverse(owner.getAnchorSets().asMap());
					Streams.stream(Optional.ofNullable(owner.getSubscribersMap().getIfPresent(component))).unordered()
							.flatMap(Collection::stream)
							.forEach(shapeAnchor ->
									shapeAnchor.getContainer()
											.map(anchorSetsInverse::get)
											.ifPresent(origin -> owner.removeAnchors(origin, ImmutableSet.of(shapeAnchor))));
				});
		}

		protected Optional<? extends UIDefaultComponentShapeAnchorController> getOwner() { return owner.getOptional(); }
	}

	public static class ModifyShapeDescriptorSubscriber
			extends DelegatingSubscriber<UIComponentModifyShapeDescriptorBusEvent> {
		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		private final OptionalWeakReference<UIDefaultComponentShapeAnchorController> owner;
		private final Deque<IShapeAnchor> anchoringAnchors = new ArrayDeque<>(CapacityUtilities.getInitialCapacityMedium());
		private boolean anchoringSelf = false;

		protected ModifyShapeDescriptorSubscriber(Subscriber<? super UIComponentModifyShapeDescriptorBusEvent> delegate, UIDefaultComponentShapeAnchorController owner) {
			super(delegate);
			this.owner = OptionalWeakReference.of(owner);
		}

		public static DisposableSubscriber<UIComponentModifyShapeDescriptorBusEvent> ofDecorated(UIDefaultComponentShapeAnchorController owner, Logger logger) {
			return ReactiveUtilities.decorateAsListener(delegate -> new ModifyShapeDescriptorSubscriber(delegate, owner), logger);
		}

		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(UIComponentModifyShapeDescriptorBusEvent event) {
			super.onNext(event);
			if (event.getStage() == EnumHookStage.POST && !isAnchoringSelf())
				getOwner().ifPresent(ctr -> {
					setAnchoringSelf(true);
					anchorSelf(event, ctr);
					setAnchoringSelf(false);
					anchorOthers(event, ctr);
				});
		}

		protected boolean isAnchoringSelf() { return anchoringSelf; }

		protected Optional<? extends UIDefaultComponentShapeAnchorController> getOwner() { return owner.getOptional(); }

		protected void anchorSelf(UIComponentModifyShapeDescriptorBusEvent event, UIDefaultComponentShapeAnchorController controller) {
			Optional.ofNullable(controller.getAnchorSets().getIfPresent(event.getComponent()))
					.ifPresent(as -> as.anchor(event.getComponent()));
		}

		protected void anchorOthers(UIComponentModifyShapeDescriptorBusEvent event, UIDefaultComponentShapeAnchorController controller) {
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

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Deque<IShapeAnchor> getAnchoringAnchors() { return anchoringAnchors; }

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		protected void setAnchoringSelf(boolean anchoringSelf) { this.anchoringSelf = anchoringSelf; }
	}
}
