package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.modifiers.EnumModifyStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;

import java.util.Optional;

public abstract class UIAbstractComponentModifier
		implements IUIComponentModifier {
	private final ThreadLocal<EnumModifyStage> threadLocalModifyStage = ThreadLocal.withInitial(ConstantValue.of(EnumModifyStage.NONE));
	private OptionalWeakReference<IUIComponent> targetComponent = OptionalWeakReference.of(null);

	@Override
	public Optional<? extends IUIComponent> getTargetComponent() { return targetComponent.getOptional(); }

	@Override
	public void setTargetComponent(@Nullable IUIComponent targetComponent) { this.targetComponent = OptionalWeakReference.of(targetComponent); }

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
