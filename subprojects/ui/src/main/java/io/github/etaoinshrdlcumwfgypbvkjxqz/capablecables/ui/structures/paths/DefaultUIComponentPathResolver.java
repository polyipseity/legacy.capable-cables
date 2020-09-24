package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.paths;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths.IPath;

import javax.annotation.Nullable;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;

public class DefaultUIComponentPathResolver
		extends AbstractPathResolver<IUIComponent> {
	@SuppressWarnings("ObjectAllocationInLoop")
	@Override
	public void resolvePath(IUIComponentContext context, Point2D point, boolean virtual) {
		IAffineTransformStack stack = context.getTransformStack();
		IPath<IUIComponent> path = context.getPath();

		@Nullable IUIComponent current = path.getPathEnd().orElseThrow(IllegalArgumentException::new);
		while (current != null) {
			current = CastUtilities.castChecked(IUIComponentContainer.class, current)
					.map(container -> {
						AffineTransform transform = stack.push();
						container.transformChildren(stack);
						Optional.ofNullable(getChildrenTransformers().getIfPresent(container))
								.ifPresent(ts -> ts.forEach(t -> t.accept(stack)));

						@Nullable IUIComponent r = null;
						childrenLoop:
						for (IUIComponent child : Lists.reverse(container.getChildrenView())) {
							@Nullable List<IUIComponent> childVirtualElements = getVirtualElements().getIfPresent(child);
							if (childVirtualElements != null) {
								for (IUIComponent childVirtualElement : Lists.reverse(childVirtualElements)) {
									if (transform.createTransformedShape(
											childVirtualElement.getShapeDescriptor().getShapeOutput()).contains(point)) {
										r = virtual ? childVirtualElement : child;
										break childrenLoop;
									}
								}
							}
							if (transform.createTransformedShape(
									child.getShapeDescriptor().getShapeOutput()).contains(point)) {
								r = child;
								break;
							}
						}

						if (r != null)
							path.subPath(ImmutableList.of(r));
						else
							stack.pop();
						return r;
					})
					.orElse(null);
		}
	}
}
