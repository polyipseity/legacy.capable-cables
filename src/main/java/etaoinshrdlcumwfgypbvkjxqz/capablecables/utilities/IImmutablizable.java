package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

@SuppressWarnings("SpellCheckingInspection")
public interface IImmutablizable<M> {
    M toImmutable();
    default boolean isImmutable() { return false; }
}
