package $group__.client.ui.mvvm.core.structures;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public interface IUIDataKeyboardKeyPress extends Cloneable {
	int getKey();

	int getScanCode();

	int getModifiers();
}
