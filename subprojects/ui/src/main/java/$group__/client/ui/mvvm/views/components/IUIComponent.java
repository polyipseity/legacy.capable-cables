package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.coredeprecated.IUIExtension;
import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.coredeprecated.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.structures.IUIDataKeyboardKeyPress;
import $group__.client.ui.mvvm.structures.IUIDataMouseButtonClick;
import $group__.utilities.interfaces.IExtensionContainer;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.Optional;

/*
TODO remove ongoing events
TODO events are not implemented as methods, but as a map
TODO improve event data, one arg will work
TODO match mvvm
TODO auto resizing based on min size and preferred size
 */
public interface IUIComponent
		extends IExtensionContainer<ResourceLocation, IUIExtension> {
	static <T> Optional<T> getYoungestParentInstanceOf(IUIComponent self, Class<T> clazz) {
		Optional<IUIComponentContainer> parent = self.getParent();
		Optional<T> ret = Optional.empty();
		while (parent.isPresent()) {
			IUIComponentContainer p = parent.get();
			if (clazz.isInstance(p)) {
				ret = Optional.of(clazz.cast(p));
				break;
			}
			parent = p.getParent();
		}
		return ret;
	}

	Optional<IUIComponentContainer> getParent();

	@SuppressWarnings("UnstableApiUsage")
	static ImmutableList<IUIDataMouseButtonClick.Tracked> getMouseButtonsBeingPressedForComponent(IUIComponent component) {
		return component.getParent().map(p -> p.getMouseButtonsBeingPressedView().values().stream().unordered()
				.filter(t -> t.getComponent().equals(component)).collect(ImmutableList.toImmutableList())).orElseGet(ImmutableList::of);
	}

	@SuppressWarnings("UnstableApiUsage")
	static ImmutableList<IUIDataKeyboardKeyPress.Tracked> getKeyboardKeysBeingPressedForComponent(IUIComponent component) {
		return component.getParent().map(p -> p.getKeyboardKeysBeingPressedView().values().stream().unordered()
				.filter(t -> t.getComponent().equals(component)).collect(ImmutableList.toImmutableList())).orElseGet(ImmutableList::of);
	}

	static boolean isBeingDragged(IUIComponent component, int button) {
		return !(component instanceof IUIComponentContainer && ((IUIComponentContainer) component).getMouseButtonsBeingPressedView().get(button) != null)
				&& component.getParent().map(p -> p.getMouseButtonsBeingPressedView().get(button)).filter(t -> t.getComponent().equals(component)).isPresent();
	}

	static boolean isBeingFocused(IUIComponent component) {
		return !(component instanceof IUIComponentContainer && ((IUIComponentContainer) component).getFocus().isPresent())
				&& component.getParent().flatMap(IUIComponentContainer::getFocus).filter(f -> f.equals(component)).isPresent();
	}

	static boolean isBeingMouseHovered(IUIComponent component) {
		return !(component instanceof IUIComponentContainer && ((IUIComponentContainer) component).getMouseHover().isPresent())
				&& component.getParent().flatMap(IUIComponentContainer::getMouseHover).filter(h -> h.equals(component)).isPresent();
	}

	static ImmutableList<IUIComponent> computeComponentPath(IUIComponent component) {
		ImmutableList.Builder<IUIComponent> builder = ImmutableList.builder();
		builder.add(component);
		Optional<IUIComponentContainer> parent = component.getParent();
		while (parent.isPresent()) {
			builder.add(parent.get());
			parent = parent.get().getParent();
		}
		return builder.build().reverse();
	}

	IShapeDescriptor<?, ?> getShapeDescriptor();

	boolean isVisible();

	boolean setVisible(final AffineTransformStack stack, boolean visible);

	void crop(final AffineTransformStack stack, EnumCropMethod method, Point2D mouse, float partialTicks, boolean write);

	boolean render(final AffineTransformStack stack, Point2D mouse, float partialTicks);

	Optional<IUIComponentManager> getManager();

	boolean contains(final AffineTransformStack stack, Point2D point);

	void onIndexMove(int previous, int next);

	void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next);

	boolean isActive();

	boolean setActive(final AffineTransformStack stack, boolean active);

	boolean onMouseMove(final AffineTransformStack stack, Point2D mouse);

	boolean onMouseHover(final AffineTransformStack stack, EnumStage stage, Point2D mouse);

	boolean onMouseButtonPress(final AffineTransformStack stack, EnumStage stage, IUIDataMouseButtonClick data);

	boolean onMouseScroll(final AffineTransformStack stack, Point2D mouse, double delta);

	boolean onKeyboardKeyPress(final AffineTransformStack stack, EnumStage stage, IUIDataKeyboardKeyPress data);

	boolean onKeyboardCharType(final AffineTransformStack stack, char codePoint, int modifiers);

	boolean onFocus(final AffineTransformStack stack, EnumStage stage);

	boolean changeFocus(final AffineTransformStack stack, boolean next);

	enum EnumCropMethod {
		GL_SCISSOR,
		STENCIL_BUFFER,
	}

	@OnlyIn(Dist.CLIENT)
	enum EnumStage {
		START,
		ONGOING,
		END,
	}
}
