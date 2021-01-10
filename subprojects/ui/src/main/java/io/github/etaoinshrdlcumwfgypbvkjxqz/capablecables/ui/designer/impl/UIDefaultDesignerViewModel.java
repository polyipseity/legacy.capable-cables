package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.impl;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.AlwaysNull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.def.IUIDesignerModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.UINamespaceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.viewmodels.UIDefaultViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AutoCloseableRotator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.DelegatingSubscriber;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.reactive.ReactiveUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBindingAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.ImmutableBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields.MemoryObservableField;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import org.jetbrains.annotations.NonNls;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UIDefaultDesignerViewModel
		extends UIDefaultViewModel<UIDefaultDesignerModel> {
	// SECTION exposed bindings
	public static final @NonNls String PROPERTY_PALETTE = UINamespaceUtilities.VIEW_BINDING_PREFIX + "default.palette";
	private static final IIdentifier PROPERTY_PALETTE_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyPalette());

	private final IBindingField<List<? extends Class<?>>> palette;

	private final AutoCloseableRotator<DisposableSubscriber<Object>, RuntimeException> componentRegistryChangeSubscriberRotator =
			new AutoCloseableRotator<>(() -> ComponentRegistryChangeSubscriber.ofDecorated(
					suppressThisEscapedWarning(() -> this),
					UIConfiguration.getInstance().getLogger()),
					DisposableSubscriber::dispose);

	protected UIDefaultDesignerViewModel(UIDefaultDesignerModel model) {
		super(model);

		this.palette = ImmutableBindingField.of(getPropertyPaletteIdentifier(),
				new MemoryObservableField<>(CastUtilities.castUnchecked(List.class),
						getPaletteFromModel(model))
		);
	}

	public static IIdentifier getPropertyPaletteIdentifier() {
		return PROPERTY_PALETTE_IDENTIFIER;
	}

	protected static @Immutable List<Class<?>> getPaletteFromModel(IUIDesignerModel model) {
		return ImmutableList.copyOf(model.getComponentRegistry().asMapView().keySet());
	}

	public static UIDefaultDesignerViewModel of(UIDefaultDesignerModel model) {
		return new UIDefaultDesignerViewModel(model);
	}

	public static @NonNls String getPropertyPalette() {
		return PROPERTY_PALETTE;
	}

	@Override
	public void initializeBindings(Supplier<? extends Optional<? extends Consumer<? super IBindingAction>>> bindingActionConsumerSupplier) {
		super.initializeBindings(bindingActionConsumerSupplier);
		BindingUtilities.supplyBindingAction(bindingActionConsumerSupplier, () ->
				ImmutableBindingAction.bind(ImmutableList.of(
						getPalette()
				))
		);
	}

	@Override
	public void cleanupBindings() {
		getBindingActionConsumerSupplierHolder().getValue().ifPresent(bindingActionConsumerSupplier ->
				BindingUtilities.supplyBindingAction(bindingActionConsumerSupplier, () ->
						ImmutableBindingAction.unbind(ImmutableList.of(
								getPalette()
						))
				)
		);
		super.cleanupBindings();
	}

	@Override
	protected void cleanup0(@AlwaysNull @Nullable Void context) {
		getComponentRegistryChangeSubscriberRotator().close();
		super.cleanup0(context);
	}

	@Override
	protected void initialize0(@AlwaysNull @Nullable Void context) {
		super.initialize0(context);
		getModel().getComponentRegistry().getPublisher().subscribe(getComponentRegistryChangeSubscriberRotator().get());
	}

	protected AutoCloseableRotator<DisposableSubscriber<Object>, RuntimeException> getComponentRegistryChangeSubscriberRotator() {
		return componentRegistryChangeSubscriberRotator;
	}

	protected IBindingField<List<? extends Class<?>>> getPalette() {
		return palette;
	}

	public static class ComponentRegistryChangeSubscriber
			extends DelegatingSubscriber<Object> {
		private final OptionalWeakReference<UIDefaultDesignerViewModel> owner;

		protected ComponentRegistryChangeSubscriber(UIDefaultDesignerViewModel owner, Subscriber<? super Object> delegate) {
			super(delegate);
			this.owner = OptionalWeakReference.of(owner);
		}

		public static DisposableSubscriber<Object> ofDecorated(UIDefaultDesignerViewModel owner, Logger logger) {
			return ReactiveUtilities.decorateAsListener(delegate -> new ComponentRegistryChangeSubscriber(owner, delegate), logger);
		}

		@Override
		public void onNext(Object o) {
			super.onNext(o);
			getOwner().ifPresent(owner ->
					owner.getPalette().setValue(
							ImmutableList.copyOf(getPaletteFromModel(owner.getModel())) // COMMENT rebuild palette
					)
			);
		}

		protected Optional<? extends UIDefaultDesignerViewModel> getOwner() {
			return owner.getOptional();
		}
	}
}
