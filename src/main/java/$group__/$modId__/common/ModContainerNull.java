package $group__.$modId__.common;

import $group__.$modId__.utilities.Singleton;
import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionRange;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;

public final class ModContainerNull extends Singleton implements ModContainer {
	/* SECTION constructors */

	private ModContainerNull() { super(LOGGER); }


	/* SECTION methods */

	@Override
	@Deprecated
	public String getModId() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public String getName() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public String getVersion() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public File getSource() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public ModMetadata getMetadata() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void bindMetadata(MetadataCollection mc) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void setEnabledState(boolean enabled) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public Set<ArtifactVersion> getRequirements() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public List<ArtifactVersion> getDependencies() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public List<ArtifactVersion> getDependants() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public String getSortingRules() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public boolean registerBus(@SuppressWarnings("UnstableApiUsage") EventBus bus, LoadController controller) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public boolean matches(Object mod) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public Object getMod() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public ArtifactVersion getProcessedVersion() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public boolean isImmutable() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public String getDisplayVersion() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public VersionRange acceptableMinecraftVersionRange() { throw rejectUnsupportedOperation(); }

	@Override
	@Nullable
	@Deprecated
	public Certificate getSigningCertificate() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public Map<String, String> getCustomModProperties() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public Class<?> getCustomResourcePackClass() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public Map<String, String> getSharedModDescriptor() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public Disableable canBeDisabled() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public String getGuiClassName() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public List<String> getOwnedPackages() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public boolean shouldLoadInEnvironment() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public URL getUpdateUrl() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public int getClassVersion() { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void setClassVersion(int classVersion) { throw rejectUnsupportedOperation(); }
}
