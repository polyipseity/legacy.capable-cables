package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.embed;

import com.google.common.collect.Iterators;
import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIComponentEmbedArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.embed.IUIComponentEmbed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.OneUseRunnable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;

public abstract class UIAbstractComponentEmbed<C extends IUIComponent>
		implements IUIComponentEmbed<C> {
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<C> typeToken;
	private final C component;
	private final Runnable embedInitializer;

	@SuppressWarnings({"UnstableApiUsage", "rawtypes", "RedundantSuppression"})
	public UIAbstractComponentEmbed(Class<C> type,
	                                IUIComponent owner,
	                                IUIComponentEmbedArguments arguments) {
		this.typeToken = TypeToken.of(type);
		this.component = type.cast(arguments.getConstructor().apply(arguments));

		OptionalWeakReference<IUIComponent> ownerReference = OptionalWeakReference.of(owner);
		this.embedInitializer = new OneUseRunnable(() ->
				ownerReference.getOptional().ifPresent(owner1 -> {
					owner1.addChildren(Iterators.singletonIterator(getComponent())); // COMMENT add to owner directly
					getChildrenView().stream()
							.map(IUIComponentEmbed::getEmbedInitializer)
							.forEach(Runnable::run);
				})
		);
	}

	@Override
	public C getComponent() {
		return component;
	}

	@Override
	public Runnable getEmbedInitializer() {
		return embedInitializer;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<C> getTypeToken() {
		return typeToken;
	}
}
