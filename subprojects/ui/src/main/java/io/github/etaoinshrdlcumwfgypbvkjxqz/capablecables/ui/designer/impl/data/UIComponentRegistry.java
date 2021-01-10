package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.impl.data;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObjectInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl.AbstractRegistry;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIComponentRegistry
		extends AbstractRegistry<Class<? extends IUIComponent>, Consumer<? super IUIComponentArguments>> {
	@SuppressWarnings("unchecked")
	private static final Supplier<UIComponentRegistry> INSTANCE = Suppliers.memoize(() -> {
		UIComponentRegistry instance = new UIComponentRegistry();

		// COMMENT empty consumer registration
		for (Class<?> clazz : new Class<?>[]{
				UIDefaultComponent.class, UIDefaultComponentManager.class, UIShapeComponent.class,
				UILabelComponent.class, UIButtonComponent.class, UIListComponent.class, UIScrollbarComponent.class,
				UIScrollPanelComponent.class, UIWindowComponent.class,
		})
			instance.register((Class<? extends IUIComponent>) clazz, FunctionUtilities.getEmptyConsumer());

		return instance;
	});

	private static final long serialVersionUID = 6296770663007275604L;
	private final ConcurrentMap<Class<? extends IUIComponent>, IRegistryObjectInternal<? extends Consumer<? super IUIComponentArguments>>> data =
			MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();

	private UIComponentRegistry() {
		super(false);
	}

	public static UIComponentRegistry getInstance() {
		return INSTANCE.get();
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected ConcurrentMap<Class<? extends IUIComponent>, IRegistryObjectInternal<? extends Consumer<? super IUIComponentArguments>>> getData() {
		return data;
	}

	@Override
	protected Logger getLogger() {
		return UIConfiguration.getInstance().getLogger();
	}
}
