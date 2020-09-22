package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.automata.core;

public interface IState<A> {
	void transitFromThis(A argument);

	void transitToThis(A argument);
}
