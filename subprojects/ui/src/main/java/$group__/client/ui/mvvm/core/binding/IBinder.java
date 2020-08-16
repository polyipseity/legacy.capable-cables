package $group__.client.ui.mvvm.core.binding;

import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.reactive.DisposableObserverAuto;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

// TODO mark as only UI thread
// TODO add binding transformers
public interface IBinder {
	static <B extends IHasBindingKey> Multimap<ResourceLocation, B> sortAndTrimBindings(Iterable<B> bindings) {
		@SuppressWarnings("UnstableApiUsage") Multimap<ResourceLocation, B> ret = MultimapBuilder
				.hashKeys(CapacityUtilities.INITIAL_CAPACITY_MEDIUM)
				.hashSetValues(CapacityUtilities.INITIAL_CAPACITY_TINY).build();
		bindings.forEach(f -> f.getBindingKey().ifPresent(rl -> ret.put(rl, f)));
		return ret;
	}

	static <T, B extends IHasBindingKey & IHasGenericClass<T>, BF extends IHasBindingKey & IHasGenericClass<?>> Iterable<B> checkAndCastBindings(ResourceLocation bindingKey, @Nullable Class<T> clazz, Iterable<BF> bindings) {
		Function<ResourceLocation, Boolean> bindingKeyEquals = bindingKey::equals;
		for (BF b : bindings) {
			if (!b.getBindingKey().map(bindingKeyEquals).orElse(true))
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
		return new DisposableObserverAuto<IBinderAction>() {
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
