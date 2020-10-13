package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.theming;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;

import java.util.function.Supplier;

public class UINullTheme
		implements IUITheme {
	private static final Supplier<UINullTheme> INSTANCE = Suppliers.memoize(UINullTheme::new);

	protected UINullTheme() {}

	@SuppressWarnings("ConstantConditions")
	public static UINullTheme getInstance() { return INSTANCE.get(); }

	@Override
	public void apply(Iterable<? extends IUIRendererContainer<?>> rendererContainers) {}
}
