package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LoopUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.ClassUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.StackTraceUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
import org.jetbrains.annotations.NonNls;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;
import org.slf4j.Marker;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.objectweb.asm.Opcodes.*;

public enum MapUtilities {
	;

	private static final Marker CLASS_MARKER = UtilitiesMarkers.getInstance().getClassMarker();
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	public static <K, V> ImmutableMap<K, V> stitchKeysValues(int size, Iterable<? extends K> keys, Iterable<? extends V> values) {
		Map<K, V> ret = new HashMap<>(size);

		Iterator<? extends V> iteratorValues = values.iterator();
		keys.forEach(k -> {
			if (!iteratorValues.hasNext())
				throw new IllegalArgumentException(
						new LogMessageBuilder()
								.addMarkers(MapUtilities::getClassMarker)
								.addKeyValue("size", size).addKeyValue("keys", keys).addKeyValue("values", values)
								.addMessages(() -> getResourceBundle().getString("stitch.keys.long"))
								.build()
				);
			ret.put(k, iteratorValues.next());
		});
		if (iteratorValues.hasNext())
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(MapUtilities::getClassMarker)
							.addKeyValue("size", size).addKeyValue("keys", keys).addKeyValue("values", values)
							.addMessages(() -> getResourceBundle().getString("stitch.values.long"))
							.build()
			);

		return ImmutableMap.copyOf(ret);
	}

	public static Marker getClassMarker() { return CLASS_MARKER; }

	@SafeVarargs
	public static <K, V> ImmutableMap<K, V> concatMaps(Map<? extends K, ? extends V>... maps) {
		List<Iterable<? extends K>> keys = new ArrayList<>(maps.length);
		List<Iterable<? extends V>> values = new ArrayList<>(maps.length);
		int size = 0;
		for (Map<? extends K, ? extends V> map : maps) {
			Collection<? extends K> mk = map.keySet();
			size += mk.size();
			keys.add(mk);
			values.add(map.values());
		}
		return stitchKeysValues(size, Iterables.concat(keys), Iterables.concat(values));
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@SuppressWarnings("UnstableApiUsage")
	public static <K, V> ImmutableMap<V, K> inverse(Map<K, V> instance)
			throws IllegalArgumentException {
		return instance.entrySet().stream().sequential()
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getValue, Map.Entry::getKey));
	}

	@SuppressWarnings("unchecked")
	public static <K, V> BiMap<K, V> newBiMap(Map<K, V> forward, Map<V, K> backward) {
		return (BiMap<K, V>) PublicAbstractBiMapHolder.CONSTRUCTOR.apply(forward, backward); // COMMENT should be safe
	}

	@SuppressWarnings("UnstableApiUsage")
	public static <K> ImmutableMap<K, ?> immutableDeepCopyOf(Map<K, ?> instance) {
		return instance.entrySet().stream().sequential()
				.map(entry -> {
					@Nullable Object value = entry.getValue();
					return Maps.immutableEntry(entry.getKey(), value instanceof Map ? immutableDeepCopyOf(((Map<?, ?>) value)) : value);
				})
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public enum PublicAbstractBiMapHolder {
		;

		private static final long serialVersionUID = 3864726593737949225L; // COMMENT should not affect Enum serialization
		private static final String SUPERCLASS_NAME = "com.google.common.collect.AbstractBiMap";
		private static final @NonNls String CLASS_SIMPLE_NAME = "AbstractBiMap_" + serialVersionUID; // COMMENT hopefully they will not name a new class like so...
		private static final Pattern LITERAL_SUPERCLASS_SIMPLE_NAME = Pattern.compile("AbstractBiMap", Pattern.LITERAL);
		private static final @NonNls String SOURCE_DEBUG = "ASM";
		private static final Class<?> CLASS;
		private static final BiFunction<Map<?, ?>, Map<?, ?>, ? extends BiMap<?, ?>> CONSTRUCTOR;

		static {
			{
				Class<?> superclass;
				try {
					superclass = Class.forName(SUPERCLASS_NAME);
				} catch (ClassNotFoundException e) {
					throw ThrowableUtilities.propagate(e);
				}
				String name = LITERAL_SUPERCLASS_SIMPLE_NAME
						.matcher(superclass.getName())
						.replaceAll(Matcher.quoteReplacement(CLASS_SIMPLE_NAME));
				String internalName = LITERAL_SUPERCLASS_SIMPLE_NAME
						.matcher(Type.getInternalName(superclass))
						.replaceAll(Matcher.quoteReplacement(CLASS_SIMPLE_NAME));

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
					cw.visit(V1_6,
							ACC_PUBLIC | ACC_SUPER, internalName,
							sw.toString(),
							Type.getInternalName(superclass),
							null);
					cw.visitSource(StackTraceUtilities.getCurrentStackTraceElement()
							.map(StackTraceElement::getFileName)
							.orElse(null), SOURCE_DEBUG);
				}
				// COMMENT field serialVersionUID
				{
					// CODE private static final long serialVersionUID = ...;
					cw.visitField(ACC_PRIVATE | ACC_STATIC | ACC_FINAL, "serialVersionUID",
							Type.LONG_TYPE.getDescriptor(),
							null,
							serialVersionUID)
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
						mv = cw.visitMethod(ACC_PUBLIC, "<init>",
								Type.getMethodDescriptor(Type.getType(void.class), Type.getType(Map.class), Type.getType(Map.class)),
								sw.toString(),
								null);
					}
					mv.visitCode();
					mv.visitVarInsn(ALOAD, 0); // CODE this
					mv.visitVarInsn(ALOAD, 1); // CODE forward
					mv.visitVarInsn(ALOAD, 2); // CODE backward
					// CODE super(forward, backward);
					mv.visitMethodInsn(INVOKESPECIAL,
							Type.getInternalName(superclass),
							"<init>",
							Type.getMethodDescriptor(Type.getType(void.class), Type.getType(Map.class), Type.getType(Map.class)),
							false);
					mv.visitInsn(RETURN);
					mv.visitMaxs(3, 3);
					mv.visitEnd();
				}
				cw.visitEnd();

				CLASS = ClassUtilities.defineClass(superclass.getClassLoader(), name, cw.toByteArray());
			}

			{
				MethodHandle constructor;
				try {
					constructor = InvokeUtilities.IMPL_LOOKUP.findConstructor(CLASS, MethodType.methodType(void.class, Map.class, Map.class));
				} catch (NoSuchMethodException | IllegalAccessException e) {
					throw ThrowableUtilities.propagate(e);
				}
				// TODO Java 9 - use LambdaMetaFactory
				CONSTRUCTOR = (forward, backward) -> {
					try {
						return (BiMap<?, ?>) constructor.invoke(forward, backward);
					} catch (Throwable throwable) {
						throw ThrowableUtilities.propagate(throwable);
					}
				};
			}
		}
	}
}
