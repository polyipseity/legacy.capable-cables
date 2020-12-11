package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text.IAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.AbstractDelegatingShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text.TextUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CachedTask;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.FloatUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DefaultDisposableObserver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.*;

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
	@Nullable
	private Dimension2D textDimension;
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	private final LoadingCache<IShapeDescriptor<?>, IShapeDescriptor<?>> autoResizeShapeDescriptorCache =
			CacheUtilities.newCacheBuilderSingleThreaded().weakKeys().build(
					CacheLoader.from(shapeDescriptor -> {
						assert shapeDescriptor != null;
						return new AutoResizeShapeDescriptor(this, shapeDescriptor);
					})
			);

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
				.subscribe(new TextDimensionInvalidatorDisposableObserver(suppressThisEscapedWarning(() -> this)));
		this.overflowPolicy.getField().getNotifier()
				.subscribe(new TextDimensionInvalidatorDisposableObserver(suppressThisEscapedWarning(() -> this)));
	}

	protected IBindingField<IAttributedText> getText() {
		return text;
	}

	protected void invalidateTextDimension() {
		textDimension = null;
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

	public static class AutoResizeShapeDescriptor
			extends AbstractDelegatingShapeDescriptor<Shape, IShapeDescriptor<?>> {
		private final OptionalWeakReference<UILabelComponent> owner;

		public AutoResizeShapeDescriptor(UILabelComponent owner, IShapeDescriptor<?> delegated) {
			super(delegated);
			this.owner = OptionalWeakReference.of(owner);
		}

		@Override
		public Shape getShapeOutput() {
			return getOwner()
					.filter(owner -> suppressUnboxing(owner.getAutoResize().getValue()))
					.<Shape>map(owner -> {
						Rectangle2D bounds = getDelegate().getShapeOutput().getBounds2D();
						Dimension2D dimension = owner.getTextDimension();
						return new Rectangle2D.Double(bounds.getX(), bounds.getY(), dimension.getWidth(), dimension.getHeight());
					})
					.orElseGet(getDelegate()::getShapeOutput);
		}

		protected Optional<? extends UILabelComponent> getOwner() {
			return owner.getOptional();
		}

		@Override
		public boolean isDynamic() {
			return true;
		}
	}

	@SuppressWarnings("UnstableApiUsage")
	protected Dimension2D getTextDimension() {
		@Nullable Dimension2D result = textDimension;
		if (result == null) {
			IOverflowPolicy overflowPolicy = getOverflowPolicy().getValue();
			// COMMENT need to use super since 'AutoResizeShapeDescriptor' calls this method, there may a better way though...
			OptionalDouble autoResizeTextWidth = overflowPolicy.getAutoResizeTextWidth(super.getShapeDescriptor());
			if (autoResizeTextWidth.isPresent()) {
				float autoResizeTextWidth1 = FloatUtilities.saturatedCast(autoResizeTextWidth.getAsDouble());
				result = TextUtilities.getLinesDimension(
						TextUtilities.separateLines(getText().getValue().compile().getIterator()).stream()
								.map(line -> new LineBreakMeasurer(line, TextUtilities.getDefaultFontRenderContext())) // TODO proper frc
								.map(line -> new TextUtilities.LineBreakMeasurerAsTextLayoutIterator(line, line1 -> line1.nextLayout(autoResizeTextWidth1)))
								.flatMap(Streams::stream)
								.collect(ImmutableList.toImmutableList())
				);
			} else {
				result = TextUtilities.getLinesDimension(
						TextUtilities.separateLines(getText().getValue().compile().getIterator()).stream()
								.map(line -> new TextLayout(line, TextUtilities.getDefaultFontRenderContext())) // TODO proper frc
								.collect(ImmutableList.toImmutableList())
				);
			}
			textDimension = result;
		}
		return result;
	}

	protected IBindingField<IOverflowPolicy> getOverflowPolicy() {
		return overflowPolicy;
	}

	@Override
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier, () ->
				ImmutableBinderAction.bind(
						getText(),
						getAutoResize(), getOverflowPolicy()
				));
	}

	@Override
	public IShapeDescriptor<?> getShapeDescriptor() {
		return getAutoResizeShapeDescriptorCache().getUnchecked(super.getShapeDescriptor());
	}

	protected LoadingCache<IShapeDescriptor<?>, IShapeDescriptor<?>> getAutoResizeShapeDescriptorCache() {
		return autoResizeShapeDescriptorCache;
	}

	@Override
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier, () ->
						ImmutableBinderAction.unbind(
								getText(),
								getAutoResize(), getOverflowPolicy()
						)));
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

	public static class TextDimensionInvalidatorDisposableObserver
			extends DefaultDisposableObserver<Object> {
		private final OptionalWeakReference<UILabelComponent> owner;

		public TextDimensionInvalidatorDisposableObserver(UILabelComponent owner) {
			this.owner = OptionalWeakReference.of(owner);
		}

		@Override
		public void onNext(@NonNull Object o) {
			super.onNext(o);
			getOwner().ifPresent(UILabelComponent::invalidateTextDimension);
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
		}

		@Override
		public void render(IUIComponentContext context, EnumRenderStage stage) {
			super.render(context, stage);
			getContainer().ifPresent(container -> {
				if (stage == EnumRenderStage.PRE_CHILDREN) {
					IOverflowPolicy overflowPolicy = container.getOverflowPolicy().getValue();
					Rectangle2D componentBounds = IUIComponent.getShape(container).getBounds2D();
					double componentBoundsWidth = componentBounds.getWidth();

					try (AutoCloseableGraphics2D graphics = AutoCloseableGraphics2D.of(context.createGraphics())) {
						Point2D textPen = new Point2D.Double(componentBounds.getX(), componentBounds.getY());
						OptionalDouble textWidth = overflowPolicy.getRenderTextWidth(container.getShapeDescriptor());
						TextUtilities.drawLines(graphics, textPen, componentBoundsWidth,
								getTextLayoutTask().apply(ImmutableTuple3.of(container.getText().getValue().compile(), graphics.getFontRenderContext(), textWidth)));
					}
				}
			});
		}

		protected CachedTask<? super ITuple3<? extends AttributedString, ? extends FontRenderContext, ? extends OptionalDouble>, ? extends Iterable<? extends TextLayout>> getTextLayoutTask() {
			return textLayoutTask;
		}
	}
}
