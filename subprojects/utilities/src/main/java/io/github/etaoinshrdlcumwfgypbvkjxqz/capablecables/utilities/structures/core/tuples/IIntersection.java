package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples;

public interface IIntersection<L, R>
		extends ITuple2<L, R> {
	@Override
	IIntersection<R, L> swap();
}
