package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core;

public interface ICursor
		extends AutoCloseable {
	long getHandle();

	@Override
	void close();
}
