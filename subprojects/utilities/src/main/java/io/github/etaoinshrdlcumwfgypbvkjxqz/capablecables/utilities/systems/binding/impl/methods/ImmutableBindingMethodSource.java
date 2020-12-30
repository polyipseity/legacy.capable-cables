package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.methods;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.methods.IBindingMethodSource;
import io.reactivex.rxjava3.processors.MulticastProcessor;
import io.reactivex.rxjava3.processors.PublishProcessor;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;

import java.util.Optional;

public final class ImmutableBindingMethodSource<T>
		implements IBindingMethodSource<T> {
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<T> typeToken;
	@Nullable
	private final IIdentifier bindingKey;
	private final Processor<T, T> notifierProcessor;

	@SuppressWarnings("UnstableApiUsage")
	private ImmutableBindingMethodSource(Class<T> type, @Nullable IIdentifier bindingKey) {
		this.typeToken = TypeToken.of(type);
		this.bindingKey = bindingKey;
		this.notifierProcessor = PublishProcessor.<T>create().subscribeWith(MulticastProcessor.create());
	}

	public static <T> ImmutableBindingMethodSource<T> of(Class<T> type, @Nullable IIdentifier bindingKey) {
		return new ImmutableBindingMethodSource<>(type, bindingKey);
	}

	@Override
	public Publisher<T> getNotifier() { return getNotifierProcessor(); }

	protected Processor<T, T> getNotifierProcessor() {
		return notifierProcessor;
	}

	@Override
	public Optional<? extends IIdentifier> getBindingKey() { return Optional.ofNullable(bindingKey); }

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<T> getTypeToken() {
		return typeToken;
	}

	@Override
	public void invoke(T argument) {
		getNotifierProcessor().onNext(argument);
	}
}
