package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import javax.annotation.OverridingMethodsMustInvokeSuper;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIActiveLifecycle<IC, CC>
		extends IUILifecycle {
	@SuppressWarnings("ConstantConditions")
	static void initializeV(IUIActiveLifecycle<@Nullable ? extends Void, ?> instance) {
		instance.initialize(null);
	}

	@OverridingMethodsMustInvokeSuper
	default void initialize(IC context) {
		// COMMENT for 'OverridingMethodsMustInvokeSuper'
	}

	@SuppressWarnings("ConstantConditions")
	static void cleanupV(IUIActiveLifecycle<?, @Nullable ? extends Void> instance) {
		instance.cleanup(null);
	}

	@OverridingMethodsMustInvokeSuper
	default void cleanup(CC context) {
		// COMMENT for 'OverridingMethodsMustInvokeSuper'
	}
}
