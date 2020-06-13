package $group__.utilities.extensions;

import $group__.annotations.InterfaceIntersection;
import $group__.utilities.helpers.Assertions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

@InterfaceIntersection
@FunctionalInterface
public interface IConsumer<T> extends Consumer<T>, Function<T, Void> {
	@Override
	@Nullable
	default Void apply(@Nonnull T t) {
		accept(t);
		return null;
	}

	@Override
	void accept(@Nonnull T t);


	@FunctionalInterface
	interface IConsumerNullable<T> extends IConsumer<T> {
		@Nullable
		@Override
		default Void apply(@Nullable T t) { return IConsumer.super.apply(Assertions.assumeNonnull(t)); }

		@Override
		void accept(@Nullable T t);
	}
}
