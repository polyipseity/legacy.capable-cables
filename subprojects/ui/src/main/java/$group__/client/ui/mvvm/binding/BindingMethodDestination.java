package $group__.client.ui.mvvm.binding;

import $group__.client.ui.mvvm.core.binding.IBindingMethod;
import $group__.utilities.interfaces.IHasGenericClass;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class BindingMethodDestination<T>
		extends IHasGenericClass.Impl<T>
		implements IBindingMethod.IDestination<T> {
	@Nullable
	protected final ResourceLocation bindingKey;
	protected final Consumer<T> action;

	public BindingMethodDestination(Class<T> genericClass, @Nullable ResourceLocation bindingKey, Consumer<T> action) {
		super(genericClass);
		this.bindingKey = bindingKey;
		this.action = action;
	}

	@Override
	public Optional<ResourceLocation> getBindingKey() { return Optional.ofNullable(bindingKey); }

	@Override
	public void accept(T argument) { action.accept(argument); }
}
