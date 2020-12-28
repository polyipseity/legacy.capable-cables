package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ResourceBundle;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressBoxing;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIStructureLifecycle<IC, CC>
		extends IUILifecycle {
	@SuppressWarnings("ConstantConditions")
	static void bindV(IUIStructureLifecycle<@Nullable ? extends Void, ?> instance) {
		instance.bind(null);
	}

	@OverridingMethodsMustInvokeSuper
	default void bind(IC context) {
		// COMMENT for 'OverridingMethodsMustInvokeSuper'
	}

	@SuppressWarnings("ConstantConditions")
	static void unbindV(IUIStructureLifecycle<?, @Nullable ? extends Void> instance) {
		instance.unbind(null);
	}

	@OverridingMethodsMustInvokeSuper
	default void unbind(CC context) {
		// COMMENT for 'OverridingMethodsMustInvokeSuper'
	}

	static void checkBoundState(boolean bound, boolean expected)
			throws IllegalStateException {
		if (bound != expected)
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerUIInfrastructure)
							.addKeyValue("bound", suppressBoxing(bound)).addKeyValue("expected", suppressBoxing(expected))
							.addMessages(bound
									? () -> StaticHolder.getResourceBundle().getString("check.bound.true")
									: () -> StaticHolder.getResourceBundle().getString("check.bound.false"))
							.build());
	}

	enum StaticHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
