package $group__.utilities.structures.paths;

import java.util.List;
import java.util.function.Function;

public class FunctionalPath<T>
		extends AbstractPath<T> {
	protected final List<T> data;
	protected final Function<? super Iterable<? extends T>, ? extends List<T>> generator;

	public FunctionalPath(Iterable<? extends T> data, Function<? super Iterable<? extends T>, ? extends List<T>> generator)
			throws EmptyPathException {
		this.generator = generator;
		this.data = this.generator.apply(data);
		EmptyPathException.checkPathData(this.data);
	}

	@Override
	public IPath<T> getParentPath()
			throws EmptyPathException {
		List<T> data = getGenerator().apply(getData()); // COMMENT snapshot data
		return new FunctionalPath<>(data.subList(0, data.size() - 1), getGenerator());
	}

	protected Function<? super Iterable<? extends T>, ? extends List<T>> getGenerator() { return generator; }

	@Override
	public FunctionalPath<T> copy() { return new FunctionalPath<>(getData(), getGenerator()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected List<T> getData() { return data; }
}
