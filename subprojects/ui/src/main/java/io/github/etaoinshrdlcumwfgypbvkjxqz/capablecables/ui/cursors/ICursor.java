package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.cursors;

public interface ICursor
		extends AutoCloseable {
	long getHandle();

	@Override
	void close();
}
