package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields.IObservableField;
import io.reactivex.rxjava3.processors.BehaviorProcessor;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;

public abstract class AbstractObservableField<T>
		implements IObservableField<T> {
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<T> typeToken;
	private final Processor<T, T> notifierProcessor;

	@SuppressWarnings("UnstableApiUsage")
	public AbstractObservableField(Class<T> type, T initialValue) {
		this.typeToken = TypeToken.of(type);
		this.notifierProcessor = BehaviorProcessor.createDefault(initialValue);
	}

	@Override
	public Publisher<T> getNotifier() {
		return getNotifierProcessor();
	}

	@Override
	@SuppressWarnings("UnstableApiUsage")
	public TypeToken<T> getTypeToken() {
		return typeToken;
	}

	protected Processor<T, T> getNotifierProcessor() {
		return notifierProcessor;
	}
}
