package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.common;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.ConstantSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.ResourceBundleLocalizedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBObjectAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.types.IConstantSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.types.IResourceBundleLocalizedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.JAXBUIUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBFunctionalObjectAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.types.ImmutableConstantSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.types.ImmutableResourceBundleLocalizedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.internationalization.ChangingLocaleResourceBundle;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import org.jetbrains.annotations.NonNls;

import java.util.Arrays;
import java.util.function.Function;

@SuppressWarnings("unused")
public enum EnumJAXBDefaultObjectAdapter {
	CONSTANT_SUPPLIER(ImmutableTuple2.of(ConstantSupplier.class, IConstantSupplier.class),
			new JAXBFunctionalObjectAdapter<>(
					(context, left) ->
							ImmutableConstantSupplier.of(IJAXBAdapterRegistry.adaptFromJAXB(context, left.getAny())),
					(context, right) -> {
						ConstantSupplier left = ObjectFactories.getDefaultUIObjectFactory().createConstantSupplier();
						left.setAny(IJAXBAdapterRegistry.adaptToJAXB(context, right.get()));
						return left;
					}
			)),
	RESOURCE_BUNDLE_LOCALIZED_STRING(ImmutableTuple2.of(ResourceBundleLocalizedString.class, IResourceBundleLocalizedString.class),
			new JAXBFunctionalObjectAdapter<>(
					(context, left) ->
							ImmutableResourceBundleLocalizedString.of(left.getKey(),
									new ChangingLocaleResourceBundle.Builder()
											.setBaseName(left.getBaseName())
											.setLocaleSupplier(UIConfiguration.getInstance().getLocaleSupplier())
											.build()),
					(context, right) -> {
						ResourceBundleLocalizedString left = ObjectFactories.getDefaultUIObjectFactory().createResourceBundleLocalizedString();
						left.setKey(right.getKey());
						left.setBaseName(right.getBaseName());
						return left;
					}
			)),
	;

	@NonNls
	private static final ImmutableMap<String, Function<@Nonnull EnumJAXBDefaultObjectAdapter, @Nullable ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<@Nonnull EnumJAXBDefaultObjectAdapter, @Nullable ?>>builder()
					.put("key", EnumJAXBDefaultObjectAdapter::getKey)
					.put("value", EnumJAXBDefaultObjectAdapter::getValue)
					.build();
	private final ITuple2<? extends Class<?>, ? extends Class<?>> key;
	private final IJAXBObjectAdapter<?, ?> value;

	<L, R, V extends IJAXBObjectAdapter<L, R>> EnumJAXBDefaultObjectAdapter(ITuple2<? extends Class<L>, ? extends Class<R>> key, V value) {
		this.key = key;
		this.value = value;
	}

	public static void registerAll(IJAXBAdapterRegistry registry) {
		Arrays.stream(values()).unordered()
				.forEach(adapter -> adapter.register(registry));
	}

	@SuppressWarnings("deprecation")
	public void register(IJAXBAdapterRegistry registry) {
		registry.getObjectRegistry().register(getKey(), getValue()); // COMMENT use deprecated, checked offers no benefits
	}

	public ITuple2<? extends Class<?>, ? extends Class<?>> getKey() {
		return key;
	}

	public IJAXBObjectAdapter<?, ?> getValue() {
		return value;
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, getObjectVariableMap());
	}

	public static ImmutableMap<String, Function<@Nonnull EnumJAXBDefaultObjectAdapter, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
}
