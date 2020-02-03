package $group__.$modId__.utilities.variables;

import $group__.$modId__.ModThis;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.common.registrable.utilities.constructs.ResourceLocationDefault;
import $group__.$modId__.utilities.constructs.interfaces.basic.IThrowableCatcher;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.MapsExtension.SINGLE_THREAD_WEAK_KEY_MAP_MAKER;
import static $group__.$modId__.utilities.helpers.Throwables.consumeCaught;
import static $group__.$modId__.utilities.variables.Constants.MOD_ID;
import static $group__.$modId__.utilities.variables.Constants.MULTI_THREAD_THREAD_COUNT;

public enum Globals implements IThrowableCatcher {
	/* SECTION enums */
	INSTANCE;


	/* SECTION static variables */

	public static final ModThis MOD = ModThis.INSTANCE;
	public static final Logger LOGGER = ModThis.LOGGER;
	public static final LoadingCache<String, Reflections> REFLECTIONS_CACHE = CacheBuilder.newBuilder().softValues().concurrencyLevel(MULTI_THREAD_THREAD_COUNT).build(CacheLoader.from(t -> {
		Reflections r = new Reflections(t);
		r.expandSuperTypes();
		return r;
	}));

	public static final ThreadLocal<Throwable> CAUGHT_THROWABLE = new ThreadLocal<>();


	/* SECTION static methods */

	public static Optional<Throwable> getCaughtThrowableStatic() { return INSTANCE.getCaughtThrowable(); }

	public static void setCaughtThrowableStatic(Throwable t) { INSTANCE.setCaughtThrowable(t); }

	@Nullable
	public static Throwable getCaughtThrowableUnboxedStatic() { return INSTANCE.getCaughtThrowableUnboxed(); }

	public static Throwable getCaughtThrowableUnboxedNonnullStatic() { return INSTANCE.getCaughtThrowableUnboxedNonnull(); }

	public static boolean caughtThrowableStatic() { return INSTANCE.caughtThrowable(); }

	public static void clearCaughtThrowableStatic() { INSTANCE.clearCaughtThrowable(); }

	public static void rethrowCaughtThrowableStatic(boolean nullable) throws RuntimeException { INSTANCE.rethrowCaughtThrowable(nullable); }

	public static RuntimeException rethrowCaughtThrowableStatic() throws RuntimeException { throw INSTANCE.rethrowCaughtThrowable(); }


	/* SECTION methods */

	@Override
	public Optional<Throwable> getCaughtThrowable() { return Optional.ofNullable(CAUGHT_THROWABLE.get()); }

	public void setCaughtThrowable(Throwable t) {
		consumeCaught(t);
		CAUGHT_THROWABLE.set(t);
	}

	@Override
	public void clearCaughtThrowable() { CAUGHT_THROWABLE.set(null); }


	/* SECTION static classes */

	@SideOnly(Side.CLIENT)
	@Mod.EventBusSubscriber(value = Side.CLIENT, modid = MOD_ID)
	public enum Client {
		/* MARK empty */;


		/* SECTION static variables */

		public static final Minecraft CLIENT = Minecraft.getMinecraft();
		private static final ConcurrentMap<Object, BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ?>> PRE_INIT_GUI_LISTENER_MAP = SINGLE_THREAD_WEAK_KEY_MAP_MAKER.makeMap();
		private static ScaledResolution resolution = new ScaledResolution(CLIENT);


		/* SECTION static methods */

		public static ScaledResolution getResolution() { return resolution; }


		public static <T> void registerPreInitGuiListener(T k, BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> v) {
			PRE_INIT_GUI_LISTENER_MAP.put(k, v);
			Thread.currentThread().getUncaughtExceptionHandler();
		}


		@SubscribeEvent
		public static void preInitGui(GuiScreenEvent.InitGuiEvent.Pre e) { PRE_INIT_GUI_LISTENER_MAP.forEach((o, f) -> f.accept(e, castUncheckedUnboxedNonnull(o))); }

		@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
		public static void preInitGuiForResolution(GuiScreenEvent.InitGuiEvent.Pre e) { resolution = new ScaledResolution(CLIENT); }


		/* SECTION static classes */

		@SuppressWarnings("HardcodedFileSeparator")
		public enum Resources {
			/* MARK empty */;


			/* SECTION static variables */

			public static final ResourceLocationDefault.Texture GUI_WRENCH = new ResourceLocationDefault.Texture("textures/gui/containers/wrench.png", 1024F, 1024F);
			public static final Rectangle<Float, ?> GUI_WRENCH_INFO = GUI_WRENCH.generateRect(-64F, 0F);
		}
	}
}
