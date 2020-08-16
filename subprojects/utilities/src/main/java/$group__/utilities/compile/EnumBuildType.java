package $group__.utilities.compile;

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
		return DEBUG; // COMMENT default case in case the string did not get replaced
	}

	public String getString() { return string; }

	public boolean isDebug() { return this == DEBUG; }

	public boolean isRelease() { return this == RELEASE; }
}
