package $group__.client.ui.mvvm.binding;

import $group__.client.ui.mvvm.core.binding.IBindingField;
import $group__.client.ui.mvvm.core.binding.IObservableField;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;

public class BindingField<T> implements IBindingField<T> {
	@Nullable
	protected final ResourceLocation bindingKey;
	protected final IObservableField<T> field;

	public BindingField(@Nullable ResourceLocation bindingKey, IObservableField<T> field) {
		this.bindingKey = bindingKey;
		this.field = field;
	}

	@Override
	public IObservableField<T> getField() { return field; }

	@Override
	public Optional<ResourceLocation> getBindingKey() { return Optional.ofNullable(bindingKey); }
}
