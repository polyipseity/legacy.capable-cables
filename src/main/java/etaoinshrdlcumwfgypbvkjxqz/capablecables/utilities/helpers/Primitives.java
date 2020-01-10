package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.INumberOperable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.MiscellaneousHelper.Comparables.greaterThan;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.MiscellaneousHelper.Comparables.lessThan;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectArguments;

public enum Primitives {
	;
	public enum Numbers {
		;
		@SuppressWarnings("unchecked")
		public static <T extends Number> T sum(T a, Iterable<T> e) {
			if (a instanceof INumberOperable<?>) {
				T r = a;
				for (T t : e) r = ((INumberOperable<T>) r).sum(t);
				return r;
			} else if (a instanceof Integer) {
				int r = a.intValue();
				for (T t : e) r += t.intValue();
				return (T) Integer.valueOf(r);
			} else if (a instanceof Double) {
				double r = a.doubleValue();
				for (T t : e) r += t.doubleValue();
				return (T) Double.valueOf(r);
			} else if (a instanceof Float) {
				float r = a.floatValue();
				for (T t : e) r += t.floatValue();
				return (T) Float.valueOf(r);
			} else if (a instanceof Long) {
				long r = a.longValue();
				for (T t : e) r += t.longValue();
				return (T) Long.valueOf(r);
			} else if (a instanceof Short) {
				short r = a.shortValue();
				for (T t : e) r += t.shortValue();
				return (T) Short.valueOf(r);
			} else if (a instanceof Byte) {
				byte r = a.byteValue();
				for (T t : e) r += t.byteValue();
				return (T) Byte.valueOf(r);
			} else throw rejectArguments(e);
		}

		@Nullable
		public static <T extends Number> T sum(List<T> e, boolean nullable) {
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
		public static <T extends Number> T sum(List<T> e) { return sum(e, false); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		@Nullable
		public static <T extends Number> T sum(boolean nullable, T... e) { return sum(Arrays.asList(e), nullable); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <T extends Number> T sum(T... e) { return sum(Arrays.asList(e)); }


		@SuppressWarnings("unchecked")
		public static <T extends Number> T minus(T a, T b) {
			if (a instanceof INumberOperable<?>) {
				return ((INumberOperable<T>) a).minus(b);
			} else if (a instanceof Integer) {
				return (T) Integer.valueOf(a.intValue() - b.intValue());
			} else if (a instanceof Double) {
				return (T) Double.valueOf(a.doubleValue() - b.doubleValue());
			} else if (a instanceof Float) {
				return (T) Float.valueOf(a.floatValue() - b.floatValue());
			} else if (a instanceof Long) {
				return (T) Long.valueOf(a.longValue() - b.longValue());
			} else if (a instanceof Short) {
				return (T) Short.valueOf((short) (a.shortValue() - b.shortValue()));
			} else if (a instanceof Byte) {
				return (T) Byte.valueOf((byte) (a.byteValue() - b.byteValue()));
			} else throw rejectArguments(a, b);
		}

		public static <T extends Number> T minus(List<T> e) {
			if (checkSize(2, e)) return minus(e.get(0), e.get(1));
			else throw rejectArguments(e);
		}

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <T extends Number> T minus(T... e) { return minus(Arrays.asList(e)); }


		@SuppressWarnings("unchecked")
		public static <T extends Number> T max(T a, Iterable<T> e) {
			if (a instanceof Comparable<?>) {
				Comparable<T> r = (Comparable<T>) a;
				for (T t : e) if (lessThan(r, t)) r = (Comparable<T>) t;
				return (T) r;
			} else throw rejectArguments(e);
		}

		@Nullable
		public static <T extends Number> T max(List<T> e, boolean nullable) {
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
		public static <T extends Number> T max(List<T> e) { return max(e, false); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		@Nullable
		public static <T extends Number> T max(boolean nullable, T... e) { return max(Arrays.asList(e), nullable); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <T extends Number> T max(T... e) { return max(Arrays.asList(e)); }


		@SuppressWarnings("unchecked")
		public static <T extends Number> T min(T a, Iterable<T> e) {
			if (a instanceof Comparable<?>) {
				Comparable<T> r = (Comparable<T>) a;
				for (T t : e) if (greaterThan(r, t)) r = (Comparable<T>) t;
				return (T) r;
			} else throw rejectArguments(e);
		}

		@Nullable
		public static <T extends Number> T min(List<T> e, boolean nullable) {
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
		public static <T extends Number> T min(List<T> e) { return min(e, false); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		@Nullable
		public static <T extends Number> T min(boolean nullable, T... e) { return min(Arrays.asList(e), nullable); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public static <T extends Number> T min(T... e) { return min(Arrays.asList(e)); }


		private static boolean checkSize(Collection<?> e) { return checkSize(1, e); }

		private static <T extends Number> boolean checkSize(int size, Collection<?> e) {
			if (e.size() == 0) throw rejectArguments(e);
			else return e.size() == size;
		}
	}
}
