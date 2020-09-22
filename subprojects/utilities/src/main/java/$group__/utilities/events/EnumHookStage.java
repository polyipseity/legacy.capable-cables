package $group__.utilities.events;

public enum EnumHookStage {
	PRE,
	POST,
	;

	public boolean isPre() { return this == PRE; }

	public boolean isPost() { return this == POST; }
}
