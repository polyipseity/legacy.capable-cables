package $group__.$modId__.utilities.constructs.interfaces.annotations;

import $group__.$modId__.utilities.constructs.interfaces.basic.IAnnotationProcessor;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.MethodAdapter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.annotation.Nonnull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IAnnotationProcessor.getMessage;
import static $group__.$modId__.utilities.helpers.Throwables.interrupt;
import static $group__.$modId__.utilities.variables.References.LOGGER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Documented
@Nonnull
@Retention(RUNTIME)
@Target(METHOD)
public @interface ExternalToImmutableMethod {
	/* SECTION methods */

	Class<?>[] value();

	@SuppressWarnings("SameReturnValue") boolean allowExtends() default false;


	/* SECTION static variables */

	LoadingCache<Class<?>, ExternalToImmutableMethod> EXTERNAL_TO_IMMUTABLE_METHOD_ANNOTATIONS_CACHE = CacheBuilder.newBuilder().build(new CacheLoader<Class<?>, ExternalToImmutableMethod>() {
		@Override
		public ExternalToImmutableMethod load(Class<?> key) throws InterruptedException {
			ExternalToImmutableMethod r = null;

			long n = Long.MAX_VALUE;
			for (Map.Entry<Class<?>, ExternalToImmutableMethod> e : EXTERNAL_TO_IMMUTABLE_METHOD_ANNOTATIONS_CACHE.asMap().entrySet()) {
				ExternalToImmutableMethod v = e.getValue();
				if (v.allowExtends()) {
					Class<?> k = e.getKey();
					if (k.isAssignableFrom(key)) {
						long l = 0;
						for (Class<?> s = key; s != null; l++) {
							if (s == k) {
								if (l < n) {
									n = l;
									r = v;
								}
								break;
							}
							s = s.getSuperclass();
						}
					}
				}
			}

			if (r != null)
				LOGGER.info("To immutable method '{}' with annotation '{}' auto-registered for class '{}'", EXTERNAL_TO_IMMUTABLE_METHOD_MAP.get(r).get().toGenericString(), r, key.toGenericString());
			else
				throw interrupt("No to immutable method for class '" + key.toGenericString() + "'");

			return r;
		}
	});
	WeakHashMap<ExternalToImmutableMethod, MethodAdapter> EXTERNAL_TO_IMMUTABLE_METHOD_MAP = new WeakHashMap<>();


	/* SECTION static classes */

	enum AnnotationProcessor implements IAnnotationProcessor.IClass.IElement.IMethod<ExternalToImmutableMethod> {
		/* SECTION enums */
		INSTANCE;


		/* SECTION methods */

		@Override
		public Class<ExternalToImmutableMethod> annotationType() { return ExternalToImmutableMethod.class; }

		@Override
		public void processMethod(Result<ExternalToImmutableMethod> result) {
			ExternalToImmutableMethod a = result.annotations[0], ap;
			Method m = result.element;

			Class<?>[] ks = a.value();
			if (ks.length == 0) {
				LOGGER.warn(getMessage(this, "Method '{}' with annotation '{}' has no usage"), m.toGenericString(), a);
				return;
			}
			EXTERNAL_TO_IMMUTABLE_METHOD_MAP.put(a, new MethodAdapter(m));

			for (Class<?> k : ks) {
				ap = EXTERNAL_TO_IMMUTABLE_METHOD_ANNOTATIONS_CACHE.getIfPresent(k);
				EXTERNAL_TO_IMMUTABLE_METHOD_ANNOTATIONS_CACHE.put(k, a);
				if (ap == null)
					LOGGER.info(getMessage(this, "Registered method '{}' with annotation '{}' for class '{}'"), m.toGenericString(), a, k.toGenericString());
				else
					LOGGER.warn(getMessage(this, "Replaced previous method '{}' with annotation '{}' with method '{}' with annotation '{}' for class '{}'"), EXTERNAL_TO_IMMUTABLE_METHOD_MAP.get(ap).get().toGenericString(), ap, m.toGenericString(), a, k.toGenericString());
			}
		}
	}
}
