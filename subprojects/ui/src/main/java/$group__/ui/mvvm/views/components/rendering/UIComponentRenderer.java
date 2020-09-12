package $group__.ui.mvvm.views.components.rendering;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRenderer;
import $group__.ui.core.parsers.components.UIRendererConstructor;
import $group__.utilities.binding.core.IBinderAction;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.interfaces.INamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.Subject;
import io.reactivex.rxjava3.subjects.UnicastSubject;

import java.util.HashMap;
import java.util.Map;

public class UIComponentRenderer<C extends IUIComponent>
		extends IHasGenericClass.Impl<C>
		implements IUIComponentRenderer<C> {
	protected final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	protected final Subject<IBinderAction> binderNotifierSubject = UnicastSubject.create();

	@UIRendererConstructor(type = UIRendererConstructor.ConstructorType.MAPPINGS__CONTAINER_CLASS)
	public UIComponentRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, Class<C> containerClass) {
		super(containerClass);

		this.mappings = new HashMap<>(mappings);
	}

	@Override
	public Iterable<? extends ObservableSource<IBinderAction>> getBinderNotifiers() { return Iterables.concat(ImmutableSet.of(getBinderNotifierSubject()), IUIComponentRenderer.super.getBinderNotifiers()); }

	protected Subject<IBinderAction> getBinderNotifierSubject() { return binderNotifierSubject; }

	@Override
	public Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingView() { return ImmutableMap.copyOf(getMappings()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappings() { return mappings; }
}
