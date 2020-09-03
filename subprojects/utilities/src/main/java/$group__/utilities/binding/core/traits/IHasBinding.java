package $group__.utilities.binding.core.traits;

import $group__.utilities.binding.BindingUtilities;
import $group__.utilities.binding.BindingUtilities.EnumOptions;
import $group__.utilities.binding.core.IBinderAction;
import $group__.utilities.binding.core.fields.IBindingField;
import $group__.utilities.binding.core.methods.IBindingMethod;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import io.reactivex.rxjava3.core.ObservableSource;

import java.util.EnumSet;

public interface IHasBinding {
	default Iterable<? extends IBindingField<?>> getBindingFields() { return BindingUtilities.getBindingFields(this, EnumSet.of(EnumOptions.SELF, EnumOptions.VARIABLES)); }

	default Iterable<? extends IBindingMethod<?>> getBindingMethods() { return BindingUtilities.getBindingMethods(this, EnumSet.of(EnumOptions.SELF, EnumOptions.VARIABLES)); }

	@SuppressWarnings("UnstableApiUsage")
	default Iterable<? extends ObservableSource<IBinderAction>> getBinderNotifiers() {
		return Streams.stream(BindingUtilities.getHasBindingsVariables(this)).unordered()
				.flatMap(v -> Streams.stream(v.getBinderNotifiers()))
				.collect(ImmutableSet.toImmutableSet());
	}
}
