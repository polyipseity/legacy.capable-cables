package $group__.$modId__.utilities.variables;

import $group__.$modId__.ModOwn;
import $group__.$modId__.common.registrable.utilities.constructs.ResourceLocationDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static java.util.Objects.requireNonNull;

public enum References {
	/* MARK empty */ ;


	/* SECTION static variables */

	public static final ModOwn MOD = ModOwn.INSTANCE;
	public static final Logger LOGGER = ModOwn.LOGGER;
	public static final Profiler PROFILER = new Profiler();

	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	public static final String
			COFH_CORE_ID = "cofhcore",
			COFH_CORE_PACKAGE = "cofh",
			BUILDCRAFT_API_ID = "BuildCraftAPI|core",
			BUILDCRAFT_API_PACKAGE = "buildcraft";


	/* SECTION static methods */

	public static <T> T markUnused(Class<T> t) {
		T r = null;
		if (Number.class.isAssignableFrom(t)) {
			if (Integer.class.isAssignableFrom(t)) r = castUnchecked(0);
			else if (Float.class.isAssignableFrom(t)) r = castUnchecked(0F);
			else if (Double.class.isAssignableFrom(t)) r = castUnchecked(0D);
			else if (Long.class.isAssignableFrom(t)) r = castUnchecked(0L);
			else if (Byte.class.isAssignableFrom(t)) r = castUnchecked(0);
			else if (Short.class.isAssignableFrom(t)) r = castUnchecked(0);
		}
		else if (Boolean.class.isAssignableFrom(t)) r = castUnchecked(false);
		else if (Character.class.isAssignableFrom(t)) r = castUnchecked(0);
		return requireNonNull(r);
	}

	@SuppressWarnings("SameReturnValue")
	@Nullable
	public static <T> T markUnused() { return null; }


	/* SECTION static classes */

	@SideOnly(Side.CLIENT)
	public enum Client {
		/* MARK empty */ ;


		/* SECTION static variables */

		public static final Minecraft GAME = Minecraft.getMinecraft();

		private static ScaledResolution resolution = new ScaledResolution(GAME);


		/* SECTION static methods */

		public static ScaledResolution getResolution() { return resolution; }


		@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
		public static void initGui(GuiScreenEvent.InitGuiEvent e) { resolution = new ScaledResolution(GAME); }
	}


	public enum ResourceLocations {
		/* MARK empty */ ;


		/* SECTION static variables */

		public static final ResourceLocation GUI_WRENCH = new ResourceLocationDefault("textures/gui/containers/wrench.png");
	}
}
