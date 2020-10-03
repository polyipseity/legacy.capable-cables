package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.paths;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIVirtualComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentPathResolverResult;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.paths.IUIComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.ImmutableUIComponentPathResolverResult;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

import java.awt.geom.Point2D;

public abstract class AbstractUIComponentPathResolver
		implements IUIComponentPathResolver {
	@Override
	public IUIComponentPathResolverResult resolvePath(IUIComponentContext context, Point2D point) {
		IUIComponentPathResolverResult result = ImmutableUIComponentPathResolverResult.getEmpty();
		do {
			IUIComponentPathResolverResult next = resolveDirectChild(context, (Point2D) point.clone());
			if (!next.isPresent()) // COMMENT no next
				return !result.isPresent() ? getResult(context, point) : result;
			result = next;
			if (result.isVirtual()) // COMMENT result exists and is virtual
				return result;
		} while (true);
	}

	@Override
	public IUIComponentPathResolverResult resolveDirectChild(IUIComponentContext context, Point2D point) {
		for (IUIComponent child :
				context.getPath().getPathEnd()
						.flatMap(pathEnd -> CastUtilities.castChecked(IUIComponentContainer.class, pathEnd))
						.map(IUIComponentContainer::getChildrenView)
						.map(Lists::reverse)
						.orElseGet(ImmutableList::of)) {
			context.getMutator().push(context, child);
			IUIComponentPathResolverResult ret = getResult(context, (Point2D) point.clone());
			if (ret.isPresent())
				return ret;
			context.getMutator().pop(context);
		}
		return ImmutableUIComponentPathResolverResult.getEmpty();
	}

	@Override
	public IUIComponentPathResolverResult getResult(IUIComponentContext context, Point2D point) {
		return context.getPath().getPathEnd()
				.map(pathEnd -> {
					ImmutableList<IUIVirtualComponent> virtualComponents = IUIVirtualComponent.findVirtualComponents(context, pathEnd, point);
					if (!virtualComponents.isEmpty()) // COMMENT hits virtual component
						return ImmutableUIComponentPathResolverResult.of(Iterables.getLast(virtualComponents), virtualComponents);
					else if (pathEnd.containsPoint(context, point)) // COMMENT hits component
						return ImmutableUIComponentPathResolverResult.of(pathEnd, virtualComponents);
					return null;
				})
				.orElseGet(ImmutableUIComponentPathResolverResult::getEmpty);
	}
}
