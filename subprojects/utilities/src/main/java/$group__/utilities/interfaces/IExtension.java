package $group__.utilities.interfaces;

public interface IExtension<C extends IExtensionContainer<?, ?>> {
	void onExtensionAdd(C container);

	void onExtensionRemove();
}
