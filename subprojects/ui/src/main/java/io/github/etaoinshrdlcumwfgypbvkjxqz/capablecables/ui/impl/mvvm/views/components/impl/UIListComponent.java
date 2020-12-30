package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.binding.UIImmutablePropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction.UIImmutableComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.ShapeDescriptorBuilderFactoryRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ImmutableShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text.ImmutableAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIAxis;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl.DelegatingSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.reactive.impl.ReactiveUtilities;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import org.jetbrains.annotations.NonNls;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UIListComponent<E>
		extends UIShapeComponent {
	/* COMMENT
	design with reference to others
	- list of datum
	- component template
	 */

	public static final @NonNls String PROPERTY_DATA = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.list.data";
	public static final @NonNls String PROPERTY_DIRECTION = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.list.direction";
	public static final @NonNls String PROPERTY_COMPONENT_FACTORY = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.list.component_factory";
	private static final IIdentifier PROPERTY_DATA_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyData());
	private static final IIdentifier PROPERTY_DIRECTION_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyDirection());
	private static final IIdentifier PROPERTY_COMPONENT_FACTORY_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyComponentFactory());

	@UIProperty(PROPERTY_DATA)
	private final IBindingField<List<? extends E>> data; // COMMENT '? extends E' because we only care about reading it
	@UIProperty(PROPERTY_DIRECTION)
	private final IBindingField<EnumUISide> direction;
	@UIProperty(PROPERTY_COMPONENT_FACTORY)
	private final IBindingField<BiFunction<@Nonnull ? super UIListComponent<? extends E>, @Nonnull ? super E, @Nonnull ? extends IUIComponent>> componentFactory;

	private final IUIRendererContainerContainer<IUIComponentRenderer<?>> rendererContainerContainer;

	private boolean rebuildChildren = true;

	@UIComponentConstructor
	public UIListComponent(IUIComponentArguments arguments) {
		super(arguments);

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(DefaultRenderer.class));

		@Immutable Map<IIdentifier, ? extends IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.data = IUIPropertyMappingValue.createBindingField(CastUtilities.castUnchecked(List.class),
				ImmutableList::of,
				mappings.get(getPropertyDataIdentifier()));
		this.direction = IUIPropertyMappingValue.createBindingField(EnumUISide.class,
				ConstantValue.of(EnumUISide.DOWN),
				mappings.get(getPropertyDirectionIdentifier()));
		this.componentFactory = IUIPropertyMappingValue.createBindingField(CastUtilities.castUnchecked(BiFunction.class),
				ToStringLabelComponentFactory::new,
				mappings.get(getPropertyComponentFactoryIdentifier()));

		this.data.getField().getNotifier().subscribe(
				RebuildChildrenSubscriber.ofDecorated(suppressThisEscapedWarning(() -> this), UIConfiguration.getInstance().getLogger()));
		this.direction.getField().getNotifier().subscribe(
				RebuildChildrenSubscriber.ofDecorated(suppressThisEscapedWarning(() -> this), UIConfiguration.getInstance().getLogger()));
	}

	public static IIdentifier getPropertyDataIdentifier() {
		return PROPERTY_DATA_IDENTIFIER;
	}

	public static @Nonnull IIdentifier getPropertyDirectionIdentifier() {
		return PROPERTY_DIRECTION_IDENTIFIER;
	}

	public static IIdentifier getPropertyComponentFactoryIdentifier() {
		return PROPERTY_COMPONENT_FACTORY_IDENTIFIER;
	}

	public static @NonNls String getPropertyData() {
		return PROPERTY_DATA;
	}

	public static @NonNls String getPropertyComponentFactory() {
		return PROPERTY_COMPONENT_FACTORY;
	}

	public static @NonNls String getPropertyDirection() {
		return PROPERTY_DIRECTION;
	}

	@Override
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		super.initializeBindings(bindingActionConsumerSupplier);
		BindingUtilities.supplyBindingAction(bindingActionConsumerSupplier,
				() -> ImmutableBindingAction.bind(ImmutableList.of(
						getData(),
						getDirection(),
						getComponentFactory()
				)));
	}

	@Override
	public void cleanupBindings() {
		getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumer ->
				BindingUtilities.supplyBindingAction(bindingActionConsumer,
						() -> ImmutableBindingAction.unbind(ImmutableList.of(
								getData(),
								getDirection(),
								getComponentFactory()
						)))
		);
		super.cleanupBindings();
	}

	@Override
	protected void update0(IUIComponentContext context) {
		super.update0(context);
		if (isRebuildChildren()) {
			rebuildChildren();
			setRebuildChildren(false);
		}
	}

	protected boolean isRebuildChildren() {
		return rebuildChildren;
	}

	@SuppressWarnings({"UnstableApiUsage", "rawtypes", "RedundantSuppression"})
	protected void rebuildChildren() {
		IUIComponent contentComponent = getContentComponent();
		IUIComponent.clearContentChildren(this);

		// data
		EnumUISide direction = getDirection().getValue();
		BiFunction<@Nonnull ? super UIListComponent<? extends E>, @Nonnull ? super E, @Nonnull ? extends IUIComponent> componentFactory = getComponentFactory().getValue();
		IUIComponentShapeAnchorController shapeAnchorController = getManager()
				.flatMap(IUIComponentManager::getView)
				.map(IUIViewComponent::getShapeAnchorController)
				.orElseThrow(IllegalStateException::new);

		// calculate
		EnumUISide startingSide = direction.getOpposite().orElseThrow(IllegalStateException::new);
		Set<EnumUISide> sideSides = direction.getAxis().getOpposite().getSides(EnumUIAxis.EnumSidesOption.NORMAL);
		List<@Nonnull ? extends IUIComponent> children = getData().getValue().stream()
				.map(datum -> componentFactory.apply(this, datum))
				.collect(ImmutableList.toImmutableList());

		IUIComponent.addContentChildren(this, children.iterator());

		// TODO enhancement: the anchor approach might be terrible for performance for large lists
		// TODO enhancement: direction-relative-side-specific borders
		final IShapeAnchor[] nextAnchor = {new ImmutableShapeAnchor(contentComponent, startingSide, startingSide, 0D)};
		children.forEach(child -> {
			shapeAnchorController.addAnchors(child, Iterators.concat(
					sideSides.stream().unordered()
							.map(sideSide -> new ImmutableShapeAnchor(contentComponent, sideSide, sideSide, 0D))
							.iterator(),
					Iterators.singletonIterator(nextAnchor[0])
			));
			nextAnchor[0] = new ImmutableShapeAnchor(child, startingSide, direction, 0D);
		});
		shapeAnchorController.anchor();
	}

	protected void setRebuildChildren(boolean rebuildChildren) {
		this.rebuildChildren = rebuildChildren;
	}

	protected IBindingField<List<? extends E>> getData() {
		return data;
	}

	protected IBindingField<EnumUISide> getDirection() {
		return direction;
	}

	protected IBindingField<BiFunction<@Nonnull ? super UIListComponent<? extends E>, @Nonnull ? super E, @Nonnull ? extends IUIComponent>> getComponentFactory() {
		return componentFactory;
	}

	@Override
	public IUIRendererContainerContainer<IUIComponentRenderer<?>> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	public static class DefaultRenderer<C extends UIListComponent<?>>
			extends UIShapeComponent.DefaultRenderer<C> {
		@UIRendererConstructor
		public DefaultRenderer(IUIRendererArguments arguments) {
			super(arguments);
		}
	}

	public static abstract class AbstractComponentFactory<E, R extends IUIComponent>
			implements BiFunction<@Nonnull UIListComponent<? extends E>, @Nonnull E, @Nonnull R> {
		@Override
		public abstract @Nonnull R apply(@Nonnull UIListComponent<? extends E> container, @Nonnull E datum);
	}

	public static class ToStringLabelComponentFactory
			extends AbstractComponentFactory<Object, UILabelComponent> {
		@Override
		public @Nonnull UILabelComponent apply(@Nonnull UIListComponent<?> container, @Nonnull Object datum) {
			return new UILabelComponent(UIImmutableComponentArguments.of(null,
					ImmutableMap.of(
							UILabelComponent.getPropertyTextIdentifier(),
							UIImmutablePropertyMappingValue.ofValue(ImmutableAttributedText.ofCharSequence(datum.toString()))
					),
					ShapeDescriptorBuilderFactoryRegistry.getDefaultFactory().createBuilder(Rectangle2D.class)
							.setX(0D).setY(0D).setWidth(1D).setHeight(1D) // COMMENT should not matter; auto-resizing of labels enabled, width anchor optional (enabled by default)
							.build(),
					null,
					ImmutableMap.of()));
		}
	}

	public static class RebuildChildrenSubscriber
			extends DelegatingSubscriber<Object> {
		private final OptionalWeakReference<UIListComponent<?>> owner;

		protected RebuildChildrenSubscriber(Subscriber<? super Object> delegate, UIListComponent<?> owner) {
			super(delegate);
			this.owner = OptionalWeakReference.of(owner);
		}

		public static DisposableSubscriber<Object> ofDecorated(UIListComponent<?> owner, Logger logger) {
			return ReactiveUtilities.decorateAsListener(delegate -> new RebuildChildrenSubscriber(delegate, owner), logger);
		}

		@Override
		public void onNext(@Nonnull Object data) {
			super.onNext(data);
			getOwner().ifPresent(owner -> owner.setRebuildChildren(true));
		}

		protected Optional<? extends UIListComponent<?>> getOwner() {
			return owner.getOptional();
		}
	}
}
