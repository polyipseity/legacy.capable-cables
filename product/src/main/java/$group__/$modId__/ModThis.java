package $group__.$modId__;

import $group__.$modId__.annotations.Marker;
import $group__.$modId__.annotations.Property;
import $group__.$modId__.client.ProxyClient;
import $group__.$modId__.client.configurations.ModGuiFactoryThis;
import $group__.$modId__.common.ModContainerNull;
import $group__.$modId__.common.registrables.items.ItemWrench;
import $group__.$modId__.proxies.Proxy;
import $group__.$modId__.proxies.ProxyNull;
import $group__.$modId__.server.ProxyServer;
import $group__.$modId__.utilities.helpers.specific.Throwables;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;

import static $group__.$modId__.utilities.Constants.MOD_ID;
import static $group__.$modId__.utilities.Constants.PACKAGE;
import static $group__.$modId__.utilities.Singleton.getSingletonInstance;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Dynamics.Invocations.Fields.FIELD_MODIFIERS_SETTER;
import static $group__.$modId__.utilities.helpers.Dynamics.*;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.*;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;
import static $group__.$modId__.Constants.*;
import static $group__.$modId__.Globals.LOGGER;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static net.minecraftforge.fml.common.Mod.EventHandler;
import static net.minecraftforge.fml.common.Mod.InstanceFactory;

@Mod(
		modid = MOD_ID,
		name = NAME,
		version = VERSION,
		dependencies = DEPENDENCIES,
		useMetadata = true,
		acceptedMinecraftVersions = ACCEPTED_MINECRAFT_VERSIONS,
		certificateFingerprint = CERTIFICATE_FINGERPRINT,
		modLanguageAdapter = PACKAGE + '.' + ModThis.CLASS_SIMPLE_NAME + '$' + ModThis.CustomLanguageAdapter.CLASS_SIMPLE_NAME,
		guiFactory = PACKAGE + '.' + ModGuiFactoryThis.SUBPACKAGE + '.' + ModGuiFactoryThis.CLASS_SIMPLE_NAME,
		updateJSON = UPDATE_JSON
)
public enum ModThis {
	/* SECTION enums */
	INSTANCE;


	/* SECTION static variables */

	public static final String
			CLASS_SIMPLE_NAME = "ModThis",
			EVENT_PROCESSOR_VALUE = "event processor",
			CONTAINER_VALUE = "container";
	@Marker(@Property(key = "Field", value = CONTAINER_VALUE))
	public static final ModContainer CONTAINER = getSingletonInstance(ModContainerNull.class, LOGGER);
	@SidedProxy(modId = MOD_ID,
			clientSide = PACKAGE + '.' + Proxy.SUBPACKAGE + '.' + ProxyClient.CLASS_SIMPLE_NAME,
			serverSide = PACKAGE + '.' + Proxy.SUBPACKAGE + '.' + ProxyServer.CLASS_SIMPLE_NAME)
	public static final Proxy PROXY = getSingletonInstance(ProxyNull.class, LOGGER);
	private static final ImmutableMap<Class<? extends FMLEvent>, BiConsumer<?, ? extends FMLEvent>> EVENT_MAP = new ImmutableMap.Builder<Class<? extends FMLEvent>, BiConsumer<?, ? extends FMLEvent>>()
			.put(FMLConstructionEvent.class, (ModThis m, FMLConstructionEvent e) ->
					processEvent("Construction", m, e, PROXY::construct))
			.put(FMLPreInitializationEvent.class, (ModThis m, FMLPreInitializationEvent e) ->
					processEvent("Pre-initialization", m, e, PROXY::preInitialize))
			.put(FMLInitializationEvent.class, (ModThis m, FMLInitializationEvent e) ->
					processEvent("Initialization", m, e, PROXY::initialize))
			.put(FMLPostInitializationEvent.class, (ModThis m, FMLPostInitializationEvent e) ->
					processEvent("Post-initialization", m, e, PROXY::postInitialize))
			.put(FMLLoadCompleteEvent.class, (ModThis m, FMLLoadCompleteEvent e) ->
					processEvent("Load completion", m, e, PROXY::completeLoading))
			.put(FMLServerAboutToStartEvent.class, (ModThis m, FMLServerAboutToStartEvent e) ->
					processEvent("Server pre-starting", m, e, PROXY::preStartServer))
			.put(FMLServerStartingEvent.class, (ModThis m, FMLServerStartingEvent e) ->
					processEvent("Server starting", m, e, PROXY::startServer))
			.put(FMLServerStartedEvent.class, (ModThis m, FMLServerStartedEvent e) ->
					processEvent("Server post-starting", m, e, PROXY::postStartServer))
			.put(FMLServerStoppingEvent.class, (ModThis m, FMLServerStoppingEvent e) ->
					processEvent("Server stopping", m, e, PROXY::stopServer))
			.put(FMLServerStoppedEvent.class, (ModThis m, FMLServerStoppedEvent e) ->
					processEvent("Server post-stopping", m, e, PROXY::postStopServer))
			.put(FMLFingerprintViolationEvent.class, (ModThis m, FMLFingerprintViolationEvent e) ->
					processEvent("Fingerprint violation", m, e, PROXY::violateFingerprint))
			.put(FMLInterModComms.IMCEvent.class, (ModThis m, FMLInterModComms.IMCEvent e) ->
					processEvent("Inter-mod communication messages processing", m, e, PROXY::processIMCMessages))
			.put(FMLModIdMappingEvent.class, (ModThis m, FMLModIdMappingEvent e) ->
					processEvent("ID mapping processing", m, e, PROXY::processIDMapping))
			.put(FMLModDisabledEvent.class, (ModThis m, FMLModDisabledEvent e) ->
					processEvent("Disabling", m, e, PROXY::disable)).build();


