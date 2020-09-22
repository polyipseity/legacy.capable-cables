package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events;

public enum EnumHookStage {
	PRE,
	POST,
	;

	public boolean isPre() { return this == PRE; }

	public boolean isPost() { return this == POST; }
}
