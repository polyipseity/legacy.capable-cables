package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ITypeCapture;
import io.reactivex.rxjava3.core.ObservableSource;

public interface IObservableField<T>
		extends IField<T>, ITypeCapture {
	ObservableSource<? extends T> getNotifier();

	@SuppressWarnings("UnstableApiUsage")
	@Override
	TypeToken<T> getTypeToken();
}
