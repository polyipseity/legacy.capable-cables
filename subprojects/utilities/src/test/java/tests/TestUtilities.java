package tests;

import static java.lang.System.out;

public enum TestUtilities {
	/* MARK empty */;

	public static void consumeCaught(Throwable t) {
		out.print("Consumed throwable");
		t.printStackTrace(out);
	}
}
