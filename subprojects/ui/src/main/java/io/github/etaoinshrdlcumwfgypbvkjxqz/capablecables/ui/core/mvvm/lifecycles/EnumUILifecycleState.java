package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

public enum EnumUILifecycleState {
	BOUND {
		@SuppressWarnings("ConstantConditions")
		@Override
		public void applyState(Object statefulObject, @Nullable Object context) {
			if (statefulObject instanceof IUIStructureLifecycle)
				((IUIStructureLifecycle<?, ?>) statefulObject).bind(CastUtilities.castUncheckedNullable(context)); // COMMENT may throw, intended
		}
	},
	INITIALIZED {
		@SuppressWarnings("ConstantConditions")
		@Override
		public void applyState(Object statefulObject, @Nullable Object context) {
			if (statefulObject instanceof IUIActiveLifecycle)
				((IUIActiveLifecycle<?, ?>) statefulObject).initialize(CastUtilities.castUncheckedNullable(context)); // COMMENT may throw, intended
		}
	},
	;

	public abstract void applyState(Object statefulObject, @Nullable Object context);
}
