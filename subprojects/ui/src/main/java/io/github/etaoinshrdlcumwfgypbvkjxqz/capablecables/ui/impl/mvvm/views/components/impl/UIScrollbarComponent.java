package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.embed.IUIComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouseWheel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.UINamespaceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.binding.UIImmutablePropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIAbstractEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed.UIChildlessComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed.UIComponentEmbedUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.SupplierShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIAxis;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.EnumTimeUnit;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IValueHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.DefaultValueHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.RangedBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods.ImmutableBindingMethodDestination;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl.KeyboardDeviceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.OptionalUtilities;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.*;

// COMMENT note: while initially writing this, the author's mind had gone insane and started doing copy-and-paste programming
public class UIScrollbarComponent
		extends UIShapeComponent {
	public static final @NonNls String PROPERTY_SCROLL_DIRECTION = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.scrollbar.direction";
	public static final @NonNls String PROPERTY_SCROLL_RELATIVE_PROGRESS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.scrollbar.relative_progress";
	public static final @NonNls String PROPERTY_THUMB_RELATIVE_SIZE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.scrollbar.thumb.relative_size";
	public static final @NonNls String PROPERTY_THUMB_MOVEMENT_RELATIVE_SPEED = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.scrollbar.thumb.movement.relative_speed";
	public static final @NonNls String PROPERTY_BUTTON_SIZE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.scrollbar.buttons.size";
	public static final @NonNls String EMBED_THUMB_NAME = "thumb";
	public static final @NonNls String EMBED_BUTTON_1_NAME = "button1";
	public static final @NonNls String EMBED_BUTTON_2_NAME = "button2";
	public static final @NonNls String INTERNAL_BINDING_BUTTON_ACTIVATE_PREFIX = "scrollbar.button.activate";
	public static final @NonNls String INTERNAL_BINDING_BUTTON_ACTIVATED_PREFIX = "scrollbar.button.activated";
	public static final @NonNls String INTERNAL_BINDING_BUTTON_CANCELED_PREFIX = "scrollbar.button.canceled";
	private static final IIdentifier PROPERTY_SCROLL_DIRECTION_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyScrollDirection());
	private static final IIdentifier PROPERTY_SCROLL_RELATIVE_PROGRESS_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyScrollRelativeProgress());
	private static final IIdentifier PROPERTY_THUMB_RELATIVE_SIZE_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyThumbRelativeSize());
	private static final IIdentifier PROPERTY_THUMB_MOVEMENT_RELATIVE_SPEED_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyThumbMovementRelativeSpeed());
	private static final IIdentifier PROPERTY_BUTTON_SIZE_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyButtonSize());
	@SuppressWarnings("UnstableApiUsage")
	private static final @Immutable Map<EnumUIAxis, EnumUISide> AXIS_TO_CONVENTIONAL_DIRECTION_MAP = Maps.immutableEnumMap(ImmutableMap.of(
			EnumUIAxis.X, EnumUISide.RIGHT,
			EnumUIAxis.Y, EnumUISide.DOWN
	));
	@UIProperty(PROPERTY_SCROLL_DIRECTION)
	private final IBindingField<EnumUISide> scrollDirection;
	@UIProperty(PROPERTY_SCROLL_RELATIVE_PROGRESS)
	private final IBindingField<Double> scrollRelativeProgress; // COMMENT relative to the whole bar
	@UIProperty(PROPERTY_THUMB_RELATIVE_SIZE)
	private final IBindingField<Double> thumbRelativeSize;
	@UIProperty(PROPERTY_THUMB_MOVEMENT_RELATIVE_SPEED)
	private final IBindingField<Double> thumbMovementRelativeSpeed; // COMMENT per second, relative to the thumb relative size
	@UIProperty(PROPERTY_BUTTON_SIZE)
	private final IBindingField<Double> buttonSize;
	private final IUIComponentEmbed<UIButtonComponent> thumbEmbed;
	private final @Immutable Map<EnumScrollbarState, IUIComponentEmbed<? extends UIButtonComponent>> buttonEmbeds;
	private final IUIRendererContainerContainer<IUIComponentRenderer<?>> rendererContainerContainer;
	private final Set<EnumScrollbarState> scrollbarStates = Collections.newSetFromMap(
			MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(EnumScrollbarState.values().length).makeMap()
	);
	private @Nullable Double thumbPullingProgressStart;
	private @Nullable Double thumbPullingStartingPoint;

	@SuppressWarnings("UnstableApiUsage")
	@UIComponentConstructor
	public UIScrollbarComponent(IUIComponentArguments arguments) {
		super(arguments);

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(DefaultRenderer.class));

		Map<IIdentifier, ? extends IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		OptionalWeakReference<UIScrollbarComponent> thisReference = OptionalWeakReference.of(suppressThisEscapedWarning(() -> this));

		this.scrollDirection = IUIPropertyMappingValue.createBindingField(EnumUISide.class, ConstantValue.of(EnumUISide.DOWN),
				mappings.get(getPropertyScrollDirectionIdentifier()));
		this.scrollRelativeProgress = RangedBindingField.of(
				IUIPropertyMappingValue.createBindingField(Double.class, ConstantValue.of(suppressBoxing(0D)),
						mappings.get(getPropertyScrollRelativeProgressIdentifier())),
				ConstantValue.of(suppressBoxing(0D)),
				() -> {
					// COMMENT see 'PropertyThumbRelativeSizeObserver'
					return thisReference.getOptional()
							.map(UIScrollbarComponent::getThumbRelativeSize)
							.map(IBindingField::getValue)
							.map(thumbRelativeSizeValue -> suppressBoxing(
									1D - suppressUnboxing(thumbRelativeSizeValue)
							))
							.orElse(null);
				}
		);
		this.thumbRelativeSize = IUIPropertyMappingValue.createBindingField(Double.class, ConstantValue.of(suppressBoxing(1D)),
				mappings.get(getPropertyThumbRelativeSizeIdentifier()));
		this.thumbMovementRelativeSpeed = IUIPropertyMappingValue.createBindingField(Double.class, ConstantValue.of(suppressBoxing(1D)),
				mappings.get(getPropertyThumbMovementRelativeSpeedIdentifier()));
		this.buttonSize = IUIPropertyMappingValue.createBindingField(Double.class, ConstantValue.of(suppressBoxing(10D)),
				mappings.get(getPropertyButtonSizeIdentifier()));

		this.thumbRelativeSize.getField().getNotifier()
				.subscribe(new PropertyThumbRelativeSizeObserver(suppressThisEscapedWarning(() -> this), UIConfiguration.getInstance().getLogger()));

		/* TODO slay the dragons
		====================================================================================================
		In front of you is a dragon, sleeping quietly.  It is huge, like a mountain.
		The scales of the dragon is as black as the cold universe,
		yet it shines like a star under the light of the warm star.
		You could not see its head or tail.[1]
		It evokes fear deep within your heart.

		It has not discovered you.

		You decide that fighting the dragon head on is not a great idea, so you start to think of solutions.
		Dragons, like all earth-like animals, have a heart.  Therefore, you decide to target the heart.

		However, such a large animal has a really large volume, so it would be difficult to find the heart.

		You find it after following some strong 'code smell', seemingly coming from the heart.
		With that, you take your trusty 'refactor' sword and stab the skin as hard as you can.

		...
		   ...
		......
		      ...
		...   ...
		   ......
		.........

		The dragon falls out of the world.
		====================================================================================================
		[1] Definitely not because we were too lazy.
		 */
		this.thumbEmbed = new UIChildlessComponentEmbed<>(UIButtonComponent.class, suppressThisEscapedWarning(() -> this),
				arguments.computeEmbedArgument(getEmbedThumbName(),
						arguments1 -> new UIButtonComponent(
								thisReference.getOptional()
										.<IUIComponentArguments>map(this1 -> {
											String keyPrefix = UINamespaceUtilities.getUniqueInternalBindingNamespace(this1);

											IIdentifier onActivateKey =
													ImmutableIdentifier.ofInterning(keyPrefix,
															getInternalBindingButtonActivatePrefix() + '.' + getEmbedThumbName(),
															false, true);
											IIdentifier onActivatedKey =
													ImmutableIdentifier.ofInterning(keyPrefix,
															getInternalBindingButtonActivatedPrefix() + '.' + getEmbedThumbName(),
															false, true);
											IIdentifier onCanceledKey =
													ImmutableIdentifier.ofInterning(keyPrefix,
															getInternalBindingButtonCanceledPrefix() + '.' + getEmbedThumbName(),
															false, true);

											IValueHolder<IUIComponentArguments> pointerArguments = DefaultValueHolder.of(arguments1);

											if (UIComponentEmbedUtilities.withMappingsIfUndefined(pointerArguments,
													ImmutableMap.of(
															UIButtonComponent.getMethodOnActivateIdentifier(),
															() -> UIImmutablePropertyMappingValue.ofKey(onActivateKey),
															UIButtonComponent.getMethodOnActivatedIdentifier(),
															() -> UIImmutablePropertyMappingValue.ofKey(onActivatedKey),
															UIButtonComponent.getMethodOnCanceledIdentifier(),
															() -> UIImmutablePropertyMappingValue.ofKey(onCanceledKey)
													))) {
												this1.getEmbedBindings()
														.addAll(ImmutableList.of(
																ImmutableBindingMethodDestination.of(UIButtonComponent.IUIEventActivate.class,
																		onActivateKey,
																		event -> thisReference.getOptional()
																				.ifPresent(this2 -> this2.onButtonActivate(EnumScrollbarState.PULLING, event))),
																ImmutableBindingMethodDestination.of(IUIEvent.class,
																		onActivatedKey,
																		event -> thisReference.getOptional()
																				.ifPresent(this2 -> this2.onButtonFinish(EnumScrollbarState.PULLING, event))),
																ImmutableBindingMethodDestination.of(IUIEvent.class,
																		onCanceledKey,
																		event -> thisReference.getOptional()
																				.ifPresent(this2 -> this2.onButtonFinish(EnumScrollbarState.PULLING, event)))
														));
											}

											return pointerArguments.getValue()
													.orElseThrow(AssertionError::new);
										}).orElse(arguments1)
						),
						new SupplierShapeDescriptor<>(() ->
								thisReference.getOptional()
										.map(this1 -> {
											Shape this1Shape = IUIComponent.getShape(this1);
											EnumUISide endingScrollDirection = this1.getScrollDirection().getValue();
											EnumUISide startingScrollDirection = endingScrollDirection.getOpposite().orElseThrow(IllegalStateException::new);
											@SuppressWarnings("AutoUnboxing") double buttonSize = this1.getButtonSize().getValue();
											@SuppressWarnings("AutoUnboxing") double thumbRelativeSize = this1.getThumbRelativeSize().getValue();
											@SuppressWarnings("AutoUnboxing") double scrollRelativeProgress = this1.getScrollRelativeProgress().getValue();

											Rectangle2D this1ShapeBounds = this1Shape.getBounds2D();

											Rectangle2D result = new Rectangle2D.Double(0D, 0D,
													this1ShapeBounds.getWidth(), this1ShapeBounds.getHeight());
											double thumbMovementSize = getThumbMovementSize(this1);

											// COMMENT the width
											double thumbThicknessStart = startingScrollDirection.getValue(result)
													+ startingScrollDirection.inwardsBy(buttonSize + thumbMovementSize * scrollRelativeProgress)
													.orElseThrow(IllegalStateException::new);
											startingScrollDirection.setValue(result, thumbThicknessStart);
											endingScrollDirection.setValue(result, thumbThicknessStart
													+ startingScrollDirection.inwardsBy(thumbMovementSize * thumbRelativeSize)
													.orElseThrow(IllegalStateException::new));

											return result;
										})
										.orElseGet(Rectangle2D.Double::new)
						)));
		this.buttonEmbeds = Maps.immutableEnumMap(ImmutableMap.of(
				// COMMENT button 1, initial position of the thumb
				EnumScrollbarState.SCROLLING_BACKWARD,
				new UIChildlessComponentEmbed<>(UIButtonComponent.class, suppressThisEscapedWarning(() -> this),
						createButtonEmbedArguments(suppressThisEscapedWarning(() -> this),
								getEmbedButton1Name(),
								arguments,
								EnumScrollbarState.SCROLLING_BACKWARD)),
				// COMMENT button 2, position limit of the thumb
				EnumScrollbarState.SCROLLING_FORWARD,
				new UIChildlessComponentEmbed<>(UIButtonComponent.class, suppressThisEscapedWarning(() -> this),
						createButtonEmbedArguments(suppressThisEscapedWarning(() -> this),
								getEmbedButton2Name(),
								arguments,
								EnumScrollbarState.SCROLLING_FORWARD))
		));
	}

	protected static IUIComponentEmbedArguments createButtonEmbedArguments(UIScrollbarComponent owner,
	                                                                       CharSequence key,
	                                                                       IUIComponentArguments arguments,
	                                                                       EnumScrollbarState state) {
		OptionalWeakReference<UIScrollbarComponent> ownerReference = OptionalWeakReference.of(owner);
		String key1 = key.toString();
		return arguments.computeEmbedArgument(key1,
				arguments1 -> new UIButtonComponent(
						ownerReference.getOptional()
								.<IUIComponentArguments>map(owner1 -> {
									String keyPrefix = UINamespaceUtilities.getUniqueInternalBindingNamespace(owner1);

									IIdentifier onActivateKey =
											ImmutableIdentifier.ofInterning(keyPrefix,
													getInternalBindingButtonActivatePrefix() + '.' + key1,
													false, true);
									IIdentifier onActivatedKey =
											ImmutableIdentifier.ofInterning(keyPrefix,
													getInternalBindingButtonActivatedPrefix() + '.' + key1,
													false, true);
									IIdentifier onCanceledKey =
											ImmutableIdentifier.ofInterning(keyPrefix,
													getInternalBindingButtonCanceledPrefix() + '.' + key1,
													false, true);

									IValueHolder<IUIComponentArguments> pointerArguments = DefaultValueHolder.of(arguments1);

									if (UIComponentEmbedUtilities.withMappingsIfUndefined(pointerArguments,
											ImmutableMap.of(
													UIButtonComponent.getMethodOnActivateIdentifier(),
													() -> UIImmutablePropertyMappingValue.ofKey(onActivateKey),
													UIButtonComponent.getMethodOnActivatedIdentifier(),
													() -> UIImmutablePropertyMappingValue.ofKey(onActivatedKey),
													UIButtonComponent.getMethodOnCanceledIdentifier(),
													() -> UIImmutablePropertyMappingValue.ofKey(onCanceledKey)
											))) {
										owner1.getEmbedBindings()
												.addAll(ImmutableList.of(
														ImmutableBindingMethodDestination.of(UIButtonComponent.IUIEventActivate.class,
																onActivateKey,
																event -> ownerReference.getOptional()
																		.ifPresent(owner2 -> owner2.onButtonActivate(state, event))),
														ImmutableBindingMethodDestination.of(IUIEvent.class,
																onActivatedKey,
																event -> ownerReference.getOptional()
																		.ifPresent(owner2 -> owner2.onButtonFinish(state, event))),
														ImmutableBindingMethodDestination.of(IUIEvent.class,
																onCanceledKey,
																event -> ownerReference.getOptional()
																		.ifPresent(owner2 -> owner2.onButtonFinish(state, event)))
												));
									}

									return pointerArguments.getValue()
											.orElseThrow(AssertionError::new);
								}).orElse(arguments1)
				),
				new SupplierShapeDescriptor<>(() ->
						ownerReference.getOptional()
								.map(owner1 -> {
									Shape ownerShape = IUIComponent.getShape(owner1);
									Rectangle2D ownerShapeBounds = ownerShape.getBounds2D();
									EnumUISide scrollDirection = owner1.getScrollDirection().getValue();
									EnumUIAxis scrollAxis = scrollDirection.getAxis();
									EnumUISide buttonSide = state.getButtonSide(scrollDirection)
											.orElseThrow(IllegalArgumentException::new);
									EnumUISide buttonSideOpposite = buttonSide.getOpposite()
											.orElseThrow(IllegalStateException::new);

									double scrollbarLength = scrollAxis.getSize(ownerShapeBounds);
									// COMMENT if the scrollbar length is too small, split the length into 2 evenly for both buttons
									@SuppressWarnings({"AutoUnboxing", "MagicNumber"}) double buttonSize =
											Math.min(owner1.getButtonSize().getValue(), scrollbarLength / 2D);

									Rectangle2D result = new Rectangle2D.Double(0D, 0D,
											ownerShapeBounds.getWidth(), ownerShapeBounds.getHeight());
									buttonSideOpposite.setValue(result,
											buttonSide.getValue(result)
													+ buttonSide.inwardsBy(buttonSize)
													.orElseThrow(IllegalStateException::new));

									return result;
								})
								.orElseGet(Rectangle2D.Double::new)
				));
	}

	public static IIdentifier getPropertyScrollDirectionIdentifier() {
		return PROPERTY_SCROLL_DIRECTION_IDENTIFIER;
	}

	public static IIdentifier getPropertyScrollRelativeProgressIdentifier() {
		return PROPERTY_SCROLL_RELATIVE_PROGRESS_IDENTIFIER;
	}

	protected IBindingField<Double> getThumbRelativeSize() {
		return thumbRelativeSize;
	}

	public static IIdentifier getPropertyThumbRelativeSizeIdentifier() {
		return PROPERTY_THUMB_RELATIVE_SIZE_IDENTIFIER;
	}

	public static IIdentifier getPropertyThumbMovementRelativeSpeedIdentifier() {
		return PROPERTY_THUMB_MOVEMENT_RELATIVE_SPEED_IDENTIFIER;
	}

	public static IIdentifier getPropertyButtonSizeIdentifier() {
		return PROPERTY_BUTTON_SIZE_IDENTIFIER;
	}

	public static @NonNls String getEmbedThumbName() {
		return EMBED_THUMB_NAME;
	}

	public static @NonNls String getInternalBindingButtonActivatePrefix() {
		return INTERNAL_BINDING_BUTTON_ACTIVATE_PREFIX;
	}

	public static @NonNls String getInternalBindingButtonActivatedPrefix() {
		return INTERNAL_BINDING_BUTTON_ACTIVATED_PREFIX;
	}

	public static @NonNls String getInternalBindingButtonCanceledPrefix() {
		return INTERNAL_BINDING_BUTTON_CANCELED_PREFIX;
	}

	protected void onButtonActivate(EnumScrollbarState state, UIButtonComponent.IUIEventActivate event) {
		UIButtonComponent.UIDefaultEventActivate.handleMouseEventCommonly(event); // COMMENT only accept mouse
		if (event.isDefaultPrevented())
			getScrollbarStates().add(state);
	}

	protected void onButtonFinish(EnumScrollbarState state, IUIEvent event) {
		getScrollbarStates().remove(state);
		setThumbPullingProgressStart(null);
		setThumbPullingStartingPoint(null);
		event.preventDefault();
	}

	protected IBindingField<EnumUISide> getScrollDirection() {
		return scrollDirection;
	}

	protected IBindingField<Double> getButtonSize() {
		return buttonSize;
	}

	protected IBindingField<Double> getScrollRelativeProgress() {
		return scrollRelativeProgress;
	}

	@SuppressWarnings("MagicNumber")
	protected static double getThumbMovementSize(UIScrollbarComponent instance) {
		Shape shape = IUIComponent.getShape(instance);
		EnumUISide endingScrollDirection = instance.getScrollDirection().getValue();
		@SuppressWarnings("AutoUnboxing") double buttonSize = instance.getButtonSize().getValue();

		return Math.max(endingScrollDirection.getAxis().getSize(shape.getBounds2D()) - buttonSize * 2D, 0D);
	}

	public static @NonNls String getEmbedButton1Name() {
		return EMBED_BUTTON_1_NAME;
	}

	public static @NonNls String getEmbedButton2Name() {
		return EMBED_BUTTON_2_NAME;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<EnumScrollbarState> getScrollbarStates() {
		return scrollbarStates;
	}

	public static @NonNls String getPropertyScrollDirection() {
		return PROPERTY_SCROLL_DIRECTION;
	}

	public static @NonNls String getPropertyScrollRelativeProgress() {
		return PROPERTY_SCROLL_RELATIVE_PROGRESS;
	}

	public static @NonNls String getPropertyThumbRelativeSize() {
		return PROPERTY_THUMB_RELATIVE_SIZE;
	}

	public static @NonNls String getPropertyButtonSize() {
		return PROPERTY_BUTTON_SIZE;
	}

	public static @NonNls String getPropertyThumbMovementRelativeSpeed() {
		return PROPERTY_THUMB_MOVEMENT_RELATIVE_SPEED;
	}

	public static @Immutable Map<EnumUIAxis, EnumUISide> getAxisToConventionalDirectionMap() {
		return AXIS_TO_CONVENTIONAL_DIRECTION_MAP;
	}

	@Override
	protected Iterable<? extends IUIComponentEmbed<?>> getComponentEmbeds() {
		return Iterables.concat(super.getComponentEmbeds(),
				ImmutableList.of(
						getThumbEmbed()
				),
				getButtonEmbeds().values()
		);
	}

	@Override
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier, () ->
				ImmutableBinderAction.bind(
						getScrollDirection(), getScrollRelativeProgress(),
						getThumbRelativeSize(), getThumbMovementRelativeSpeed(),
						getButtonSize()
				));
	}

	@Override
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier, () ->
						ImmutableBinderAction.unbind(
								getScrollDirection(), getScrollRelativeProgress(),
								getThumbRelativeSize(), getThumbMovementRelativeSpeed(),
								getButtonSize()
						)));
		super.cleanupBindings();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void update0(IUIComponentContext context) {
		super.update0(context);
		Set<EnumScrollbarState> scrollbarStates = getScrollbarStates();
		if (scrollbarStates.contains(EnumScrollbarState.PULLING)) {
			if (!getThumbPullingProgressStart().isPresent())
				setThumbPullingProgressStart(getScrollRelativeProgress().getValue());
			context.getViewContext().getInputDevices().getPointerDevice()
					.map(IPointerDevice::getPositionView)
					.ifPresent(pointerPosition -> {
						EnumUISide scrollDirection = getScrollDirection().getValue();
						double thumbPullingCoordinate = scrollDirection.getAxis().getCoordinate(pointerPosition);

						if (!getThumbPullingStartingPoint().isPresent())
							setThumbPullingStartingPoint(suppressBoxing(thumbPullingCoordinate));

						double lastThumbPullingCoordinate = getThumbPullingStartingPoint()
								.orElse(thumbPullingCoordinate); // COMMENT no starting point - no delta
						double thumbPullingDiff = thumbPullingCoordinate - lastThumbPullingCoordinate;
						double thumbRelativeDiff = thumbPullingDiff / getThumbMovementSize(this);
						double directionalThumbRelativeDiff = scrollDirection.outwardsBy(thumbRelativeDiff)
								.orElseThrow(IllegalStateException::new);
						assert getThumbPullingProgressStart().isPresent();
						double relativeProgress = getThumbPullingProgressStart().getAsDouble() + directionalThumbRelativeDiff;

						getScrollRelativeProgress().setValue(suppressBoxing(relativeProgress));
					});
		} else if (!scrollbarStates.isEmpty()) {
			// COMMENT we are not pulling the thumb
			double nominalThumbDiff = getThumbMovementSpeed(this)
					* getUpdateTimeDelta().orElse(0L)
					* EnumTimeUnit.getScale(EnumTimeUnit.SECOND, EnumTimeUnit.NANOSECOND);
			double thumbDiff = scrollbarStates.stream().unordered()
					.mapToDouble(scrollbarState -> scrollbarState.getDiff(nominalThumbDiff))
					.sum();
			getScrollRelativeProgress().setValue(suppressBoxing(
					suppressUnboxing(getScrollRelativeProgress().getValue())
							+ thumbDiff
			));
		}
	}

	protected static double getThumbMovementSpeed(UIScrollbarComponent instance) {
		return suppressUnboxing(instance.getThumbMovementRelativeSpeed().getValue())
				* suppressUnboxing(instance.getThumbRelativeSize().getValue());
	}

	protected OptionalDouble getThumbPullingProgressStart() {
		return OptionalUtilities.ofDouble(thumbPullingProgressStart);
	}

	protected OptionalDouble getThumbPullingStartingPoint() {
		return OptionalUtilities.ofDouble(thumbPullingStartingPoint);
	}

	protected IBindingField<Double> getThumbMovementRelativeSpeed() {
		return thumbMovementRelativeSpeed;
	}

	protected void setThumbPullingStartingPoint(@Nullable Double thumbPullingStartingPoint) {
		this.thumbPullingStartingPoint = thumbPullingStartingPoint;
	}

	protected void setThumbPullingProgressStart(@Nullable Double thumbPullingProgressStart) {
		this.thumbPullingProgressStart = thumbPullingProgressStart;
	}

	protected IUIComponentEmbed<? extends UIButtonComponent> getThumbEmbed() {
		return thumbEmbed;
	}

	@Override
	protected IUIRendererContainerContainer<IUIComponentRenderer<?>> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	protected @Immutable Map<EnumScrollbarState, ? extends IUIComponentEmbed<? extends UIButtonComponent>> getButtonEmbeds() {
		return buttonEmbeds;
	}

	public static class DefaultRenderer<C extends UIScrollbarComponent>
			extends UIShapeComponent.DefaultRenderer<C> {
		@UIRendererConstructor
		public DefaultRenderer(IUIRendererArguments arguments) {
			super(arguments);
		}
	}

	public static class PropertyThumbRelativeSizeObserver
			extends LoggingDisposableObserver<Double> {
		private final OptionalWeakReference<UIScrollbarComponent> owner;

		public PropertyThumbRelativeSizeObserver(UIScrollbarComponent owner, Logger logger) {
			super(logger);
			this.owner = OptionalWeakReference.of(owner);
		}

		@Override
		public void onNext(@Nonnull Double value) {
			super.onNext(value);
			getOwner()
					.map(UIScrollbarComponent::getScrollRelativeProgress)
					.ifPresent(IField::updateValue);
		}

		protected Optional<? extends UIScrollbarComponent> getOwner() {
			return owner.getOptional();
		}
	}

	public enum EnumScrollbarState {
		PULLING {
			@Override
			public double getDiff(double diff) {
				return 0D;
			}

			@Override
			public Optional<? extends EnumUISide> getButtonSide(EnumUISide scrollDirection) {
				return Optional.empty();
			}
		},
		SCROLLING_FORWARD {
			@Override
			public double getDiff(double diff) {
				return diff;
			}

			@Override
			public Optional<? extends EnumUISide> getButtonSide(EnumUISide scrollDirection) {
				return Optional.of(scrollDirection);
			}
		},
		SCROLLING_BACKWARD {
			@Override
			public double getDiff(double diff) {
				return -diff;
			}

			@Override
			public Optional<? extends EnumUISide> getButtonSide(EnumUISide scrollDirection) {
				return Optional.of(
						scrollDirection.getOpposite()
								.orElseThrow(IllegalStateException::new)
				);
			}
		},
		;

		public abstract double getDiff(double diff);

		public abstract Optional<? extends EnumUISide> getButtonSide(EnumUISide scrollDirection);
	}

	public static class FunctionalWheelEventListener<T extends UIScrollbarComponent>
			extends UIAbstractEventListener<IUIEventMouseWheel> {
		private final OptionalWeakReference<T> owner;
		private final BiConsumer<@Nonnull ? super T, @Nonnull ? super IUIEventMouseWheel> action;

		protected FunctionalWheelEventListener(T owner,
		                                       BiConsumer<@Nonnull ? super T, @Nonnull ? super IUIEventMouseWheel> action) {
			this.owner = OptionalWeakReference.of(owner);
			this.action = action;
		}

		public static <T extends UIScrollbarComponent> FunctionalWheelEventListener<T> ofConventional(T owner, Supplier<@Nonnull ? extends OptionalDouble> deltaDivisorSupplier) {
			return of(owner, (owner1, event) -> {
				EnumUISide scrollDirection = owner1.getScrollDirection().getValue();
				EnumUIAxis scrollAxis = scrollDirection.getAxis();

				boolean scroll;
				switch (scrollAxis) {
					case X:
						// COMMENT has keyboard and when shift mod is active
						scroll = event.getViewContext().getInputDevices().getKeyboardDevice()
								.filter(KeyboardDeviceUtilities::isShiftModifierActive)
								.isPresent();
						break;
					case Y:
						// COMMENT no keyboards or when shift mod is inactive
						scroll = !event.getViewContext().getInputDevices().getKeyboardDevice()
								.filter(KeyboardDeviceUtilities::isShiftModifierActive)
								.isPresent();
						break;
					default:
						throw new AssertionError();
				}

				if (scroll) {
					IBindingField<Double> scrollRelativeProgressField = owner1.getScrollRelativeProgress();

					@SuppressWarnings("AutoUnboxing") double scrollRelativeProgress = scrollRelativeProgressField.getValue();
					// COMMENT scrolling down gives negative delta
					double scrollRelativeProgressDiff = -event.getDelta() / deltaDivisorSupplier.get().orElse(0D); // COMMENT 0D cancels the scroll (non-finite)

					if (Double.isFinite(scrollRelativeProgressDiff)) {
						scrollRelativeProgressField.setValue(suppressBoxing(scrollRelativeProgress + scrollRelativeProgressDiff));
						event.stopPropagation();
					}
				}
			});
		}

		public static <T extends UIScrollbarComponent> FunctionalWheelEventListener<T> of(T owner,
		                                                                                  BiConsumer<@Nonnull ? super T, @Nonnull ? super IUIEventMouseWheel> action) {
			return new FunctionalWheelEventListener<>(owner, action);
		}

		@Override
		protected void accept0(IUIEventMouseWheel event) {
			getOwner()
					.filter(IUIComponent::isActive)
					.ifPresent(owner -> getAction().accept(owner, event));
		}

		protected Optional<? extends T> getOwner() {
			return owner.getOptional();
		}

		protected BiConsumer<? super T, ? super IUIEventMouseWheel> getAction() {
			return action;
		}
	}
}
