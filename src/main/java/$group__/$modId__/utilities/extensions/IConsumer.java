package $group__.$modId__.utilities.extensions;

import $group__.$modId__.annotations.InterfaceIntersection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.Assertions.assumeNonnull;

@InterfaceIntersection
@FunctionalInterface
public interface IConsumer<T> extends Consumer<T>, Function<T, Void> {
	/* SECTION methods */

	@Override
	@Nullable
	default Void apply(@Nonnull T t) {
		accept(t);
		return null;
	}

	@Override
	void accept(@Nonnull T t);


	/* SECTION static classes */

	@FunctionalInterface
	interface IConsumerNullable<T> extends IConsumer<T> {
		/* SECTION methods */

		@Nullable
		@Override
		default Void apply(@Nullable T t) { return IConsumer.super.apply(assumeNonnull(t)); }

		@Override
		void accept(@Nullable T t);
	}
}
