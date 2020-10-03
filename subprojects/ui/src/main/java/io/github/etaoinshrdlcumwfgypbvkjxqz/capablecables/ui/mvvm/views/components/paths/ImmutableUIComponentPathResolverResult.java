package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.paths;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIVirtualComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentPathResolverResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public final class ImmutableUIComponentPathResolverResult
		implements IUIComponentPathResolverResult {
	private static final ImmutableUIComponentPathResolverResult EMPTY = new ImmutableUIComponentPathResolverResult();
	@Nullable
	private final IUIComponent component;
	private final List<IUIVirtualComponent> virtualComponents;

	private ImmutableUIComponentPathResolverResult() {
		this.component = null;
		this.virtualComponents = ImmutableList.of();
	}

	private ImmutableUIComponentPathResolverResult(IUIComponent component, List<? extends IUIVirtualComponent> virtualComponents) {
		this.component = component;
		this.virtualComponents = ImmutableList.copyOf(virtualComponents);
	}

	public static ImmutableUIComponentPathResolverResult of(@Nullable IUIComponent component) { return of(component, ImmutableList.of()); }

	public static ImmutableUIComponentPathResolverResult of(@Nullable IUIComponent component, List<? extends IUIVirtualComponent> virtualComponents) {
		ImmutableUIComponentPathResolverResult result;
		if (component == null) {
			assert virtualComponents.isEmpty();
			result = getEmpty();
		} else {
			result = new ImmutableUIComponentPathResolverResult(component, virtualComponents);
		}
		return result;
	}

	public static ImmutableUIComponentPathResolverResult getEmpty() { return EMPTY; }

	@Override
	public Optional<? extends IUIComponent> getComponent() { return Optional.ofNullable(component); }

	@Override
	public Optional<? extends IUIComponent> getConcreteComponent() {
		return isVirtual() ?
				getComponent().map(IUIVirtualComponent.class::cast).flatMap(IUIComponentModifier::getTargetComponent) :
				getComponent();
	}

	@Override
	public List<? extends IUIVirtualComponent> getVirtualComponentsView() { return ImmutableList.copyOf(getVirtualComponents()); }

	protected List<IUIVirtualComponent> getVirtualComponents() { return virtualComponents; }

	@Override
	public boolean isPresent() { return getComponent().isPresent(); }

	@Override
	public boolean isVirtual() { return getComponent().map(IUIVirtualComponent.class::isInstance).orElse(false); }

	@Override
	public ImmutableUIComponentPathResolverResult copy() {
		return of(getComponent().orElse(null), getVirtualComponents());
	}
}
