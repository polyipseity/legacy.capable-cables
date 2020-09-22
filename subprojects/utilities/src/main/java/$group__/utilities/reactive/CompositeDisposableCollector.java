package $group__.utilities.reactive;

import $group__.utilities.CastUtilities;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.disposables.DisposableContainer;
import io.reactivex.rxjava3.internal.disposables.ListCompositeDisposable;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public abstract class CompositeDisposableCollector<DC extends Disposable & DisposableContainer>
		implements Collector<Disposable, DC, Disposable> {
	protected CompositeDisposableCollector() {}

	public static Collector<Disposable, ?, Disposable> collect(boolean ordered) { return ordered ? ListCompositeDisposableCollector.getInstance() : SetCompositeDisposableCollector.getInstance(); }

	@Override
	public BiConsumer<DC, Disposable> accumulator() { return DisposableContainer::add; }

	@Override
	public Function<DC, Disposable> finisher() { return CastUtilities::upcast; }

	protected static class SetCompositeDisposableCollector
			extends CompositeDisposableCollector<CompositeDisposable> {
		private static final SetCompositeDisposableCollector INSTANCE = new SetCompositeDisposableCollector();
		private static final ImmutableSet<Characteristics> CHARACTERISTICS = Sets.immutableEnumSet(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);

		private static SetCompositeDisposableCollector getInstance() { return INSTANCE; }

		@Override
		public Supplier<CompositeDisposable> supplier() { return CompositeDisposable::new; }

		@Override
		public BinaryOperator<CompositeDisposable> combiner() { return CompositeDisposable::new; }

		@Override
		public Set<Characteristics> characteristics() { return getCharacteristics(); }

		protected static ImmutableSet<Characteristics> getCharacteristics() { return CHARACTERISTICS; }
	}

	protected static class ListCompositeDisposableCollector
			extends CompositeDisposableCollector<ListCompositeDisposable> {
		private static final ListCompositeDisposableCollector INSTANCE = new ListCompositeDisposableCollector();
		private static final ImmutableSet<Characteristics> CHARACTERISTICS = Sets.immutableEnumSet(Characteristics.IDENTITY_FINISH);

		private static ListCompositeDisposableCollector getInstance() { return INSTANCE; }

		@Override
		public Supplier<ListCompositeDisposable> supplier() { return ListCompositeDisposable::new; }

		@Override
		public BinaryOperator<ListCompositeDisposable> combiner() { return ListCompositeDisposable::new; }

		@Override
		public Set<Characteristics> characteristics() { return getCharacteristics(); }

		protected static ImmutableSet<Characteristics> getCharacteristics() { return CHARACTERISTICS; }
	}
}
