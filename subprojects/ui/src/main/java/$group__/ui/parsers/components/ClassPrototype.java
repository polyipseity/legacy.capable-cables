package $group__.ui.parsers.components;

public abstract class ClassPrototype<T> {
	public static final String CLASS_ATTRIBUTE_NAME = "class";
	protected final Class<? extends T> prototypeClass;

	@SuppressWarnings("unchecked")
	protected ClassPrototype(String prototypeClassName)
			throws ClassNotFoundException {
		this.prototypeClass = (Class<? extends T>) Class.forName(prototypeClassName); // COMMENT we will know later...
	}

	protected Class<? extends T> getPrototypeClass() { return prototypeClass; }
}
