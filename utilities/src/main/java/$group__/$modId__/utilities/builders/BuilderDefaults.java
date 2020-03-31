package $group__.$modId__.utilities.builders;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

import static $group__.$modId__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.MAP_MAKER_MULTI_THREAD;

public enum BuilderDefaults {
    /* SECTION enum */
    INSTANCE;


    /* SECTION static variables */

    public static final ThreadLocal<ConcurrentMap<Map.Entry<Class<?>, String>, Deque<?>>> DEFAULTS_MAP = ThreadLocal.withInitial(MAP_MAKER_MULTI_THREAD::makeMap);

    private static final ThreadLocal<Boolean> PUSHING = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<List<Map.Entry<Class<?>, String>>> PUSHED = ThreadLocal.withInitial(() -> new ArrayList<>(INITIAL_CAPACITY_2));


    /* SECTION static methods */

    public static <T> Deque<T> getDefaults(Map.Entry<Class<T>, String> key) { return castUncheckedUnboxedNonnull(DEFAULTS_MAP.get().computeIfAbsent(castUncheckedUnboxedNonnull(key), c -> new ArrayDeque<T>(INITIAL_CAPACITY_2))); }

    @Nullable
    public static <T> T peekDefault(Map.Entry<Class<T>, String> key) { return getDefaults(key).peek(); }

    public static <T> BuilderDefaults pushDefaultStart(Map.Entry<Class<T>, String> key, @Nullable T defaultObj) { return INSTANCE.startPushing().pushDefault(key, defaultObj); }


    /* SECTION methods */

    public <T> BuilderDefaults pushDefault(Map.Entry<Class<T>, String> key, @Nullable T defaultObj) {
        assert PUSHING.get();
        getDefaults(key).push(defaultObj);
        PUSHED.get().add(castUncheckedUnboxedNonnull(key));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> T popDefault(Map.Entry<Class<T>, String> key) {
        assert !PUSHING.get();
        return getDefaults(key).pop();
    }


    public BuilderDefaults startPushing() {
        assert !PUSHING.get();
        PUSHED.get().clear();
        PUSHING.set(true);
        return this;
    }

    public Runnable stopPushing() {
        assert PUSHING.get();
        PUSHING.set(false);
        return () -> {
            PUSHED.get().forEach(t -> popDefault(castUncheckedUnboxedNonnull(t)));
            PUSHED.get().clear();
        };
    }
}
