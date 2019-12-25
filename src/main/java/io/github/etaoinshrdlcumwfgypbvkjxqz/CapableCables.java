package io.github.etaoinshrdlcumwfgypbvkjxqz;

import io.github.etaoinshrdlcumwfgypbvkjxqz.utilities.ThrowHelper;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.utilities.Constants.*;

@Mod(modid = MOD_ID,
        name = NAME,
        version = VERSION,
        dependencies = DEPENDENCIES,
        useMetadata = true,
        acceptedMinecraftVersions = ACCEPTED_MINECRAFT_VERSIONS,
        certificateFingerprint = CERTIFICATE_FINGERPRINT,
        updateJSON = UPDATE_JSON)
public class CapableCables {
    private static final CapableCables INSTANCE = new CapableCables();
    private CapableCables() { if (Objects.nonNull(INSTANCE)) throw ThrowHelper.rejectInstantiation(); }
    @Mod.InstanceFactory
    public static CapableCables getInstance() { return INSTANCE; }
}
