package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasGenericClass<T> {
	Class<T> getGenericClass();

	static <T, E extends T> Class<? extends T> getActualClass(IHasGenericClass<T> obj) {
		return CastUtilities.castChecked(Extended.class, obj)
				.<Class<? extends T>>map(Extended<T, E>::getExtendedClass)
				.orElseGet(obj::getGenericClass);
	}

	interface Extended<T, E extends T> extends IHasGenericClass<T> {
		Class<E> getExtendedClass();

		class Impl<T, E extends T>
				extends IHasGenericClass.Impl<T>
				implements Extended<T, E> {
			private final Class<E> extendedClass;

			public Impl(Class<T> genericClass, Class<E> extendedClass) {
				super(genericClass);
				this.extendedClass = extendedClass;
			}

			@Override
			public Class<E> getExtendedClass() { return extendedClass; }
		}
	}

	class Impl<T>
			implements IHasGenericClass<T> {
		private final Class<T> genericClass;

		public Impl(Class<T> genericClass) { this.genericClass = genericClass; }

		@Override
		public Class<T> getGenericClass() { return genericClass; }
	}
}
