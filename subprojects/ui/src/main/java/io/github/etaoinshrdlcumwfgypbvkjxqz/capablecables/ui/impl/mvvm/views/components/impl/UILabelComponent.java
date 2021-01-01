package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.text.IAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIComponentModifyShapeDescriptorBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text.TextUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CachedTask;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ColorUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.FloatUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DelegatingSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.ReactiveUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.ITuple3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.AutoSubscribingDisposable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.ImmutableSubscribeEvent;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NonNls;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import sun.misc.Cleaner;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressBoxing;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UILabelComponent
		extends UIDefaultComponent {
	// COMMENT I do not think there is a need to implement vertical text...  Think of it, when was the last time you see some vertical text online?
	public static final @NonNls String PROPERTY_TEXT = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.label.text";
	public static final @NonNls String PROPERTY_AUTO_RESIZE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.label.auto_resize";
	public static final @NonNls String PROPERTY_OVERFLOW_POLICY = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.label.overflow_policy";
	private static final IIdentifier PROPERTY_TEXT_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyText());
	private static final IIdentifier PROPERTY_AUTO_RESIZE_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyAutoResize());
	private static final IIdentifier PROPERTY_OVERFLOW_POLICY_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyOverflowPolicy());

	private final IUIRendererContainerContainer<IUIComponentRenderer<?>> rendererContainerContainer;

	@UIProperty(PROPERTY_TEXT)
	private final IBindingField<IAttributedText> text;
	@UIProperty(PROPERTY_AUTO_RESIZE)
	private final IBindingField<Boolean> autoResize;
	@UIProperty(PROPERTY_OVERFLOW_POLICY)
	private final IBindingField<IOverflowPolicy> overflowPolicy; // COMMENT accepted type: CharSequence
	private boolean resizeTextDimension = true;

	@UIComponentConstructor
	public UILabelComponent(IUIComponentArguments arguments) {
		super(arguments);

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(DefaultRenderer.class));

		@Immutable Map<IIdentifier, ? extends IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.text = IUIPropertyMappingValue.createBindingField(IAttributedText.class, ConstantValue.of(TextUtilities.getEmptyAttributedText()),
				mappings.get(getPropertyTextIdentifier()));
		this.autoResize = IUIPropertyMappingValue.createBindingField(Boolean.class, ConstantValue.of(suppressBoxing(true)),
				mappings.get(getPropertyAutoResizeIdentifier()));
		this.overflowPolicy = IUIPropertyMappingValue.createBindingField(IOverflowPolicy.class, ConstantValue.of(EnumOverflowPolicy.WARP),
				mappings.get(getPropertyOverflowPolicyIdentifier()),
				CharSequence.class,
				mappingValue -> EnumOverflowPolicy.valueOf(mappingValue.toString()));

		this.text.getField().getNotifier()
				.subscribe(TextDimensionInvalidatorSubscriber.ofDecorated(suppressThisEscapedWarning(() -> this), UIConfiguration.getInstance().getLogger()));
		this.overflowPolicy.getField().getNotifier()
				.subscribe(TextDimensionInvalidatorSubscriber.ofDecorated(suppressThisEscapedWarning(() -> this), UIConfiguration.getInstance().getLogger()));

		Cleaner.create(suppressThisEscapedWarning(() -> this),
				AutoSubscribingDisposable.of(UIEventBusEntryPoint.getBusPublisher(),
						ImmutableList.of(
								ModifyShapeDescriptorSubscriber.ofDecorated(suppressThisEscapedWarning(() -> this), UIConfiguration.getInstance().getLogger())
						)
				)::dispose);
	}

	@Override
	protected void update0(IUIComponentContext context) {
		super.update0(context);
		if (isResizeTextDimension()) {
			@SuppressWarnings("AutoUnboxing") boolean autoResize = getAutoResize().getValue();
			if (autoResize) {
				Rectangle2D currentBounds = IUIComponent.getShape(this).getBounds2D();
				Dimension2D targetDimension = calculateTextDimension();
				AffineTransform transform = AffineTransformUtilities.getTransformFromTo(currentBounds,
						new Rectangle2D.Double(currentBounds.getX(), currentBounds.getY(),
								targetDimension.getWidth(), targetDimension.getHeight()));
				modifyShape(() -> getShapeDescriptor().transform(transform)); // COMMENT should be before setting the boolean flag to ensure no cycles
			}
			setResizeTextDimension(false);
		}
	}

	protected boolean isResizeTextDimension() {
		return resizeTextDimension;
	}

	protected void setResizeTextDimension(boolean resizeTextDimension) {
		this.resizeTextDimension = resizeTextDimension;
	}

	protected IBindingField<IAttributedText> getText() {
		return text;
	}

	@SuppressWarnings("UnstableApiUsage")
	protected Dimension2D calculateTextDimension() {
		Iterable<? extends TextLayout> lines;

		IOverflowPolicy overflowPolicy = getOverflowPolicy().getValue();
		OptionalDouble autoResizeTextWidth = overflowPolicy.getAutoResizeTextWidth(getShapeDescriptor());
		if (autoResizeTextWidth.isPresent()) {
			float autoResizeTextWidth1 = FloatUtilities.saturatedCast(autoResizeTextWidth.getAsDouble());
			lines = TextUtilities.separateLines(getText().getValue().compile().getIterator()).stream()
					.map(line -> new LineBreakMeasurer(line, TextUtilities.getDefaultFontRenderContext())) // TODO proper frc
					.map(line -> new TextUtilities.LineBreakMeasurerAsTextLayoutIterator(line, line1 -> line1.nextLayout(autoResizeTextWidth1)))
					.flatMap(Streams::stream)
					.collect(ImmutableList.toImmutableList());
		} else {
			lines = TextUtilities.separateLines(getText().getValue().compile().getIterator()).stream()
					.map(line -> new TextLayout(line, TextUtilities.getDefaultFontRenderContext())) // TODO proper frc
					.collect(ImmutableList.toImmutableList());
		}

		return TextUtilities.getLinesDimension(lines);
	}

	public static IIdentifier getPropertyTextIdentifier() {
		return PROPERTY_TEXT_IDENTIFIER;
	}

	public static IIdentifier getPropertyAutoResizeIdentifier() {
		return PROPERTY_AUTO_RESIZE_IDENTIFIER;
	}

	public static IIdentifier getPropertyOverflowPolicyIdentifier() {
		return PROPERTY_OVERFLOW_POLICY_IDENTIFIER;
	}

	public static @NonNls String getPropertyText() {
		return PROPERTY_TEXT;
	}

	public static @NonNls String getPropertyAutoResize() {
		return PROPERTY_AUTO_RESIZE;
	}

	@Override
	public IUIRendererContainerContainer<IUIComponentRenderer<?>> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	public static @NonNls String getPropertyOverflowPolicy() {
		return PROPERTY_OVERFLOW_POLICY;
	}

	protected IBindingField<Boolean> getAutoResize() {
		return autoResize;
	}

	protected void invalidateTextDimension() {
		setResizeTextDimension(true);
	}

	protected IBindingField<IOverflowPolicy> getOverflowPolicy() {
		return overflowPolicy;
	}

	@Override
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		super.initializeBindings(bindingActionConsumerSupplier);
		BindingUtilities.supplyBindingAction(bindingActionConsumerSupplier, () ->
				ImmutableBindingAction.bind(ImmutableList.of(
						getText(),
						getAutoResize(), getOverflowPolicy()
				)));
	}

	@Override
	public void cleanupBindings() {
		getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumer ->
				BindingUtilities.supplyBindingAction(bindingActionConsumer, () ->
						ImmutableBindingAction.unbind(ImmutableList.of(
								getText(),
								getAutoResize(), getOverflowPolicy()
						))));
		super.cleanupBindings();
	}

	public enum EnumOverflowPolicy
			implements IOverflowPolicy {
		WARP {
			@Override
			public OptionalDouble getAutoResizeTextWidth(IShapeDescriptor<?> originalShapeDescriptor) {
				return OptionalDouble.of(originalShapeDescriptor.getShapeOutput().getBounds2D().getWidth()); // COMMENT do not resize
			}

			@Override
			public OptionalDouble getRenderTextWidth(IShapeDescriptor<?> resizedShapeDescriptor) {
				return OptionalDouble.of(resizedShapeDescriptor.getShapeOutput().getBounds2D().getWidth()); // COMMENT limit rendering within view
			}
		},
		TRUNCATE {
			@Override
			public OptionalDouble getAutoResizeTextWidth(IShapeDescriptor<?> originalShapeDescriptor) {
				return OptionalDouble.empty(); // COMMENT resize to match
			}

			@Override
			public OptionalDouble getRenderTextWidth(IShapeDescriptor<?> resizedShapeDescriptor) {
				return OptionalDouble.empty(); // COMMENT infinitely far is OK
			}
		},
	}

	public static class TextDimensionInvalidatorSubscriber
			extends DelegatingSubscriber<Object> {
		private final OptionalWeakReference<UILabelComponent> owner;

		protected TextDimensionInvalidatorSubscriber(Subscriber<? super Object> delegate, UILabelComponent owner) {
			super(delegate);
			this.owner = OptionalWeakReference.of(owner);
		}

		public static DisposableSubscriber<Object> ofDecorated(UILabelComponent owner, Logger logger) {
			return ReactiveUtilities.decorateAsListener(delegate -> new TextDimensionInvalidatorSubscriber(delegate, owner), logger);
		}

		@Override
		public void onNext(@Nonnull Object o) {
			super.onNext(o);
			getOwner().ifPresent(UILabelComponent::invalidateTextDimension);
		}

		protected Optional<? extends UILabelComponent> getOwner() {
			return owner.getOptional();
		}
	}

	public static class ModifyShapeDescriptorSubscriber
			extends DelegatingSubscriber<UIComponentModifyShapeDescriptorBusEvent> {
		private final OptionalWeakReference<UILabelComponent> owner;

		protected ModifyShapeDescriptorSubscriber(Subscriber<? super UIComponentModifyShapeDescriptorBusEvent> delegate, UILabelComponent owner) {
			super(delegate);
			this.owner = OptionalWeakReference.of(owner);
		}

		@SuppressWarnings("AnonymousInnerClass")
		public static DisposableSubscriber<UIComponentModifyShapeDescriptorBusEvent> ofDecorated(UILabelComponent owner, Logger logger) {
			return new EventBusSubscriber<UIComponentModifyShapeDescriptorBusEvent>(
					ImmutableSubscribeEvent.of(EventPriority.LOWEST, true),
					ReactiveUtilities.decorateAsListener(delegate -> new ModifyShapeDescriptorSubscriber(delegate, owner), logger)
			) {
				@Override
				public void onNext(UIComponentModifyShapeDescriptorBusEvent event) {
					onNextImpl(event);
				}
			};
		}

		@Override
		@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
		public void onNext(@Nonnull UIComponentModifyShapeDescriptorBusEvent event) {
			super.onNext(event);
			getOwner()
					.filter(Predicate.isEqual(event.getComponent()))
					.ifPresent(UILabelComponent::invalidateTextDimension);
		}

		protected Optional<? extends UILabelComponent> getOwner() {
			return owner.getOptional();
		}
	}

	public interface IOverflowPolicy {
		OptionalDouble getAutoResizeTextWidth(IShapeDescriptor<?> originalShapeDescriptor);

		OptionalDouble getRenderTextWidth(IShapeDescriptor<?> resizedShapeDescriptor);
	}

	public static class DefaultRenderer<C extends UILabelComponent>
			extends UIDefaultComponentRenderer<C> {
		public static final @NonNls String PROPERTY_DEFAULT_FOREGROUND_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.label.color.foreground.default";
		public static final @NonNls String PROPERTY_DEFAULT_BACKGROUND_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.label.color.background.default";
		private static final IIdentifier PROPERTY_DEFAULT_FOREGROUND_COLOR_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyDefaultForegroundColor());
		private static final IIdentifier PROPERTY_DEFAULT_BACKGROUND_COLOR_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyDefaultBackgroundColor());

		@UIProperty(PROPERTY_DEFAULT_FOREGROUND_COLOR)
		private final IBindingField<Color> defaultForegroundColor; // TODO Color to Paint
		@UIProperty(PROPERTY_DEFAULT_BACKGROUND_COLOR)
		private final IBindingField<Color> defaultBackgroundColor; // TODO Color to Paint

		@SuppressWarnings("UnstableApiUsage")
		private final CachedTask<ITuple3<? extends AttributedString, ? extends FontRenderContext, ? extends OptionalDouble>, Iterable<? extends TextLayout>> textLayoutTask =
				new CachedTask<>(data -> {
					OptionalDouble right = data.getRight();
					if (right.isPresent()) {
						float right1 = FloatUtilities.saturatedCast(right.getAsDouble());
						return TextUtilities.separateLines(data.getLeft().getIterator()).stream()
								.map(line -> new LineBreakMeasurer(TextUtilities.ensureNonEmpty(line), data.getMiddle()))
								.map(line -> new TextUtilities.LineBreakMeasurerAsTextLayoutIterator(line, line1 -> line1.nextLayout(right1)))
								.flatMap(Streams::stream)
								.collect(ImmutableList.toImmutableList());
					} else {
						// COMMENT draw without limits
						return TextUtilities.separateLines(data.getLeft().getIterator()).stream()
								.map(line -> new TextLayout(TextUtilities.ensureNonEmpty(line), data.getMiddle()))
								.collect(ImmutableList.toImmutableList());
					}
				}, Object::equals);

		@UIRendererConstructor
		public DefaultRenderer(IUIRendererArguments arguments) {
			super(arguments);

			@Immutable Map<IIdentifier, IUIPropertyMappingValue> mappings = arguments.getMappingsView();

			this.defaultForegroundColor = IUIPropertyMappingValue.createBindingField(Color.class,
					ConstantValue.of(Color.WHITE),
					mappings.get(getPropertyDefaultForegroundColorIdentifier()));
			this.defaultBackgroundColor = IUIPropertyMappingValue.createBindingField(Color.class,
					ConstantValue.of(ColorUtilities.getColorless()),
					mappings.get(getPropertyDefaultBackgroundColorIdentifier()));
		}

		public static IIdentifier getPropertyDefaultForegroundColorIdentifier() {
			return PROPERTY_DEFAULT_FOREGROUND_COLOR_IDENTIFIER;
		}

		public static IIdentifier getPropertyDefaultBackgroundColorIdentifier() {
			return PROPERTY_DEFAULT_BACKGROUND_COLOR_IDENTIFIER;
		}

		public static @NonNls String getPropertyDefaultForegroundColor() {
			return PROPERTY_DEFAULT_FOREGROUND_COLOR;
		}

		public static @NonNls String getPropertyDefaultBackgroundColor() {
			return PROPERTY_DEFAULT_BACKGROUND_COLOR;
		}

		@Override
		public void render(IUIComponentContext context, EnumRenderStage stage) {
			super.render(context, stage);
			getContainer().ifPresent(container -> {
				if (stage == EnumRenderStage.PRE_CHILDREN) {
					IOverflowPolicy overflowPolicy = container.getOverflowPolicy().getValue();
					Rectangle2D componentBounds = IUIComponent.getShape(container).getBounds2D();
					double componentBoundsWidth = componentBounds.getWidth();

					IUIComponentContext.withGraphics(context, graphics -> {
						Point2D textPen = new Point2D.Double(componentBounds.getX(), componentBounds.getY());
						OptionalDouble textWidth = overflowPolicy.getRenderTextWidth(container.getShapeDescriptor());

						graphics.setColor(getDefaultForegroundColor().getValue());
						graphics.setBackground(getDefaultBackgroundColor().getValue());
						TextUtilities.drawLines(graphics, textPen, componentBoundsWidth,
								getTextLayoutTask().apply(ImmutableTuple3.of(container.getText().getValue().compile(), graphics.getFontRenderContext(), textWidth)).iterator());
					});
				}
			});
		}

		protected IBindingField<Color> getDefaultForegroundColor() {
			return defaultForegroundColor;
		}

		protected IBindingField<Color> getDefaultBackgroundColor() {
			return defaultBackgroundColor;
		}

		@Override
		public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
			super.initializeBindings(bindingActionConsumerSupplier);
			BindingUtilities.supplyBindingAction(bindingActionConsumerSupplier,
					() -> ImmutableBindingAction.bind(ImmutableList.of(
							getDefaultForegroundColor(), getDefaultBackgroundColor()
					)));
		}

		@Override
		public void cleanupBindings() {
			getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumer -> BindingUtilities.supplyBindingAction(bindingActionConsumer,
					() -> ImmutableBindingAction.unbind(ImmutableList.of(
							getDefaultForegroundColor(), getDefaultBackgroundColor()
					))));
			super.cleanupBindings();
		}

		protected CachedTask<? super ITuple3<? extends AttributedString, ? extends FontRenderContext, ? extends OptionalDouble>, ? extends Iterable<? extends TextLayout>> getTextLayoutTask() {
			return textLayoutTask;
		}
	}
}
