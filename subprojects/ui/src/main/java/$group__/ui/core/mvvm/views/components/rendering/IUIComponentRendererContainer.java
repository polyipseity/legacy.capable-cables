package $group__.ui.core.mvvm.views.components.rendering;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.binding.core.traits.IHasBinding;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiConsumer;

public interface IUIComponentRendererContainer<R extends IUIComponentRenderer<?>>
		extends IHasBinding { // TODO should remove IHasBinding?
	Optional<? extends R> getRenderer();

	void setRenderer(@Nullable R renderer);

	static <C extends IUIComponent & IUIComponentRendererContainer<? super R>, R extends IUIComponentRenderer<? super C>> void setRendererChecked(C container, R renderer) { container.setRenderer(renderer); }

	static <C, R extends IUIComponentRenderer<?>> void setRendererImpl(C self, @Nullable R renderer, BiConsumer<? super C, ? super R> setter) {
		if (!(renderer == null || renderer.getGenericClass().isInstance(self)))
			throw ThrowableUtilities.BecauseOf.illegalArgument("Self is not an instance of renderer's container class",
					"self.getClass()", self.getClass(),
					"renderer.getGenericClass()", renderer.getGenericClass(),
					"renderer", renderer,
					"self", self);
		setter.accept(self, renderer);
	}
}
