package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.theming;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class UILambdaTheme
		implements IUITheme {
	private final List<Consumer<? super @Immutable Map<String, ? extends IUIRendererContainer<?>>>> rendererContainerAppliers;

	public UILambdaTheme(List<Consumer<? super @Immutable Map<String, ? extends IUIRendererContainer<?>>>> rendererContainerAppliers) {
		this.rendererContainerAppliers = ImmutableList.copyOf(rendererContainerAppliers);
	}

	@Override
	public void apply(Iterable<? extends IUIRendererContainer<?>> rendererContainers) {
		@Immutable Map<String, ? extends IUIRendererContainer<?>> rendererContainerMap = INamed.toNamedMap(rendererContainers);
		getRendererContainerAppliers()
				.forEach(applier -> applier.accept(rendererContainerMap));
	}

	protected List<Consumer<? super Map<String, ? extends IUIRendererContainer<?>>>> getRendererContainerAppliers() { return rendererContainerAppliers; }

	public static class Builder {
		private final List<Consumer<? super @Immutable Map<String, ? extends IUIRendererContainer<?>>>> rendererContainerAppliers = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);

		@SuppressWarnings("UnusedReturnValue")
		public Builder addRendererContainerAppliers(Iterable<? extends Consumer<? super Map<String, ? extends IUIRendererContainer<?>>>> appliers) {
			Iterables.addAll(getRendererContainerAppliers(), appliers);
			return this;
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected List<Consumer<? super Map<String, ? extends IUIRendererContainer<?>>>> getRendererContainerAppliers() { return rendererContainerAppliers; }

		public UILambdaTheme build() { return new UILambdaTheme(ImmutableList.copyOf(getRendererContainerAppliers())); }
	}
}
