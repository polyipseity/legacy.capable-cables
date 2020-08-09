package $group__.client.ui.mvvm.structures;

import $group__.client.ui.mvvm.core.structures.IUIDataKeyboardKeyPress;
import $group__.client.ui.mvvm.views.components.IUIComponent;
import $group__.utilities.ObjectUtilities;
import $group__.utilities.specific.MapUtilities;
import $group__.utilities.specific.ThrowableUtilities.Try;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

@Immutable
public final class UIDataKeyboardKeyPress implements IUIDataKeyboardKeyPress, Cloneable {
	public static final ImmutableList<Function<UIDataKeyboardKeyPress, Object>> OBJECT_VARIABLES = ImmutableList.of(
			UIDataKeyboardKeyPress::getKey, UIDataKeyboardKeyPress::getScanCode, UIDataKeyboardKeyPress::getModifiers);
	public static final ImmutableMap<String, Function<UIDataKeyboardKeyPress, Object>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("key", "scanCode", "modifiers"), OBJECT_VARIABLES));
	private static final Logger LOGGER = LogManager.getLogger();
	protected final int key, scanCode, modifiers;

	public UIDataKeyboardKeyPress(int key, int scanCode, int modifiers) {
		this.key = key;
		this.scanCode = scanCode;
		this.modifiers = modifiers;
	}

	@Override
	public int getKey() { return key; }

	@Override
	public int getScanCode() { return scanCode; }

	@Override
	public int getModifiers() { return modifiers; }

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, OBJECT_VARIABLES); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, OBJECT_VARIABLES); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	public UIDataKeyboardKeyPress clone() { return (UIDataKeyboardKeyPress) Try.call(() -> super.clone(), LOGGER).orElseThrow(InternalError::new); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }

	public static class Tracked {
		protected final IUIComponent component;
		protected final UIDataKeyboardKeyPress data;

		public Tracked(IUIComponent component, UIDataKeyboardKeyPress data) {
			this.component = component;
			this.data = data;
		}

		public IUIComponent getComponent() { return component; }

		public UIDataKeyboardKeyPress getData() { return data; }
	}
}
