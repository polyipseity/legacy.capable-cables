package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components.parsers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.UIDefaultComponentParser;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftComponentParser<T extends IUIViewComponent<?, ?>>
		extends UIDefaultComponentParser<T> {
	public UIDefaultMinecraftComponentParser(Class<T> managerClass) { super(managerClass); }

	public static <T extends UIDefaultMinecraftComponentParser<?>> T makeParserMinecraft(T parser) { return parser; }
}
