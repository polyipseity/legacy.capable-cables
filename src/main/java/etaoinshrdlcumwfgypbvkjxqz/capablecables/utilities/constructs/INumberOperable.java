package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs;

public interface INumberOperable<N extends Number> extends Comparable<N> {
	N sum(Number... o);

	N minus(Number o);
}
