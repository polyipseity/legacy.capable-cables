package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components.parsers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.DefaultUIComponentParser;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefaultMinecraftUIComponentParser<T extends IUIViewComponent<?, ?>>
		extends DefaultUIComponentParser<T> {
	public DefaultMinecraftUIComponentParser(Class<T> managerClass) { super(managerClass); }

	public static <T extends DefaultMinecraftUIComponentParser<?>> T makeParserMinecraft(T parser) {
		// TODO add something here
		return parser;
	}
}
