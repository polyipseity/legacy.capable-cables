package $group__.client.ui.mvvm.viewmodels;

import $group__.client.ui.core.mvvm.IUIInfrastructure;
import $group__.client.ui.core.mvvm.binding.IBinderAction;
import $group__.client.ui.core.mvvm.extensions.IUIExtension;
import $group__.client.ui.core.mvvm.models.IUIModel;
import $group__.client.ui.core.mvvm.viewmodels.IUIViewModel;
import $group__.utilities.MapUtilities;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.Subject;
import io.reactivex.rxjava3.subjects.UnicastSubject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIViewModel<M extends IUIModel>
		implements IUIViewModel<M> {
	private static final Logger LOGGER = LogManager.getLogger();
	protected final ConcurrentMap<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? super IUIViewModel<?>>> extensions = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected WeakReference<IUIInfrastructure<?, ?, ?>> infrastructure = new WeakReference<>(null);
	protected final Subject<IBinderAction> binderNotifierSubject = UnicastSubject.create();
	protected M model;

	public UIViewModel(M model) { this.model = model; }

	@Override
	public M getModel() { return model; }

	@Override
	public void setModel(M model) { this.model = model; }

	@Override
	public Consumer<Supplier<? extends Observer<? super IBinderAction>>> getBinderSubscriber() { return s -> getBinderNotifierSubject().subscribe(s.get()); }

	protected Subject<IBinderAction> getBinderNotifierSubject() { return binderNotifierSubject; }

	@Override
	public Optional<IUIExtension<INamespacePrefixedString, ? super IUIViewModel<?>>> addExtension(IUIExtension<INamespacePrefixedString, ? super IUIViewModel<?>> extension) {
		IExtension.RegExtension.checkExtensionRegistered(extension);
		return IExtensionContainer.addExtension(this, getExtensions(), extension.getType().getKey(), extension);
	}

	@Override
	public Optional<IUIExtension<INamespacePrefixedString, ? super IUIViewModel<?>>> removeExtension(INamespacePrefixedString key) { return IExtensionContainer.removeExtension(getExtensions(), key); }

	@Override
	public Optional<IUIExtension<INamespacePrefixedString, ? super IUIViewModel<?>>> getExtension(INamespacePrefixedString key) { return Optional.ofNullable(getExtensions().get(key)); }

	@Override
	public Map<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? super IUIViewModel<?>>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IUIExtension<INamespacePrefixedString, ? super IUIViewModel<?>>> getExtensions() { return extensions; }

	@Override
	public Optional<? extends IUIInfrastructure<?, ?, ?>> getInfrastructure() { return Optional.ofNullable(infrastructure.get()); }

	@Override
	public void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure) { this.infrastructure = new WeakReference<>(infrastructure); }
}
