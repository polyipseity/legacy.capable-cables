package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.modifiers;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.EnumModifyStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIVirtualComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction.UIImmutableComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl.UIDefaultComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;

import java.util.Optional;

public abstract class UIAbstractVirtualComponent
		extends UIDefaultComponent
		implements IUIComponentModifier, IUIVirtualComponent {
	private final ThreadLocal<EnumModifyStage> threadLocalModifyStage = ThreadLocal.withInitial(ConstantValue.of(EnumModifyStage.NONE));
	private OptionalWeakReference<IUIComponent> targetComponent = new OptionalWeakReference<>(null);

	public UIAbstractVirtualComponent(IShapeDescriptor<?> shapeDescriptor) {
		super(UIImmutableComponentArguments.of(null, ImmutableMap.of(), shapeDescriptor, null, ImmutableMap.of()));
	}

	@Override
	public Optional<? extends IUIComponent> getTargetComponent() { return targetComponent.getOptional(); }

	@Override
	public void setTargetComponent(@Nullable IUIComponent targetComponent) { this.targetComponent = new OptionalWeakReference<>(targetComponent); }

	@Override
	public void advanceModifyStage()
			throws IllegalStateException {
		setModifyStage(getModifyStage().next());
	}

	@Override
	public void resetModifyStage() { setModifyStage(EnumModifyStage.NONE); }

	@Override
	public EnumModifyStage getModifyStage() { return AssertionUtilities.assertNonnull(getThreadLocalModifyStage().get()); }

	protected void setModifyStage(EnumModifyStage stage) {
		getThreadLocalModifyStage().set(stage);
	}

	protected ThreadLocal<EnumModifyStage> getThreadLocalModifyStage() { return threadLocalModifyStage; }
}
