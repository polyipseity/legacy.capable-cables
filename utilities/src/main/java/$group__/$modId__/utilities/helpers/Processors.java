package $group__.$modId__.utilities.helpers;

import com.google.common.collect.ImmutableSet;

import javax.annotation.Nullable;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;
import static $group__.$modId__.utilities.helpers.specific.StreamsExtension.streamSmart;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectArguments;

public enum Processors {
	/* MARK empty */;


	/* SECTION static methods */

	public static boolean isElementAbstract(Element element) { return element.getModifiers().contains(Modifier.ABSTRACT) || element.getKind().isInterface(); }

	public static boolean isElementFinal(Element element) { return element.getModifiers().contains(Modifier.FINAL); }


	public static ImmutableSet<TypeElement> getSuperclasses(TypeElement type, Types types) { return getIntermediateSuperclasses(type, null, types); }

	public static ImmutableSet<TypeElement> getThisAndSuperclasses(TypeElement type, Types types) { return getLowerAndIntermediateSuperclasses(type, null, types); }

	public static ImmutableSet<TypeElement> getIntermediateSuperclasses(TypeElement lower, @Nullable TypeElement upper, Types types) { return getLowerAndIntermediateSuperclasses((TypeElement) types.asElement(lower.getSuperclass()), upper, types); }

	public static ImmutableSet<TypeElement> getLowerAndIntermediateSuperclasses(@Nullable TypeElement lower, @Nullable TypeElement upper, Types types) {
		ImmutableSet.Builder<TypeElement> r = new ImmutableSet.Builder<>();
		for (@Nullable TypeElement i = lower; i != upper && i != null; i =
				(TypeElement) types.asElement(i.getSuperclass()))
			r.add(i);
		return r.build();
	}


	public static ImmutableSet<ImmutableSet<TypeElement>> getSuperclassesAndInterfaces(TypeElement type, Types types) {
		LinkedHashSet<ImmutableSet<TypeElement>> r = new LinkedHashSet<>(INITIAL_CAPACITY_2);

		ImmutableSet<TypeElement> scs = getSuperclasses(type, types);
		r.add(scs);
		AtomicReference<List<TypeElement>> cur = new AtomicReference<>(streamSmart(type.getInterfaces(), 2).map(m -> (TypeElement) types.asElement(m)).collect(Collectors.toList()));
		scs.forEach(sc -> r.add(ImmutableSet.copyOf(cur.getAndUpdate(c -> {
			List<TypeElement> next = streamSmart(sc.getInterfaces(), 2).map(m -> (TypeElement) types.asElement(m)).collect(Collectors.toList());
			c.forEach(t -> next.addAll(streamSmart(t.getInterfaces(), 2).map(m -> (TypeElement) types.asElement(m)).collect(Collectors.toList())));
			return next;
		}))));
		while (!cur.get().isEmpty()) r.add(ImmutableSet.copyOf(cur.getAndUpdate(c -> {
			List<TypeElement> next = new ArrayList<>(INITIAL_CAPACITY_2);
			c.forEach(t -> next.addAll(streamSmart(t.getInterfaces(), 2).map(m -> (TypeElement) types.asElement(m)).collect(Collectors.toList())));
			return next;
		})));

		r.removeIf(Collection::isEmpty);
		return ImmutableSet.copyOf(r);
	}

	public static ImmutableSet<ImmutableSet<TypeElement>> getThisAndSuperclassesAndInterfaces(TypeElement type, Types types) { return new ImmutableSet.Builder<ImmutableSet<TypeElement>>().add(ImmutableSet.of(type)).addAll(getSuperclassesAndInterfaces(type, types)).build(); }

	public static <A extends Annotation> A[] getEffectiveAnnotationsWithInheritingConsidered(Class<A> annotationType, ExecutableElement executable, Elements elements, Types types) {
		A[] r = executable.getAnnotationsByType(annotationType);
		if (r.length != 0) return r;

		TypeElement type = (TypeElement) executable.getEnclosingElement();
		sss:
		for (ImmutableSet<TypeElement> ss : getSuperclassesAndInterfaces(type, types)) {
			for (TypeElement s : ss) {
				for (Element m : s.getEnclosedElements()) {
					if (m.getKind() == ElementKind.METHOD) {
						ExecutableElement m1 = (ExecutableElement) m;
						if (elements.overrides(executable, m1, type)) {
							r = m1.getAnnotationsByType(annotationType);
							break;
						}
					}
				}
				if (r.length != 0) break sss;
			}
		}

		return r;
	}

	public static <A extends Annotation> A getEffectiveAnnotationWithInheritingConsidered(Class<A> annotationType, ExecutableElement executable, Elements elements, Types types) throws IllegalArgumentException {
		A[] r = getEffectiveAnnotationsWithInheritingConsidered(annotationType, executable, elements, types);
		if (r.length != 1) rejectArguments(annotationType, executable);
		return r[0];
	}
}
