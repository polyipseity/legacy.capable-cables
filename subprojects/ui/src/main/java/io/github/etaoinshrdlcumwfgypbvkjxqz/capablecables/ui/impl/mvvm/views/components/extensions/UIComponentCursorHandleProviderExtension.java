package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIExtensionArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.extensions.cursors.IUICursorHandleProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction.UIImmutableExtensionArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.modifiers.UIEmptyComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.ICursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IInputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.Optional2;

import java.util.Optional;

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
	public Optional<? extends ICursor> getCursorHandle() {
		return getContainer()
				.flatMap(view ->
						Optional2.of(
								() -> IUIViewComponent.createComponentContextWithManager(view)
										.orElse(null),
								() -> view.getContext()
										.map(IUIViewContext::getInputDevices)
										.flatMap(IInputDevices::getPointerDevice)
										.orElse(null))
								.flatMap((componentContext, pointerDevice) -> {
									try (IUIComponentContext safeComponentContext = componentContext) {
										Optional<? extends ICursor> ret = Optional.empty();
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
				);
	}

	@Override
	public IExtensionType<IIdentifier, ?, IUIView<?>> getType() { return StaticHolder.getType().getValue(); }
}
