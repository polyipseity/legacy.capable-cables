package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.FloatUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.DelegatingBindingField;
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
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UILabelComponent
		extends UIDefaultComponent {
	public static final @NonNls String PROPERTY_TEXT = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.text";
	public static final @NonNls String PROPERTY_AUTO_RESIZE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.auto_resize";
	private static final INamespacePrefixedString PROPERTY_TEXT_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyText());
	private static final INamespacePrefixedString PROPERTY_AUTO_RESIZE_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyAutoResize());

	private final IUIRendererContainerContainer<IUIComponentRenderer<?>> rendererContainerContainer;

	@UIProperty(PROPERTY_TEXT)
	private final IBindingField<IAttributedText> text;
	@UIProperty(PROPERTY_TEXT)
	private final IBindingField<Boolean> autoResize;
	@Nullable
	private Dimension2D textDimension;

	@UIComponentConstructor
	public UILabelComponent(IUIComponentArguments arguments) {
		super(arguments);

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(DefaultRenderer.class));

		@Immutable Map<INamespacePrefixedString, ? extends IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.text = new TextBindingField(suppressThisEscapedWarning(() -> this),
				IUIPropertyMappingValue.createBindingField(IAttributedText.class, TextUtilities.getEmptyAttributedText(),
						mappings.get(getPropertyTextLocation())));
		this.autoResize = IUIPropertyMappingValue.createBindingField(Boolean.class, true,
				mappings.get(getPropertyAutoResizeLocation()));
	}

	public static INamespacePrefixedString getPropertyTextLocation() {
		return PROPERTY_TEXT_LOCATION;
	}

	public static INamespacePrefixedString getPropertyAutoResizeLocation() {
		return PROPERTY_AUTO_RESIZE_LOCATION;
	}

	public static String getPropertyText() {
		return PROPERTY_TEXT;
	}

	public static String getPropertyAutoResize() {
		return PROPERTY_AUTO_RESIZE;
	}

	@SuppressWarnings("UnstableApiUsage")
	protected Dimension2D getTextDimension() {
		@Nullable Dimension2D result = textDimension;
		if (result == null)
			result = textDimension = TextUtilities.getLinesDimension(
					TextUtilities.separateLines(getText().getValue().compile().getIterator()).stream()
							.map(line -> new TextLayout(line, TextUtilities.getDefaultFontRenderContext())) // TODO proper frc
							.collect(ImmutableList.toImmutableList())
			);
		return result;
	}

	protected IBindingField<IAttributedText> getText() {
		return text;
	}

	protected void invalidateTextDimension() {
		textDimension = null;
	}

	@Override
	public IShapeDescriptor<?> getShapeDescriptor() {
		return new AutoResizeShapeDescriptor(this, super.getShapeDescriptor());
	}

	@Override
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		super.initializeBindings(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier, () ->
				ImmutableBinderAction.bind(getText()));
	}

	@Override
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
				BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier, () ->
						ImmutableBinderAction.unbind(getText())));
		super.cleanupBindings();
	}

	@Override
	public IUIRendererContainerContainer<IUIComponentRenderer<?>> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	protected IBindingField<Boolean> getAutoResize() {
		return autoResize;
	}

	protected static class AutoResizeShapeDescriptor
			extends AbstractDelegatingShapeDescriptor<Shape, IShapeDescriptor<?>> {
		private final OptionalWeakReference<UILabelComponent> owner;

		public AutoResizeShapeDescriptor(UILabelComponent owner, IShapeDescriptor<?> delegated) {
			super(delegated);
			this.owner = OptionalWeakReference.of(owner);
		}

		@Override
		public Shape getShapeOutput() {
			return getOwner()
					.filter(owner -> owner.getAutoResize().getValue())
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

	protected static class TextBindingField
			extends DelegatingBindingField<IAttributedText> {
		private final OptionalWeakReference<UILabelComponent> owner;

		public TextBindingField(UILabelComponent owner, IBindingField<IAttributedText> delegate) {
			super(delegate);
			this.owner = OptionalWeakReference.of(owner);
		}

		@Override
		public void setValue(IAttributedText value) {
			getOwner().ifPresent(UILabelComponent::invalidateTextDimension);
			super.setValue(value);
		}

		protected Optional<? extends UILabelComponent> getOwner() {
			return owner.getOptional();
		}
	}

	public static class DefaultRenderer<C extends UILabelComponent>
			extends UIDefaultComponentRenderer<C> {
		@SuppressWarnings("UnstableApiUsage")
		private final CachedTask<ITuple3<? extends AttributedString, ? extends FontRenderContext, ? extends Float>, Iterable<? extends TextLayout>> textLayoutTask =
				new CachedTask<>(data -> {
					float right = data.getRight();
					return TextUtilities.separateLines(data.getLeft().getIterator()).stream()
							.map(line -> new LineBreakMeasurer(TextUtilities.ensureNonEmpty(line), data.getMiddle()))
							.map(line -> TextUtilities.asTextLayoutIterator(line, line1 -> line1.nextLayout(right)))
							.flatMap(Streams::stream)
							.collect(ImmutableList.toImmutableList());
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
					Rectangle2D componentBounds = IUIComponent.getShape(container).getBounds2D();
					try (AutoCloseableGraphics2D graphics = AutoCloseableGraphics2D.of(context.createGraphics())) {
						Point2D textPen = new Point2D.Double(componentBounds.getX(), componentBounds.getY());
						float textWidth = FloatUtilities.saturatedCast(componentBounds.getWidth());
						TextUtilities.drawLines(graphics, textPen, textWidth,
								getTextLayoutTask().apply(ImmutableTuple3.of(container.getText().getValue().compile(), graphics.getFontRenderContext(), textWidth)));
					}
				}
			});
		}

		protected CachedTask<? super ITuple3<? extends AttributedString, ? extends FontRenderContext, ? extends Float>, ? extends Iterable<? extends TextLayout>> getTextLayoutTask() {
			return textLayoutTask;
		}
	}
}
