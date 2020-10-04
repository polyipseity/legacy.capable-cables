package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LoopUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IFunction3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public enum EnumModifyStage {
	NONE {
		@Override
		public EnumModifyStage next() { return PRE; }
	},
	PRE {
		@Override
		public EnumModifyStage next() { return POST; }
	},
	POST {
		@Override
		public EnumModifyStage next() throws IllegalStateException { throw new IllegalStateException(); }
	},
	;

	public static <R, RSelf, RPrePost,
			M extends Iterable<? extends IUIComponentModifier>,
			TH extends Throwable>
	Optional<R> handleModifiers
			(Supplier<? extends RSelf> action,
			 M modifiers,
			 Function<? super M, ? extends RPrePost> prePostAction,
			 IFunction3<? super RSelf, ? super RPrePost, ? super RPrePost, ? extends R, ? extends TH> combiner)
			throws TH {
		return handleModifiers(action, modifiers, prePostAction, prePostAction, combiner);
	}

	public static <R, RSelf, RPre, RPost,
			M extends Iterable<? extends IUIComponentModifier>,
			TH extends Throwable>
	Optional<R> handleModifiers
			(Supplier<? extends RSelf> action,
			 M modifiers,
			 Function<? super M, ? extends RPre> preAction,
			 Function<? super M, ? extends RPost> postAction,
			 IFunction3<? super RSelf, ? super RPre, ? super RPost, ? extends R, ? extends TH> combiner)
			throws TH {
		modifiers.forEach(IUIComponentModifier::advanceModifyStage);
		@Nullable RPre preRet = preAction.apply(modifiers);

		@Nullable RSelf selfRet = action.get();

		modifiers.forEach(IUIComponentModifier::advanceModifyStage);
		@Nullable RPost postRet = postAction.apply(modifiers);

		modifiers.forEach(IUIComponentModifier::resetModifyStage);
		return Optional.ofNullable(combiner.apply(selfRet, preRet, postRet));
	}

	public static <M extends Iterable<? extends IUIComponentModifier>> void handleModifiers
			(Runnable action,
			 M modifiers,
			 Consumer<? super M> prePostAction) {
		handleModifiers(action, modifiers, prePostAction, prePostAction);
	}

	public static <M extends Iterable<? extends IUIComponentModifier>> void handleModifiers
			(Runnable action,
			 M modifiers,
			 Consumer<? super M> preAction,
			 Consumer<? super M> postAction) {
		handleModifiers(FunctionUtilities.asVoidSupplier(action),
				modifiers,
				FunctionUtilities.asVoidFunction(preAction),
				FunctionUtilities.asVoidFunction(postAction),
				IFunction3.StaticHolder.<Void, Void, Void, Void, RuntimeException>getEmpty());
	}

	public void advanceModifyStage(IUIComponentModifier modifier) {
		LoopUtilities.doNTimes(ordinal() - modifier.getModifyStage().ordinal(), modifier::advanceModifyStage);
	}

	public boolean isPre() { return this == PRE; }

	public boolean isPost() { return this == POST; }

	public abstract EnumModifyStage next()
			throws IllegalStateException;
}
