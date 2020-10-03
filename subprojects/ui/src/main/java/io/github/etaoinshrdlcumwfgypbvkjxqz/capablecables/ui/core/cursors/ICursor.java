package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.cursors;

public interface ICursor
		extends AutoCloseable {
	long getHandle();

	@Override
	void close();
}
