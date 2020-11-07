package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;

public enum JAXBUIAdapters {
	;

	public static void registerAll(IJAXBAdapterRegistry registry) {
		// COMMENT UI
		EnumJAXBUIDefaultObjectAdapter.registerAll(registry);
		EnumJAXBUIDefaultElementAdapter.registerAll(registry);
		// COMMENT UI component
		EnumJAXBUIDefaultComponentObjectAdapter.registerAll(registry);
		registry.getObjectRegistry().registerChecked(
				ImmutableTuple2.of(ComponentUI.class, CastUtilities.castUnchecked(IUIViewComponent.class)),
				new JAXBUIDefaultComponentAdapter()
		);
		registry.getObjectRegistry().registerChecked(
				ImmutableTuple2.of(Anchor.class, JAXBUIDefaultComponentAnchorAdapter.Result.class),
				new JAXBUIDefaultComponentAnchorAdapter()
		);
		registry.getObjectRegistry().registerChecked(
				ImmutableTuple2.of(RendererContainer.class, JAXBUIDefaultComponentRendererContainerAdapter.Result.class),
				new JAXBUIDefaultComponentRendererContainerAdapter()
		);
		registry.getObjectRegistry().registerChecked(
				ImmutableTuple2.of(Extension.class, JAXBUIDefaultComponentExtensionAdapter.Result.class),
				new JAXBUIDefaultComponentExtensionAdapter()
		);
		registry.getObjectRegistry().registerChecked(
				ImmutableTuple2.of(Renderer.class, JAXBUIDefaultComponentThemeRendererAdapter.Result.class),
				new JAXBUIDefaultComponentThemeRendererAdapter()
		);
	}
}
