package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;

import java.util.Arrays;
import java.util.function.Supplier;

public final class SecurityManagerHolder
		extends SecurityManager {
	private static final Supplier<@Nonnull SecurityManagerHolder> INSTANCE = Suppliers.memoize(SecurityManagerHolder::new);

	private SecurityManagerHolder() {}

	public static SecurityManagerHolder getInstance() { return INSTANCE.get(); }

	@Override
	public Class<?>[] getClassContext() {
		Class<?>[] r = super.getClassContext();
		return Arrays.copyOfRange(r, 1, r.length);
	}
}
