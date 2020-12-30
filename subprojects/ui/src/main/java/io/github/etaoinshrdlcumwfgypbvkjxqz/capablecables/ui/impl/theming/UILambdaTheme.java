package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class UILambdaTheme
		implements IUITheme {
	private final List<Consumer<@Nonnull ? super @Immutable Map<? super String, ? extends IUIRendererContainer<?>>>> rendererContainerAppliers;

	protected UILambdaTheme(List<Consumer<@Nonnull ? super @Immutable Map<? super String, ? extends IUIRendererContainer<?>>>> rendererContainerAppliers) {
		this.rendererContainerAppliers = ImmutableList.copyOf(rendererContainerAppliers);
	}

	@Override
	public void apply(Iterator<? extends IUIRendererContainer<?>> rendererContainers) {
		@Immutable Map<String, IUIRendererContainer<?>> rendererContainerMap = INamed.toNamedMap(rendererContainers);
		getRendererContainerAppliers()
				.forEach(applier -> applier.accept(rendererContainerMap));
	}

	protected List<Consumer<@Nonnull ? super Map<? super String, ? extends IUIRendererContainer<?>>>> getRendererContainerAppliers() { return rendererContainerAppliers; }

	public static class Builder {
		private final List<Consumer<@Nonnull ? super @Immutable Map<? super String, ? extends IUIRendererContainer<?>>>> rendererContainerAppliers = new ArrayList<>(CapacityUtilities.getInitialCapacityMedium());

		@SuppressWarnings("UnusedReturnValue")
		public Builder addRendererContainerAppliers(Iterator<? extends Consumer<@Nonnull ? super Map<? super String, ? extends IUIRendererContainer<?>>>> appliers) {
			Iterators.addAll(getRendererContainerAppliers(), appliers);
			return this;
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected List<Consumer<@Nonnull ? super Map<? super String, ? extends IUIRendererContainer<?>>>> getRendererContainerAppliers() { return rendererContainerAppliers; }

		public UILambdaTheme build() { return new UILambdaTheme(ImmutableList.copyOf(getRendererContainerAppliers())); }
	}
}
