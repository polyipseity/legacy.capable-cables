package $group__;

public enum Constants {
	/* MARK empty */;


	/* SECTION static variables */

	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	public static final String
			NAME = "${name}",
			VERSION = "${version}",
			DEPENDENCIES = "${dependencies}",
			ACCEPTED_MINECRAFT_VERSIONS = "${minecraftVersionRange}",
			CERTIFICATE_FINGERPRINT = "${certificateFingerprint}",
			UPDATE_JSON = "${updateJSON}";

	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	public static final String
			COFH_CORE_ID = "cofhcore", COFH_CORE_PACKAGE = "cofh",
			BUILDCRAFT_API_ID = "BuildCraftAPI|core", BUILDCRAFT_API_PACKAGE = "buildcraft";
}
