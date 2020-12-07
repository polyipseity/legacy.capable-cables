package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIExtensionArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.cursors.IUICursorHandleProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction.UIImmutableExtensionArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.modifiers.UIEmptyComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.Optional2;

import java.util.OptionalLong;

public class UIComponentCursorHandleProviderExtension
		extends AbstractContainerAwareExtension<IIdentifier, IUIView<?>, IUIViewComponent<?, ?>>
		implements IUICursorHandleProviderExtension {
	private static final IUIExtensionArguments DEFAULT_ARGUMENTS = UIImmutableExtensionArguments.of(ImmutableMap.of(), IUIViewComponent.class, null);

	public UIComponentCursorHandleProviderExtension() { this(getDefaultArguments()); }

	@UIExtensionConstructor
	public UIComponentCursorHandleProviderExtension(@SuppressWarnings("unused") IUIExtensionArguments arguments) {
		super(CastUtilities.castUnchecked(IUIViewComponent.class));
	}

	protected static IUIExtensionArguments getDefaultArguments() { return DEFAULT_ARGUMENTS; }

	@Override
	public OptionalLong getCursorHandle() {
		return getContainer()
				.flatMap(view ->
						Optional2.of(
								() -> IUIViewComponent.createComponentContextWithManager(view)
										.orElse(null),
								() -> view.getContext()
										.map(IUIViewContext::getInputDevices)
										.flatMap(IInputDevices::getPointerDevice)
										.orElse(null))
								.map(values -> {
									IUIComponentContext componentContext = values.getValue1Nonnull();
									IInputPointerDevice pointerDevice = values.getValue2Nonnull();
									try (IUIComponentContext safeComponentContext = componentContext) {
										OptionalLong ret = OptionalLong.empty();
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
								})
				)
				.orElseGet(OptionalLong::empty);
	}

	@Override
	public IExtensionType<IIdentifier, ?, IUIView<?>> getType() { return StaticHolder.getType().getValue(); }
}
