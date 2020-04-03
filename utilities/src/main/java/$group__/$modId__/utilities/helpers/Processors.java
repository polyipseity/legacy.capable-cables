package $group__.$modId__.utilities.helpers;

import com.google.common.collect.ImmutableSet;

import javax.annotation.Nullable;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.List;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.StreamsExtension.streamSmart;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectArguments;

public enum Processors {
	/* MARK empty */;


	/* SECTION static methods */

	public static boolean isElementAbstract(Element element) { return element.getModifiers().contains(Modifier.ABSTRACT) || element.getKind().isInterface(); }

	public static boolean isElementFinal(Element element) { return element.getModifiers().contains(Modifier.FINAL); }


	public static ImmutableSet<TypeElement> getSuperclasses(@Nullable TypeElement type, Types types) { return getIntermediateSuperclasses(type, null, types); }

	public static ImmutableSet<TypeElement> getThisAndSuperclasses(@Nullable TypeElement type, Types types) { return getLowerAndIntermediateSuperclasses(type, null, types); }

	public static ImmutableSet<TypeElement> getIntermediateSuperclasses(@Nullable TypeElement lower,
	                                                                    @Nullable TypeElement upper, Types types) {
		return getLowerAndIntermediateSuperclasses((TypeElement) types.asElement(lower.getSuperclass()), upper, types);
	}

	public static ImmutableSet<TypeElement> getLowerAndIntermediateSuperclasses(@Nullable TypeElement lower,
	                                                                            @Nullable TypeElement upper,
	                                                                            Types types) {
		ImmutableSet.Builder<TypeElement> r = new ImmutableSet.Builder<>();
		for (@Nullable TypeElement i = lower; i != upper && i != null; i =
				(TypeElement) types.asElement(i.getSuperclass()))
			r.add(i);
		return r.build();
	}


	@SuppressWarnings("UnstableApiUsage")
	public static ImmutableSet<ImmutableSet<TypeElement>> getSuperclassesAndInterfaces(TypeElement type, Types types) {
		ImmutableSet.Builder<ImmutableSet<TypeElement>> r = new ImmutableSet.Builder<>();
		ImmutableSet<TypeElement> scs = getSuperclasses(type, types);
		r.add(scs);
		scs.forEach(sc -> r.add(streamSmart(sc.getInterfaces(), 2).map(t -> (TypeElement) types.asElement(t)).collect(ImmutableSet.toImmutableSet())));
		return r.build();
	}


	public static <A extends Annotation> A[] getEffectiveAnnotationsIfInheritingConsidered(Class<A> annotationType,
	                                                                                       TypeElement type,
	                                                                                       ExecutableElement executable, Elements elements, Types types) {
		Name exeName = executable.getSimpleName();
		List<? extends VariableElement> exeArgs = executable.getParameters();

		final A[][] r = castUncheckedUnboxedNonnull(Array.newInstance(annotationType, 1, 0));
		sss:
		for (ImmutableSet<TypeElement> ss : getSuperclassesAndInterfaces(type, types)) {
			for (TypeElement s : ss) {
				elements.getAllMembers(s).forEach(m -> {
					if (m.getKind() == ElementKind.METHOD) {
						ExecutableElement m1 = (ExecutableElement) m;
						if (elements.overrides(m1, executable, s)) r[0] = m1.getAnnotationsByType(annotationType);
					}
				});
				if (r[0].length != 0) break sss;
			}
		}

		return r[0];
	}

	public static <A extends Annotation> A getEffectiveAnnotationIfInheritingConsidered(Class<A> annotationType,
	                                                                                    TypeElement type,
	                                                                                    ExecutableElement executable,
	                                                                                    Elements elements,
	                                                                                    Types types) throws IllegalArgumentException {
		A[] r = getEffectiveAnnotationsIfInheritingConsidered(annotationType, type, executable, elements, types);
		if (r.length != 1) rejectArguments(annotationType, type, executable);
		return r[0];
	}
}
