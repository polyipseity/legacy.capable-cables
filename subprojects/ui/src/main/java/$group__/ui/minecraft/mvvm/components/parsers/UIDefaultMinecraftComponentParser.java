package $group__.ui.minecraft.mvvm.components.parsers;

import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.parsers.components.UIDefaultComponentParser;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftComponentParser<T extends IUIViewComponent<?, ?>>
		extends UIDefaultComponentParser<T> {
	public UIDefaultMinecraftComponentParser(Class<T> managerClass) { super(managerClass); }

	public static <T extends UIDefaultMinecraftComponentParser<?>> T makeParserMinecraft(T parser) {
		// TODO add something here
		return parser;
	}
}
