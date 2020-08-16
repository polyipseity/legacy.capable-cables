package $group__.utilities.interfaces;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasGenericClass<T> {
	Class<T> getGenericClass();

	class Impl<T>
			implements IHasGenericClass<T> {
		protected final Class<T> genericClass;

		public Impl(Class<T> genericClass) { this.genericClass = genericClass; }

		@Override
		public Class<T> getGenericClass() { return genericClass; }
	}

	interface Extended<T, E extends T> extends IHasGenericClass<T> {
		Class<E> getExtendedClass();

		class Impl<T, E extends T>
				extends IHasGenericClass.Impl<T>
				implements Extended<T, E> {
			protected final Class<E> extendedClass;

			public Impl(Class<T> genericClass, Class<E> extendedClass) {
				super(genericClass);
				this.extendedClass = extendedClass;
			}

			@Override
			public Class<E> getExtendedClass() { return extendedClass; }
		}
	}
}
