package $group__.utilities.extensions;

import $group__.utilities.interfaces.IHasGenericClass;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.lang.ref.WeakReference;
import java.util.Optional;

public abstract class ExtensionContainerAware<K, C extends IExtensionContainer<? super K, ?>>
		extends IHasGenericClass.Impl<C>
		implements IExtension<K, C> {
	protected WeakReference<C> container = new WeakReference<>(null);

	public ExtensionContainerAware(Class<C> genericClass) { super(genericClass); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(C container) { setContainer(container); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() { setContainer(null); }

	protected Optional<C> getContainer() { return Optional.ofNullable(container.get()); }

	protected void setContainer(@Nullable C container) { this.container = new WeakReference<>(container); }
}
