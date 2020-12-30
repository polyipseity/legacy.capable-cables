package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.embed.IUIComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouseWheel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.UINamespaceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.binding.UIImmutablePropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIPhasedDelegatingEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed.UIChildlessComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed.UIComponentEmbedUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.SupplierShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIAxis;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CollectionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IValueHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.DefaultValueHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.ImmutableBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.MemoryObservableField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl.DelegatingSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl.ReactiveUtilities;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMaps;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import org.jetbrains.annotations.NonNls;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.*;

public class UIScrollPanelComponent
		extends UIShapeComponent {
	public static final @NonNls String PROPERTY_SCROLLBAR_SIDES = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.scroll_panel.scrollbar.sides";
	public static final @NonNls String PROPERTY_SCROLLBAR_THICKNESSES = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.scroll_panel.scrollbar.thicknesses"; // COMMENT 'thicknesses' is cursed, try reading it
	public static final @NonNls String EMBED_VERTICAL_SCROLLBAR_NAME = "scrollbar.vertical";
	public static final @NonNls String EMBED_HORIZONTAL_SCROLLBAR_NAME = "scrollbar.horizontal";
	public static final @NonNls String INTERNAL_BINDING_SCROLLBAR_RELATIVE_PROGRESS_PREFIX = "scroll_panel.scrollbar.relative_progress";
	public static final @NonNls String INTERNAL_BINDING_SCROLLBAR_THUMB_RELATIVE_SIZE_PREFIX = "scroll_panel.scrollbar.thumb.relative_size";
	private static final IIdentifier PROPERTY_SCROLLBAR_SIDES_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyScrollbarSides());
	private static final IIdentifier PROPERTY_SCROLLBAR_THICKNESSES_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyScrollbarThicknesses());
	@UIProperty(PROPERTY_SCROLLBAR_SIDES)
	private final IBindingField<Map<EnumUIAxis, EnumUISide>> scrollbarSides;
	@UIProperty(PROPERTY_SCROLLBAR_THICKNESSES) // COMMENT accepted type: Map<? extends EnumUIAxis, ? extends Double>
	private final IBindingField<Object2DoubleMap<EnumUIAxis>> scrollbarThicknesses;

	private final @Immutable Map<EnumUIAxis, IUIComponentEmbed<UIScrollbarComponent>> scrollbarEmbeds;
	private final @Immutable Map<EnumUIAxis, IBindingField<Double>> scrollRelativeProgressMap; // COMMENT may not contain both axes
	private final @Immutable Map<EnumUIAxis, IBindingField<Double>> scrollbarThumbRelativeSizeMap; // COMMENT may not contain both axes
	private final IUIRendererContainerContainer<IUIComponentRenderer<?>> rendererContainerContainer;

	private final Runnable eventTargetListenersInitializer;

	private final Point2D contentScrollOffset = new Point2D.Double();
	private final Point2D nextContentScrollOffset = new Point2D.Double();
	private final Rectangle2D lastContentFullBounds = new Rectangle2D.Double();
	private final Rectangle2D lastContentBounds = new Rectangle2D.Double();

	@SuppressWarnings("UnstableApiUsage")
	@UIComponentConstructor
	public UIScrollPanelComponent(IUIComponentArguments arguments) {
		super(arguments);

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(DefaultRenderer.class));

		@Immutable Map<IIdentifier, ? extends IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		OptionalWeakReference<UIScrollPanelComponent> thisReference = OptionalWeakReference.of(suppressThisEscapedWarning(() -> this));

		this.scrollbarSides = IUIPropertyMappingValue.createBindingField(CastUtilities.castUnchecked(Map.class),
				() -> ImmutableMap.<EnumUIAxis, EnumUISide>builder()
						.put(EnumUIAxis.X, EnumUISide.DOWN)
						.put(EnumUIAxis.Y, EnumUISide.RIGHT)
						.build(),
				mappings.get(getPropertyScrollbarSidesIdentifier()));
		this.scrollbarThicknesses = IUIPropertyMappingValue.createBindingField(CastUtilities.castUnchecked(Object2DoubleMap.class),
				() -> {
					Object2DoubleMap<EnumUIAxis> defaultValue = new Object2DoubleOpenHashMap<>(EnumUIAxis.values().length);
					defaultValue.put(EnumUIAxis.X, 10D);
					defaultValue.put(EnumUIAxis.Y, 10D);
					return Object2DoubleMaps.unmodifiable(defaultValue);
				},
				mappings.get(getPropertyScrollbarThicknessesIdentifier()),
				CastUtilities.<Class<Map<? extends EnumUIAxis, ? extends Double>>>castUnchecked(Map.class),
				mappingValue -> Object2DoubleMaps.unmodifiable(new Object2DoubleOpenHashMap<>(mappingValue)));

		Map<EnumUIAxis, IBindingField<Double>> scrollRelativeProgressMap = new EnumMap<>(EnumUIAxis.class);
		Map<EnumUIAxis, IBindingField<Double>> thumbRelativeSizeMap = new EnumMap<>(EnumUIAxis.class);
		this.scrollbarEmbeds = ImmutableMap.<EnumUIAxis, IUIComponentEmbed<UIScrollbarComponent>>builder()
				.put(EnumUIAxis.X, new UIChildlessComponentEmbed<>(UIScrollbarComponent.class, suppressThisEscapedWarning(() -> this),
						createScrollbarEmbedArguments(suppressThisEscapedWarning(() -> this),
								getEmbedVerticalScrollbarName(),
								EnumUIAxis.X,
								arguments,
								scrollRelativeProgressMap,
								thumbRelativeSizeMap)))
				.put(EnumUIAxis.Y, new UIChildlessComponentEmbed<>(UIScrollbarComponent.class, suppressThisEscapedWarning(() -> this),
						createScrollbarEmbedArguments(suppressThisEscapedWarning(() -> this),
								getEmbedHorizontalScrollbarName(),
								EnumUIAxis.Y,
								arguments,
								scrollRelativeProgressMap,
								thumbRelativeSizeMap)))
				.build();

		this.eventTargetListenersInitializer = new OneUseRunnable(() ->
				getScrollbarEmbeds().forEach((axis, scrollbarEmbed) -> {
					IUIEventListener<IUIEventMouseWheel> conventionalListener =
							UIScrollbarComponent.FunctionalWheelEventListener.ofConventional(scrollbarEmbed.getComponent(),
									() -> thisReference.getOptional()
											.map(UIScrollPanelComponent::getContentBounds)
											.map(contentBounds -> OptionalDouble.of(axis.getSize(contentBounds)))
											.orElseGet(OptionalDouble::empty)
							);
					addEventListener(EnumUIEventDOMType.WHEEL.getEventType(),
							UIPhasedDelegatingEventListener.of(Sets.immutableEnumSet(IUIEvent.EnumPhase.AT_TARGET, IUIEvent.EnumPhase.BUBBLING_PHASE),
									conventionalListener),
							false);
				})
		);

		this.scrollRelativeProgressMap = Maps.immutableEnumMap(scrollRelativeProgressMap);
		this.scrollbarThumbRelativeSizeMap = Maps.immutableEnumMap(thumbRelativeSizeMap);
	}

	public static IIdentifier getPropertyScrollbarSidesIdentifier() {
		return PROPERTY_SCROLLBAR_SIDES_IDENTIFIER;
	}

	public static IIdentifier getPropertyScrollbarThicknessesIdentifier() {
		return PROPERTY_SCROLLBAR_THICKNESSES_IDENTIFIER;
	}

	public static @NonNls String getPropertyScrollbarThicknesses() {
		return PROPERTY_SCROLLBAR_THICKNESSES;
	}

	public static @NonNls String getPropertyScrollbarSides() {
		return PROPERTY_SCROLLBAR_SIDES;
	}

	protected static IUIComponentEmbedArguments createScrollbarEmbedArguments(UIScrollPanelComponent owner,
	                                                                          CharSequence key,
	                                                                          EnumUIAxis axis,
	                                                                          IUIComponentArguments arguments,
	                                                                          Map<EnumUIAxis, IBindingField<Double>> scrollRelativeProgressMap,
	                                                                          Map<EnumUIAxis, IBindingField<Double>> thumbRelativeSizeMap) {
		OptionalWeakReference<UIScrollPanelComponent> ownerReference = OptionalWeakReference.of(owner);
		return arguments.computeEmbedArgument(key,
				arguments1 -> new UIScrollbarComponent(
						ownerReference.getOptional()
								.<IUIComponentArguments>map(owner1 -> {
									String keyPrefix = UINamespaceUtilities.getUniqueInternalBindingNamespace(owner1);

									IIdentifier scrollRelativeProgressKey =
											ImmutableIdentifier.ofInterning(keyPrefix,
													getInternalBindingScrollbarRelativeProgressPrefix() + '.' + axis.name(),
													false, true);
									IIdentifier thumbRelativeSizeKey =
											ImmutableIdentifier.ofInterning(keyPrefix,
													getInternalBindingScrollbarThumbRelativeSizePrefix() + '.' + axis.name(),
													false, true);

									IValueHolder<IUIComponentArguments> pointerArguments = DefaultValueHolder.of(arguments1);

									UIComponentEmbedUtilities.withMappingsIfUndefined(pointerArguments,
											ImmutableMap.of(
													UIScrollbarComponent.getPropertyScrollDirectionIdentifier(),
													() -> UIImmutablePropertyMappingValue.ofValue(UIScrollbarComponent.getAxisToConventionalDirectionMap().get(axis))
											));

									if (UIComponentEmbedUtilities.withMappingsIfUndefined(pointerArguments,
											ImmutableMap.of(
													UIScrollbarComponent.getPropertyScrollRelativeProgressIdentifier(),
													() -> UIImmutablePropertyMappingValue.ofKey(scrollRelativeProgressKey),
													UIScrollbarComponent.getPropertyThumbRelativeSizeIdentifier(),
													() -> UIImmutablePropertyMappingValue.ofKey(thumbRelativeSizeKey)
											))) {
										IBindingField<Double> scrollRelativeProgressField =
												ImmutableBindingField.of(scrollRelativeProgressKey,
														new MemoryObservableField<>(Double.class, suppressBoxing(0D)));
										IBindingField<Double> thumbRelativeProgressField =
												ImmutableBindingField.of(thumbRelativeSizeKey,
														new MemoryObservableField<>(Double.class, suppressBoxing(1D)));

										scrollRelativeProgressField.getField().getNotifier().subscribe(
												ContentScrollOffsetUpdater.ofDecorated(owner1, axis, UIConfiguration.getInstance().getLogger())
										);

										scrollRelativeProgressMap.put(axis, scrollRelativeProgressField);
										thumbRelativeSizeMap.put(axis, thumbRelativeProgressField);
									}

									return pointerArguments.getValue()
											.orElseThrow(AssertionError::new);
								}).orElse(arguments1)
				),
				new SupplierShapeDescriptor<>(() ->
						ownerReference.getOptional()
								.map(owner2 -> {
									// COMMENT collect data for self
									Object2DoubleMap<EnumUIAxis> scrollbarThicknesses = owner2.getScrollbarThicknesses().getValue();
									Point2D contentScrollOffset = owner2.getContentScrollOffset(); // COMMENT the scrollbar should resist translation caused by itself
									Rectangle2D owner2ShapeBounds = getContentBounds(owner2);
									EnumUISide scrollbarSide = AssertionUtilities.assertNonnull(owner2.getScrollbarSides().getValue().get(axis));
									EnumUISide scrollbarSideOpposite = scrollbarSide.getOpposite().orElseThrow(IllegalStateException::new);
									double scrollbarThickness = scrollbarThicknesses.getDouble(axis);

									// COMMENT generate
									Rectangle2D result = new Rectangle2D.Double(-contentScrollOffset.getX(), -contentScrollOffset.getY(),
											owner2ShapeBounds.getWidth() + scrollbarThicknesses.getDouble(EnumUIAxis.X),
											owner2ShapeBounds.getHeight() + scrollbarThicknesses.getDouble(EnumUIAxis.Y));
									scrollbarSideOpposite.setValue(result,
											scrollbarSide.getValue(result)
													+ scrollbarSide.inwardsBy(scrollbarThickness).orElseThrow(IllegalStateException::new));

									// COMMENT consider the other scrollbar
									EnumUIAxis otherAxis = axis.getOpposite();
									EnumUISide otherScrollbarSide = AssertionUtilities.assertNonnull(owner2.getScrollbarSides().getValue().get(otherAxis));
									double otherScrollbarThickness = scrollbarThicknesses.getDouble(otherAxis);
									otherScrollbarSide.setValue(result,
											otherScrollbarSide.getValue(result)
													+ otherScrollbarSide.inwardsBy(otherScrollbarThickness).orElseThrow(IllegalStateException::new));

									return result;
								})
								.orElseGet(Rectangle2D.Double::new)
				));
	}

	public static @NonNls String getInternalBindingScrollbarRelativeProgressPrefix() {
		return INTERNAL_BINDING_SCROLLBAR_RELATIVE_PROGRESS_PREFIX;
	}

	public static @NonNls String getInternalBindingScrollbarThumbRelativeSizePrefix() {
		return INTERNAL_BINDING_SCROLLBAR_THUMB_RELATIVE_SIZE_PREFIX;
	}

	public static @NonNls String getEmbedVerticalScrollbarName() {
		return EMBED_VERTICAL_SCROLLBAR_NAME;
	}

	public static @NonNls String getEmbedHorizontalScrollbarName() {
		return EMBED_HORIZONTAL_SCROLLBAR_NAME;
	}

	@Override
	public void transformChildren(AffineTransform transform) {
		super.transformChildren(transform);
		AffineTransformUtilities.translateByPoint(transform, getContentScrollOffset());
	}

	protected static Rectangle2D getContentFullBounds(UIScrollPanelComponent instance) {
		// COMMENT 0, 0 is after 'transformChildren'
		return instance.getChildren().stream().unordered()
				.filter(FunctionUtilities.notPredicate(ImmutableSet.copyOf(Spliterators.iterator(getEmbedComponents(instance)))::contains)) // COMMENT embeds are not content
				.map(IUIComponent::getShape)
				.map(Shape::getBounds2D)
				.reduce(getContentBounds(instance), Rectangle2D::createUnion);
	}

	@Override
	protected Iterable<? extends IUIComponentEmbed<?>> getComponentEmbeds() {
		return Iterables.concat(super.getComponentEmbeds(),
				getScrollbarEmbeds().values());
	}

	@Override
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		super.initializeBindings(bindingActionConsumerSupplier);
		BindingUtilities.supplyBindingAction(bindingActionConsumerSupplier,
				() -> ImmutableBindingAction.bind(Iterables.concat(
						ImmutableList.of(
								getScrollbarSides(), getScrollbarThicknesses()
						),
						getScrollRelativeProgressMap().values(),
						getScrollbarThumbRelativeSizeMap().values()
				)));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void initialize0(IUIComponentContext context) {
		super.initialize0(context);
		getContentScrollOffset().setLocation(0D, 0D);
		getLastContentFullBounds().setFrame(0D, 0D, 0D, 0D);
	}

	@Override
	public void cleanupBindings() {
		getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumer ->
				BindingUtilities.supplyBindingAction(bindingActionConsumer,
						() -> ImmutableBindingAction.unbind(Iterables.concat(
								ImmutableList.of(
										getScrollbarSides(), getScrollbarThicknesses()
								),
								getScrollRelativeProgressMap().values(),
								getScrollbarThumbRelativeSizeMap().values()
						)))
		);
		super.cleanupBindings();
	}

	@SuppressWarnings("ObjectAllocationInLoop")
	@Override
	@OverridingMethodsMustInvokeSuper
	protected void update0(IUIComponentContext context) {
		super.update0(context);

		getContentScrollOffset().setLocation(getNextContentScrollOffset());

		// COMMENT 0th - collect data
		Rectangle2D lastContentBounds = getLastContentBounds();
		Rectangle2D contentBounds = getContentBounds(this);
		Rectangle2D lastContentFullBounds = getLastContentFullBounds(), previousContentFullBounds = (Rectangle2D) lastContentFullBounds.clone();
		Rectangle2D contentFullBounds = getContentFullBounds(this);

		// COMMENT 0.5th - boolean
		boolean updateThumbSize = false;
		boolean updateScrollProgress = false;
		if (!lastContentBounds.equals(contentBounds)) {
			updateThumbSize = true;
			lastContentBounds.setFrame(contentBounds); // COMMENT update first, setValue may use this
		}
		if (!lastContentFullBounds.equals(contentFullBounds)) {
			updateThumbSize = updateScrollProgress = true;
			lastContentFullBounds.setFrame(contentFullBounds); // COMMENT update first, setValue may use this
		}

		// COMMENT 1st - update thumb size
		if (updateThumbSize) {
			for (EnumUIAxis axis : EnumUIAxis.values()) {
				double thumbRelativeSize = axis.getSize(contentBounds) / axis.getSize(contentFullBounds);
				if (Double.isFinite(thumbRelativeSize)) {
					// COMMENT the divisor may be 0D
					Optional.ofNullable(getScrollbarThumbRelativeSizeMap().get(axis))
							.ifPresent(scrollbarThumbRelativeSizeField ->
									scrollbarThumbRelativeSizeField.setValue(suppressBoxing(thumbRelativeSize)));
				}
			}
		}

		// COMMENT 2nd - update progress
		if (updateScrollProgress) {
			for (EnumUIAxis axis : EnumUIAxis.values()) {
				// COMMENT note that the scale is reversed, this can avoid another division which may result in another NaN
				double contentBoundsReverseScale = axis.getSize(previousContentFullBounds) / axis.getSize(contentFullBounds);
				if (Double.isFinite(contentBoundsReverseScale)) {
					// COMMENT the divisor may be 0D
					Optional.ofNullable(getScrollRelativeProgressMap().get(axis))
							.ifPresent(scrollRelativeProgressField ->
									scrollRelativeProgressField.setValue(suppressBoxing(
											suppressUnboxing(scrollRelativeProgressField.getValue()) * contentBoundsReverseScale
									)));
				}
			}
		}
	}

	protected Point2D getNextContentScrollOffset() {
		return nextContentScrollOffset;
	}

	protected Rectangle2D getLastContentBounds() {
		return lastContentBounds;
	}

	protected static Rectangle2D getContentBounds(UIScrollPanelComponent instance) {
		// COMMENT 0, 0 is after 'transformChildren'
		Rectangle2D bounds = IUIComponent.getShape(instance).getBounds2D();
		// COMMENT consider scrollbars
		for (EnumUIAxis axis : EnumUIAxis.values()) {
			EnumUISide scrollbarSide = AssertionUtilities.assertNonnull(instance.getScrollbarSides().getValue().get(axis));
			double scrollbarThickness = instance.getScrollbarThicknesses().getValue().getDouble(axis);
			scrollbarSide.setValue(bounds,
					scrollbarSide.getValue(bounds)
							+ scrollbarSide.inwardsBy(scrollbarThickness)
							.orElseThrow(IllegalStateException::new));
		}
		return UIObjectUtilities.unPositionRectangularShape(bounds, bounds);
	}

	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public boolean addChildren(Iterator<? extends IUIComponent> components) {
		// COMMENT add components before the scrollbars so that the scrollbars can draw above all others
		return addChildrenImpl(this,
				(self, child) -> self.getScrollbarEmbeds().values().stream()
						.map(IUIComponentEmbed::getComponent)
						.map(embedComponent -> CollectionUtilities.indexOf(self.getChildren(), embedComponent))
						.mapToInt(embedComponentIndex -> embedComponentIndex.orElseGet(self.getChildren()::size))
						.min()
						.orElseGet(self.getChildren()::size)
				, components);
	}

	protected @Immutable Map<EnumUIAxis, ? extends IBindingField<Double>> getScrollbarThumbRelativeSizeMap() {
		return scrollbarThumbRelativeSizeMap;
	}

	protected @Immutable Map<EnumUIAxis, ? extends IBindingField<Double>> getScrollRelativeProgressMap() {
		return scrollRelativeProgressMap;
	}

	protected IBindingField<Map<EnumUIAxis, EnumUISide>> getScrollbarSides() {
		return scrollbarSides;
	}

	protected IBindingField<Object2DoubleMap<EnumUIAxis>> getScrollbarThicknesses() {
		return scrollbarThicknesses;
	}

	protected Point2D getContentScrollOffset() {
		return contentScrollOffset;
	}

	protected Rectangle2D getLastContentFullBounds() {
		return lastContentFullBounds;
	}

	protected @Immutable Map<EnumUIAxis, ? extends IUIComponentEmbed<? extends UIScrollbarComponent>> getScrollbarEmbeds() {
		return scrollbarEmbeds;
	}

	@Override
	protected IUIRendererContainerContainer<IUIComponentRenderer<?>> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	@Override
	protected SetMultimap<IIdentifier, UIEventListenerWithParameters> getEventTargetListeners() {
		eventTargetListenersInitializer.run();
		return super.getEventTargetListeners();
	}

	public static class DefaultRenderer<C extends UIScrollPanelComponent>
			extends UIShapeComponent.DefaultRenderer<C> {
		@UIRendererConstructor
		public DefaultRenderer(IUIRendererArguments arguments) {
			super(arguments);
		}
	}

	public static class ContentScrollOffsetUpdater
			extends DelegatingSubscriber<Double> {
		private final OptionalWeakReference<UIScrollPanelComponent> owner;
		private final EnumUIAxis axis;

		protected ContentScrollOffsetUpdater(Subscriber<? super Double> delegate, UIScrollPanelComponent owner, EnumUIAxis axis) {
			super(delegate);
			this.owner = OptionalWeakReference.of(owner);
			this.axis = axis;
		}

		public static DisposableSubscriber<Double> ofDecorated(UIScrollPanelComponent owner, EnumUIAxis axis, Logger logger) {
			return ReactiveUtilities.decorateAsListener(delegate -> new ContentScrollOffsetUpdater(delegate, owner, axis), logger);
		}

		@Override
		public void onNext(@Nonnull Double relativeScrollProgress) {
			super.onNext(relativeScrollProgress);
			getOwner().ifPresent(owner -> {
				@SuppressWarnings("AutoUnboxing") double relativeScrollProgress1 = relativeScrollProgress;
				EnumUIAxis axis = getAxis();
				Point2D nextContentScrollOffset = owner.getNextContentScrollOffset(); // COMMENT need to delay, otherwise the scrollbar will go berserk
				axis.setCoordinate(nextContentScrollOffset,
						-axis.getSize(owner.getLastContentFullBounds()) * relativeScrollProgress1);
			});
		}

		protected Optional<? extends UIScrollPanelComponent> getOwner() {
			return owner.getOptional();
		}

		protected EnumUIAxis getAxis() {
			return axis;
		}
	}
}
