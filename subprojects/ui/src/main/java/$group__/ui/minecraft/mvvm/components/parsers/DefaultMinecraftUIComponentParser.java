package $group__.ui.minecraft.mvvm.components.parsers;

import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.parsers.components.DefaultUIComponentParser;
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
