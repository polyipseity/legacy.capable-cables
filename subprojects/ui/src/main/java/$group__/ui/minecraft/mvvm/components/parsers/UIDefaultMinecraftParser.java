package $group__.ui.minecraft.mvvm.components.parsers;

import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.parsers.components.UIDefaultParser;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftParser<T extends IUIViewComponent<?, ?>>
		extends UIDefaultParser<T> {
	public UIDefaultMinecraftParser(Class<T> managerClass) { super(managerClass); }

	public static <T extends UIDefaultMinecraftParser<?>> T makeParserMinecraft(T parser) {
		// TODO add something here
		return parser;
	}
}
