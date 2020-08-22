package $group__.utilities.extensions;

import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.interfaces.IHasGenericClass;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.lang.ref.WeakReference;
import java.util.Optional;

public abstract class ExtensionContainerAware<K, C extends IExtensionContainer<? super K, ?>, E extends C>
		extends IHasGenericClass.Extended.Impl<C, E>
		implements IExtension<K, C> {
	protected WeakReference<E> container = new WeakReference<>(null);

	public ExtensionContainerAware(Class<C> genericClass, Class<E> extendedClass) { super(genericClass, extendedClass); }

	@SuppressWarnings("unchecked")
	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(C container) {
		if (!getExtendedClass().isInstance(container))
			throw BecauseOf.illegalArgument("Container is not an instance of extended class",
					"getExtendedClass()", getExtendedClass(),
					"getGenericClass()", getGenericClass(),
					"container", container);
		setContainer((E) container); // COMMENT checked
		Cleaner.create(container,
				this::onExtensionRemoved); // TODO CLEANER not working
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() { setContainer(null); }

	protected Optional<E> getContainer() { return Optional.ofNullable(container.get()); }

	protected void setContainer(@Nullable E container) { this.container = new WeakReference<>(container); }
}
