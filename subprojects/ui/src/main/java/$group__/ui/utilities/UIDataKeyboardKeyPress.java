package $group__.ui.utilities;

import $group__.ui.core.structures.IUIDataKeyboardKeyPress;
import $group__.utilities.ObjectUtilities;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class UIDataKeyboardKeyPress implements IUIDataKeyboardKeyPress, Cloneable {
	protected final int key, scanCode, modifiers;
	protected final long timestamp;

	public UIDataKeyboardKeyPress(int key, int scanCode, int modifiers) { this(key, scanCode, modifiers, System.currentTimeMillis()); }

	protected UIDataKeyboardKeyPress(int key, int scanCode, int modifiers, long timestamp) {
		this.key = key;
		this.scanCode = scanCode;
		this.modifiers = modifiers;
		this.timestamp = timestamp;
	}

	@Override
	public int getKey() { return key; }

	@Override
	public int getScanCode() { return scanCode; }

	@Override
	public int getModifiers() { return modifiers; }

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, StaticHolder.getObjectVariables()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, StaticHolder.getObjectVariables()); }

	@Override
	public UIDataKeyboardKeyPress clone() throws CloneNotSupportedException { return (UIDataKeyboardKeyPress) super.clone(); }

	@Override
	public long getTimestampMills() { return timestamp; }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, StaticHolder.getObjectVariablesMap()); }

	@Override
	public IUIDataKeyboardKeyPress recreate() { return new UIDataKeyboardKeyPress(getKey(), getScanCode(), getModifiers()); }

	@Override
	public UIDataKeyboardKeyPress copy() { return new UIDataKeyboardKeyPress(getKey(), getScanCode(), getModifiers(), getTimestampMills()); }
}
