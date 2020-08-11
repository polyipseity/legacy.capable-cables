package $group__.client.ui.mvvm.structures;

import $group__.client.ui.mvvm.core.structures.IUIDataKeyboardKeyPress;
import $group__.utilities.ObjectUtilities;
import $group__.utilities.specific.ThrowableUtilities.Try;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Immutable
public final class UIDataKeyboardKeyPress implements IUIDataKeyboardKeyPress, Cloneable {
	private static final Logger LOGGER = LogManager.getLogger();
	protected final int key, scanCode, modifiers;
	protected final long timestamp = System.currentTimeMillis();

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
	public long getTimestampMills() { return timestamp; }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }

	@Override
	public IUIDataKeyboardKeyPress recreate() { return new UIDataKeyboardKeyPress(getKey(), getScanCode(), getModifiers()); }
}
