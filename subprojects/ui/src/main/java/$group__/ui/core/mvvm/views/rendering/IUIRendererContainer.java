package $group__.ui.core.mvvm.views.rendering;

import $group__.ui.UIConfiguration;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.binding.core.traits.IHasBinding;
import $group__.utilities.templates.CommonConfigurationTemplate;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public interface IUIRendererContainer<R extends IUIRenderer<?>>
		extends IHasBinding {
	Optional<? extends R> getRenderer();

	@Deprecated
		// COMMENT unchecked, use 'setRendererChecked'
	void setRenderer(@Nullable R renderer);

	static <C extends IUIRendererContainer<? super R>, R extends IUIRenderer<? super C>> void setRendererChecked(C container, R renderer) { container.setRenderer(renderer); }

	Class<? extends R> getDefaultRendererClass();

	enum StaticHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		public static <C, R extends IUIRenderer<?>> void setRendererImpl(C container, @Nullable R renderer, BiConsumer<? super C, ? super R> setter) {
			if (!(renderer == null || renderer.getGenericClass().isInstance(container)))
				throw ThrowableUtilities.logAndThrow(new IllegalArgumentException(
						new LogMessageBuilder()
								.addKeyValue("container", container).addKeyValue("renderer", renderer).addKeyValue("setter", setter)
								.appendMessages(getResourceBundle().getString("renderer.set.impl.instance_of.fail"))
								.build()
				), UIConfiguration.getInstance().getLogger());
			setter.accept(container, renderer);
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
