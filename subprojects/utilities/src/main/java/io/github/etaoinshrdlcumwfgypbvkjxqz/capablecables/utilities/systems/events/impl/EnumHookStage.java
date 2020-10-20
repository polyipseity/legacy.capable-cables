package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl;

public enum EnumHookStage {
	PRE,
	POST,
	;

	public boolean isPre() { return this == PRE; }

	public boolean isPost() { return this == POST; }
}
