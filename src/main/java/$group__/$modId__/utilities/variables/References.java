package $group__.$modId__.utilities.variables;

import $group__.$modId__.ModOwn;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.common.registrable.utilities.constructs.ResourceLocationDefault;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static java.util.Objects.requireNonNull;

public enum References {
	/* MARK empty */ ;


	/* SECTION static variables */

	public static final ModOwn MOD = ModOwn.INSTANCE;
	public static final Logger LOGGER = ModOwn.LOGGER;

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

		public static final Minecraft CLIENT = Minecraft.getMinecraft();

		private static final Cache<Object, BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ?>> PRE_INIT_GUI_LISTENER = CacheBuilder.newBuilder().weakKeys().build();


		private static ScaledResolution resolution = new ScaledResolution(CLIENT);


		/* SECTION static methods */

		public static <T> void registerPreInitGuiListener(T k, BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> v) { PRE_INIT_GUI_LISTENER.put(k, v); }


		public static ScaledResolution getResolution() { return resolution; }


		@SubscribeEvent
		public static void preInitGui(GuiScreenEvent.InitGuiEvent.Pre e) { PRE_INIT_GUI_LISTENER.asMap().forEach((k, v) -> v.accept(e, castUnchecked(k))); }

		@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
		public static void preInitGuiForResolution(GuiScreenEvent.InitGuiEvent.Pre e) { resolution = new ScaledResolution(CLIENT); }


		/* SECTION static classes */

		public enum Resources {
			/* MARK empty */ ;


			/* SECTION static variables */

			public static final ResourceLocationDefault.Texture GUI_WRENCH = new ResourceLocationDefault.Texture("textures/gui/containers/wrench.png", 256, 256);
			public static final Rectangle<Float, ?> GUI_WRENCH_INFO = GUI_WRENCH.generateRect(240F, 0F);
		}
	}
}
