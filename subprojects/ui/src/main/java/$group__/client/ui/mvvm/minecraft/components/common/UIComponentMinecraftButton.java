package $group__.client.ui.mvvm.minecraft.components.common;

import $group__.client.ui.events.ui.UIEventListener;
import $group__.client.ui.mvvm.binding.BindingMethodSource;
import $group__.client.ui.mvvm.core.binding.IBindingField;
import $group__.client.ui.mvvm.core.binding.IBindingMethod;
import $group__.client.ui.mvvm.core.binding.IHasBinding;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.components.parsers.UIConstructor;
import $group__.client.ui.mvvm.core.views.components.parsers.UIMethod;
import $group__.client.ui.mvvm.core.views.components.parsers.UIProperty;
import $group__.client.ui.mvvm.core.views.events.IUIEvent;
import $group__.client.ui.mvvm.core.views.events.IUIEventKeyboard;
import $group__.client.ui.mvvm.core.views.events.IUIEventMouse;
import $group__.client.ui.mvvm.minecraft.core.views.IUIComponentMinecraft;
import $group__.client.ui.mvvm.structures.EnumCursor;
import $group__.client.ui.mvvm.views.components.UIComponent;
import $group__.client.ui.mvvm.views.events.ui.UIEventKeyboard;
import $group__.client.ui.mvvm.views.events.ui.UIEventMouse;
import $group__.client.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.client.minecraft.GLUtilities;
import $group__.utilities.functions.IFunction4;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// TODO responsibility of this classes may need to be delegated to the view model via some means
@OnlyIn(Dist.CLIENT)
public class UIComponentMinecraftButton
		extends UIComponent
		implements IUIComponentMinecraft {
	public static final String PROPERTY_COLOR_BASE = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT + "button.colors.base";
	public static final String PROPERTY_COLOR_BASE_BORDER = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT + "button.colors.base.border";
	public static final String PROPERTY_COLOR_HOVERING = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT + "button.colors.hovering";
	public static final String PROPERTY_COLOR_HOVERING_BORDER = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT + "button.colors.hovering.border";
	public static final String PROPERTY_COLOR_PRESSED = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT + "button.colors.pressed";
	public static final String PROPERTY_COLOR_PRESSED_BORDER = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT + "button.colors.pressed.border";
	public static final String METHOD_ON_ACTIVATED = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT + "button.methods.onActivated";

	@UIProperty(PROPERTY_COLOR_BASE)
	protected final IBindingField<Color> colorBase;
	@UIProperty(PROPERTY_COLOR_BASE_BORDER)
	protected final IBindingField<Color> colorBaseBorder;
	@UIProperty(PROPERTY_COLOR_HOVERING)
	protected final IBindingField<Color> colorHovering;
	@UIProperty(PROPERTY_COLOR_HOVERING_BORDER)
	protected final IBindingField<Color> colorHoveringBorder;
	@UIProperty(PROPERTY_COLOR_PRESSED)
	protected final IBindingField<Color> colorPressed;
	@UIProperty(PROPERTY_COLOR_PRESSED_BORDER)
	protected final IBindingField<Color> colorPressedBorder;
	@UIMethod(METHOD_ON_ACTIVATED)
	protected final IBindingMethod.ISource<IUIEvent> methodOnActivated;
	protected final List<IFunction4<? super IAffineTransformStack, ? super Point2D, ? super Double, ? super Boolean, ? extends Boolean>> scheduledActions = new LinkedList<>();
	protected final EnumSet<IButtonState> buttonStates = EnumSet.noneOf(IButtonState.class);

	@UIConstructor
	public UIComponentMinecraftButton(Map<String, IUIPropertyMappingValue> propertyMapping) {
		super(propertyMapping);

		this.colorBase = IHasBinding.createBindingField(Color.class,
				getPropertyMapping().get(PROPERTY_COLOR_BASE),
				s -> new Color(Integer.decode(s), true), Color.DARK_GRAY);
		this.colorBaseBorder = IHasBinding.createBindingField(Color.class,
				getPropertyMapping().get(PROPERTY_COLOR_BASE_BORDER),
				s -> new Color(Integer.decode(s), true), Color.DARK_GRAY);
		this.colorHovering = IHasBinding.createBindingField(Color.class,
				getPropertyMapping().get(PROPERTY_COLOR_HOVERING),
				s -> new Color(Integer.decode(s), true), Color.GRAY);
		this.colorHoveringBorder = IHasBinding.createBindingField(Color.class,
				getPropertyMapping().get(PROPERTY_COLOR_HOVERING_BORDER),
				s -> new Color(Integer.decode(s), true), Color.GRAY);
		this.colorPressed = IHasBinding.createBindingField(Color.class,
				getPropertyMapping().get(PROPERTY_COLOR_PRESSED),
				s -> new Color(Integer.decode(s), true), Color.LIGHT_GRAY);
		this.colorPressedBorder = IHasBinding.createBindingField(Color.class,
				getPropertyMapping().get(PROPERTY_COLOR_PRESSED_BORDER),
				s -> new Color(Integer.decode(s), true), Color.LIGHT_GRAY);

		this.methodOnActivated = new BindingMethodSource<>(IUIEvent.class, new ResourceLocation(METHOD_ON_ACTIVATED));
	}

	@Override
	public void onCreated() {
		super.onCreated();

		// TODO activation events
		addEventListener(UIEventMouse.TYPE_MOUSE_ENTER_SELF, new UIEventListener.Functional<IUIEventMouse>(e -> {
			if (e.getPhase() == IUIEvent.EnumPhase.AT_TARGET) {
				getButtonStates().add(IButtonState.HOVERING);
				GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), EnumCursor.STANDARD_HAND_CURSOR.getHandle());
			}
		}), false);
		addEventListener(UIEventMouse.TYPE_MOUSE_LEAVE_SELF, new UIEventListener.Functional<IUIEventMouse>(e -> {
			if (e.getPhase() == IUIEvent.EnumPhase.AT_TARGET) {
				getButtonStates().remove(IButtonState.HOVERING);
				GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), MemoryUtil.NULL);
			}
		}), false);

		addEventListener(UIEventMouse.TYPE_MOUSE_DOWN, new UIEventListener.Functional<IUIEventMouse>(e -> {
			getButtonStates().add(IButtonState.PRESSING);
			e.stopPropagation();
		}), false);
		addEventListener(UIEventMouse.TYPE_MOUSE_UP, new UIEventListener.Functional<IUIEventMouse>(e -> {
			getButtonStates().remove(IButtonState.PRESSING);
			e.stopPropagation();
		}), false);

		addEventListener(UIEventMouse.TYPE_CLICK, new UIEventListener.Functional<IUIEventMouse>(e -> {
			getMethodOnActivated().invoke(e);
			e.stopPropagation();
		}), false);

		addEventListener(UIEventKeyboard.TYPE_KEY_DOWN, new UIEventListener.Functional<IUIEventKeyboard>(e -> {
			getMethodOnActivated().invoke(e);
			e.stopPropagation();
		}), false);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected EnumSet<IButtonState> getButtonStates() { return buttonStates; }

	protected IBindingMethod.ISource<IUIEvent> getMethodOnActivated() {
		return methodOnActivated;
	}

	@Override
	public void render(final IAffineTransformStack stack, Point2D cursorPosition, double partialTicks, boolean pre) {
		getScheduledActions().removeIf(a -> a.apply(stack, cursorPosition, partialTicks, pre));
		if (pre) {
			AffineTransform transform = stack.getDelegated().peek();
			Shape transformed = transform.createTransformedShape(getShapeDescriptor().getShapeProcessed());
			if (getButtonStates().contains(IButtonState.PRESSING)) {
				DrawingUtilities.drawShape(transformed, true, getColorPressed().getValue(), 0);
				DrawingUtilities.drawShape(transformed, false, getColorPressedBorder().getValue(), 0);
			} else if (getButtonStates().contains(IButtonState.HOVERING)) {
				DrawingUtilities.drawShape(transformed, true, getColorHovering().getValue(), 0);
				DrawingUtilities.drawShape(transformed, false, getColorHoveringBorder().getValue(), 0);
			} else {
				DrawingUtilities.drawShape(transformed, true, getColorBase().getValue(), 0);
				DrawingUtilities.drawShape(transformed, false, getColorBaseBorder().getValue(), 0);
			}
		}
	}

	@Override
	public void schedule(IFunction4<? super IAffineTransformStack, ? super Point2D, ? super Double, ? super Boolean, ? extends Boolean> action) { getScheduledActions().add(action); }

	@Override
	public void crop(IAffineTransformStack stack, EnumCropMethod method, boolean push, Point2D mouse, double partialTicks) { IUIComponentMinecraft.crop(this, stack, method, push, mouse, partialTicks); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IFunction4<? super IAffineTransformStack, ? super Point2D, ? super Double, ? super Boolean, ? extends Boolean>> getScheduledActions() { return scheduledActions; }

	protected IBindingField<Color> getColorPressed() {
		return colorPressed;
	}

	protected IBindingField<Color> getColorPressedBorder() {
		return colorPressedBorder;
	}

	protected IBindingField<Color> getColorHovering() {
		return colorHovering;
	}

	protected IBindingField<Color> getColorHoveringBorder() {
		return colorHoveringBorder;
	}

	protected IBindingField<Color> getColorBase() {
		return colorBase;
	}

	protected IBindingField<Color> getColorBaseBorder() {
		return colorBaseBorder;
	}

	@Override
	public boolean isFocusable() { return true; }

	@OnlyIn(Dist.CLIENT)
	protected enum IButtonState {
		HOVERING,
		PRESSING,
	}
}
