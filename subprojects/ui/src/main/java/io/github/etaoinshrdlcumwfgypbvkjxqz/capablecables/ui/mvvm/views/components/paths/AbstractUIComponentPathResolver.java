package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.paths;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIVirtualComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IUIComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentPathResolverResult;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

import java.awt.geom.Point2D;

public abstract class AbstractUIComponentPathResolver
		implements IUIComponentPathResolver {
	@Override
	public IUIComponentPathResolverResult resolvePath(IUIComponentContext componentContext, Point2D point) {
		IUIComponentPathResolverResult result = getResult(componentContext, (Point2D) point.clone());
		do {
			IUIComponentPathResolverResult next = resolveDirectChild(componentContext, (Point2D) point.clone());
			if (!next.isPresent()) // COMMENT no next
				return result;
			result = next;
			if (result.isVirtual()) // COMMENT result exists and is virtual
				return result;
		} while (true);
	}

	@Override
	public IUIComponentPathResolverResult resolveDirectChild(IUIComponentContext componentContext, Point2D point) {
		IUIComponentPathResolverResult result = IUIComponentContext.StaticHolder.getCurrentComponent(componentContext)
				.flatMap(pathEnd -> CastUtilities.castChecked(IUIComponentContainer.class, pathEnd))
				.map(IUIComponentContainer::getChildrenView)
				.map(Lists::reverse)
				.orElseGet(ImmutableList::of)
				.stream().sequential()
				.map(child -> {
					try (IUIComponentContext componentContextCopy = componentContext.copy()) {
						componentContextCopy.getMutator().push(componentContextCopy.getStackRef(), child);
						return getResult(componentContextCopy, (Point2D) point.clone());
					}
				})
				.filter(IUIComponentPathResolverResult::isPresent)
				.findFirst()
				.orElseGet(ImmutableUIComponentPathResolverResult::getEmpty);
		result.getConcreteComponent()
				.ifPresent(child -> componentContext.getMutator().push(componentContext.getStackRef(), child));
		return result;
	}

	@Override
	public IUIComponentPathResolverResult getResult(IUIComponentContext componentContext, Point2D point) {
		return IUIComponentContext.StaticHolder.getCurrentComponent(componentContext)
				.map(pathEnd -> {
					ImmutableList<IUIVirtualComponent> virtualComponents = IUIVirtualComponent.findVirtualComponents(componentContext, pathEnd, point);
					if (!virtualComponents.isEmpty()) // COMMENT hits virtual component
						return ImmutableUIComponentPathResolverResult.of(Iterables.getLast(virtualComponents), virtualComponents);
					else if (pathEnd.containsPoint(componentContext, point)) // COMMENT hits component
						return ImmutableUIComponentPathResolverResult.of(pathEnd, virtualComponents);
					return null;
				})
				.orElseGet(ImmutableUIComponentPathResolverResult::getEmpty);
	}
}
