package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;

import java.util.Arrays;
import java.util.function.Supplier;

public final class SecurityManagerHolder
		extends SecurityManager {
	private static final Supplier<SecurityManagerHolder> INSTANCE = Suppliers.memoize(SecurityManagerHolder::new);

	private SecurityManagerHolder() {}

	public static SecurityManagerHolder getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	@Override
	public Class<?>[] getClassContext() {
		Class<?>[] r = super.getClassContext();
		return Arrays.copyOfRange(r, 1, r.length);
	}
}
