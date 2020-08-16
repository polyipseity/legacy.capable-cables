package $group__.utilities.automata.core;

public interface IState<A> {
	void transitFromThis(A argument);

	void transitToThis(A argument);
}
