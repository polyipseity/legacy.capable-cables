package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.common;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IContextIndependentJAXBAdapterFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.JAXBUIUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.JAXBUIUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBFunctionalElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import jakarta.xml.bind.JAXBElement;
import org.jetbrains.annotations.NonNls;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.function.Function;

@SuppressWarnings("unused")
public enum EnumJAXBDefaultElementAdapter {
	BOOLEAN(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createBoolean), Boolean.class),
			new JAXBFunctionalElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createBoolean))),
	BYTE(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createByte), Byte.class),
			new JAXBFunctionalElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createByte))),
	SHORT(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createShort), Short.class),
			new JAXBFunctionalElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createShort))),
	INT(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createInt), Integer.class),
			new JAXBFunctionalElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createInt))),
	LONG(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createLong), Long.class),
			new JAXBFunctionalElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createLong))),
	FLOAT(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createFloat), Float.class),
			new JAXBFunctionalElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createFloat))),
	DOUBLE(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createDouble), Double.class),
			new JAXBFunctionalElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createDouble))),
	STRING(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createString), String.class),
			new JAXBFunctionalElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createString))),
	;

	@NonNls
	private static final ImmutableMap<String, Function<@Nonnull EnumJAXBDefaultElementAdapter, @Nullable ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<@Nonnull EnumJAXBDefaultElementAdapter, @Nullable ?>>builder()
					.put("key", EnumJAXBDefaultElementAdapter::getKey)
					.put("value", EnumJAXBDefaultElementAdapter::getValue)
					.build();
	private final ITuple2<? extends QName, ? extends Class<?>> key;
	private final IJAXBElementAdapter<?, ?> value;

	<L, R, V extends IJAXBElementAdapter<L, R>> EnumJAXBDefaultElementAdapter(ITuple2<? extends QName, ? extends Class<R>> key, V value) {
		this.key = key;
		this.value = value;
	}

	public static void registerAll(IJAXBAdapterRegistry registry) {
		Arrays.stream(values()).unordered()
				.forEach(adapter -> adapter.register(registry));
	}

	@SuppressWarnings("deprecation")
	public void register(IJAXBAdapterRegistry registry) {
		registry.getElementRegistry().register(getKey(), getValue()); // COMMENT use deprecated, checked offers no benefits
	}

	public ITuple2<? extends QName, ? extends Class<?>> getKey() {
		return key;
	}

	public IJAXBElementAdapter<?, ?> getValue() {
		return value;
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, getObjectVariableMap());
	}

	public static ImmutableMap<String, Function<@Nonnull EnumJAXBDefaultElementAdapter, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
}
