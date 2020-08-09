package $group__.client.ui.mvvm.viewmodels;

import $group__.client.ui.mvvm.core.binding.IBinderAction;
import $group__.client.ui.mvvm.core.binding.IBindingField;
import $group__.client.ui.mvvm.core.models.IUIModel;
import $group__.client.ui.mvvm.core.viewmodels.IUIViewModel;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.specific.ThrowableUtilities.Try;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.Subject;
import io.reactivex.rxjava3.subjects.UnicastSubject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class UIViewModel<M extends IUIModel> implements IUIViewModel<M> {
	private static final Logger LOGGER = LogManager.getLogger();
	protected final Subject<IBinderAction> binderNotifier = UnicastSubject.create();
	protected final AtomicReference<ImmutableSet<Supplier<Optional<IBindingField<?>>>>> bindingFields = new AtomicReference<>();
	protected M model;

	public UIViewModel(M model) { this.model = model; }

	@Override
	public M getModel() { return model; }

	@Override
	public void setModel(M model) { this.model = model; }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public Iterable<IBindingField<?>> getBindingFields() {
		synchronized (bindingFields) {
			if (bindingFields.get() == null) {
				bindingFields.set(DynamicUtilities.getSuperclasses(getClass()).stream().unordered()
						.flatMap(c -> Sets.newHashSet(c.getDeclaredFields()).stream())
						.filter(f -> !Modifier.isStatic(f.getModifiers()) && IBindingField.class.isAssignableFrom(f.getType()))
						.map(f -> Try.call(() -> DynamicUtilities.IMPL_LOOKUP.unreflectGetter(f), LOGGER))
						.filter(Optional::isPresent)
						.map(m -> (Supplier<Optional<IBindingField<?>>>) () ->
								Try.call(() -> (IBindingField<?>) m.get().invoke(this), LOGGER))
						.collect(ImmutableSet.toImmutableSet()));
			}
		}
		return bindingFields.get().stream().unordered()
				.map(Supplier::get)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(ImmutableSet.toImmutableSet());
	}

	@Override
	public void subscribe(@NonNull Observer<? super IBinderAction> observer) { getBinderNotifier().subscribe(observer); }

	protected Subject<IBinderAction> getBinderNotifier() { return binderNotifier; }
}
