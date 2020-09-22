package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

public abstract class AbstractDelegatingObject<T> {
	private final T delegate;

	public AbstractDelegatingObject(T delegate) { this.delegate = delegate; }

	protected T getDelegate() { return delegate; }
}
