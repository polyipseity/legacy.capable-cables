package $group__.client.ui.mvvm.views;

import $group__.client.ui.mvvm.core.binding.IBinderAction;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.views.IUIView;
import $group__.utilities.MapUtilities;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import com.google.common.collect.ImmutableMap;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.Subject;
import io.reactivex.rxjava3.subjects.UnicastSubject;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public abstract class UIView<S extends Shape>
		implements IUIView<S> {
	protected final ConcurrentMap<ResourceLocation, IUIExtension<ResourceLocation, ? extends IUIView<?>>> extensions = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected final Subject<IBinderAction> binderNotifierSubject = UnicastSubject.create();

	@Override
	public Consumer<Supplier<? extends Observer<? super IBinderAction>>> getBinderSubscriber() { return s -> getBinderNotifierSubject().subscribe(s.get()); }

	protected Subject<IBinderAction> getBinderNotifierSubject() { return binderNotifierSubject; }

	@Override
	public Optional<IUIExtension<ResourceLocation, ? extends IUIView<?>>> addExtension(IUIExtension<ResourceLocation, ? extends IUIView<?>> extension) {
		IExtension.RegExtension.checkExtensionRegistered(extension);
		return IExtensionContainer.addExtension(this, getExtensions(), extension.getType().getKey(), extension);
	}

	@Override
	public Optional<IUIExtension<ResourceLocation, ? extends IUIView<?>>> removeExtension(ResourceLocation key) { return IExtensionContainer.removeExtension(getExtensions(), key); }

	@Override
	public Optional<IUIExtension<ResourceLocation, ? extends IUIView<?>>> getExtension(ResourceLocation key) { return Optional.ofNullable(getExtensions().get(key)); }

	@Override
	public Map<ResourceLocation, IUIExtension<ResourceLocation, ? extends IUIView<?>>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<ResourceLocation, IUIExtension<ResourceLocation, ? extends IUIView<?>>> getExtensions() { return extensions; }
}
