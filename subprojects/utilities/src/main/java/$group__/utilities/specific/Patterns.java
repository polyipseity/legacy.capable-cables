package $group__.utilities.specific;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;

public enum Patterns {
	/* MARK empty */;


	public static final Pattern
			HASH_PATTERN = compile(quote("#")),
			TWO_MINUS_SIGNS_PATTERN = compile(quote("--")),
			LEFT_N_RIGHT_BRACKETS_PATTERN = compile(quote("[]"));
}
