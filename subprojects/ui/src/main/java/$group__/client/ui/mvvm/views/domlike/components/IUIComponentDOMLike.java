package $group__.client.ui.mvvm.views.domlike.components;

import $group__.client.ui.coredeprecated.IUIExtension;
import $group__.client.ui.coredeprecated.structures.AffineTransformStack;
import $group__.client.ui.coredeprecated.structures.IShapeDescriptor;
import $group__.client.ui.coredeprecated.structures.UIKeyboardKeyPressData;
import $group__.client.ui.coredeprecated.structures.UIMouseButtonPressData;
import $group__.utilities.interfaces.IExtensionContainer;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.w3c.dom.events.EventTarget;

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
public interface IUIComponentDOMLike
		extends EventTarget, IExtensionContainer<ResourceLocation, IUIExtension> {
	static <T> Optional<T> getYoungestParentInstanceOf(IUIComponentDOMLike self, Class<T> clazz) {
		Optional<IUIComponentContainerDOMLike> parent = self.getParent();
		Optional<T> ret = Optional.empty();
		while (parent.isPresent()) {
			IUIComponentContainerDOMLike p = parent.get();
			if (clazz.isInstance(p)) {
				ret = Optional.of(clazz.cast(p));
				break;
			}
			parent = p.getParent();
		}
		return ret;
	}

	Optional<IUIComponentContainerDOMLike> getParent();

	@SuppressWarnings("UnstableApiUsage")
	static ImmutableList<UIMouseButtonPressData.Tracked> getMouseButtonsBeingPressedForComponent(IUIComponentDOMLike component) {
		return component.getParent().map(p -> p.getMouseButtonsBeingPressedView().values().stream().unordered()
				.filter(t -> t.getComponent().equals(component)).collect(ImmutableList.toImmutableList())).orElseGet(ImmutableList::of);
	}

	@SuppressWarnings("UnstableApiUsage")
	static ImmutableList<UIKeyboardKeyPressData.Tracked> getKeyboardKeysBeingPressedForComponent(IUIComponentDOMLike component) {
		return component.getParent().map(p -> p.getKeyboardKeysBeingPressedView().values().stream().unordered()
				.filter(t -> t.getComponent().equals(component)).collect(ImmutableList.toImmutableList())).orElseGet(ImmutableList::of);
	}

	static boolean isBeingDragged(IUIComponentDOMLike component, int button) {
		return !(component instanceof IUIComponentContainerDOMLike && ((IUIComponentContainerDOMLike) component).getMouseButtonsBeingPressedView().get(button) != null)
				&& component.getParent().map(p -> p.getMouseButtonsBeingPressedView().get(button)).filter(t -> t.getComponent().equals(component)).isPresent();
	}

	static boolean isBeingFocused(IUIComponentDOMLike component) {
		return !(component instanceof IUIComponentContainerDOMLike && ((IUIComponentContainerDOMLike) component).getFocus().isPresent())
				&& component.getParent().flatMap(IUIComponentContainerDOMLike::getFocus).filter(f -> f.equals(component)).isPresent();
	}

	static boolean isBeingMouseHovered(IUIComponentDOMLike component) {
		return !(component instanceof IUIComponentContainerDOMLike && ((IUIComponentContainerDOMLike) component).getMouseHover().isPresent())
				&& component.getParent().flatMap(IUIComponentContainerDOMLike::getMouseHover).filter(h -> h.equals(component)).isPresent();
	}

	static ImmutableList<IUIComponentDOMLike> computeComponentPath(IUIComponentDOMLike component) {
		ImmutableList.Builder<IUIComponentDOMLike> builder = ImmutableList.builder();
		builder.add(component);
		Optional<IUIComponentContainerDOMLike> parent = component.getParent();
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
	
	Optional<IUIComponentManagerDOMLike> getManager();

	boolean contains(final AffineTransformStack stack, Point2D point);

	void onIndexMove(int previous, int next);

	void onParentChange(@Nullable IUIComponentContainerDOMLike previous, @Nullable IUIComponentContainerDOMLike next);
	
	boolean isActive();

	boolean setActive(final AffineTransformStack stack, boolean active);

	boolean onMouseMove(final AffineTransformStack stack, Point2D mouse);

	boolean onMouseHover(final AffineTransformStack stack, EnumStage stage, Point2D mouse);

	boolean onMouseButtonPress(final AffineTransformStack stack, EnumStage stage, UIMouseButtonPressData data);

	boolean onMouseScroll(final AffineTransformStack stack, Point2D mouse, double delta);

	boolean onKeyboardKeyPress(final AffineTransformStack stack, EnumStage stage, UIKeyboardKeyPressData data);

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
