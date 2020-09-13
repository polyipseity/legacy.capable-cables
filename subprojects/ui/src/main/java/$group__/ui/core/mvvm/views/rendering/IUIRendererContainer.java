package $group__.ui.core.mvvm.views.rendering;

import $group__.utilities.ThrowableUtilities;
import $group__.utilities.binding.core.traits.IHasBinding;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiConsumer;

public interface IUIRendererContainer<R extends IUIRenderer<?>>
		extends IHasBinding {
	Optional<? extends R> getRenderer();

	void setRenderer(@Nullable R renderer);

	static <C extends IUIRendererContainer<? super R>, R extends IUIRenderer<? super C>> void setRendererChecked(C container, R renderer) { container.setRenderer(renderer); }

	static <C, R extends IUIRenderer<?>> void setRendererImpl(C self, @Nullable R renderer, BiConsumer<? super C, ? super R> setter) {
		if (!(renderer == null || renderer.getGenericClass().isInstance(self)))
			throw ThrowableUtilities.BecauseOf.illegalArgument("Self is not an instance of renderer's container class",
					"self.getClass()", self.getClass(),
					"renderer.getGenericClass()", renderer.getGenericClass(),
					"renderer", renderer,
					"self", self);
		setter.accept(self, renderer);
	}

	Class<? extends R> getDefaultRendererClass();
}
