package $group__.client.ui.mvvm.views.domlike;

import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.coredeprecated.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentContainerDOMLike;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentManagerDOMLike;
import $group__.utilities.client.GLUtilities;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class UIManagerDOMLikeComponentDOMLike
		extends UIContainerDOMLikeComponentDOMLike
		implements IUIComponentManagerDOMLike {

	@Override
	public boolean contains(final AffineTransformStack stack, Point2D point) { return true; }	@Override
	public AffineTransformStack getCleanTransformStack() { return new AffineTransformStack(); }

	@Override
	public void onParentChange(@Nullable IUIComponentContainerDOMLike previous, @Nullable IUIComponentContainerDOMLike next)
			throws UnsupportedOperationException { IUIComponentManagerDOMLike.super.onParentChange(previous, next); }	@Override
	public <CL extends IUIComponentDOMLike, R> Optional<R> callAsComponent(CL component, BiFunction<? super CL, ? super AffineTransformStack, ? extends R> action) {
		AffineTransformStack stack = getCleanTransformStack();
		ImmutableList<IUIComponentDOMLike> path = IUIComponentDOMLike.computeComponentPath(component);
		if (!path.contains(this))
			return Optional.empty();
		int popTimes = 0;
		for (IUIComponentDOMLike element : path) {
			if (element instanceof IUIComponentContainerDOMLike) {
				((IUIComponentContainerDOMLike) element).transformChildren(stack);
				++popTimes;
			}
		}
		return Optional.ofNullable(action.apply(component, stack));
	}

	@Override
	public void onIndexMove(int previous, int next)
			throws UnsupportedOperationException { IUIComponentManagerDOMLike.super.onIndexMove(previous, next); }

	@Override
	public boolean reshape(Function<? super IShapeDescriptor, Boolean> action) throws ConcurrentModificationException {
		// TODO implement resize logic
		return getShapeDescriptor().modify(getShapeDescriptor(), action);
	}

	/*
	TODO
	protected static boolean visitChildren(IManager<?, ?, ?, ?, ?> self,final AffineTransformStack stack, Function<IGuiComponent<?, ?, ?>, Boolean> action) {
		return TreeUtilities.<IGuiComponent<?, ?, ?>, Boolean>visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, self,
				c -> {
					if (c != self)
						return action.apply(c);
					return false;
				},
				c -> {
					if (c instanceof IGuiComponent.IUIComponentContainerDOMLike) {
						IUIComponentContainerDOMLike cc = (IUIComponentContainerDOMLike) c;
						stack.push();
						cc.transformChildren(stack);
						return cc.getChildrenView();
					}
					return ImmutableSet.of();
				},
				(p, c) ->  {
					stack.getDelegated().pop();
					return p || Streams.stream(c).unordered().anyMatch(b -> b);
				}, null).orElse(false);
	}

	 */

	@Override
	public boolean render0(final AffineTransformStack stack, Point2D mouse, float partialTicks) {
		GLUtilities.GLStacksUtilities.clearAll();
		RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, Minecraft.IS_RUNNING_ON_MAC);
		getShapeDescriptor().modify(getShapeDescriptor(), s -> {
			s.getConstraintsRef().clear();
			return true;
		});
		return super.render0(stack, mouse, partialTicks);
	}




}
