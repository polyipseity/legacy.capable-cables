package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.EnumModifyStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.ConstantSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractUIComponentModifier
		implements IUIComponentModifier {
	private final ThreadLocal<EnumModifyStage> threadLocalModifyStage = ThreadLocal.withInitial(ConstantSupplier.of(EnumModifyStage.NONE));
	private OptionalWeakReference<IUIComponent> targetComponent = new OptionalWeakReference<>(null);

	@Override
	public void setTargetComponent(@Nullable IUIComponent targetComponent) { this.targetComponent = new OptionalWeakReference<>(targetComponent); }

	@Override
	public Optional<? extends IUIComponent> getTargetComponent() { return targetComponent.getOptional(); }

	@Override
	public void advanceModifyStage()
			throws IllegalStateException {
		getThreadLocalModifyStage().set(
				AssertionUtilities.assertNonnull(getThreadLocalModifyStage().get())
						.next()
		);
	}

	@Override
	public void resetModifyStage() { getThreadLocalModifyStage().set(EnumModifyStage.NONE); }

	@Override
	public EnumModifyStage getModifyStage() { return AssertionUtilities.assertNonnull(getThreadLocalModifyStage().get()); }

	protected ThreadLocal<EnumModifyStage> getThreadLocalModifyStage() { return threadLocalModifyStage; }
}
