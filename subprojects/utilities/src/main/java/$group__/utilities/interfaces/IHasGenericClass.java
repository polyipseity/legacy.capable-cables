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
}
