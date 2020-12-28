package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers;

import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ITypeCapture;

public abstract class JAXBUIAbstractSubContextualAdapterHandler<L, SC>
		extends JAXBUIAbstractAdapterHandler<L>
		implements ITypeCapture {
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<SC> typeToken;

	@SuppressWarnings("UnstableApiUsage")
	public JAXBUIAbstractSubContextualAdapterHandler(Class<SC> subContextClazz) {
		this.typeToken = TypeToken.of(subContextClazz);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public final void accept(@Nonnull IJAXBAdapterContext context, @Nonnull L left) {
		@SuppressWarnings({"unchecked", "RedundantSuppression"})
		SC subContext = (SC) // COMMENT should be always safe
				context.getDatum(getTypeToken().getRawType())
						.orElseThrow(IllegalArgumentException::new);
		accept0(context, subContext, left);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<SC> getTypeToken() {
		return typeToken;
	}

	protected abstract void accept0(IJAXBAdapterContext context, SC subContext, L left);
}
