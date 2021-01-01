package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.ComponentTheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.ComponentUI;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.JAXBUIDefaultComponentAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.JAXBUIDefaultComponentThemeAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;

public enum JAXBUIAdapters {
	;

	public static void registerAll(IJAXBAdapterRegistry registry) {
		// COMMENT UI
		EnumJAXBUIDefaultObjectAdapter.registerAll(registry);
		EnumJAXBUIDefaultElementAdapter.registerAll(registry);
		// COMMENT UI component
		registry.getObjectRegistry().registerChecked(
				ImmutableTuple2.of(ComponentUI.class, CastUtilities.castUnchecked(IUIComponentView.class)),
				JAXBUIDefaultComponentAdapter.makeParserStandard(new JAXBUIDefaultComponentAdapter())
		);
		registry.getObjectRegistry().registerChecked(
				ImmutableTuple2.of(ComponentTheme.class, IUITheme.class),
				JAXBUIDefaultComponentThemeAdapter.makeParserStandard(new JAXBUIDefaultComponentThemeAdapter())
		);
	}
}
