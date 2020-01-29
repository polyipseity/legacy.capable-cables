package $group__.$modId__.utilities.constructs.classes.concrete;

import $group__.$modId__.utilities.constructs.classes.Singleton;
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

import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

public class ModContainerNull extends Singleton implements ModContainer {
	/* SECTION methods */

	@Override
	public String getModId() { throw rejectUnsupportedOperation(); }

	@Override
	public String getName() { throw rejectUnsupportedOperation(); }

	@Override
	public String getVersion() { throw rejectUnsupportedOperation(); }

	@Override
	public File getSource() { throw rejectUnsupportedOperation(); }

	@Override
	public ModMetadata getMetadata() { throw rejectUnsupportedOperation(); }

	@Override
	public void bindMetadata(MetadataCollection mc) { throw rejectUnsupportedOperation(); }

	@Override
	public void setEnabledState(boolean enabled) { throw rejectUnsupportedOperation(); }
	@Override
	public Set<ArtifactVersion> getRequirements() { throw rejectUnsupportedOperation(); }

	@Override
	public List<ArtifactVersion> getDependencies() { throw rejectUnsupportedOperation(); }

	@Override
	public List<ArtifactVersion> getDependants() { throw rejectUnsupportedOperation(); }

	@Override
	public String getSortingRules() { throw rejectUnsupportedOperation(); }

	@Override
	public boolean registerBus(@SuppressWarnings("UnstableApiUsage") EventBus bus, LoadController controller) { throw rejectUnsupportedOperation(); }

	@Override
	public boolean matches(Object mod) { throw rejectUnsupportedOperation(); }

	@Override
	public Object getMod() { throw rejectUnsupportedOperation(); }

	@Override
	public ArtifactVersion getProcessedVersion() { throw rejectUnsupportedOperation(); }

	@Override
	public boolean isImmutable() { throw rejectUnsupportedOperation(); }

	@Override
	public String getDisplayVersion() { throw rejectUnsupportedOperation(); }

	@Override
	public VersionRange acceptableMinecraftVersionRange() { throw rejectUnsupportedOperation(); }
	@Nullable
	@Override
	public Certificate getSigningCertificate() { throw rejectUnsupportedOperation(); }

	@Override
	public Map<String, String> getCustomModProperties() { throw rejectUnsupportedOperation(); }

	@Override
	public Class<?> getCustomResourcePackClass() { throw rejectUnsupportedOperation(); }

	@Override
	public Map<String, String> getSharedModDescriptor() { throw rejectUnsupportedOperation(); }

	@Override
	public Disableable canBeDisabled() { throw rejectUnsupportedOperation(); }

	@Override
	public String getGuiClassName() { throw rejectUnsupportedOperation(); }

	@Override
	public List<String> getOwnedPackages() { throw rejectUnsupportedOperation(); }

	@Override
	public boolean shouldLoadInEnvironment() { throw rejectUnsupportedOperation(); }

	@Override
	public URL getUpdateUrl() { throw rejectUnsupportedOperation(); }

	@Override
	public void setClassVersion(int classVersion) { throw rejectUnsupportedOperation(); }

	@Override
	public int getClassVersion() { throw rejectUnsupportedOperation(); }
}
