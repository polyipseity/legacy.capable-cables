package $group__.utilities.interfaces;

import $group__.utilities.ThrowableUtilities.BecauseOf;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasGenericClass<T> {
	Class<T> getGenericClass();

	@SuppressWarnings("unchecked")
	static <T> Class<? extends T> getActualClass(IHasGenericClass<T> obj) {
		return obj instanceof Extended
				? (Class<? extends T>) ((Extended<?, ?>) obj).getExtendedClass() // COMMENT should always extends T
				: obj.getGenericClass();
	}

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

				if (!genericClass.isAssignableFrom(extendedClass))
					throw BecauseOf.illegalArgument("Generic class is not assignable from extended class",
							"extendedClass", extendedClass,
							"genericClass", genericClass);
			}

			@Override
			public Class<E> getExtendedClass() { return extendedClass; }
		}
	}
}
