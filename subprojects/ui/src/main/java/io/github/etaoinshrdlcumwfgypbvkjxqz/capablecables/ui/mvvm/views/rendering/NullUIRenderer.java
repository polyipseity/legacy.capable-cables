package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.rendering;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.Subject;
import io.reactivex.rxjava3.subjects.UnicastSubject;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public class NullUIRenderer<C>
		extends IHasGenericClass.Impl<C>
		implements IUIRenderer<C> {
	@Nullable
	private final String name;
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private final Subject<IBinderAction> binderNotifierSubject = UnicastSubject.create();
	private OptionalWeakReference<C> container = new OptionalWeakReference<>(null);

	@SuppressWarnings("unchecked")
	@UIRendererConstructor
	public NullUIRenderer(UIRendererConstructor.IArguments arguments) {
		super((Class<C>) arguments.getContainerClass());
		this.name = arguments.getName().orElse(null);

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.mappings = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);
	}

	@Override
	public Iterable<? extends ObservableSource<IBinderAction>> getBinderNotifiers() { return Iterables.concat(ImmutableList.of(getBinderNotifierSubject()), IUIRenderer.super.getBinderNotifiers()); }

	protected Subject<IBinderAction> getBinderNotifierSubject() { return binderNotifierSubject; }

	@Override
	public Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappings() { return mappings; }

	@Override
	public Optional<? extends String> getName() { return Optional.ofNullable(name); }

	@Override
	public void onRendererAdded(C container) {
		setContainer(container);
	}

	@Override
	public void onRendererRemoved() {
		setContainer(null);
	}

	public Optional<? extends C> getContainer() { return container.getOptional(); }

	public void setContainer(@Nullable C container) { this.container = new OptionalWeakReference<>(container); }
}
