package io.github.etaoinshrdlcumwfgypbvkjxqz.utilities;

public class Constants {
    private Constants() { throw ThrowHelper.rejectInstantiation(); }

    public static final String
            MOD_ID = "${modid}",
            NAME = "${name}",
            VERSION = "${version}",
            DEPENDENCIES = "${dependencies}",
            ACCEPTED_MINECRAFT_VERSIONS = "${minecraftVersionRange}",
            CERTIFICATE_FINGERPRINT = "${certificateFingerprint}",
            UPDATE_JSON = "${updateJson}";
}
