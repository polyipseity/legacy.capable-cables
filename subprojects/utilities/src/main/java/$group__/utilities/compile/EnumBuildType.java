package $group__.utilities.compile;

import $group__.utilities.specific.ThrowableUtilities;

import java.util.Arrays;

public enum EnumBuildType {
	DEBUG("debug"),
	RELEASE("release"),
	;

	protected final String string;

	EnumBuildType(String string) { this.string = string; }

	public static EnumBuildType fromString(String string) {
		for (EnumBuildType value : values()) {
			if (string.equals(value.getString()))
				return value;
		}
		throw ThrowableUtilities.BecauseOf.illegalArgument("Invalid build type string",
				"string", string,
				"values()", Arrays.toString(values()));
	}

	public String getString() { return string; }

	public boolean isDebug() { return this == DEBUG; }

	public boolean isRelease() { return this == RELEASE; }
}
