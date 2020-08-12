package $group__.client.ui.mvvm.core.binding;

import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.specific.ThrowableUtilities.ThrowableCatcher;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// TODO mark as only UI thread
// TODO add binding transformers
public interface IBinder {
	Logger LOGGER = LogManager.getLogger();

	static <B extends IHasBindingKey> Multimap<ResourceLocation, B> sortAndTrimBindings(Iterable<B> bindings) {
		@SuppressWarnings("UnstableApiUsage") Multimap<ResourceLocation, B> ret = MultimapBuilder
				.hashKeys(CapacityUtilities.INITIAL_CAPACITY_MEDIUM)
				.hashSetValues(CapacityUtilities.INITIAL_CAPACITY_TINY).build();
		bindings.forEach(f -> f.getBindingKey().ifPresent(rl -> ret.put(rl, f)));
		return ret;
	}

	static <T, B extends IHasBindingKey & IHasGenericClass<T>, BF extends IHasBindingKey & IHasGenericClass<?>> Iterable<B> checkAndCastBindings(ResourceLocation bindingKey, @Nullable Class<T> clazz, Iterable<BF> bindings) {
		for (BF b : bindings) {
			if (!b.getBindingKey().map(bindingKey::equals).orElse(true))
				throw BecauseOf.illegalArgument("The strings of all bindings are not the same",
						"b.getBindingKey()", b.getBindingKey(),
						"bindingKey", bindingKey,
						"b", b,
						"bindings", bindings);

			if (clazz == null)
				clazz = CastUtilities.castUnchecked(b.getGenericClass()); // COMMENT the first binding determines the type
			else if (!b.getGenericClass().equals(clazz))
				throw BecauseOf.illegalArgument("The types of all bindings are not the same",
						"b.getGenericClass()", b.getGenericClass(),
						"clazzFinal", clazz,
						"b", b,
						"bindings", bindings);
		}
		return CastUtilities.castUnchecked(bindings); // COMMENT safe, type checked
	}

	static DisposableObserver<IBinderAction> createBinderActionObserver(IBinder binder) {
		return new DisposableObserver<IBinderAction>() {
			@Override
			public void onNext(@Nonnull IBinderAction o) {
				switch (o.getType()) {
					case BIND:
						binder.bindFields(o.getFields());
						binder.bindMethods(o.getMethods());
						break;
					case UNBIND:
						binder.unbindFields(o.getFields());
						binder.unbindMethods(o.getMethods());
						break;
					default:
						onError(new InternalError());
						break;
				}
			}

			@Override
			public void onError(@NonNull Throwable e) {
				ThrowableCatcher.catch_(e, LOGGER);
				dispose();
			}

			@Override
			public void onComplete() { dispose(); }
		};
	}

	boolean bindFields(Iterable<IBindingField<?>> fields);

	boolean bindMethods(Iterable<IBindingMethod<?>> methods);

	boolean unbindFields(Iterable<IBindingField<?>> fields);

	boolean unbindMethods(Iterable<IBindingMethod<?>> methods);

	default boolean unbindAll() { return unbindAllFields() | unbindAllMethods(); }

	boolean unbindAllFields();

	boolean unbindAllMethods();
}
