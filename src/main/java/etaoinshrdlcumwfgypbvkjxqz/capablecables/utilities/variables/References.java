package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.variables;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.CapableCables;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.utilities.constructs.ResourceLocationDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

public enum References {
	;
	public static final CapableCables MOD = CapableCables.INSTANCE;
	public static final Logger LOGGER = CapableCables.LOGGER;


	@SideOnly(Side.CLIENT)
	public enum Client {
		;


		public static final Minecraft GAME = Minecraft.getMinecraft();
		public static final ScaledResolution RESOLUTION = new ScaledResolution(GAME);
	}


	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	public static final String
			COFH_CORE_ID = "cofhcore",
			COFH_CORE_PACKAGE = "cofh",
			BUILDCRAFT_API_ID = "BuildCraftAPI|core",
			BUILDCRAFT_API_PACKAGE = "buildcraft";

	public enum ResourceLocations {
		;
		public static final ResourceLocation GUI_WRENCH = new ResourceLocationDefault("textures/gui/containers/wrench.png");
	}
}
