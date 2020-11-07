package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.cursors.IUICursorHandleProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.annotations.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.modifiers.UIEmptyComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.Optional2;

import java.util.Optional;

public class UIComponentCursorHandleProviderExtension
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIView<?>, IUIViewComponent<?, ?>>
		implements IUICursorHandleProviderExtension {
	private static final UIExtensionConstructor.IArguments DEFAULT_ARGUMENTS = new UIExtensionConstructor.ImmutableArguments(ImmutableMap.of(), IUIViewComponent.class);

	public UIComponentCursorHandleProviderExtension() { this(getDefaultArguments()); }

	@UIExtensionConstructor
	public UIComponentCursorHandleProviderExtension(@SuppressWarnings("unused") UIExtensionConstructor.IArguments arguments) {
		super(CastUtilities.castUnchecked(IUIViewComponent.class));
	}

	protected static UIExtensionConstructor.IArguments getDefaultArguments() { return DEFAULT_ARGUMENTS; }

	@Override
	public Optional<Long> getCursorHandle() {
		return getContainer()
				.flatMap(view ->
						Optional2.of(
								() -> IUIViewComponent.createComponentContextWithManager(view)
										.orElse(null),
								() -> view.getContext()
										.map(IUIViewContext::getInputDevices)
										.flatMap(IInputDevices::getPointerDevice)
										.orElse(null))
								.flatMap(values -> {
									IUIComponentContext componentContext = values.getValue1Nonnull();
									IInputPointerDevice pointerDevice = values.getValue2Nonnull();
									try (IUIComponentContext safeComponentContext = componentContext) {
										Optional<Long> ret = Optional.empty();
										IUIViewComponent.getPathResolver(view).resolvePath(safeComponentContext, pointerDevice.getPositionView());
										while (!safeComponentContext.getPathView().isEmpty()) {
											IUIComponent component = IUIComponentContext.getCurrentComponent(safeComponentContext)
													.orElseThrow(AssertionError::new);
											IUIComponentCursorHandleProviderModifier componentAsModifier =
													CastUtilities.castChecked(IUIComponentCursorHandleProviderModifier.class, component)
															.orElseGet(UIEmptyComponentCursorHandleProviderModifier::new);
											ret = IUIComponentCursorHandleProviderModifier
													.handleComponentModifiers(componentAsModifier,
															component.getModifiersView(),
															safeComponentContext);
											if (ret.isPresent())
												break;
											safeComponentContext.getMutator().pop(safeComponentContext);
										}
										return ret;
									}
								}));
	}

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIView<?>> getType() { return StaticHolder.getType().getValue(); }
}
