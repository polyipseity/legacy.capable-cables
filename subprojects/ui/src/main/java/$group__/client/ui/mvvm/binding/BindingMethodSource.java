package $group__.client.ui.mvvm.binding;

import $group__.client.ui.mvvm.core.binding.IBindingMethod;
import $group__.utilities.interfaces.IHasGenericClass;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;

public class BindingMethodSource<T>
		extends IHasGenericClass.Impl<T>
		implements IBindingMethod.ISource<T> {
	@Nullable
	protected final ResourceLocation bindingKey;
	protected final Subject<T> notifierSubject = PublishSubject.create();

	public BindingMethodSource(Class<T> genericClass, @Nullable ResourceLocation bindingKey) {
		super(genericClass);
		this.bindingKey = bindingKey;
	}

	@Override
	public ObservableSource<T> getNotifier() { return getNotifierSubject(); }

	@Override
	public void invoke(T argument) { getNotifierSubject().onNext(argument); }

	protected Subject<T> getNotifierSubject() { return notifierSubject; }

	@Override
	public Optional<ResourceLocation> getBindingKey() { return Optional.ofNullable(bindingKey); }
}
