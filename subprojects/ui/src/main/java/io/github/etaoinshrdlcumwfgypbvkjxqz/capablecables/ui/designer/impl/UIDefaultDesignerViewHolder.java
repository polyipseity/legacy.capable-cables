package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.AbstractParsedSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.ParserUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIDefaultComponentSchemaHolder;
import org.jetbrains.annotations.NonNls;

import java.util.function.Supplier;

public enum UIDefaultDesignerViewHolder {
	;

	public static final @NonNls String VIEW_PATH = "designer-default.xml"; // COMMENT relative path
	public static final @NonNls String THEME_PATH = "designer-theme-default.xml"; // COMMENT relative path

	private static final Supplier<IUIComponentView<?, ?>> VIEW_SUPPLIER = AbstractParsedSupplier.Functional.of(
			() -> ParserUtilities.parseJAXBResource(UIDefaultComponentSchemaHolder.getContext(),
					UIDefaultDesignerViewHolder.class,
					getViewPath(),
					UIConfiguration.getInstance().getThrowableHandler()),
			parsed -> (IUIComponentView<?, ?>) ParserUtilities.transformJAXBResource(UIDefaultComponentSchemaHolder.getAdapterRegistry(), parsed)
	);
	private static final Supplier<IUITheme> THEME_SUPPLIER = AbstractParsedSupplier.Functional.of(
			() -> ParserUtilities.parseJAXBResource(UIDefaultComponentSchemaHolder.getContext(),
					UIDefaultDesignerViewHolder.class,
					getThemePath(),
					UIConfiguration.getInstance().getThrowableHandler()),
			parsed -> (IUITheme) ParserUtilities.transformJAXBResource(UIDefaultComponentSchemaHolder.getAdapterRegistry(), parsed)
	);

	public static IUIComponentView<?, ?> createView() {
		return getViewSupplier().get();
	}

	private static Supplier<IUIComponentView<?, ?>> getViewSupplier() {
		return VIEW_SUPPLIER;
	}

	public static IUITheme createTheme() {
		return getThemeSupplier().get();
	}

	private static Supplier<IUITheme> getThemeSupplier() {
		return THEME_SUPPLIER;
	}

	public static @NonNls String getViewPath() {
		return VIEW_PATH;
	}

	public static @NonNls String getThemePath() {
		return THEME_PATH;
	}
}
