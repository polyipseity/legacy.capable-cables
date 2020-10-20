package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IKeyboardKeyPressData;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ImmutableKeyboardKeyPressData implements IKeyboardKeyPressData, Cloneable {
	private final int key, scanCode, modifiers;
	private final long timestamp;

	public ImmutableKeyboardKeyPressData(int key, int scanCode, int modifiers) { this(key, scanCode, modifiers, System.currentTimeMillis()); }

	private ImmutableKeyboardKeyPressData(int key, int scanCode, int modifiers, long timestamp) {
		this.key = key;
		this.scanCode = scanCode;
		this.modifiers = modifiers;
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, StaticHolder.getObjectVariables()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, StaticHolder.getObjectVariables()); }

	@Override
	public ImmutableKeyboardKeyPressData clone() throws CloneNotSupportedException { return (ImmutableKeyboardKeyPressData) super.clone(); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, StaticHolder.getObjectVariablesMap()); }

	@Override
	public ImmutableKeyboardKeyPressData copy() { return new ImmutableKeyboardKeyPressData(getKey(), getScanCode(), getModifiers(), getTimestampMills()); }

	@Override
	public int getKey() { return key; }

	@Override
	public int getScanCode() { return scanCode; }

	@Override
	public int getModifiers() { return modifiers; }

	@Override
	public long getTimestampMills() { return timestamp; }


	@Override
	public IKeyboardKeyPressData recreate() { return new ImmutableKeyboardKeyPressData(getKey(), getScanCode(), getModifiers()); }


}
