package $group__.ui.mvvm.views.components.common;

import $group__.ui.core.mvvm.binding.IBindingField;
import $group__.ui.core.mvvm.binding.IBindingMethod;
import $group__.ui.core.mvvm.binding.IHasBinding;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.events.IUIEvent;
import $group__.ui.core.mvvm.views.events.IUIEventKeyboard;
import $group__.ui.core.mvvm.views.events.IUIEventMouse;
import $group__.ui.core.parsers.binding.UIMethod;
import $group__.ui.core.parsers.binding.UIProperty;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.events.ui.UIEventListener;
import $group__.ui.mvvm.binding.BindingMethodSource;
import $group__.ui.mvvm.views.components.UIComponent;
import $group__.ui.mvvm.views.events.ui.UIEventKeyboard;
import $group__.ui.mvvm.views.events.ui.UIEventMouse;
import $group__.ui.structures.EnumCursor;
import $group__.ui.utilities.BindingUtilities;
import $group__.utilities.client.minecraft.GLUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

public class UIComponentButton
		extends UIComponent {
	public static final String PROPERTY_COLOR_BASE = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.base";
	public static final String PROPERTY_COLOR_BASE_BORDER = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.base.border";
	public static final String PROPERTY_COLOR_HOVERING = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.hovering";
	public static final String PROPERTY_COLOR_HOVERING_BORDER = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.hovering.border";
	public static final String PROPERTY_COLOR_PRESSED = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.pressed";
	public static final String PROPERTY_COLOR_PRESSED_BORDER = INamespacePrefixedString.DEFAULT_PREFIX + "button.colors.pressed.border";
	public static final String METHOD_ON_ACTIVATED = INamespacePrefixedString.DEFAULT_PREFIX + "button.methods.activated";

	public static final INamespacePrefixedString PROPERTY_COLOR_BASE_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_BASE);
	public static final INamespacePrefixedString PROPERTY_COLOR_BASE_BORDER_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_BASE_BORDER);
	public static final INamespacePrefixedString PROPERTY_COLOR_HOVERING_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_HOVERING);
	public static final INamespacePrefixedString PROPERTY_COLOR_HOVERING_BORDER_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_HOVERING_BORDER);
	public static final INamespacePrefixedString PROPERTY_COLOR_PRESSED_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_PRESSED);
	public static final INamespacePrefixedString PROPERTY_COLOR_PRESSED_BORDER_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_PRESSED_BORDER);
	public static final INamespacePrefixedString METHOD_ON_ACTIVATED_LOCATION = new NamespacePrefixedString(METHOD_ON_ACTIVATED);

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

	protected final EnumSet<IButtonState> buttonStates = EnumSet.noneOf(IButtonState.class);

	@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
	@UIComponentConstructor(type = UIComponentConstructor.ConstructorType.SHAPE_DESCRIPTOR__MAPPING)
	public UIComponentButton(IShapeDescriptor<?> shapeDescriptor, Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping) {
		super(shapeDescriptor, mapping);

		this.colorBase = IHasBinding.createBindingField(Color.class,
				this.mapping.get(PROPERTY_COLOR_BASE_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.DARK_GRAY);
		this.colorBaseBorder = IHasBinding.createBindingField(Color.class,
				this.mapping.get(PROPERTY_COLOR_BASE_BORDER_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.DARK_GRAY);
		this.colorHovering = IHasBinding.createBindingField(Color.class,
				this.mapping.get(PROPERTY_COLOR_HOVERING_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.GRAY);
		this.colorHoveringBorder = IHasBinding.createBindingField(Color.class,
				this.mapping.get(PROPERTY_COLOR_HOVERING_BORDER_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.GRAY);
		this.colorPressed = IHasBinding.createBindingField(Color.class,
				this.mapping.get(PROPERTY_COLOR_PRESSED_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.LIGHT_GRAY);
		this.colorPressedBorder = IHasBinding.createBindingField(Color.class,
				this.mapping.get(PROPERTY_COLOR_PRESSED_BORDER_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.LIGHT_GRAY);

		this.methodOnActivated = new BindingMethodSource<>(IUIEvent.class,
				Optional.ofNullable(this.mapping.get(METHOD_ON_ACTIVATED_LOCATION)).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));

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

	protected IBindingField<Color> getColorPressed() { return colorPressed; }

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
