package $group__.client.ui.mvvm.views.domlike;

import $group__.client.ui.coredeprecated.events.EventUIComponentHierarchyChanged;
import $group__.client.ui.coredeprecated.structures.*;
import $group__.client.ui.coredeprecated.traits.IUIReshapeExplicitly;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentContainerDOMLike;
import $group__.utilities.CastUtilities;
import $group__.utilities.client.GLUtilities;
import $group__.utilities.client.GLUtilities.GLStacksUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventUtilities;
import $group__.utilities.specific.MapUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

@OnlyIn(Dist.CLIENT)
public class UIContainerDOMLikeComponentDOMLike
		extends UIDOMLikeDOMLikeComponentDOMLike
		implements IUIComponentContainerDOMLike {
	protected final List<IUIComponentDOMLike> children = new ArrayList<>(INITIAL_CAPACITY_SMALL);
	protected final ConcurrentMap<Integer, UIKeyboardKeyPressData.Tracked>
			keyboardKeysBeingPressed = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<Integer, UIMouseButtonPressData.Tracked>
			mouseButtonsBeingPressed = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	@Nullable
	protected IUIComponentDOMLike focus = null, mouseHover = null;

	@Override
	public boolean initialize0(final AffineTransformStack stack) {
		return IUIComponentContainerDOMLike.getWithStackTransformed(this, stack, () -> getChildrenView().stream().sequential().anyMatch(c -> c.initialize(stack)));
	}

	@Override
	public boolean tick0(final AffineTransformStack stack) {
		return IUIComponentContainerDOMLike.getWithStackTransformed(this, stack, () -> getChildrenView().stream().sequential().anyMatch(c -> c.tick(stack)));
	}

	@Override
	public boolean close0(final AffineTransformStack stack) {
		return IUIComponentContainerDOMLike.getWithStackTransformed(this, stack, () -> getChildrenView().stream().sequential().anyMatch(c -> c.close(stack)));
	}

	@Override
	public boolean render0(final AffineTransformStack stack, Point2D mouse, float partialTicks) {
		return IUIComponentContainerDOMLike.getWithStackTransformed(this, stack, () -> {
			final boolean[] r = {false};
			if (Minecraft.getInstance().getFramebuffer().isStencilEnabled()) {
				GLStacksUtilities.push("GL_STENCIL_TEST",
						() -> GL11.glEnable(GL11.GL_STENCIL_TEST), () -> GL11.glDisable(GL11.GL_STENCIL_TEST));
				GLStacksUtilities.push("stencilMask",
						() -> RenderSystem.stencilMask(GLUtilities.GL_MASK_ALL_BITS), GLStacksUtilities.STENCIL_MASK_FALLBACK);
				getChildrenView().forEach(c -> {
					if (c.isVisible()) {
						c.crop(stack, IUIComponentDOMLike.EnumCropMethod.STENCIL_BUFFER, mouse, partialTicks, true);
						r[0] |= c.render(stack, mouse, partialTicks);
						c.crop(stack, IUIComponentDOMLike.EnumCropMethod.STENCIL_BUFFER, mouse, partialTicks, false);
					}
				});
				GLStacksUtilities.pop("stencilMask");
				GLStacksUtilities.pop("GL_STENCIL_TEST");
			} else {
				GLStacksUtilities.push("GL_SCISSOR_TEST",
						() -> GL11.glEnable(GL11.GL_SCISSOR_TEST),
						() -> GL11.glDisable(GL11.GL_SCISSOR_TEST));
				getChildrenView().forEach(c -> {
					if (c.isVisible()) {
						c.crop(stack, IUIComponentDOMLike.EnumCropMethod.GL_SCISSOR, mouse, partialTicks, true);
						r[0] |= c.render(stack, mouse, partialTicks);
						c.crop(stack, IUIComponentDOMLike.EnumCropMethod.GL_SCISSOR, mouse, partialTicks, false);
					}
				});
				GLStacksUtilities.pop("GL_SCISSOR_TEST");
			}
			return r[0];
		});
	}

	@Override
	public boolean changeFocus(final AffineTransformStack stack, boolean next) {
		return IUIComponentContainerDOMLike.getWithStackTransformed(this, stack,  () -> {
			Optional<IUIComponentDOMLike> foc = getFocus();
			if (foc.filter(f -> f.isActive() && f.changeFocus(stack, next)).isPresent())
				return true;
			else {
				List<IUIComponentDOMLike> children = CastUtilities.castUnchecked(getChildrenView());
				int i = foc.filter(children::contains)
						.map(f -> children.indexOf(f) + (next ? 1 : 0))
						.orElseGet(() -> next ? 0 : children.size());

				ListIterator<IUIComponentDOMLike> iterator = children.listIterator(i);
				BooleanSupplier canAdvance = next ? iterator::hasNext : iterator::hasPrevious;
				Supplier<IUIComponentDOMLike> advance = next ? iterator::next : iterator::previous;

				while (canAdvance.getAsBoolean()) {
					IUIComponentDOMLike advanced = advance.get();
					if (advanced.isActive() && advanced.changeFocus(stack, next)) {
						setFocus(stack, advanced);
						return true;
					}
				}

				setFocus(stack, null);
				return false;
			}
		});
	}

	@Override
	public boolean onMouseMove0(final AffineTransformStack stack, Point2D mouse) {
		return getChildAtPoint(stack, mouse)
				.map(c -> IUIComponentContainerDOMLike.getWithStackTransformed(this, stack,
						() -> c.onMouseMove(stack, (Point2D) mouse.clone())))
				.orElse(false);
	}

	@Override
	public boolean onMouseHover0(final AffineTransformStack stack, IUIComponentDOMLike.EnumStage stage, Point2D mouse) {
		if (stage == EnumStage.ONGOING) {
			Optional<IUIComponentDOMLike> next = CastUtilities.castUnchecked(getChildAtPoint(stack, mouse));
			if (!next.equals(getMouseHover()))
				setMouseHover(stack, mouse, next.orElse(null));
		}
		return getMouseHover().map(h -> h.onMouseHover(stack, stage, mouse)).orElse(false);
	}

	@Override
	public boolean onMouseButtonPress0(final AffineTransformStack stack, IUIComponentDOMLike.EnumStage stage, UIMouseButtonPressData data) {
		Optional<UIMouseButtonPressData.Tracked> t;
		switch (stage) {
			case START:
				t = getChildAtPoint(stack, data.getCursorView()).map(c -> new UIMouseButtonPressData.Tracked(c, data));
				t.ifPresent(td -> getMouseButtonsBeingPressed().put(td.getData().getButton(), td));
				break;
			case ONGOING:
				t = Optional.ofNullable(getMouseButtonsBeingPressed().get(data.getButton()));
				break;
			case END:
				t = Optional.ofNullable(getMouseButtonsBeingPressed().remove(data.getButton()));
				break;
			default:
				throw new InternalError();
		}
		return t.filter(d ->
				IUIComponentContainerDOMLike.getWithStackTransformed(this, stack, () ->
						d.getComponent().onMouseButtonPress(stack, stage, data))).isPresent();
	}

	@Override
	public boolean onMouseScroll0(final AffineTransformStack stack, Point2D mouse, double delta) {
		return getFocus()
				.map(c -> IUIComponentContainerDOMLike.getWithStackTransformed(this, stack,
						() -> c.onMouseScroll(stack, (Point2D) mouse.clone(), delta)))
				.orElse(false);
	}

	@Override
	public boolean onKeyboardKeyPress0(final AffineTransformStack stack, IUIComponentDOMLike.EnumStage stage, UIKeyboardKeyPressData data) {
		Optional<UIKeyboardKeyPressData.Tracked> t;
		switch (stage) {
			case START:
				t = getFocus().map(c -> new UIKeyboardKeyPressData.Tracked(c, data));
				t.ifPresent(td -> getKeyboardKeysBeingPressed().put(td.getData().getKey(), td));
				break;
			case ONGOING:
				t = Optional.ofNullable(getKeyboardKeysBeingPressed().get(data.getKey()));
				break;
			case END:
				t = Optional.ofNullable(getKeyboardKeysBeingPressed().remove(data.getKey()));
				break;
			default:
				throw new InternalError();
		}
		return t.filter(d ->
				IUIComponentContainerDOMLike.getWithStackTransformed(this, stack, () ->
						d.getComponent().onKeyboardKeyPress(stack, stage, data))).isPresent();
	}

	@Override
	public boolean onKeyboardCharType0(final AffineTransformStack stack, char codePoint, int modifiers) {
		return getFocus()
				.map(c -> IUIComponentContainerDOMLike.getWithStackTransformed(this, stack,
						() -> c.onKeyboardCharType(stack, codePoint, modifiers)))
				.orElse(false);
	}

	@Override
	public boolean onFocus0(final AffineTransformStack stack, EnumStage stage) {
		if (stage == EnumStage.ONGOING)
			return getFocus().filter(f ->
					IUIComponentContainerDOMLike.getWithStackTransformed(this, stack, () ->
							f.onFocus(stack, stage))).isPresent();
		return super.onFocus0(stack, stage);
	}

	@Override
	public Optional<IUIComponentDOMLike> getFocus() { return Optional.ofNullable(focus); }

	@Override
	public void transformChildren(final AffineTransformStack stack) {
		Rectangle2D bounds = getShapeDescriptor().getShapeProcessed().getBounds2D();
		stack.getDelegated().peek().translate(bounds.getX(), bounds.getY());
	}

	@Override
	public void setFocus(final AffineTransformStack stack, @Nullable IUIComponentDOMLike focus) {
		IUIComponentContainerDOMLike.runWithStackTransformed(this, stack, () -> {
			getFocus().ifPresent(f -> f.onFocus(stack, EnumStage.END));
			this.focus = focus;
			getFocus().ifPresent(f -> f.onFocus(stack, EnumStage.START));
		});
	}

	@Override
	public Optional<IUIComponentDOMLike> getMouseHover() { return Optional.ofNullable(mouseHover); }

	@Override
	public Map<Integer, UIKeyboardKeyPressData.Tracked> getKeyboardKeysBeingPressedView() { return ImmutableMap.copyOf(getKeyboardKeysBeingPressed()); }

	@Override
	public Map<Integer, UIMouseButtonPressData.Tracked> getMouseButtonsBeingPressedView() { return ImmutableMap.copyOf(getMouseButtonsBeingPressed()); }

	@Override
	public ImmutableList<IUIComponentDOMLike> getChildrenView() { return ImmutableList.copyOf(getChildren()); }

	@Override
	public boolean addChildren(Iterable<? extends IUIComponentDOMLike> components) {
		boolean ret = false;
		for (IUIComponentDOMLike component : components)
			ret |= !getChildren().contains(component) && addChildAt(getChildren().size(), component);
		return ret;
	}

	@Override
	public boolean addChildAt(int index, IUIComponentDOMLike component) {
		if (component == this)
			throw BecauseOf.illegalArgument("Adding self as child", "component", component);
		if (getChildren().contains(component))
			return moveChildTo(index, component);
		boolean ret = false;
		if (getParent().map(p -> p.removeChildren(ImmutableList.of(component))).orElse(true)) {
			ret = EventUtilities.callWithPrePostHooks(() -> {
						getChildren().add(index, component);
						List<IUIComponentDOMLike> childrenMoved = getChildren().subList(index + 1, getChildren().size());
						for (int i = 0; i < childrenMoved.size(); i++)
							childrenMoved.get(i).onIndexMove(index + i, index + i + 1);
						onParentChange(null, this);
						return true;
					},
					new EventUIComponentHierarchyChanged.Parent(EnumEventHookStage.PRE, this, null, this),
					new EventUIComponentHierarchyChanged.Parent(EnumEventHookStage.POST, this, null, this));
		}
		if (ret && EnumUIState.READY.isReachedBy(getState())) {
			getManager().ifPresent(mag -> mag.callAsComponent(this, (c, s) -> c.initialize(s)));
			IUIComponentDOMLike.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh);
		}
		return ret;
	}

	@SuppressWarnings({"ObjectAllocationInLoop"})
	@Override
	public boolean removeChildren(Iterable<? extends IUIComponentDOMLike> components) {
		boolean ret = false;
		for (IUIComponentDOMLike component : components) {
			int index = getChildren().indexOf(component);
			if (index != -1) {
				ret |= EventUtilities.callWithPrePostHooks(() -> {
							getChildren().remove(component);
							List<IUIComponentDOMLike> childrenMoved = getChildren().subList(index, getChildren().size());
							for (int i = 0; i < childrenMoved.size(); i++)
								childrenMoved.get(i).onIndexMove(index + i + 1, index + i);
							onParentChange(this, null);
							return true;
						}, new EventUIComponentHierarchyChanged.Parent(EnumEventHookStage.PRE, this, this, null),
						new EventUIComponentHierarchyChanged.Parent(EnumEventHookStage.POST, this, this, null));
			}
		}
		if (ret && EnumUIState.READY.isReachedBy(getState()))
			IUIComponentDOMLike.getYoungestParentInstanceOf(this, IUIReshapeExplicitly.class).ifPresent(IUIReshapeExplicitly::refresh);
		return ret;
	}

	@Override
	public boolean moveChildTo(int index, IUIComponentDOMLike component) {
		int previous = getChildren().indexOf(component);
		if (previous == index)
			return false;
		return getChildren().contains(component) && EventUtilities.callWithPrePostHooks(() -> {
					getChildren().remove(previous);
					getChildren().add(index, component); // COMMENT cast is always safe
					List<IUIComponentDOMLike> childrenMoved;
					if (index > previous) {
						childrenMoved = getChildren().subList(previous, index);
						for (int i = 0; i < childrenMoved.size(); i++)
							childrenMoved.get(i).onIndexMove(previous + i + 1, previous + i);
					} else {
						childrenMoved = getChildren().subList(index + 1, previous + 1);
						for (int i = 0; i < childrenMoved.size(); i++)
							childrenMoved.get(i).onIndexMove(index + i, index + i + 1);
					}
					onIndexMove(previous, index);
					return true;
				}, new EventUIComponentHierarchyChanged.Index(EnumEventHookStage.PRE, this, previous, index),
				new EventUIComponentHierarchyChanged.Index(EnumEventHookStage.POST, this, previous, index));
	}

	@Override
	public boolean moveChildToTop(IUIComponentDOMLike component) { return moveChildTo(getChildren().size() - 1, component); }

	@Override
	public Optional<IUIComponentDOMLike> getChildAtPoint(final AffineTransformStack stack, Point2D point) {
		return IUIComponentContainerDOMLike.getWithStackTransformed(this, stack, () -> {
			for (IUIComponentDOMLike child : Lists.reverse(getChildren())) {
				if (child.contains(stack, point))
					return Optional.of(child);
			}
			return Optional.empty();
		});
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IUIComponentDOMLike> getChildren() { return children; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Integer, UIMouseButtonPressData.Tracked> getMouseButtonsBeingPressed() { return mouseButtonsBeingPressed; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Integer, UIKeyboardKeyPressData.Tracked> getKeyboardKeysBeingPressed() { return keyboardKeysBeingPressed; }

	protected void setMouseHover(final AffineTransformStack stack, Point2D mouse, @Nullable IUIComponentDOMLike mouseHover) {
		IUIComponentContainerDOMLike.runWithStackTransformed(this, stack, () -> {
			getMouseHover().ifPresent(h -> h.onMouseHover(stack, EnumStage.END, mouse));
			this.mouseHover = mouseHover;
			getMouseHover().ifPresent(h -> h.onMouseHover(stack, EnumStage.START, mouse));
		});
	}
}
