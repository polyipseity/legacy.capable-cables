package $group__.$modId__

import java.lang.reflect.{Field, Method, Modifier}
import java.util.concurrent.TimeUnit.NANOSECONDS

import $group__.$modId__.compat.convert.JavaConversionsExtension.asJavaLog4jSupplier
import $group__.$modId__.macros.MacroTypeNames.getTypeFullName
import $group__.$modId__.server.ProxyServer
import com.google.common.base.Stopwatch
import com.google.common.collect.ListMultimap
import net.minecraftforge.fml.common._

//noinspection JavaMutatorMethodAccessedAsParameterless
@Mod(
	modid = MOD_ID,
	name = NAME,
	version = VERSION,
	dependencies = DEPENDENCIES,
	useMetadata = true,
	acceptedMinecraftVersions = ACCEPTED_MINECRAFT_VERSIONS,
	certificateFingerprint = CERTIFICATE_FINGERPRINT,
	modLanguageAdapter = getTypeFullName[ModThis.CustomLanguageAdapter],
	guiFactory = getTypeFullName[ModGuiFactoryThis],
	updateJSON = UPDATE_JSON
)
object ModThis {
	val CLASS_SIMPLE_NAME = "ModThis"
	val EVENT_PROCESSOR_VALUE = "event processor"
	val CONTAINER_VALUE = "container"
	@Marker(Array(new Property(key = "Field", value = CONTAINER_VALUE)))
	val CONTAINER: ModContainer = getSingletonInstance(classOf[ModContainerNull], LOGGER)
	@SidedProxy(
		modId = MOD_ID,
		clientSide = getTypeFullName[ProxyClient],
		serverSide = getTypeFullName[ProxyServer.type]
	)
	val PROXY: Proxy = getSingletonInstance(classOf[ProxyNull], LOGGER)
	private val EVENT_MAP = Map(
		classOf[FMLConstructionEvent] -> ((m: ModThis.type, e: FMLConstructionEvent) => processEvent("Construction", m, e, PROXY.construct _)),
		classOf[FMLPreInitializationEvent] -> ((m: ModThis.type, e: FMLPreInitializationEvent) => processEvent("Pre-initialization", m, e, PROXY.preInitialize _)),
		classOf[FMLInitializationEvent] -> ((m: ModThis.type, e: FMLInitializationEvent) => processEvent("Initialization", m, e, PROXY.initialize _)),
		classOf[FMLPostInitializationEvent] -> ((m: ModThis.type, e: FMLPostInitializationEvent) => processEvent("Post-initialization", m, e, PROXY.postInitialize _)),
		classOf[FMLLoadCompleteEvent] -> ((m: ModThis.type, e: FMLLoadCompleteEvent) => processEvent("Load completion", m, e, PROXY.completeLoading _)),
		classOf[FMLServerAboutToStartEvent] -> ((m: ModThis.type, e: FMLServerAboutToStartEvent) => processEvent("Server pre-starting", m, e, PROXY.preStartServer _)),
		classOf[FMLServerStartingEvent] -> ((m: ModThis.type, e: FMLServerStartingEvent) => processEvent("Server starting", m, e, PROXY.startServer _)),
		classOf[FMLServerStartedEvent] -> ((m: ModThis.type, e: FMLServerStartedEvent) => processEvent("Server post-starting", m, e, PROXY.postStartServer _)),
		classOf[FMLServerStoppingEvent] -> ((m: ModThis.type, e: FMLServerStoppingEvent) => processEvent("Server stopping", m, e, PROXY.stopServer _)),
		classOf[FMLServerStoppedEvent] -> ((m: ModThis.type, e: FMLServerStoppedEvent) => processEvent("Server post-stopping", m, e, PROXY.postStopServer _)),
		classOf[FMLFingerprintViolationEvent] -> ((m: ModThis.type, e: FMLFingerprintViolationEvent) => processEvent("Fingerprint violation", m, e, PROXY.violateFingerprint _)),
		classOf[FMLInterModComms.IMCEvent] -> ((m: ModThis.type, e: FMLInterModComms.IMCEvent) => processEvent("Inter-mod communication messages processing", m, e, PROXY.processIMCMessages _)),
		classOf[FMLModIdMappingEvent] -> ((m: ModThis.type, e: FMLModIdMappingEvent) => processEvent("ID mapping processing", m, e, PROXY.processIDMapping _)),
		classOf[FMLModDisabledEvent] -> ((m: ModThis.type, e: FMLModDisabledEvent) => processEvent("Disabling", m, e, PROXY.disable _))
	)

	@InstanceFactory
	@deprecated(message = "use this", since = "0.0.1")
	def instance: ModThis.type = this

	@Marker(Array(new Property(key = "Method", value = EVENT_PROCESSOR_VALUE)))
	@EventHandler
	def processEvent(event: FMLEvent): Unit = {
		EVENT_MAP.getOrElse(event.getClass, (_: Any, e: FMLEvent) => LOGGER.info(asJavaLog4jSupplier(() => FACTORY_PARAMETERIZED_MESSAGE.makeMessage("FMLEvent '{}' received, but " + "processing is NOT implemented", e))))(this, castUncheckedUnboxedNonnull(event))
	}

