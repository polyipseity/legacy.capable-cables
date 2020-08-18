package $group__.client.ui.mvvm.views.components.extensions.caches;

import $group__.client.ui.core.IShapeDescriptor;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.extensions.caches.IUIExtensionCache;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.MapUtilities;
import $group__.utilities.extensions.IExtensionContainer;
import com.google.common.collect.ImmutableSet;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class UICacheShapeDescriptor<V, I extends IExtensionContainer<ResourceLocation, ?>>
		extends IUIExtensionCache.IType.Impl<V, I> {
	protected final Set<I> instances = Collections.newSetFromMap(
			MapUtilities.getMapMakerSingleThreadedWithWeakKeys().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap());

	public UICacheShapeDescriptor(ResourceLocation key, BiFunction<? super IUIExtensionCache.IType<V, I>, ? super I, ? extends Optional<? extends V>> getter, BiConsumer<? super IUIExtensionCache.IType<V, I>, ? super I> invalidator, Function<? super IUIExtensionCache.IType<V, I>, ? extends List<? extends DisposableObserver<?>>> eventListenersFunction) {
		super(key, getter, invalidator, eventListenersFunction);
	}

	public static Optional<IUIComponent> getInstanceFromShapeDescriptor(Set<? extends IUIComponent> instances, IShapeDescriptor<?> shapeDescriptor) {
		for (IUIComponent i : instances) {
			if (shapeDescriptor.equals(i.getShapeDescriptor()))
				return Optional.of(i);
		}
		return Optional.empty();
	}

	@Override
	public Optional<V> get(I instance) {
		getInstances().add(instance);
		return super.get(instance);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<I> getInstances() { return instances; }

	public Set<I> getInstancesView() { return ImmutableSet.copyOf(getInstances()); }
}