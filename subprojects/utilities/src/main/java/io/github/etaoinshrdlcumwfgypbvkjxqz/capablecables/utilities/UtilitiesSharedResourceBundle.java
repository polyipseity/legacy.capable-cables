package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.SharedResourceBundleTemplate;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public final class UtilitiesSharedResourceBundle
		extends SharedResourceBundleTemplate {
	private static final Supplier<UtilitiesSharedResourceBundle> INSTANCE = Suppliers.memoize(UtilitiesSharedResourceBundle::new);

	private UtilitiesSharedResourceBundle() {
		super(CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance()));
	}

	public static UtilitiesSharedResourceBundle getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	public enum EnumKey
			implements IResourceBundleKey {
		REFLECT_MEMBER_GET_FAIL("reflect.member.get.fail"),
		REFLECT_MEMBER_ACCESSIBLE_SET_FAIL("reflect.member.accessible.set.fail"),
		REFLECT_MEMBER_FIELD_GET_FAIL("reflect.member.field.get.fail"),
		REFLECT_MEMBER_FIELD_SET_FAIL("reflect.member.field.set.fail"),
		INVOKE_HANDLE_FIND_FAIL("invoke.handle.find.fail"),
		INVOKE_HANDLE_UN_REFLECT_FAIL("invoke.handle.un_reflect.fail"),
		INVOKE_HANDLE_INVOKE_FAIL("invoke.handle.invoke.fail"),
		;

		private static final @NonNls String RESOURCE_BUNDLE_PATH = "io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesSharedResourceBundle";
		@PropertyKey(resourceBundle = RESOURCE_BUNDLE_PATH)
		private final String key;

		EnumKey(@PropertyKey(resourceBundle = RESOURCE_BUNDLE_PATH) String key) { this.key = key; }

		public static String getResourceBundlePath() { return RESOURCE_BUNDLE_PATH; }

		@Override
		@PropertyKey(resourceBundle = RESOURCE_BUNDLE_PATH)
		public String getKey() { return key; }
	}
}
