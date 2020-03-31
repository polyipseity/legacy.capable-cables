package $group__.$modId__.utilities.builders;

import $group__.$modId__.utilities.concurrent.IMutatorImmutablizable;
import $group__.$modId__.utilities.concurrent.MutatorMutable;
import $group__.$modId__.logging.ILogging;
import $group__.$modId__.logging.LoggingNull;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.function.Function;

import static $group__.$modId__.utilities.builders.BuilderDefaults.peekDefault;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Optionals.optionalNonnull;
import static com.google.common.collect.Maps.immutableEntry;
import static java.util.function.Function.identity;

public class BuilderStructure<T extends BuilderStructure<T, V>, V> extends Builder<T, V> {
    /* SECTION static variables */

    public static final Map.Entry<Class<IMutatorImmutablizable<?, ?>>, String> KEY_DEFAULT_MUTATOR = immutableEntry(castUncheckedUnboxedNonnull(IMutatorImmutablizable.class), "mutator");
    public static final Map.Entry<Class<ILogging<Logger>>, String> KEY_DEFAULT_LOGGING = immutableEntry(castUncheckedUnboxedNonnull(ILogging.class), "logging");


    /* SECTION variables */

    public IMutatorImmutablizable<?, ?> mutator = castUncheckedUnboxedNonnull(optionalNonnull(peekDefault(KEY_DEFAULT_MUTATOR), identity(), () -> MutatorMutable.INSTANCE));
    public ILogging<Logger> logging = castUncheckedUnboxedNonnull(optionalNonnull(peekDefault(KEY_DEFAULT_LOGGING), identity(), LoggingNull::getInstance));


    /* SECTION constructors */

    public BuilderStructure(Function<? super T, ? extends V> constructor) { super(constructor); }

    public BuilderStructure(BuilderStructure<T, V> copy) {
        super(copy);
        mutator = copy.mutator;
        logging = copy.logging;
    }


    /* SECTION methods */

    public T setMutator(IMutatorImmutablizable<?, ?> mutator) {
        this.mutator = mutator;
        return castUncheckedUnboxedNonnull(this);
    }

    public T setLogging(ILogging<Logger> logging) {
        this.logging = logging;
        return castUncheckedUnboxedNonnull(this);
    }
}
