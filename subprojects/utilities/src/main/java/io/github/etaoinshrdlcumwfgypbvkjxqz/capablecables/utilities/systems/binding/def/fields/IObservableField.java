package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ITypeCapture;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.INotifier;

public interface IObservableField<T>
		extends IField<T>, INotifier<T>, ITypeCapture {
	@SuppressWarnings("UnstableApiUsage")
	@Override
	TypeToken<T> getTypeToken();
}
