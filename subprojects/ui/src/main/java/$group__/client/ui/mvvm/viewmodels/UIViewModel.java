package $group__.client.ui.mvvm.viewmodels;

import $group__.client.ui.mvvm.core.IUIInfrastructure;
import $group__.client.ui.mvvm.core.binding.IBinderAction;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.models.IUIModel;
import $group__.client.ui.mvvm.core.viewmodels.IUIViewModel;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableMap;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.Subject;
import io.reactivex.rxjava3.subjects.UnicastSubject;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIViewModel<M extends IUIModel>
		implements IUIViewModel<M> {
	private static final Logger LOGGER = LogManager.getLogger();
	protected final ConcurrentMap<ResourceLocation, IUIExtension<ResourceLocation, ? super IUIViewModel<?>>> extensions = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	@Nullable
	protected IUIInfrastructure<?, ?, ?> infrastructure;
	protected final Subject<IBinderAction> binderNotifierSubject = UnicastSubject.create();
	protected M model;

	public UIViewModel(M model) { this.model = model; }

	@Override
	public M getModel() { return model; }

	@Override
	public void setModel(M model) { this.model = model; }

	@Override
	public ObservableSource<IBinderAction> getBinderNotifier() { return getBinderNotifierSubject(); }

	protected Subject<IBinderAction> getBinderNotifierSubject() { return binderNotifierSubject; }

	@Override
	public Optional<IUIExtension<ResourceLocation, ? super IUIViewModel<?>>> addExtension(IUIExtension<ResourceLocation, ? super IUIViewModel<?>> extension) {
		IExtension.RegExtension.checkExtensionRegistered(extension);
		return IExtensionContainer.addExtension(this, getExtensions(), extension.getType().getKey(), extension);
	}

	@Override
	public Optional<IUIExtension<ResourceLocation, ? super IUIViewModel<?>>> removeExtension(ResourceLocation key) { return IExtensionContainer.removeExtension(getExtensions(), key); }

	@Override
	public Optional<IUIExtension<ResourceLocation, ? super IUIViewModel<?>>> getExtension(ResourceLocation key) { return Optional.ofNullable(getExtensions().get(key)); }

	@Override
	public Map<ResourceLocation, IUIExtension<ResourceLocation, ? super IUIViewModel<?>>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<ResourceLocation, IUIExtension<ResourceLocation, ? super IUIViewModel<?>>> getExtensions() { return extensions; }

	@Override
	public Optional<IUIInfrastructure<?, ?, ?>> getInfrastructure() { return Optional.ofNullable(infrastructure); }

	@Override
	public void setInfrastructure(@Nullable IUIInfrastructure<?, ?, ?> infrastructure) { this.infrastructure = infrastructure; }
}
