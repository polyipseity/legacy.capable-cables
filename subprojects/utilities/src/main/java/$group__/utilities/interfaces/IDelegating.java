package $group__.utilities.interfaces;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IDelegating<T> {
	T getDelegated();

	class Impl<T> implements IDelegating<T> {
		private final T delegated;

		public Impl(T delegated) { this.delegated = delegated; }

		@Override
		public T getDelegated() { return delegated; }
	}
}
