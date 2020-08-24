package $group__.client.ui.mvvm.views;

import $group__.client.ui.core.mvvm.IUIInfrastructure;
import $group__.client.ui.core.mvvm.binding.IBinderAction;
import $group__.client.ui.core.mvvm.extensions.IUIExtension;
import $group__.client.ui.core.mvvm.views.IUIView;
import $group__.utilities.MapUtilities;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.Subject;
import io.reactivex.rxjava3.subjects.UnicastSubject;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public abstract class UIView<S extends Shape>
		implements IUIView<S> {
	protected WeakReference<IUIInfrastructure<?, ?, ?>> infrastructure = new WeakReference<>(null);
	protected final ConcurrentMap<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? extends IUIView<?>>> extensions = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected final Subject<IBinderAction> binderNotifierSubject = UnicastSubject.create();

	@Override
	public Consumer<Supplier<? extends Observer<? super IBinderAction>>> getBinderSubscriber() { return s -> getBinderNotifierSubject().subscribe(s.get()); }

	protected Subject<IBinderAction> getBinderNotifierSubject() { return binderNotifierSubject; }

	@Override
	public Optional<IUIExtension<INamespacePrefixedString, ? extends IUIView<?>>> addExtension(IUIExtension<INamespacePrefixedString, ? extends IUIView<?>> extension) {
		IExtension.RegExtension.checkExtensionRegistered(extension);
		return IExtensionContainer.addExtension(this, getExtensions(), extension.getType().getKey(), extension);
	}

	@Override
	public Optional<IUIExtension<INamespacePrefixedString, ? extends IUIView<?>>> removeExtension(INamespacePrefixedString key) { return IExtensionContainer.removeExtension(getExtensions(), key); }

	@Override
	public Optional<IUIExtension<INamespacePrefixedString, ? extends IUIView<?>>> getExtension(INamespacePrefixedString key) { return Optional.ofNullable(getExtensions().get(key)); }

	@Override
	public Map<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? extends IUIView<?>>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? extends IUIView<?>>> getExtensions() { return extensions; }

	@Override
	public Optional<? extends IUIInfrastructure<?, ?, ?>> getInfrastructure() { return Optional.ofNullable(infrastructure.get()); }

	@Override
	public void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure) { this.infrastructure = new WeakReference<>(infrastructure); }
}
