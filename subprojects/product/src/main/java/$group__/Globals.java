package $group__;

import $group__.client.gui.coordinates.XY;
import $group__.client.gui.polygons.Rectangle;
import $group__.client.utilities.ResourceLocationTexture;
import $group__.logging.ILogging;
import $group__.utilities.Constants;
import $group__.utilities.concurrent.MutatorImmutable;
import $group__.utilities.helpers.specific.MapsExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

import static $group__.Constants.NAME;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;

public enum Globals {
	/* MARK empty */;


	public static final ModThis MOD = ModThis.INSTANCE;
	public static final Logger LOGGER = LogManager.getLogger(NAME + '|' + Constants.MOD_ID);


	@SideOnly(Side.CLIENT)
	@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Constants.MOD_ID)
	public enum Client {
		/* MARK empty */;


		public static final Minecraft CLIENT = Minecraft.getMinecraft();
		private static final ConcurrentMap<Object, BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ?>> PRE_INIT_GUI_LISTENER_MAP = MapsExtension.MAP_MAKER_SINGLE_THREAD_WEAK_KEYS.makeMap();
		private static ScaledResolution resolution = new ScaledResolution(CLIENT);


		public static ScaledResolution getResolution() { return resolution; }


		public static <T> void registerPreInitGuiListener(T k, BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ?
				super T> v) { PRE_INIT_GUI_LISTENER_MAP.put(k, v); }


		@SubscribeEvent
		public static void preInitGui(GuiScreenEvent.InitGuiEvent.Pre e) { PRE_INIT_GUI_LISTENER_MAP.forEach((o, f) -> f.accept(e, castUncheckedUnboxedNonnull(o))); }

		@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
		public static void preInitGuiForResolution(GuiScreenEvent.InitGuiEvent.Pre e) { resolution = new ScaledResolution(CLIENT); }


		@SuppressWarnings("HardcodedFileSeparator")
		public enum Resources {
			/* MARK empty */;


			public static final ResourceLocationTexture GUI_WRENCH = new ResourceLocationTexture(new ResourceLocation(MOD_ID, "textures/gui/containers/wrench.png"), new XY<>(1024F, 1024F, MutatorImmutable.INSTANCE,
					ILogging.of(LOGGER, MutatorImmutable.INSTANCE)));
			public static final Rectangle<?, Float> GUI_WRENCH_INFO = GUI_WRENCH.makeRectangle(new XY<>(-64.0f, 0F, MutatorImmutable.INSTANCE, ILogging.of(LOGGER, MutatorImmutable.INSTANCE)));
		}
	}
}
