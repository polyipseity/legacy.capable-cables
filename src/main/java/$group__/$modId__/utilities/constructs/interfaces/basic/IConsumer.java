package $group__.$modId__.utilities.constructs.interfaces.basic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.Assertions.assumeNonnull;

@FunctionalInterface
public interface IConsumer<T> extends Consumer<T>, Function<T, Void> {
	/* SECTION methods */

	@Override
	void accept(@Nonnull T t);

	@Override
	@Nullable
	default Void apply(@Nonnull T t) {
		accept(t);
		return null;
	}


	/* SECTION static classes */

	interface IConsumerNullable<T> extends IConsumer<T> {
		/* SECTION methods */

		@Override
		void accept(@Nullable T t);

		@Nullable
		@Override
		default Void apply(@Nullable T t) { return IConsumer.super.apply(assumeNonnull(t)); }
	}
}
