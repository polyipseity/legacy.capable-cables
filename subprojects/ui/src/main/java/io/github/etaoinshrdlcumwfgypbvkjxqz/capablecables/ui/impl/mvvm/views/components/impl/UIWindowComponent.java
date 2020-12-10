package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIStructureLifecycleContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.embed.IUIComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventFocus;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouseWheel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.UINamespaceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.binding.UIImmutablePropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIFunctionalEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIPhasedDelegatingEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed.UIAbstractComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed.UIChildlessComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed.UIComponentEmbedUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIComponentModifyShapeDescriptorBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.SupplierShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIAxis;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIRotation;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISideType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CollectionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.LoggingDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IValueHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.DefaultValueHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.ImmutableBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.MemoryObservableField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods.ImmutableBindingMethodDestination;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMaps;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.List;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.*;

public class UIWindowComponent
		extends UIShapeComponent
		implements IUIReshapeExplicitly<RectangularShape> {
	public static final @NonNls String PROPERTY_CONTROLS_SIDE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.side";
	public static final @NonNls String PROPERTY_CONTROLS_THICKNESS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.thickness";
	public static final @NonNls String PROPERTY_CONTROLS_DIRECTION = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.controls.direction";
	public static final @NonNls String PROPERTY_SCROLLBAR_SIDES = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.scrollbar.sides";
	public static final @NonNls String PROPERTY_SCROLLBAR_THICKNESSES = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.window.scrollbar.thicknesses"; // COMMENT 'thicknesses' is so cursed
	public static final @NonNls String INTERNAL_BINDING_CONTROLS_BUTTON_ACTIVATE_PREFIX = "window.controls.button.activate";
	public static final @NonNls String INTERNAL_BINDING_CONTROLS_BUTTON_ACTIVATED_PREFIX = "window.controls.button.activated";
	public static final @NonNls String INTERNAL_BINDING_SCROLLBAR_RELATIVE_PROGRESS_PREFIX = "window.scrollbar.relative_progress";
	public static final @NonNls String INTERNAL_BINDING_SCROLLBAR_THUMB_RELATIVE_SIZE_PREFIX = "window.scrollbar.thumb.relative_size";
	public static final @NonNls String EMBED_VERTICAL_SCROLLBAR_NAME = "scrollbar.vertical";
	public static final @NonNls String EMBED_HORIZONTAL_SCROLLBAR_NAME = "scrollbar.horizontal";
	private static final IIdentifier PROPERTY_CONTROLS_SIDE_IDENTIFIER = ImmutableIdentifier.of(getPropertyControlsSide());
	private static final IIdentifier PROPERTY_CONTROLS_THICKNESS_IDENTIFIER = ImmutableIdentifier.of(getPropertyControlsThickness());
	private static final IIdentifier PROPERTY_CONTROLS_DIRECTION_IDENTIFIER = ImmutableIdentifier.of(getPropertyControlsDirection());
	private static final IIdentifier PROPERTY_SCROLLBAR_SIDES_IDENTIFIER = ImmutableIdentifier.of(getPropertyScrollbarSides());
	private static final IIdentifier PROPERTY_SCROLLBAR_THICKNESSES_IDENTIFIER = ImmutableIdentifier.of(getPropertyScrollbarThicknesses());

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final AutoCloseableRotator<ModifyShapeDescriptorObserver, RuntimeException> modifyShapeDescriptorObserver =
			new AutoCloseableRotator<>(() -> new ModifyShapeDescriptorObserver(this, UIConfiguration.getInstance().getLogger()), Disposable::dispose);
	private final IUIRendererContainerContainer<IUIComponentRenderer<?>> rendererContainerContainer;

	@UIProperty(PROPERTY_CONTROLS_SIDE)
	private final IBindingField<EnumUISide> controlsSide;
	@UIProperty(PROPERTY_CONTROLS_THICKNESS)
	private final IBindingField<Double> controlsThickness;
	@UIProperty(PROPERTY_CONTROLS_DIRECTION)
	private final IBindingField<EnumUIRotation> controlsDirection;
	@UIProperty(PROPERTY_SCROLLBAR_SIDES)
	private final IBindingField<Map<EnumUIAxis, EnumUISide>> scrollbarSides;
	@UIProperty(PROPERTY_SCROLLBAR_THICKNESSES) // COMMENT accepted type: Map<? extends EnumUIAxis, ? extends Double>
	private final IBindingField<Object2DoubleMap<EnumUIAxis>> scrollbarThicknesses;

	private final IUIControlsEmbed<?> controlsEmbed;
	private final @Immutable Map<EnumUIAxis, IUIComponentEmbed<UIScrollbarComponent>> scrollbarEmbeds;

	private final Runnable eventTargetListenersInitializer;

	private final @Immutable Map<EnumUIAxis, IBindingField<Double>> scrollRelativeProgressMap; // COMMENT may not contain both axes
	private final @Immutable Map<EnumUIAxis, IBindingField<Double>> scrollbarThumbRelativeSizeMap; // COMMENT may not contain both axes
	private final Point2D contentScrollOffset = new Point2D.Double();
	private final Point2D nextContentScrollOffset = new Point2D.Double();
	private final Rectangle2D lastContentFullBounds = new Rectangle2D.Double();
	private final Rectangle2D lastContentBounds = new Rectangle2D.Double();

	@SuppressWarnings({"rawtypes", "RedundantSuppression", "UnstableApiUsage"})
	@UIComponentConstructor
	public UIWindowComponent(IUIComponentArguments arguments) {
		super(arguments);

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(DefaultRenderer.class));

		Map<IIdentifier, ? extends IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.controlsSide = IUIPropertyMappingValue.createBindingField(EnumUISide.class, ConstantValue.of(EnumUISide.UP), mappings.get(getPropertyControlsSideIdentifier()));
		this.controlsThickness = IUIPropertyMappingValue.createBindingField(Double.class, ConstantValue.of(suppressBoxing(10D)), mappings.get(getPropertyControlsThicknessIdentifier()));
		this.controlsDirection = IUIPropertyMappingValue.createBindingField(EnumUIRotation.class, ConstantValue.of(EnumUIRotation.CLOCKWISE), mappings.get(getPropertyControlsDirectionIdentifier()));
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

		OptionalWeakReference<UIWindowComponent> thisReference = OptionalWeakReference.of(suppressThisEscapedWarning(() -> this));
		// COMMENT here be dragons
		this.controlsEmbed = new UIDefaultControlsEmbed<>(
				UIShapeComponent.class,
				suppressThisEscapedWarning(() -> this),
				ImmutableMap.<String, IUIComponentEmbedArguments>builder()
						.put(IUIControlsEmbed.StaticHolder.getName(),
								arguments.computeEmbedArgument(IUIControlsEmbed.StaticHolder.getName(),
										UIShapeComponent::new,
										new SupplierShapeDescriptor<>(() ->
												thisReference.getOptional()
														.map(this1 -> {
															// COMMENT renders the controls
															EnumUISide controlsSide = this1.getControlsSide().getValue();
															Rectangle2D this1Bounds = IUIComponent.getShape(this1).getBounds2D();
															Point2D contentTranslation = getWindowContentTranslation(this1);

															Rectangle2D result = new Rectangle2D.Double(
																	-contentTranslation.getX(),
																	-contentTranslation.getY(),
																	this1Bounds.getWidth(),
																	this1Bounds.getHeight()
															);
															EnumUISide oppositeBorderSide = controlsSide.getOpposite().orElseThrow(IllegalStateException::new);
															oppositeBorderSide.setValue(result,
																	controlsSide.getValue(result)
																			+ controlsSide.inwardsBy(suppressUnboxing(this1.getControlsThickness().getValue()))
																			.orElseThrow(IllegalStateException::new));

															return result;
														})
														.orElseGet(Rectangle2D.Double::new)
										)))
						.put(EnumControlsAction.CLOSE.getName(),
								arguments.computeEmbedArgument(EnumControlsAction.CLOSE.getName(),
										arguments1 -> new UIButtonComponent(
												thisReference.getOptional()
														.<IUIComponentArguments>map(this1 -> {
															String keyPrefix = UINamespaceUtilities.getUniqueInternalBindingNamespace(this1);

															IIdentifier onActivateKey =
																	ImmutableIdentifier.of(keyPrefix,
																			getInternalBindingControlsButtonActivatePrefix() + '.' + EnumControlsAction.CLOSE.getName());
															IIdentifier onActivatedKey =
																	ImmutableIdentifier.of(keyPrefix,
																			getInternalBindingControlsButtonActivatedPrefix() + '.' + EnumControlsAction.CLOSE.getName());

															IValueHolder<IUIComponentArguments> pointerArguments = DefaultValueHolder.of(arguments1);

															if (UIComponentEmbedUtilities.withMappingsIfUndefined(pointerArguments,
																	ImmutableMap.of(
																			UIButtonComponent.getMethodOnActivateIdentifier(),
																			() -> UIImmutablePropertyMappingValue.of(null, onActivateKey),
																			UIButtonComponent.getMethodOnActivatedIdentifier(),
																			() -> UIImmutablePropertyMappingValue.of(null, onActivatedKey)
																	))) {
																this1.getEmbedBindings()
																		.addAll(ImmutableList.of(
																				ImmutableBindingMethodDestination.of(UIButtonComponent.IUIEventActivate.class,
																						onActivateKey,
																						event -> thisReference.getOptional()
																								.ifPresent(this2 -> this2.onControlsButtonActivate(EnumControlsAction.CLOSE, event))),
																				ImmutableBindingMethodDestination.of(IUIEvent.class,
																						onActivatedKey,
																						event -> thisReference.getOptional()
																								.ifPresent(this2 -> this2.onControlsButtonActivated(EnumControlsAction.CLOSE, event)))
																		));
															}

															return pointerArguments.getValue()
																	.orElseThrow(AssertionError::new);
														}).orElse(arguments1)
										),
										new SupplierShapeDescriptor<>(() ->
												thisReference.getOptional()
														.map(this1 -> {
															EnumUISide controlsSide = this1.getControlsSide().getValue();
															EnumUIRotation controlsRotation = this1.getControlsDirection().getValue();
															Rectangle2D controlsBounds = IUIComponent.getShape(this1.getControlsEmbed().getComponent()).getBounds2D();

															Rectangle2D result = UIObjectUtilities.unPositionedRectangularShape(controlsBounds, new Rectangle2D.Double());
															double size = controlsSide.getAxis().getSize(controlsBounds);
															EnumUISide startingResultSide = controlsRotation.rotateBy(controlsSide, 1L)
																	.orElseThrow(IllegalStateException::new);
															EnumUISide endingResultSide = startingResultSide.getOpposite()
																	.orElseThrow(IllegalStateException::new);
															endingResultSide.setValue(result,
																	startingResultSide.getValue(result)
																			+ startingResultSide.inwardsBy(size)
																			.orElseThrow(IllegalStateException::new));

															return result;
														})
														.orElseGet(Rectangle2D.Double::new)
										)
								))
						.build()
		);
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

		this.eventTargetListenersInitializer = new OneUseRunnable(() -> {
			addEventListener(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(), new UIFunctionalEventListener<IUIEventFocus>(e ->
					getParent().ifPresent(parent ->
							parent.moveChildToTop(this))
			), true);
			getScrollbarEmbeds().forEach((axis, scrollbarEmbed) -> {
				IUIEventListener<IUIEventMouseWheel> conventionalListener =
						UIScrollbarComponent.FunctionalWheelEventListener.ofConventional(scrollbarEmbed.getComponent(),
								() -> thisReference.getOptional()
										.map(UIWindowComponent::getWindowContentBounds)
										.map(contentBounds -> OptionalDouble.of(axis.getSize(contentBounds)))
										.orElseGet(OptionalDouble::empty)
						);
				addEventListener(EnumUIEventDOMType.WHEEL.getEventType(),
						UIPhasedDelegatingEventListener.of(IUIEvent.EnumPhase.AT_TARGET, conventionalListener),
						false);
			});
		});

		this.scrollRelativeProgressMap = Maps.immutableEnumMap(scrollRelativeProgressMap);
		this.scrollbarThumbRelativeSizeMap = Maps.immutableEnumMap(thumbRelativeSizeMap);
	}

	public static IIdentifier getPropertyControlsSideIdentifier() {
		return PROPERTY_CONTROLS_SIDE_IDENTIFIER;
	}

	public static IIdentifier getPropertyControlsThicknessIdentifier() {
		return PROPERTY_CONTROLS_THICKNESS_IDENTIFIER;
	}

	protected static Point2D getWindowContentTranslation(UIWindowComponent instance) {
		Point2D translation = getWindowContentBaseTranslation(instance);
		Point2D contentScrollOffset = instance.getContentScrollOffset(); // COMMENT do NOT modify
		translation.setLocation(translation.getX() + contentScrollOffset.getX(),
				translation.getY() + contentScrollOffset.getY());
		return translation;
	}

	public static @NonNls String getInternalBindingControlsButtonActivatePrefix() {
		return INTERNAL_BINDING_CONTROLS_BUTTON_ACTIVATE_PREFIX;
	}

	public static @NonNls String getInternalBindingControlsButtonActivatedPrefix() {
		return INTERNAL_BINDING_CONTROLS_BUTTON_ACTIVATED_PREFIX;
	}

	public static IIdentifier getPropertyControlsDirectionIdentifier() {
		return PROPERTY_CONTROLS_DIRECTION_IDENTIFIER;
	}

	public static @NonNls String getEmbedVerticalScrollbarName() {
		return EMBED_VERTICAL_SCROLLBAR_NAME;
	}

	public static @NonNls String getEmbedHorizontalScrollbarName() {
		return EMBED_HORIZONTAL_SCROLLBAR_NAME;
	}

	protected static Point2D getWindowContentBaseTranslation(UIWindowComponent instance) {
		Point2D translation = new Point2D.Double();
		EnumUISide controlsSide = instance.getControlsSide().getValue();
		if (controlsSide.getType() == EnumUISideType.LOCATION)
			controlsSide.getAxis().setCoordinate(translation, suppressUnboxing(instance.getControlsThickness().getValue()));
		return translation;
	}

	protected Point2D getContentScrollOffset() {
		return contentScrollOffset;
	}

	public static @NonNls String getInternalBindingScrollbarRelativeProgressPrefix() {
		return INTERNAL_BINDING_SCROLLBAR_RELATIVE_PROGRESS_PREFIX;
	}

	public static @NonNls String getInternalBindingScrollbarThumbRelativeSizePrefix() {
		return INTERNAL_BINDING_SCROLLBAR_THUMB_RELATIVE_SIZE_PREFIX;
	}

	public static IIdentifier getPropertyScrollbarSidesIdentifier() {
		return PROPERTY_SCROLLBAR_SIDES_IDENTIFIER;
	}

	public static IIdentifier getPropertyScrollbarThicknessesIdentifier() {
		return PROPERTY_SCROLLBAR_THICKNESSES_IDENTIFIER;
	}

	protected static IUIComponentEmbedArguments createScrollbarEmbedArguments(UIWindowComponent owner,
	                                                                          CharSequence key,
	                                                                          EnumUIAxis axis,
	                                                                          IUIComponentArguments arguments,
	                                                                          Map<EnumUIAxis, IBindingField<Double>> scrollRelativeProgressMap,
	                                                                          Map<EnumUIAxis, IBindingField<Double>> thumbRelativeSizeMap) {
		OptionalWeakReference<UIWindowComponent> ownerReference = OptionalWeakReference.of(owner);
		return arguments.computeEmbedArgument(key,
				arguments1 -> new UIScrollbarComponent(
						ownerReference.getOptional()
								.<IUIComponentArguments>map(owner1 -> {
									String keyPrefix = UINamespaceUtilities.getUniqueInternalBindingNamespace(owner1);

									IIdentifier scrollRelativeProgressKey =
											ImmutableIdentifier.of(keyPrefix,
													getInternalBindingScrollbarRelativeProgressPrefix() + '.' + axis.name());
									IIdentifier thumbRelativeSizeKey =
											ImmutableIdentifier.of(keyPrefix,
													getInternalBindingScrollbarThumbRelativeSizePrefix() + '.' + axis.name());

									IValueHolder<IUIComponentArguments> pointerArguments = DefaultValueHolder.of(arguments1);

									UIComponentEmbedUtilities.withMappingsIfUndefined(pointerArguments,
											ImmutableMap.of(
													UIScrollbarComponent.getPropertyScrollDirectionIdentifier(),
													() -> UIImmutablePropertyMappingValue.of(UIScrollbarComponent.getAxisToConventionalDirectionMap().get(axis), null)
											));

									if (UIComponentEmbedUtilities.withMappingsIfUndefined(pointerArguments,
											ImmutableMap.of(
													UIScrollbarComponent.getPropertyScrollRelativeProgressIdentifier(),
													() -> UIImmutablePropertyMappingValue.of(null, scrollRelativeProgressKey),
													UIScrollbarComponent.getPropertyThumbRelativeSizeIdentifier(),
													() -> UIImmutablePropertyMappingValue.of(null, thumbRelativeSizeKey)
											))) {
										IBindingField<Double> scrollRelativeProgressField =
												ImmutableBindingField.of(scrollRelativeProgressKey,
														new MemoryObservableField<>(Double.class, suppressBoxing(0D)));
										IBindingField<Double> thumbRelativeProgressField =
												ImmutableBindingField.of(thumbRelativeSizeKey,
														new MemoryObservableField<>(Double.class, suppressBoxing(1D)));

										scrollRelativeProgressField.getField().getNotifier().subscribe(
												new ContentScrollOffsetUpdater(owner1, axis, UIConfiguration.getInstance().getLogger())
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
									Rectangle2D owner2ShapeBounds = getWindowContentBounds(owner2);
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

	protected IBindingField<Object2DoubleMap<EnumUIAxis>> getScrollbarThicknesses() {
		return scrollbarThicknesses;
	}

	protected static Rectangle2D getWindowContentBounds(UIWindowComponent instance) {
		// COMMENT 0, 0 is after 'transformChildren'
		Rectangle2D bounds = IUIComponent.getShape(instance).getBounds2D();
		// COMMENT consider controls
		EnumUISide controlsSide = instance.getControlsSide().getValue();
		controlsSide.getAxis().setSize(bounds,
				controlsSide.getAxis().getSize(bounds)
						- suppressUnboxing(instance.getControlsThickness().getValue()));
		// COMMENT consider scrollbars
		for (EnumUIAxis axis : EnumUIAxis.values()) {
			EnumUISide scrollbarSide = AssertionUtilities.assertNonnull(instance.getScrollbarSides().getValue().get(axis));
			double scrollbarThickness = instance.getScrollbarThicknesses().getValue().getDouble(axis);
			scrollbarSide.setValue(bounds,
					scrollbarSide.getValue(bounds)
							+ scrollbarSide.inwardsBy(scrollbarThickness)
							.orElseThrow(IllegalStateException::new));
		}
		return UIObjectUtilities.unPositionedRectangularShape(bounds, bounds);
	}

	protected IBindingField<EnumUISide> getControlsSide() {
		return controlsSide;
	}

	protected IBindingField<Map<EnumUIAxis, EnumUISide>> getScrollbarSides() {
		return scrollbarSides;
	}

	public static @NonNls String getPropertyScrollbarThicknesses() {
		return PROPERTY_SCROLLBAR_THICKNESSES;
	}

	protected IBindingField<Double> getControlsThickness() {
		return controlsThickness;
	}

	protected IBindingField<EnumUIRotation> getControlsDirection() {
		return controlsDirection;
	}

	public static @NonNls String getPropertyScrollbarSides() {
		return PROPERTY_SCROLLBAR_SIDES;
	}

	@Override
	public boolean reshape(Predicate<? super IShapeDescriptor<? super RectangularShape>> action) throws ConcurrentModificationException {
		return IUIComponent.reshapeComponent(this, getShapeDescriptor(), action);
		// TODO reshape logic
	}

	protected void onControlsButtonActivate(@SuppressWarnings({"SameParameterValue", "unused"}) EnumControlsAction action, UIButtonComponent.IUIEventActivate event) {
		UIButtonComponent.UIDefaultEventActivate.handleEventCommonly(event);
	}

	@Override
	public void transformChildren(AffineTransform transform) {
		super.transformChildren(transform);
		AffineTransformUtilities.translateByPoint(transform, getWindowContentBaseTranslation(this));
		AffineTransformUtilities.translateByPoint(transform, getContentScrollOffset());
	}

	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	protected void onControlsButtonActivated(@SuppressWarnings("SameParameterValue") EnumControlsAction action, @SuppressWarnings("unused") IUIEvent event) {
		switch (action) {
			case CLOSE:
				getParent().ifPresent(parent ->
						parent.removeChildren(ImmutableSet.of(this)));
				break;
			default:
				break; // COMMENT just do nothing
		}
	}

	@Override
	protected SetMultimap<IIdentifier, UIEventListenerWithParameters> getEventTargetListeners() {
		eventTargetListenersInitializer.run();
		return super.getEventTargetListeners();
	}

	@SuppressWarnings("unchecked")
	@Override
	public IShapeDescriptor<RectangularShape> getShapeDescriptor() {
		return (IShapeDescriptor<RectangularShape>) super.getShapeDescriptor(); // COMMENT should be safe, see constructor
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean addChildren(Iterable<? extends IUIComponent> components) {
		// COMMENT add components before the controls so that the controls can draw above all others
		return addChildrenImpl(this,
				(self, child) ->
						Streams.concat(
								self.getScrollbarEmbeds().values().stream(),
								Stream.of(self.getControlsEmbed())
						)
								.map(IUIComponentEmbed::getComponent)
								.map(embedComponent -> CollectionUtilities.indexOf(self.getChildren(), embedComponent))
								.mapToInt(embedComponentIndex -> embedComponentIndex.orElseGet(self.getChildren()::size))
								.min()
								.orElseGet(self.getChildren()::size)
				, components);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void bind0(IUIStructureLifecycleContext context) {
		super.bind0(context);
		IUIReshapeExplicitly.refresh(this);
	}

	protected IUIControlsEmbed<?> getControlsEmbed() {
		return controlsEmbed;
	}

	public static @NonNls String getPropertyControlsDirection() {
		return PROPERTY_CONTROLS_DIRECTION;
	}

	public static @NonNls String getPropertyControlsSide() {
		return PROPERTY_CONTROLS_SIDE;
	}

	public static @NonNls String getPropertyControlsThickness() {
		return PROPERTY_CONTROLS_THICKNESS;
	}

	@Override
	protected Iterable<? extends IUIComponentEmbed<?>> getComponentEmbeds() {
		return Iterables.concat(super.getComponentEmbeds(),
				getScrollbarEmbeds().values(),
				ImmutableSet.of(
						getControlsEmbed() // COMMENT controls on top of scrollbars
				));
	}

	@Override
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(Iterables.concat(
						ImmutableList.of(
								getControlsSide(), getControlsThickness(), getControlsDirection(),
								getScrollbarSides(), getScrollbarThicknesses()
						),
						getScrollRelativeProgressMap().values(),
						getScrollbarThumbRelativeSizeMap().values()
				)));
	}

	@Override
	protected IUIRendererContainerContainer<IUIComponentRenderer<?>> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void cleanup0(IUIComponentContext context) {
		getModifyShapeDescriptorObserver().close();
		super.cleanup0(context);
	}

	protected AutoCloseableRotator<ModifyShapeDescriptorObserver, RuntimeException> getModifyShapeDescriptorObserver() { return modifyShapeDescriptorObserver; }

	protected @Immutable Map<EnumUIAxis, ? extends IBindingField<Double>> getScrollRelativeProgressMap() {
		return scrollRelativeProgressMap;
	}

	protected @Immutable Map<EnumUIAxis, ? extends IBindingField<Double>> getScrollbarThumbRelativeSizeMap() {
		return scrollbarThumbRelativeSizeMap;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void initialize0(IUIComponentContext context) {
		super.initialize0(context);
		getContentScrollOffset().setLocation(0D, 0D);
		getLastContentFullBounds().setFrame(0D, 0D, 0D, 0D);
		UIEventBusEntryPoint.<UIComponentModifyShapeDescriptorBusEvent>getEventBus()
				.subscribe(getModifyShapeDescriptorObserver().get());
	}

	public enum EnumControlsAction {
		CLOSE {
			@Override
			public @NonNls String getName() {
				return "close";
			}
		},
		;

		public abstract @NonNls String getName();
	}

	public interface IUIControlsEmbed<C extends UIShapeComponent>
			extends IUIComponentEmbed<C> {
		@Immutable Map<EnumControlsAction, ? extends IUIComponentEmbed<? extends UIButtonComponent>> getActionButtonsView();

		enum StaticHolder {
			;

			public static final @NonNls String NAME = "controls";

			public static @NonNls String getName() {
				return NAME;
			}
		}
	}

	protected static class ModifyShapeDescriptorObserver
			extends LoggingDisposableObserver<UIComponentModifyShapeDescriptorBusEvent> {
		private final OptionalWeakReference<UIWindowComponent> owner;

		public ModifyShapeDescriptorObserver(UIWindowComponent owner, Logger logger) {
			super(logger);
			this.owner = OptionalWeakReference.of(owner);
		}

		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(UIComponentModifyShapeDescriptorBusEvent event) {
			super.onNext(event);
			if (event.getStage() == EnumHookStage.POST)
				getOwner()
						.filter(owner -> owner.getParent().filter(p -> p.equals(event.getComponent())).isPresent())
						.ifPresent(IUIReshapeExplicitly::refresh);
		}

		protected Optional<? extends UIWindowComponent> getOwner() { return owner.getOptional(); }
	}

	public static class DefaultRenderer<C extends UIWindowComponent>
			extends UIShapeComponent.DefaultRenderer<C> {
		@UIRendererConstructor
		public DefaultRenderer(IUIRendererArguments arguments) {
			super(arguments);
		}
	}

	public static class UIDefaultControlsEmbed<C extends UIShapeComponent>
			extends UIAbstractComponentEmbed<C>
			implements IUIControlsEmbed<C> {
		private final Map<EnumControlsAction, IUIComponentEmbed<? extends UIButtonComponent>> actionButtons;

		@SuppressWarnings("UnstableApiUsage")
		public UIDefaultControlsEmbed(Class<C> type,
		                              IUIComponent owner,
		                              Map<? super String, ? extends IUIComponentEmbedArguments> arguments) {
			super(type, owner, AssertionUtilities.assertNonnull(arguments.get(StaticHolder.getName())));
			this.actionButtons = Maps.immutableEnumMap(Maps.asMap(EnumSet.allOf(EnumControlsAction.class),
					key -> {
						assert key != null;
						return new UIChildlessComponentEmbed<>(
								UIButtonComponent.class,
								getComponent(),
								AssertionUtilities.assertNonnull(arguments.get(key.getName()))
						);
					}
			));
		}

		@SuppressWarnings("UnstableApiUsage")
		@Override
		public @Immutable Map<EnumControlsAction, ? extends IUIComponentEmbed<? extends UIButtonComponent>> getActionButtonsView() {
			return Maps.immutableEnumMap(getActionButtons());
		}

		protected Map<EnumControlsAction, ? extends IUIComponentEmbed<? extends UIButtonComponent>> getActionButtons() {
			return actionButtons;
		}

		@Override
		public List<? extends IUIComponentEmbed<?>> getChildrenView() {
			return ImmutableList.copyOf(getActionButtons().values());
		}
	}

	@Override
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
						() -> ImmutableBinderAction.unbind(Iterables.concat(
								ImmutableList.of(
										getControlsSide(), getControlsThickness(), getControlsDirection(),
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
		Rectangle2D contentBounds = getWindowContentBounds(this);
		Rectangle2D lastContentFullBounds = getLastContentFullBounds(), previousContentFullBounds = (Rectangle2D) lastContentFullBounds.clone();
		Rectangle2D contentFullBounds = calculateWindowContentFullBounds(this);

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

	protected static Rectangle2D calculateWindowContentFullBounds(UIWindowComponent instance) {
		// COMMENT 0, 0 is after 'transformChildren'
		return instance.getChildren().stream().unordered()
				.filter(FunctionUtilities.notPredicate(UIDefaultComponent.getEmbedComponents(instance)::contains)) // COMMENT embeds are not content
				.map(IUIComponent::getShape)
				.map(Shape::getBounds2D)
				.reduce(UIWindowComponent.getWindowContentBounds(instance), Rectangle2D::createUnion);
	}

	protected Rectangle2D getLastContentFullBounds() {
		return lastContentFullBounds;
	}

	protected @Immutable Map<EnumUIAxis, ? extends IUIComponentEmbed<? extends UIScrollbarComponent>> getScrollbarEmbeds() {
		return scrollbarEmbeds;
	}

	public static class ContentScrollOffsetUpdater
			extends LoggingDisposableObserver<Double> {
		private final OptionalWeakReference<UIWindowComponent> owner;
		private final EnumUIAxis axis;

		public ContentScrollOffsetUpdater(UIWindowComponent owner, EnumUIAxis axis, Logger logger) {
			super(logger);
			this.owner = OptionalWeakReference.of(owner);
			this.axis = axis;
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

		protected Optional<? extends UIWindowComponent> getOwner() {
			return owner.getOptional();
		}

		protected EnumUIAxis getAxis() {
			return axis;
		}
	}
}
