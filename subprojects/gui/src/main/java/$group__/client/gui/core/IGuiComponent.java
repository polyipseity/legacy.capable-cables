package $group__.client.gui.core;

import $group__.client.gui.core.structures.AffineTransformStack;
import $group__.client.gui.core.structures.GuiKeyboardKeyPressData;
import $group__.client.gui.core.structures.GuiMouseButtonPressData;
import $group__.client.gui.core.structures.IShapeDescriptor;
import $group__.client.gui.core.traits.IGuiReshapeExplicitly;
import $group__.utilities.interfaces.IExtensionContainer;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public interface IGuiComponent
		<M extends IGuiModel<?>, V extends IGuiView<?, ?>, C extends IGuiController<?>>
		extends IExtensionContainer<ResourceLocation, IGuiExtension> {
	static <T> Optional<T> getYoungestParentInstanceOf(IGuiComponent<?, ?, ?> self, Class<T> clazz) {
		Optional<IContainer<?, ?, ?, ?>> parent = self.getParent();
		Optional<T> ret = Optional.empty();
		while (parent.isPresent()) {
			IContainer<?, ?, ?, ?> parentUnboxed = parent.get();
			if (clazz.isInstance(parentUnboxed)) {
				ret = Optional.of(clazz.cast(parentUnboxed));
				break;
			}
			parent = parentUnboxed.getParent();
		}
		return ret;
	}

	Optional<IContainer<?, ?, ?, ?>> getParent();

	@SuppressWarnings("UnstableApiUsage")
	static ImmutableList<GuiMouseButtonPressData.Tracked> getMouseButtonsBeingPressedForComponent(IGuiComponent<?, ?, ?> component) {
		return component.getParent().map(p -> p.getController().getMouseButtonsBeingPressedView().values().stream().unordered()
				.filter(t -> t.getComponent() == component).collect(ImmutableList.toImmutableList())).orElseGet(ImmutableList::of);
	}

	C getController();

	@SuppressWarnings("UnstableApiUsage")
	static ImmutableList<GuiKeyboardKeyPressData.Tracked> getKeyboardKeysBeingPressedForComponent(IGuiComponent<?, ?, ?> component) {
		return component.getParent().map(p -> p.getController().getKeyboardKeysBeingPressedView().values().stream().unordered()
				.filter(t -> t.getComponent() == component).collect(ImmutableList.toImmutableList())).orElseGet(ImmutableList::of);
	}

	static boolean isBeingDragged(IGuiComponent<?, ?, ?> component, int button) {
		return !(component instanceof IContainer && ((IContainer<?, ?, ?, ?>) component).getController().getMouseButtonsBeingPressedView().get(button) != null)
				&& component.getParent().map(p -> p.getController().getMouseButtonsBeingPressedView().get(button)).filter(t -> t.getComponent() == component).isPresent();
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	static boolean isBeingFocused(IGuiComponent<?, ?, ?> component) {
		return !(component instanceof IContainer && ((IContainer<?, ?, ?, ?>) component).getController().getFocus().isPresent())
				&& component.getParent().flatMap(p -> p.getController().getFocus()).filter(f -> f == component).isPresent();
	}

	static boolean isBeingMouseHovered(IGuiComponent<?, ?, ?> component) {
		return !(component instanceof IContainer && ((IContainer<?, ?, ?, ?>) component).getController().getMouseHover().isPresent())
				&& component.getParent().flatMap(p -> p.getController().getMouseHover()).filter(h -> h == component).isPresent();
	}

	static ImmutableList<IGuiComponent<?, ?, ?>> getComponentPath(IGuiComponent<?, ?, ?> component) {
		ImmutableList.Builder<IGuiComponent<?, ?, ?>> builder = ImmutableList.builder();
		builder.add(component);
		Optional<IContainer<?, ?, ?, ?>> parent = component.getParent();
		while (parent.isPresent()) {
			builder.add(parent.get());
			parent = parent.get().getParent();
		}
		return builder.build().reverse();
	}

	M getModel();

	V getView();

	Optional<IManager<?, ?, ?, ?, ?>> getManager();

	boolean contains(final AffineTransformStack stack, Point2D point);

	void onIndexMove(int previous, int next);

	void onParentChange(@Nullable IContainer<?, ?, ?, ?> previous, @Nullable IContainer<?, ?, ?, ?> next);

	@OnlyIn(Dist.CLIENT)
	interface IContainer
			<M extends IGuiModel<?>, V extends IGuiView<?, ?>, C extends IGuiController.IContainer<?, CH>,
					CH extends IGuiComponent<?, ?, ?>>
			extends IGuiComponent<M, V, C> {
		static void runWithStackTransformed(IContainer<?, ?, ?, ?> self, final AffineTransformStack stack, Runnable call) {
			getWithStackTransformed(self, stack, () -> {
				call.run();
				return null;
			});
		}

		static <R> R getWithStackTransformed(IContainer<?, ?, ?, ?> self, final AffineTransformStack stack, Supplier<R> call) {
			stack.push();
			self.transformChildren(stack);
			R ret = call.get();
			stack.getDelegated().pop();
			return ret;
		}

		void transformChildren(final AffineTransformStack stack);

		List<CH> getChildrenView();

		boolean addChildren(Iterable<? extends CH> components);

		boolean addChildAt(int index, CH component);

		boolean removeChildren(Iterable<? extends IGuiComponent<?, ?, ?>> components);

		boolean moveChildTo(int index, IGuiComponent<?, ?, ?> component);

		boolean moveChildToTop(IGuiComponent<?, ?, ?> component);

		Optional<CH> getChildAtPoint(final AffineTransformStack stack, Point2D point);
	}

	@OnlyIn(Dist.CLIENT)
	interface IManager
			<M extends IGuiModel<?>, V extends IGuiView<?, SD>, C extends IGuiController.IManager<?, CH>,
					CH extends IGuiComponent<?, ?, ?>,
					SD extends IShapeDescriptor<?, ?>>
			extends IContainer<M, V, C, CH>, IGuiReshapeExplicitly<SD> {
		AffineTransformStack getCleanTransformStack();

		@SuppressWarnings("UnusedReturnValue")
		<CL extends IGuiComponent<?, ?, ?>, R> Optional<R> callAsComponent(CL component, BiFunction<? super CL, ? super AffineTransformStack, ? extends R> action);

		@Override
		default void onIndexMove(int previous, int next)
				throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

		@Override
		default void onParentChange(@Nullable IContainer<?, ?, ?, ?> previous, @Nullable IContainer<?, ?, ?, ?> next)
				throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

		/*
		TODO
		@SuppressWarnings("ObjectAllocationInLoop")
		static List<IGuiComponent<?, ?, ?>> getComponentsAtPointAndTransformStack(IGuiComponent<?, ?, ?> self, final AffineTransformStack stack, Point2D point) {
			List<IGuiComponent<?, ?, ?>> ret = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM);
			Predicate<IGuiComponent<?, ?, ?>> isWithChildren = IContainer.class::isInstance;

			Optional<IGuiComponent<?, ?, ?>> current = Optional.of(self);
			while (current.isPresent()) {
				current = current.filter(isWithChildren).flatMap(r ->  {
					IContainer<?, ?, ?, ?> rwc = (IContainer<?, ?, ?, ?>) r;
					Optional<IGuiComponent<?, ?, ?>> c = rwc.getChildAtPoint(stack, point);
					if (c.isPresent()) {
						stack.push();
						rwc.transformChildren(stack);
						ret.add(c.get());
					}
					return c;
				});
			}
			return ret;
		}
		 */
	}
}
