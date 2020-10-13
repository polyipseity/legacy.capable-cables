package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers;

public interface IUIExtensibleParser<T, R, H, HD>
		extends IUIParser<T, R> {
	void addHandler(HD discriminator, H handler);
}
