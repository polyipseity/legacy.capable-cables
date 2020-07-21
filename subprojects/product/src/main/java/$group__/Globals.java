package $group__;

import $group__.utilities.specific.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

import static $group__.Constants.MOD_ID;
import static $group__.Constants.NAME;

public enum Globals {
	;

	public static final Logger LOGGER = LogManager.getLogger(NAME + '|' + MOD_ID);


	@OnlyIn(Dist.CLIENT)
	@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MOD_ID)
	public enum Client {
		;

		private static final ConcurrentMap<Object, BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ?>> PRE_INIT_GUI_LISTENER_MAP = Maps.MAP_MAKER_SINGLE_THREAD_WEAK_KEYS.makeMap();


		@SuppressWarnings("HardcodedFileSeparator")
		public enum Resources {
			;

			public static final ResourceLocation GUI_WRENCH = ModThis.getResourceLocation("textures/gui/containers/wrench.png");
		}
	}
}
