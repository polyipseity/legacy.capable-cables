package $group__.utilities.events;

public enum EnumEventHookStage {
	PRE,
	POST,
	;

	public boolean isPre() { return this == PRE; }

	public boolean isPost() { return this == POST; }
}
