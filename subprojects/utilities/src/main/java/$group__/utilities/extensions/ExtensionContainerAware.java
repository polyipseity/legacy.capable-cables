package $group__.utilities.extensions;

import $group__.utilities.LogMessageBuilder;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.UtilitiesConfiguration;
import $group__.utilities.UtilitiesMarkers;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.templates.CommonConfigurationTemplate;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class ExtensionContainerAware<K, C extends IExtensionContainer<? super K>, E extends C>
		extends IHasGenericClass.Extended.Impl<C, E>
		implements IExtension<K, C> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	@SuppressWarnings("unchecked")
	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(C container) {
		if (!getExtendedClass().isInstance(container))
			throw ThrowableUtilities.logAndThrow(
					new IllegalArgumentException(
							new LogMessageBuilder()
									.addMarkers(UtilitiesMarkers.getInstance()::getMarkerExtension)
									.addKeyValue("container", container)
									.addMessages(() -> getResourceBundle().getString("this.added.container.instance_of.fail"))
									.build()
					),
					UtilitiesConfiguration.getInstance().getLogger()
			);
		setContainer((E) container); // COMMENT checked
		Cleaner.create(container,
				this::onExtensionRemoved);
	}

	protected WeakReference<E> container = new WeakReference<>(null);

	public ExtensionContainerAware(Class<C> genericClass, Class<E> containerClass) { super(genericClass, containerClass); }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() { setContainer(null); }

	protected Optional<E> getContainer() { return Optional.ofNullable(container.get()); }

	protected void setContainer(@Nullable E container) { this.container = new WeakReference<>(container); }
}
