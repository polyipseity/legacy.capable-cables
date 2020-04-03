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

import static $group__.$modId__.Globals.LOGGER;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;

public final class ModContainerNull extends Singleton implements ModContainer {
	/* SECTION constructors */

	private ModContainerNull() { super(LOGGER); }


	/* SECTION methods */

	@Override
	@Deprecated
	public String getModId() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public String getName() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public String getVersion() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public File getSource() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public ModMetadata getMetadata() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void bindMetadata(MetadataCollection mc) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void setEnabledState(boolean enabled) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public Set<ArtifactVersion> getRequirements() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public List<ArtifactVersion> getDependencies() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public List<ArtifactVersion> getDependants() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public String getSortingRules() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public boolean registerBus(@SuppressWarnings("UnstableApiUsage") EventBus bus, LoadController controller) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public boolean matches(Object mod) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public Object getMod() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public ArtifactVersion getProcessedVersion() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public boolean isImmutable() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public String getDisplayVersion() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public VersionRange acceptableMinecraftVersionRange() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Nullable
	@Deprecated
	public Certificate getSigningCertificate() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public Map<String, String> getCustomModProperties() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public Class<?> getCustomResourcePackClass() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public Map<String, String> getSharedModDescriptor() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public Disableable canBeDisabled() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public String getGuiClassName() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public List<String> getOwnedPackages() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public boolean shouldLoadInEnvironment() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public URL getUpdateUrl() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public int getClassVersion() throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void setClassVersion(int classVersion) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }
}
