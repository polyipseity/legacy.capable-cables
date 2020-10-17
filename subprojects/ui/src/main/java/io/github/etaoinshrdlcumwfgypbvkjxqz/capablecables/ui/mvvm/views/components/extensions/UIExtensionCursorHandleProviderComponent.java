package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.cursors.IUIExtensionCursorHandleProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.modifiers.NullUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs.IInputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs.IInputPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.optionals.Optional2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.util.Optional;

public class UIExtensionCursorHandleProviderComponent
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIView<?>, IUIViewComponent<?, ?>>
		implements IUIExtensionCursorHandleProvider {
	private static final UIExtensionConstructor.IArguments DEFAULT_ARGUMENTS = new UIExtensionConstructor.ImmutableArguments(ImmutableMap.of(), IUIViewComponent.class);

	public UIExtensionCursorHandleProviderComponent() { this(getDefaultArguments()); }

	@UIExtensionConstructor
	public UIExtensionCursorHandleProviderComponent(@SuppressWarnings("unused") UIExtensionConstructor.IArguments arguments) {
		super(CastUtilities.castUnchecked(IUIView.class),
				CastUtilities.castUnchecked(IUIViewComponent.class));
	}

	protected static UIExtensionConstructor.IArguments getDefaultArguments() { return DEFAULT_ARGUMENTS; }

	@Override
	public Optional<Long> getCursorHandle() {
		return getContainer()
				.flatMap(view ->
						Optional2.of(IUIViewComponent.createComponentContextWithManager(view)
								.orElse(null), view.getContext()
								.map(IUIViewContext::getInputDevices)
								.flatMap(IInputDevices::getPointerDevice)
								.orElse(null))
								.flatMap(values -> {
									IUIComponentContext componentContext = values.getValue1Nonnull();
									IInputPointerDevice pointerDevice = values.getValue2Nonnull();
									try (IUIComponentContext safeComponentContext = componentContext) {
										Optional<Long> ret = Optional.empty();
										view.getPathResolver().resolvePath(safeComponentContext, pointerDevice.getPositionView());
										while (!safeComponentContext.getStackRef().getPathRef().isEmpty()) {
											IUIComponent component = IUIComponentContext.getCurrentComponent(safeComponentContext)
													.orElseThrow(AssertionError::new);
											IUIComponentCursorHandleProviderModifier componentAsModifier =
													CastUtilities.castChecked(IUIComponentCursorHandleProviderModifier.class, component)
															.orElseGet(NullUIComponentCursorHandleProviderModifier::getInstance);
											ret = IUIComponentCursorHandleProviderModifier
													.handleComponentModifiers(componentAsModifier,
															component.getModifiersView(),
															safeComponentContext);
											if (ret.isPresent())
												break;
											safeComponentContext.getMutator().pop(safeComponentContext.getStackRef());
										}
										return ret;
									}
								}));
	}

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIView<?>> getType() { return TYPE.getValue(); }
}
