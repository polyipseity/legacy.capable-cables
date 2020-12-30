package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.EmptyPathException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IConcurrentPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractConcurrentPath<T>
		extends AbstractPath<T>
		implements IConcurrentPath<T> {
	private static final long LOCK_FIELD_OFFSET;

	static {
		try {
			LOCK_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(AbstractConcurrentPath.class.getDeclaredField("lock"));
		} catch (NoSuchFieldException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public AbstractConcurrentPath<T> clone() throws CloneNotSupportedException {
		AbstractConcurrentPath<T> result = (AbstractConcurrentPath<T>) super.clone();
		DynamicUtilities.getUnsafe().putObjectVolatile(result, getLockFieldOffset(), new ReentrantReadWriteLock());
		return result;
	}

	@Override
	public int size() {
		Lock readLock = getLock().readLock();
		readLock.lock();
		int ret = super.size();
		readLock.unlock();
		return ret;
	}

	protected ReadWriteLock getLock() { return lock; }

	@Override
	public Optional<? extends T> getAt(int depth) {
		Lock readLock = getLock().readLock();
		readLock.lock();
		Optional<? extends T> ret = super.getAt(depth);
		readLock.unlock();
		return ret;
	}

	@Override
	public void parentPath(int amount)
			throws EmptyPathException {
		Lock writeLock = getLock().writeLock();
		writeLock.lock();
		super.parentPath(amount);
		writeLock.unlock();
	}

	@Override
	public List<T> asList() {
		Lock readLock = getLock().readLock();
		readLock.lock();
		List<T> ret = super.asList();
		readLock.unlock();
		return ret;
	}

	@Override
	public void subPath(Iterator<? extends T> elements) {
		Lock writeLock = getLock().writeLock();
		writeLock.lock();
		super.subPath(elements);
		writeLock.unlock();
	}

	protected static long getLockFieldOffset() {
		return LOCK_FIELD_OFFSET;
	}
}
