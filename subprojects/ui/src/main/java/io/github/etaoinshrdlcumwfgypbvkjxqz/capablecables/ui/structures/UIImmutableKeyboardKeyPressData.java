package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIKeyboardKeyPressData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class UIImmutableKeyboardKeyPressData implements IUIKeyboardKeyPressData, Cloneable {
	private final int key, scanCode, modifiers;
	private final long timestamp;

	public UIImmutableKeyboardKeyPressData(int key, int scanCode, int modifiers) { this(key, scanCode, modifiers, System.currentTimeMillis()); }

	private UIImmutableKeyboardKeyPressData(int key, int scanCode, int modifiers, long timestamp) {
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
	public UIImmutableKeyboardKeyPressData clone() throws CloneNotSupportedException { return (UIImmutableKeyboardKeyPressData) super.clone(); }

	@Override
	public long getTimestampMills() { return timestamp; }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, StaticHolder.getObjectVariablesMap()); }

	@Override
	public IUIKeyboardKeyPressData recreate() { return new UIImmutableKeyboardKeyPressData(getKey(), getScanCode(), getModifiers()); }

	@Override
	public UIImmutableKeyboardKeyPressData copy() { return new UIImmutableKeyboardKeyPressData(getKey(), getScanCode(), getModifiers(), getTimestampMills()); }
}
