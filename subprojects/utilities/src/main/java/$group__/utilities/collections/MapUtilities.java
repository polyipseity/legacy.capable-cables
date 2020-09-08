package $group__.utilities.collections;

import $group__.utilities.CastUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.LoopUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;

import java.lang.invoke.MethodType;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static $group__.utilities.ConcurrencyUtilities.NORMAL_THREAD_THREAD_COUNT;
import static $group__.utilities.ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT;
import static org.objectweb.asm.Opcodes.*;

public enum MapUtilities {
	;

	public static final long CACHE_EXPIRATION_ACCESS_DURATION = 15;
	public static final TimeUnit CACHE_EXPIRATION_ACCESS_TIME_UNIT = TimeUnit.MINUTES;

	public static MapMaker newMapMakerSingleThreaded() { return new MapMaker().concurrencyLevel(SINGLE_THREAD_THREAD_COUNT); }

	public static MapMaker newMapMakerNormalThreaded() { return new MapMaker().concurrencyLevel(NORMAL_THREAD_THREAD_COUNT); }

	@SafeVarargs
	public static <K, V> Map<K, V> concatMaps(Map<? extends K, ? extends V>... maps) {
		List<Iterable<? extends K>> keys = new ArrayList<>(maps.length);
		List<Iterable<? extends V>> values = new ArrayList<>(maps.length);
		int size = 0;
		for (Map<? extends K, ? extends V> map : maps) {
			Collection<? extends K> mk = map.keySet();
			size += mk.size();
			keys.add(mk);
			values.add(map.values());
		}
		return stitchIterables(size, Iterables.concat(keys), Iterables.concat(values));
	}

	public static <K, V> Map<K, V> stitchIterables(int size, Iterable<? extends K> keys, Iterable<? extends V> values) {
		Map<K, V> ret = new HashMap<>(size);

		Iterator<? extends V> iteratorValues = values.iterator();
		keys.forEach(k -> {
			if (!iteratorValues.hasNext())
				throw ThrowableUtilities.BecauseOf.illegalArgument("Keys too long", "keys", keys, "values", values);
			ret.put(k, iteratorValues.next());
		});
		if (iteratorValues.hasNext())
			throw ThrowableUtilities.BecauseOf.illegalArgument("Values too long", "keys", keys, "values", values);

		return ret;
	}

	@SuppressWarnings("UnstableApiUsage")
	public static <K, V> Map<V, K> inverse(Map<K, V> instance)
			throws IllegalArgumentException {
		return instance.entrySet().stream().sequential()
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getValue, Map.Entry::getKey));
	}

	@SuppressWarnings("unchecked")
	public static <K, V> BiMap<K, V> newBiMap(Map<K, V> forward, Map<V, K> backward) {
		return (BiMap<K, V>) PublicAbstractBiMapHolder.CONSTRUCTOR.apply(forward, backward); // COMMENT should be safe
	}

	public enum PublicAbstractBiMapHolder {
		;

		private static final String SUPERCLASS_NAME = "com.google.common.collect.AbstractBiMap";
		private static final String CLASS_SIMPLE_NAME = "PublicAbstractBiMap";
		private static final Pattern LITERAL_SUPERCLASS_NAME = Pattern.compile("AbstractBiMap", Pattern.LITERAL);
		private static final Logger LOGGER = LogManager.getLogger();
		private static final Class<?> CLASS;
		private static final BiFunction<Map<?, ?>, Map<?, ?>, ? extends BiMap<?, ?>> CONSTRUCTOR;

		static {
			{
				Class<?> superclass = Try.call(() -> Class.forName(SUPERCLASS_NAME), LOGGER)
						.orElseThrow(ThrowableCatcher::rethrow);
				String name = LITERAL_SUPERCLASS_NAME
						.matcher(superclass.getName())
						.replaceAll(Matcher.quoteReplacement(CLASS_SIMPLE_NAME));
				String internalName = LITERAL_SUPERCLASS_NAME
						.matcher(Type.getInternalName(superclass))
						.replaceAll(Matcher.quoteReplacement(CLASS_SIMPLE_NAME));

				ClassWriter cw = new ClassWriter(0);
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
						// CODE extends AbstractBiMap<K, V>
						SignatureVisitor swSc = sw.visitSuperclass();
						swSc.visitClassType(Type.getInternalName(superclass));
						swSc.visitTypeArgument(SignatureVisitor.INSTANCEOF).visitTypeVariable("K");
						swSc.visitTypeArgument(SignatureVisitor.INSTANCEOF).visitTypeVariable("V");
						swSc.visitEnd();
					}

					// CODE package com.google.common.collect;
					// CODE public class PublicAbstractBiMap
					cw.visit(V1_6,
							ACC_PUBLIC | ACC_SUPER,
							internalName,
							sw.toString(),
							Type.getInternalName(superclass),
							null);
				}

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

						// COMMENT constructor
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

				CLASS = DynamicUtilities.defineClass(name, cw.toByteArray());
			}

			CONSTRUCTOR = Try.call(() ->
					DynamicUtilities.InvocationUtilities.LambdaMetaFactoryUtilities
							.makeBiFunction(DynamicUtilities.PUBLIC_LOOKUP.findConstructor(CLASS, MethodType.methodType(void.class, Map.class, Map.class)),
									BiMap.class, Map.class, Map.class), LOGGER)
					.map(CastUtilities::<BiFunction<Map<?, ?>, Map<?, ?>, ? extends BiMap<?, ?>>>castUnchecked)
					.orElseThrow(ThrowableCatcher::rethrow);
		}
	}
}
