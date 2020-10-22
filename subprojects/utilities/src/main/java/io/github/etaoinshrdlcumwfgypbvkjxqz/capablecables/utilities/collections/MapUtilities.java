package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LoopUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.ClassUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.StackTraceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import org.jetbrains.annotations.NonNls;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;
import org.slf4j.Marker;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MapUtilities {
	;

	private static final Marker CLASS_MARKER = UtilitiesMarkers.getInstance().getClassMarker();
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	@SafeVarargs
	@SuppressWarnings("varargs")
	public static <K, V> ImmutableMap<K, V> concatMaps(Map<? extends K, ? extends V>... maps) {
		return concatMaps(Arrays.asList(maps)); // COMMENT reuse the array
	}

	public static <K, V> ImmutableMap<K, V> concatMaps(Iterable<? extends Map<? extends K, ? extends V>> maps) {
		ImmutableList.Builder<Iterable<? extends K>> keys = ImmutableList.builder();
		ImmutableList.Builder<Iterable<? extends V>> values = ImmutableList.builder();
		maps.forEach(map -> {
			keys.add(map.keySet());
			values.add(map.values());
		});
		return zipKeysValues(Iterables.concat(keys.build()), Iterables.concat(values.build()));
	}

	public static <K, V> ImmutableMap<K, V> zipKeysValues(Iterable<? extends K> keys, Iterable<? extends V> values) {
		ImmutableMap.Builder<K, V> ret = ImmutableMap.builder();

		Iterator<? extends V> iteratorValues = values.iterator();
		keys.forEach(k -> {
			if (!iteratorValues.hasNext())
				throw new IllegalArgumentException(
						new LogMessageBuilder()
								.addMarkers(MapUtilities::getClassMarker)
								.addKeyValue("keys", keys).addKeyValue("values", values)
								.addMessages(() -> getResourceBundle().getString("stitch.keys.long"))
								.build()
				);
			ret.put(k, iteratorValues.next());
		});
		if (iteratorValues.hasNext())
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(MapUtilities::getClassMarker)
							.addKeyValue("keys", keys).addKeyValue("values", values)
							.addMessages(() -> getResourceBundle().getString("stitch.values.long"))
							.build()
			);

		return ret.build();
	}

	public static Marker getClassMarker() { return CLASS_MARKER; }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@SuppressWarnings("UnstableApiUsage")
	public static <K, V> ImmutableMap<V, K> inverse(Map<K, V> instance)
			throws IllegalArgumentException {
		return instance.entrySet().stream()
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getValue, Map.Entry::getKey));
	}

	@SuppressWarnings("unchecked")
	public static <K, V> BiMap<K, V> newBiMap(Map<K, V> forward, Map<V, K> backward) {
		return (BiMap<K, V>) PublicAbstractBiMapHolder.getConstructor().apply(forward, backward); // COMMENT should be safe
	}

	@SuppressWarnings("UnstableApiUsage")
	public static <K> ImmutableMap<K, ?> immutableDeepCopyOf(Map<K, ?> instance) {
		return instance.entrySet().stream()
				.map(entry -> {
					@Nullable Object value = entry.getValue();
					return Maps.immutableEntry(entry.getKey(), value instanceof Map ? immutableDeepCopyOf(((Map<?, ?>) value)) : value);
				})
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public enum PublicAbstractBiMapHolder {
		;

		private static final long serialVersionUID = 3864726593737949225L; // COMMENT should not affect Enum serialization
		public static final String SUPERCLASS_NAME = "com.google.common.collect.AbstractBiMap";
		public static final @NonNls String CLASS_SIMPLE_NAME = "AbstractBiMap_" + serialVersionUID; // COMMENT hopefully they will not name a new class like so...
		private static final Pattern LITERAL_SUPERCLASS_SIMPLE_NAME = Pattern.compile("AbstractBiMap", Pattern.LITERAL);
		public static final @NonNls String SOURCE_DEBUG = "ASM";
		private static final Class<?> CLAZZ;
		private static final BiFunction<Map<?, ?>, Map<?, ?>, ? extends BiMap<?, ?>> CONSTRUCTOR;

		static {
			{
				Class<?> superclass;
				try {
					superclass = Class.forName(getSuperclassName());
				} catch (ClassNotFoundException e) {
					throw ThrowableUtilities.propagate(e);
				}
				String name = getLiteralSuperclassSimpleName()
						.matcher(superclass.getName())
						.replaceAll(Matcher.quoteReplacement(getClassSimpleName()));
				String internalName = getLiteralSuperclassSimpleName()
						.matcher(Type.getInternalName(superclass))
						.replaceAll(Matcher.quoteReplacement(getClassSimpleName()));

				ClassWriter cw = new ClassWriter(0);
				// COMMENT class
				{
					SignatureVisitor sw = new SignatureWriter();
					{
						// CODE <K, V>
						{
							sw.visitFormalTypeParameter("K");
							SignatureVisitor swB = sw.visitClassBound();
							swB.visitClassType(Type.getInternalName(Object.class));
							swB.visitEnd();
						}
						{
							sw.visitFormalTypeParameter("V");
							SignatureVisitor swB = sw.visitClassBound();
							swB.visitClassType(Type.getInternalName(Object.class));
							swB.visitEnd();
						}
					}
					{
						// CODE extends ...<K, V>
						SignatureVisitor swSc = sw.visitSuperclass();
						swSc.visitClassType(Type.getInternalName(superclass));
						swSc.visitTypeArgument(SignatureVisitor.INSTANCEOF).visitTypeVariable("K");
						swSc.visitTypeArgument(SignatureVisitor.INSTANCEOF).visitTypeVariable("V");
						swSc.visitEnd();
					}

					// CODE package ...;
					// CODE public class ...
					cw.visit(Opcodes.V1_6,
							Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, internalName,
							sw.toString(),
							Type.getInternalName(superclass),
							null);
					cw.visitSource(StackTraceUtilities.getCurrentStackTraceElement()
							.map(StackTraceElement::getFileName)
							.orElse(null), getSourceDebug());
				}
				// COMMENT field serialVersionUID
				{
					// CODE private static final long serialVersionUID = ...;
					cw.visitField(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL, "serialVersionUID",
							Type.LONG_TYPE.getDescriptor(),
							null,
							getSerialVersionUID())
							.visitEnd();
				}
				// COMMENT method <init>
				{
					MethodVisitor mv;
					{
						SignatureVisitor sw = new SignatureWriter();
						// CODE (Map<K, V> forward, Map<K, V> backward)
						LoopUtilities.doNTimes(2, () -> {
							SignatureVisitor swPt = sw.visitParameterType();
							{
								swPt.visitClassType(Type.getInternalName(Map.class));
								swPt.visitTypeArgument(SignatureVisitor.INSTANCEOF).visitTypeVariable("K");
								swPt.visitTypeArgument(SignatureVisitor.INSTANCEOF).visitTypeVariable("V");
								swPt.visitEnd();
							}
						});
						{
							// COMMENT return type
							SignatureVisitor swRt = sw.visitReturnType();
							swRt.visitClassType(Type.getInternalName(void.class));
							swRt.visitEnd();
						}

						// CODE public <init>
						mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>",
								Type.getMethodDescriptor(Type.getType(void.class), Type.getType(Map.class), Type.getType(Map.class)),
								sw.toString(),
								null);
					}
					mv.visitCode();
					mv.visitVarInsn(Opcodes.ALOAD, 0); // CODE this
					mv.visitVarInsn(Opcodes.ALOAD, 1); // CODE forward
					mv.visitVarInsn(Opcodes.ALOAD, 2); // CODE backward
					// CODE super(forward, backward);
					mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
							Type.getInternalName(superclass),
							"<init>",
							Type.getMethodDescriptor(Type.getType(void.class), Type.getType(Map.class), Type.getType(Map.class)),
							false);
					mv.visitInsn(Opcodes.RETURN);
					mv.visitMaxs(3, 3);
					mv.visitEnd();
				}
				cw.visitEnd();

				CLAZZ = ClassUtilities.defineClass(superclass.getClassLoader(), name, cw.toByteArray());
			}

			{
				MethodHandle invokeHandle;
				try {
					invokeHandle = InvokeUtilities.getImplLookup().findConstructor(getClazz(), MethodType.methodType(void.class, Map.class, Map.class));
				} catch (NoSuchMethodException | IllegalAccessException e) {
					throw ThrowableUtilities.propagate(e);
				}
				// TODO Java 9 - use LambdaMetaFactory
				CONSTRUCTOR = (forward, backward) -> {
					try {
						return (BiMap<?, ?>) invokeHandle.invoke(forward, backward);
					} catch (Throwable throwable) {
						throw ThrowableUtilities.propagate(throwable);
					}
				};
			}
		}

		public static String getSuperclassName() {
			return SUPERCLASS_NAME;
		}

		public static String getClassSimpleName() {
			return CLASS_SIMPLE_NAME;
		}

		private static Pattern getLiteralSuperclassSimpleName() {
			return LITERAL_SUPERCLASS_SIMPLE_NAME;
		}

		public static String getSourceDebug() {
			return SOURCE_DEBUG;
		}

		private static Class<?> getClazz() {
			return CLAZZ;
		}

		private static BiFunction<Map<?, ?>, Map<?, ?>, ? extends BiMap<?, ?>> getConstructor() {
			return CONSTRUCTOR;
		}

		private static long getSerialVersionUID() {
			return serialVersionUID;
		}
	}
}