	/* SECTION static getters & setters */

	@SuppressWarnings("SameReturnValue")
	@InstanceFactory
	@Deprecated
	public static ModThis getInstance() { return INSTANCE; }


	/* SECTION static methods */

	private static <M, E> void processEvent(String name, M mod, E event, BiConsumer<? super M, ? super E> process) {
		LOGGER.info(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("{} started", name));
		Stopwatch stopwatch = Stopwatch.createStarted();
		process.accept(mod, event);
		LOGGER.info(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("{} ended ({} <- {} ns)", name, stopwatch.stop(), stopwatch.elapsed(NANOSECONDS)));
	}


	/* SECTION methods */

	@Marker(@Property(key = "Method", value = EVENT_PROCESSOR_VALUE))
	@EventHandler
	public void processEvent(FMLEvent event) { EVENT_MAP.getOrDefault(event.getClass(), (m, e) -> LOGGER.info(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("FMLEvent '{}' received, but processing is NOT implemented", e))).accept(castUncheckedUnboxedNonnull(this), castUncheckedUnboxedNonnull(event)); }


	/* SECTION static classes */

	@Config(modid = MOD_ID, name = NAME, category = "General")
	@Config.LangKey(Configuration.LANG_KEY_BASE + ".name")
	public enum Configuration {
		;
		@Config.Ignore
		public static final String LANG_KEY_BASE = "configurations." + MOD_ID;
		@Config.LangKey(Behavior.LANG_KEY_BASE + ".name")
		public static final Behavior behavior = new Behavior();
		@SuppressWarnings("unused")
		@Config.Comment({"1st line", "2nd line"})
		@Config.Name("Template") // COMMENT overridden by @Config.LangKey
		@Config.LangKey("template.template.template_") // COMMENT overrides @Config.Name
		@Config.RequiresMcRestart
		@Config.RequiresWorldRestart
		@Config.Ignore
		private static final String TEMPLATE_ = "default";
		@SuppressWarnings("unused")
		@Config.RangeInt(min = -1337, max = 1337)
		@Config.Ignore
		private static final int INT_TEMPLATE_ = 69;
		@SuppressWarnings("unused")
		@Config.RangeDouble(min = -13.37D, max = 13.37D)
		@Config.Ignore
		private static final double DOUBLE_TEMPLATE_ = 6.9D;

		public static final class Behavior {
			public static final String LANG_KEY_BASE = Configuration.LANG_KEY_BASE + ".behavior";
			@Config.LangKey(Items.LANG_KEY_BASE + ".name")
			public final Items items = new Items();

