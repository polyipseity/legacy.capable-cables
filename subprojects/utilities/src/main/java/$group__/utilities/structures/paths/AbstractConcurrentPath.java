package $group__.utilities.structures.paths;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractConcurrentPath<T>
		extends AbstractPath<T>
		implements IConcurrentPath<T> {
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();

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
	public T getAt(int depth) {
		Lock readLock = getLock().readLock();
		readLock.lock();
		T ret = super.getAt(depth);
		readLock.unlock();
		return ret;
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
	public void subPath(Iterable<? extends T> elements) {
		Lock writeLock = getLock().writeLock();
		writeLock.lock();
		super.subPath(elements);
		writeLock.unlock();
	}

	@Override
	public void parentPath(int amount) {
		Lock writeLock = getLock().writeLock();
		writeLock.lock();
		super.parentPath(amount);
		writeLock.unlock();
	}

	@Override
	public abstract AbstractConcurrentPath<T> copy();
}
