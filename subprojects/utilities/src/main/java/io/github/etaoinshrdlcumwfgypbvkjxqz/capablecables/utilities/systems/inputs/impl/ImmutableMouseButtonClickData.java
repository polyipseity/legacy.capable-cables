package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IMouseButtonClickData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;

@Immutable
public final class ImmutableMouseButtonClickData
		extends AbstractTimestampedInputData
		implements IMouseButtonClickData {
	private final Point2D cursorPosition;
	private final int button;

	private static final long CURSOR_POSITION_FIELD_OFFSET;

	static {
		try {
			CURSOR_POSITION_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(ImmutableMouseButtonClickData.class.getDeclaredField("cursorPosition"));
		} catch (NoSuchFieldException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	@Override
	public Point2D getCursorPositionView() { return (Point2D) getCursorPosition().clone(); }

	protected Point2D getCursorPosition() { return cursorPosition; }

	@Override
	public int getButton() { return button; }

	public ImmutableMouseButtonClickData(Point2D cursorPosition) { this(cursorPosition, IMouseButtonClickData.StaticHolder.getMouseButtonNull()); }

	public ImmutableMouseButtonClickData(Point2D cursorPosition, int button) {
		this.cursorPosition = (Point2D) cursorPosition.clone();
		this.button = button;
	}

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, IMouseButtonClickData.StaticHolder.getObjectVariables()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, IMouseButtonClickData.class, obj, true, null, IMouseButtonClickData.StaticHolder.getObjectVariables()); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, IMouseButtonClickData.StaticHolder.getObjectVariablesMap()); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public ImmutableMouseButtonClickData recreate() {
		return (ImmutableMouseButtonClickData) super.recreate();
	}

	@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
	@Override
	public ImmutableMouseButtonClickData clone() {
		ImmutableMouseButtonClickData result;
		try {
			result = (ImmutableMouseButtonClickData) super.clone();
			DynamicUtilities.getUnsafe().putObjectVolatile(result, getCursorPositionFieldOffset(), result.cursorPosition.clone());
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
		return result;
	}

	protected static long getCursorPositionFieldOffset() {
		return CURSOR_POSITION_FIELD_OFFSET;
	}
}
