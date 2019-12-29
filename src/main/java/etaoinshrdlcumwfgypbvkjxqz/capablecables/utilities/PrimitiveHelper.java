package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectArguments;

public enum PrimitiveHelper {
    ;
    public enum Numbers {
        ;
        @SuppressWarnings("unchecked")
        public static <T extends Number> T sum(T a, T... e) {
            if (a instanceof Integer) {
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
            } else throw rejectArguments((Object) e);
        }
        @SuppressWarnings("unchecked")
        public static <T extends Number> T minus(T a, T b) {
            if (a instanceof Integer) {
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
        
        @SuppressWarnings("unchecked")
        public static <T extends Number> T max(T a, T... e) {
            if (a instanceof Integer) {
                int r = a.intValue();
                for (T t : e) if (t.intValue() > r) r = t.intValue();
                return (T) Integer.valueOf(r);
            } else if (a instanceof Double) {
                double r = a.doubleValue();
                for (T t : e) if (t.doubleValue() > r) r = t.doubleValue();
                return (T) Double.valueOf(r);
            } else if (a instanceof Float) {
                float r = a.floatValue();
                for (T t : e) if (t.floatValue() > r) r = t.floatValue();
                return (T) Float.valueOf(r);
            } else if (a instanceof Long) {
                long r = a.longValue();
                for (T t : e) if (t.longValue() > r) r = t.longValue();
                return (T) Long.valueOf(r);
            } else if (a instanceof Short) {
                short r = a.shortValue();
                for (T t : e) if (t.shortValue() > r) r = t.shortValue();
                return (T) Short.valueOf(r);
            } else if (a instanceof Byte) {
                byte r = a.byteValue();
                for (T t : e) if (t.byteValue() > r) r = t.byteValue();
                return (T) Byte.valueOf(r);
            } else throw rejectArguments((Object) e);
        }
        @SuppressWarnings("unchecked")
        public static <T extends Number> T min(T a, T... e) {
            if (a instanceof Integer) {
                int r = a.intValue();
                for (T t : e) if (t.intValue() < r) r = t.intValue();
                return (T) Integer.valueOf(r);
            } else if (a instanceof Double) {
                double r = a.doubleValue();
                for (T t : e) if (t.doubleValue() < r) r = t.doubleValue();
                return (T) Double.valueOf(r);
            } else if (a instanceof Float) {
                float r = a.floatValue();
                for (T t : e) if (t.floatValue() < r) r = t.floatValue();
                return (T) Float.valueOf(r);
            } else if (a instanceof Long) {
                long r = a.longValue();
                for (T t : e) if (t.longValue() < r) r = t.longValue();
                return (T) Long.valueOf(r);
            } else if (a instanceof Short) {
                short r = a.shortValue();
                for (T t : e) if (t.shortValue() < r) r = t.shortValue();
                return (T) Short.valueOf(r);
            } else if (a instanceof Byte) {
                byte r = a.byteValue();
                for (T t : e) if (t.byteValue() < r) r = t.byteValue();
                return (T) Byte.valueOf(r);
            } else throw rejectArguments((Object) e);
        }
    }
}
