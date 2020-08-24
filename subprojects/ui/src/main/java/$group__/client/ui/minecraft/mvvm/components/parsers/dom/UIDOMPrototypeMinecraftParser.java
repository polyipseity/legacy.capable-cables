package $group__.client.ui.minecraft.mvvm.components.parsers.dom;

import $group__.client.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.client.ui.mvvm.views.components.parsers.dom.UIDOMPrototypeParser;

public class UIDOMPrototypeMinecraftParser<T extends IUIComponentManager<?>>
		extends UIDOMPrototypeParser<T> {
	public UIDOMPrototypeMinecraftParser(Class<T> managerClass) { super(managerClass); }

	public static <T extends UIDOMPrototypeMinecraftParser<?>> T makeParserMinecraft(T parser) {
		// TODO add something here
		return parser;
	}
}