			public static final class Items {
				public static final String LANG_KEY_BASE = Behavior.LANG_KEY_BASE + ".items";
				@Config.LangKey(ItemWrench.Configuration.LANG_KEY_BASE + ".name")
				public final ItemWrench.Configuration wrench = new ItemWrench.Configuration();
			}
		}
	}

	public static class CustomLanguageAdapter extends ILanguageAdapter.JavaAdapter {
		/* SECTION static variables */

		public static final String CLASS_SIMPLE_NAME = "CustomLanguageAdapter";


		/* SECTION methods */

		@Override
		public Object getNewInstance(FMLModContainer container, Class<?> objectClass, ClassLoader classLoader, Method factoryMarkedMethod) throws Exception {
			Object r = super.getNewInstance(container, objectClass, classLoader, factoryMarkedMethod);

			// COMMENT steal container
			for (Field f : objectClass.getDeclaredFields()) {
				if (isMemberStatic(f)) {
					Marker m = f.getDeclaredAnnotation(Marker.class);
					if (m != null) {
						Property[] mps = m.value();
						if (mps.length == 1) {
							Property mp = mps[0];
							Class<? extends Field> fc = f.getClass();
							if (mp.key().equals(fc.getSimpleName()) && mp.value().equals(CONTAINER_VALUE)) {
								tryRun(() -> f.setAccessible(true), LOGGER);
								consumeIfCaughtThrowable(t -> LOGGER.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(REFLECTION_UNABLE_TO_SET_ACCESSIBLE.makeMessage("container field", f, objectClass, true), t)));

								int fMod = f.getModifiers() & ~Modifier.FINAL;
								tryRun(() -> FIELD_MODIFIERS_SETTER.invokeExact(f, fMod), LOGGER);
								consumeIfCaughtThrowable(t -> LOGGER.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_INVOKE_METHOD_HANDLE.makeMessage(FIELD_MODIFIERS_SETTER, f.toGenericString(), fMod), t)));

								tryRun(() -> f.set(null, container), LOGGER);
								rethrowCaughtThrowableStatic(true);
								break;
							}
						}
					}
				}
			}

			// COMMENT modify event methods
			for (Method m : objectClass.getDeclaredMethods()) {
				if (!isMemberStatic(m)) {
					Marker mm = m.getDeclaredAnnotation(Marker.class);
					if (mm != null && m.getDeclaredAnnotation(EventHandler.class) != null) {
						Property[] mmPs = mm.value();
						if (mmPs.length == 1) {
							Property mp = mmPs[0];
							if (mp.key().equals(m.getClass().getSimpleName()) && mp.value().equals(EVENT_PROCESSOR_VALUE)) {
								Class<? extends FMLModContainer> containerC = container.getClass();
								for (Field f : containerC.getDeclaredFields()) {
									if (ListMultimap.class.isAssignableFrom(f.getType()) && !isMemberStatic(f)) {
										ListMultimap<Class<? extends FMLEvent>, Method> fv;

										f.setAccessible(true);
										rethrowCaughtThrowableStatic(true);

										fv = castUncheckedUnboxedNonnull(tryCall(() -> f.get(container), LOGGER).orElseThrow(Throwables::rethrowCaughtThrowableStatic));
										REFLECTIONS_CACHE.get(getPackage(FMLEvent.class)).getSubTypesOf(FMLEvent.class).forEach(t -> fv.put(t, m));

										break;
									}
								}
								break;
							}
						}
					}
				}
			}

			return r;
		}

		@Override
		public void setProxy(Field target, Class<?> proxyTarget, Object proxy) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
			int targetMod = target.getModifiers() & ~Modifier.FINAL;
			tryRun(() -> FIELD_MODIFIERS_SETTER.invokeExact(target, targetMod), LOGGER);
			consumeIfCaughtThrowable(t -> LOGGER.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_INVOKE_METHOD_HANDLE.makeMessage(FIELD_MODIFIERS_SETTER, target.toGenericString(), targetMod), t)));

			super.setProxy(target, proxyTarget, proxy);
		}
	}
}
