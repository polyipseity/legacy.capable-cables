package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import javax.annotation.concurrent.Immutable;

@Immutable
public enum GrammarHelper {
    ;
    public static String appendSuffixIfPlural(long n, String suf) { return n == 1 ? "" : suf; }
}
