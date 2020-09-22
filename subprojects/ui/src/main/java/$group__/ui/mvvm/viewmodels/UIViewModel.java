package $group__.ui.mvvm.viewmodels;

import $group__.ui.core.mvvm.IUIInfrastructure;
import $group__.ui.core.mvvm.models.IUIModel;
import $group__.ui.core.mvvm.viewmodels.IUIViewModel;
import $group__.ui.mvvm.extensions.UIExtensionRegistry;
import $group__.utilities.binding.core.IBinderAction;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.extensions.core.IExtension;
import $group__.utilities.extensions.core.IExtensionContainer;
import $group__.utilities.references.OptionalWeakReference;
import $group__.utilities.structures.INamespacePrefixedString;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.Subject;
import io.reactivex.rxjava3.subjects.UnicastSubject;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIViewModel<M extends IUIModel>
		implements IUIViewModel<M> {
	protected final ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> extensions = MapUtilities.newMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected OptionalWeakReference<IUIInfrastructure<?, ?, ?>> infrastructure = new OptionalWeakReference<>(null);
	protected final Subject<IBinderAction> binderNotifierSubject = UnicastSubject.create();
	protected M model;

	public UIViewModel(M model) { this.model = model; }

	@Override
	public M getModel() { return model; }

	@Override
	public void setModel(M model) { this.model = model; }

	@Override
	public Iterable<? extends ObservableSource<IBinderAction>> getBinderNotifiers() { return Iterables.concat(ImmutableList.of(getBinderNotifierSubject()), IUIViewModel.super.getBinderNotifiers()); }

	protected Subject<IBinderAction> getBinderNotifierSubject() { return binderNotifierSubject; }

	@Override
	@Deprecated
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> addExtension(IExtension<? extends INamespacePrefixedString, ?> extension) {
		UIExtensionRegistry.getInstance().checkExtensionRegistered(extension);
		return StaticHolder.addExtensionImpl(this, getExtensions(), extension.getType().getKey(), extension);
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) { return StaticHolder.removeExtensionImpl(getExtensions(), key); }

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return IExtensionContainer.StaticHolder.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() { return extensions; }

	@Override
	public Optional<? extends IUIInfrastructure<?, ?, ?>> getInfrastructure() { return infrastructure.getOptional(); }

	@Override
	public void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure) { this.infrastructure = new OptionalWeakReference<>(infrastructure); }

	@Override
	public void initialize() {}

	@Override
	public void removed() {}
}
