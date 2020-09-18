package $group__.utilities.structures.paths;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

public class FunctionalConcurrentPath<T>
		extends AbstractConcurrentPath<T> {
	protected final List<T> data;
	protected final Function<? super Iterable<? extends T>, ? extends List<T>> generator;

	public FunctionalConcurrentPath(Iterable<? extends T> data, Function<? super Iterable<? extends T>, ? extends List<T>> generator)
			throws EmptyPathException {
		this.generator = generator;
		this.data = this.generator.apply(data);
		EmptyPathException.checkPathData(this.data);
	}

	@Override
	public IConcurrentPath<T> getParentPath() throws EmptyPathException {
		Lock readLock = getLock().readLock();
		readLock.lock();
		List<T> data = getData();
		IConcurrentPath<T> ret = new FunctionalConcurrentPath<>(data.subList(0, data.size() - 1), getGenerator());
		readLock.unlock();
		return ret;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected List<T> getData() { return data; }

	protected Function<? super Iterable<? extends T>, ? extends List<T>> getGenerator() { return generator; }

	@Override
	public FunctionalConcurrentPath<T> copy() {
		Lock readLock = getLock().readLock();
		readLock.lock();
		FunctionalConcurrentPath<T> ret = new FunctionalConcurrentPath<>(getData(), getGenerator());
		readLock.unlock();
		return ret;
	}
}
