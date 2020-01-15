package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.INumberOperable;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IOperable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Comparables.greaterThan;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Comparables.lessThan;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectArguments;

public enum Primitives {
	/* MARK empty */ ;


	/* SECTION static classes */

	public enum Numbers {
		/* MARK empty */ ;


		/* SECTION static methods */

		public static <N> N sum(N a, Iterable<N> e) {
			if (a instanceof IOperable<?, ?>) {
				N r = a;
				for (N n : e) r = castUnchecked(r, (IOperable<N, Object>) null).sum(n);
				return castUnchecked(r);
			} else if (a instanceof Integer) {
				int r = (Integer) a;
				for (int t : castUnchecked(e, (Iterable<Integer>) null)) r += t;
				return castUnchecked(r);
			} else if (a instanceof Float) {
				float r = (Float) a;
				for (float t : castUnchecked(e, (Iterable<Float>) null)) r += t;
				return castUnchecked(r);
			} else if (a instanceof Double) {
				double r = (Double) a;
				for (double t : castUnchecked(e, (Iterable<Double>) null)) r += t;
				return castUnchecked(r);
			} else if (a instanceof Long) {
				long r = (Long) a;
				for (long t : castUnchecked(e, (Iterable<Long>) null)) r += t;
				return castUnchecked(r);
			} else if (a instanceof Byte) {
				byte r = (Byte) a;
				for (byte t : castUnchecked(e, (Iterable<Byte>) null)) r += t;
				return castUnchecked(r);
			} else if (a instanceof Short) {
				short r = (Short) a;
				for (short t : castUnchecked(e, (Iterable<Short>) null)) r += t;
				return castUnchecked(r);
			} else throw rejectArguments(e);
		}

		@Nullable
		public static <N> N sum(List<N> e, boolean nullable) {
			switch (e.size()) {
				case 0:
					if (nullable) return null;
					else throw rejectArguments(e);
				case 1:
					return e.get(0);
				default:
					return sum(e.get(0), e.subList(1, e.size()));
			}
		}

		@SuppressWarnings("ConstantConditions")
		public static <N> N sum(List<N> e) { return sum(e, false); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		@Nullable
		public static <N> N sum(boolean nullable, N... e) { return sum(Arrays.asList(e), nullable); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <N> N sum(N... e) { return sum(Arrays.asList(e)); }


		@SuppressWarnings({"unchecked", "RedundantCast"})
		public static <N> N minus(N a, N b) {
			if (a instanceof INumberOperable<?>) {
				return ((IOperable<N, Object>) a).minus(b);
			} else if (a instanceof Integer) {
				return castUnchecked((Integer) a - (Integer) b);
			} else if (a instanceof Float) {
				return castUnchecked((Float) a - (Float) b);
			} else if (a instanceof Double) {
				return castUnchecked((Double) a - (Double) b);
			} else if (a instanceof Long) {
				return castUnchecked((Long) a - (Long) b);
			} else if (a instanceof Byte) {
				return castUnchecked((Byte) a - (Byte) b);
			} else if (a instanceof Short) {
				return castUnchecked((Short) a - (Short) b);
			} else throw rejectArguments(a, b);
		}

		public static <N> N minus(List<N> e) {
			if (e.size() == 2) return minus(e.get(0), e.get(1));
			else throw rejectArguments(e);
		}

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <N> N minus(N... e) { return minus(Arrays.asList(e)); }


		public static <N> N max(N a, Iterable<N> e) {
			if (a instanceof Comparable<?>) {
				Comparable<N> r = castUnchecked(a);
				for (N n : e) if (lessThan(r, n)) r = castUnchecked(n);
				return castUnchecked(r);
			} else throw rejectArguments(e);
		}

		@Nullable
		public static <N> N max(List<N> e, boolean nullable) {
			switch (e.size()) {
				case 0:
					if (nullable) return null;
					else throw rejectArguments(e);
				case 1:
					return e.get(0);
				default:
					return max(e.get(0), e.subList(1, e.size()));
			}
		}

		@SuppressWarnings("ConstantConditions")
		public static <N> N max(List<N> e) { return max(e, false); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		@Nullable
		public static <N> N max(boolean nullable, N... e) { return max(Arrays.asList(e), nullable); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <N> N max(N... e) { return max(Arrays.asList(e)); }


		public static <N> N min(N a, Iterable<N> e) {
			if (a instanceof Comparable<?>) {
				Comparable<N> r = castUnchecked(a);
				for (N n : e) if (greaterThan(r, n)) r = castUnchecked(n);
				return castUnchecked(r);
			} else throw rejectArguments(e);
		}

		@Nullable
		public static <N> N min(List<N> e, boolean nullable) {
			switch (e.size()) {
				case 0:
					if (nullable) return null;
					else throw rejectArguments(e);
				case 1:
					return e.get(0);
				default:
					return min(e.get(0), e.subList(1, e.size()));
			}
		}

		@SuppressWarnings("ConstantConditions")
		public static <N> N min(List<N> e) { return min(e, false); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		@Nullable
		public static <N> N min(boolean nullable, N... e) { return min(Arrays.asList(e), nullable); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <N> N min(N... e) { return min(Arrays.asList(e)); }
	}
}