	private def processEvent[M, E](name: String, mod: M, event: E, process: (_ >: M, _ >: E) => Unit): Unit = {
		LOGGER.info(asJavaLog4jSupplier(() => FACTORY_PARAMETERIZED_MESSAGE.makeMessage("{} started", name)))
		val stopwatch = Stopwatch.createStarted
		process(mod, event)
		LOGGER.info(asJavaLog4jSupplier(() => FACTORY_PARAMETERIZED_MESSAGE.makeMessage("{} ended ({} <- {} ns)", name, stopwatch.stop, stopwatch.elapsed(NANOSECONDS))))
	}

	class CustomLanguageAdapter extends ILanguageAdapter.ScalaAdapter {

		import $group__.$modId__.compat.convert.JavaConvertersExtension._

		import scala.compat.java8.FunctionConverters._

		@throws[Exception]
		override def getNewInstance(container: FMLModContainer, objectClass: Class[_], classLoader: ClassLoader, factoryMarkedMethod: Method): AnyRef = {
			val r = super.getNewInstance(container, objectClass, classLoader, factoryMarkedMethod)
			// COMMENT steal container
			objectClass.getDeclaredFields.foreach(f => {
				if (isMemberStatic(f)) {
					val m = f.getDeclaredAnnotation(classOf[Marker])
					if (m != null) {
						val mps = m.value
						if (mps.length == 1) {
							val mp = mps(0)
							val fc = f.getClass
							if (mp.key == fc.getSimpleName && mp.value == CONTAINER_VALUE) {
								tryRun((() => f.setAccessible(true)).asJava, LOGGER)
								consumeIfCaughtThrowable(((t: Throwable) => LOGGER.warn(asJavaLog4jSupplier(() => SUFFIX_WITH_THROWABLE.makeMessage(REFLECTION_UNABLE_TO_SET_ACCESSIBLE.makeMessage("container field", f, objectClass, true), t)))).asJava)
								val fMod = f.getModifiers & ~Modifier.FINAL
								tryRun((() => FIELD_MODIFIERS_SETTER.invokeExact(f, fMod).asInstanceOf[Unit]).asJava, LOGGER)
								consumeIfCaughtThrowable(((t: Throwable) => LOGGER.warn(asJavaLog4jSupplier(() => SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_INVOKE_METHOD_HANDLE.makeMessage(FIELD_MODIFIERS_SETTER, f.toGenericString, fMod), t)))).asJava)
								tryRun((() => f.set(null, container)).asJava, LOGGER)
								rethrowCaughtThrowableStatic(true)
								return
							}
						}
					}
				}
			})
			// COMMENT modify event methods
			objectClass.getDeclaredMethods.foreach(m => {
				if (!isMemberStatic(m)) {
					val mm = m.getDeclaredAnnotation(classOf[Marker])
					if (mm != null && m.getDeclaredAnnotation(classOf[Mod.EventHandler]) != null) {
						val mmPs = mm.value
						if (mmPs.length == 1) {
							val mp = mmPs(0)
							if (mp.key == m.getClass.getSimpleName && mp.value == EVENT_PROCESSOR_VALUE) {
								container.getClass.getDeclaredFields.foreach(f => {
									if (classOf[ListMultimap[_, _]].isAssignableFrom(f.getType) && !isMemberStatic(f)) {
										var fv: ListMultimap[Class[_ <: FMLEvent], Method] = null
										f.setAccessible(true)
										rethrowCaughtThrowableStatic(true)
										fv = castUncheckedUnboxedNonnull(tryCall(asJavaCallableConverter(() => f.get(container)).asJava, LOGGER).orElseThrow(asJavaSupplier(() => Throwables.rethrowCaughtThrowableStatic())))
										// REFLECTIONS_CACHE.get(getPackage(classOf[FMLEvent])).getSubTypesOf(classOf[FMLEvent]).forEach((t: Class[_ <: FMLEvent]) => fv.put(t, m))
										return
									}
								})
								return
							}
						}
					}
				}
			})
			r
		}

		@throws[IllegalArgumentException]
		@throws[IllegalAccessException]
		@throws[NoSuchFieldException]
		@throws[SecurityException]
		override def setProxy(target: Field, proxyTarget: Class[_], proxy: Any): Unit = {
			val targetMod = target.getModifiers & ~Modifier.FINAL
			tryRun((() => FIELD_MODIFIERS_SETTER.invokeExact(target, targetMod).asInstanceOf[Unit]).asJava, LOGGER)
			consumeIfCaughtThrowable(((t: Throwable) => LOGGER.warn(asJavaLog4jSupplier(() => SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_INVOKE_METHOD_HANDLE.makeMessage(FIELD_MODIFIERS_SETTER, target.toGenericString, targetMod), t)))).asJava)
			super.setProxy(target, proxyTarget, proxy)
		}
	}

}
