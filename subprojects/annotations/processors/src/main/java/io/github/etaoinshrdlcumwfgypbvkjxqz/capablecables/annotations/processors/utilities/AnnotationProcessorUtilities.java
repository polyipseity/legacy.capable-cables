package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.processors.utilities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import org.slf4j.Marker;

import javax.annotation.Nullable;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.BaseStream;

/**
 * Contains utilities that can be used for annotation processing.
 *
 * @author William So
 * @since 0.0.1
 */
public enum AnnotationProcessorUtilities {
	;

	/**
	 * The marker for this class.
	 *
	 * @see Marker
	 * @since 0.0.1
	 */
	private static final Marker CLASS_MARKER = UtilitiesMarkers.getInstance().getClassMarker();

	/**
	 * The resource bundle for this class.
	 *
	 * @see ResourceBundle
	 * @since 0.0.1
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	/**
	 * Same as {@link #getEffectiveAnnotationsWithInheritingConsidered(Elements, Types, Class, ExecutableElement)}, but
	 * an IllegalArgumentException is thrown if not only one annotation is found.
	 *
	 * @param <A>            type of Annotation
	 * @param elements       the Elements utilities
	 * @param types          the Types utilities
	 * @param annotationType type of Annotation to be searched for
	 * @param executable     executable to be searched first
	 *
	 * @return the Annotations found for the specified ExecutableElement, with inheritance considered
	 *
	 * @throws IllegalArgumentException if not only one annotation is found
	 * @see #getEffectiveAnnotationsWithInheritingConsidered(Elements, Types, Class, ExecutableElement)
	 * @see Annotation
	 * @since 0.0.1
	 */
	public static <A extends Annotation> A getEffectiveAnnotationWithInheritingConsidered(Elements elements, Types types, Class<A> annotationType, ExecutableElement executable)
			throws IllegalArgumentException {
		A[] r = getEffectiveAnnotationsWithInheritingConsidered(elements, types, annotationType, executable);
		if (r.length != 1)
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(AnnotationProcessorUtilities::getClassMarker)
							.addKeyValue("annotationType", annotationType).addKeyValue("executable", executable).addKeyValue("elements", elements).addKeyValue("types", types)
							.addMessages(() -> getResourceBundle().getString("annotations.get.plural.fail"))
							.build()
			);
		return r[0];
	}

	/**
	 * Gets the Annotations for the specified {@link ExecutableElement}.  If the specified Annotations are not found on
	 * the specified ExecutableElement, then super ExecutableElements are searched.
	 *
	 * @param <A>            type of Annotation
	 * @param elements       the Elements utilities
	 * @param types          the Types utilities
	 * @param annotationType type of Annotation to be searched for
	 * @param executable     executable to be searched first
	 *
	 * @return the Annotations found for the specified ExecutableElement, with inheritance considered
	 *
	 * @see #getEffectiveAnnotationWithInheritingConsidered(Elements, Types, Class, ExecutableElement)
	 * @see Annotation
	 * @since 0.0.1
	 */
	public static <A extends Annotation> A[] getEffectiveAnnotationsWithInheritingConsidered(Elements elements, Types types, Class<A> annotationType, ExecutableElement executable) {
		A[] ret = executable.getAnnotationsByType(annotationType);
		if (ret.length != 0)
			return ret;
		TypeElement type = (TypeElement) executable.getEnclosingElement();
		return getSuperclassesAndInterfaces(types, type).stream().sequential()
				.flatMap(Collection::stream)
				.map(TypeElement::getEnclosedElements)
				.flatMap(Collection::stream)
				.filter(enclosedElements -> enclosedElements.getKind() == ElementKind.METHOD)
				.map(ExecutableElement.class::cast)
				.filter(executableElement -> elements.overrides(executable, executableElement, type))
				.map(executableElement -> executableElement.getAnnotationsByType(annotationType))
				.filter(annotations -> annotations.length != 0)
				.findFirst()
				.orElse(ret);
	}

	/**
	 * Returns the marker for this class.
	 *
	 * @return the marker for this class
	 *
	 * @see Marker
	 * @since 0.0.1
	 */
	public static Marker getClassMarker() { return CLASS_MARKER; }

	/**
	 * Returns the resource bundle for this class.
	 *
	 * @return the resource bundle for this class
	 *
	 * @see ResourceBundle
	 * @since 0.0.1
	 */
	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	/**
	 * Returns the superclasses and superinterfaces of the specified TypeElement.
	 * <p>
	 * The list contains an ordered set of superclasses as its first element.
	 * The rest of the elements contains an unordered set of interfaces.
	 * The sets of interfaces are ordered in ascending type distance from the specified TypeElement.
	 *
	 * @param types the Types utilities
	 * @param type  the lower bound, exclusive
	 *
	 * @return the superclasses and superinterfaces of the specified TypeElement
	 *
	 * @since 0.0.1
	 */
	@SuppressWarnings({"UnstableApiUsage", "ObjectAllocationInLoop"})
	@Immutable
	public static List<Set<TypeElement>> getSuperclassesAndInterfaces(Types types, TypeElement type) {
		List<Set<TypeElement>> ret = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);

		@Immutable List<TypeElement> superclasses = getSuperclasses(type, types);
		ret.add(ImmutableSet.copyOf(superclasses));
		@SuppressWarnings("UnstableApiUsage") AtomicReference<Set<TypeElement>> currentLayerInterfaces =
				new AtomicReference<>(
						type.getInterfaces().stream().sequential()
								.map(types::asElement)
								.map(TypeElement.class::cast)
								.collect(ImmutableSet.toImmutableSet()));
		superclasses.stream().sequential()
				.map(TypeElement::getInterfaces)
				.map(Collection::stream)
				.map(BaseStream::sequential)
				.map(superclassInterfacesStream ->
						superclassInterfacesStream
								.map(types::asElement)
								.map(TypeElement.class::cast)
								.collect(ImmutableSet.toImmutableSet()))
				.forEachOrdered(superclassInterfaces ->
						ret.add(ImmutableSet.copyOf(AssertionUtilities.assertNonnull(
								currentLayerInterfaces.getAndUpdate(currentLayerInterfaces2 -> {
									ImmutableSet.Builder<TypeElement> nextLayerInterfaces = ImmutableSet.builder();
									nextLayerInterfaces.addAll(superclassInterfaces);
									currentLayerInterfaces2.stream().sequential()
											.map(TypeElement::getInterfaces)
											.flatMap(Collection::stream)
											.map(types::asElement)
											.map(TypeElement.class::cast)
											.forEachOrdered(nextLayerInterfaces::add);
									return nextLayerInterfaces.build();
								})))));
		while (!AssertionUtilities.assertNonnull(currentLayerInterfaces.get())
				.isEmpty()) {
			ret.add(ImmutableSet.copyOf(AssertionUtilities.assertNonnull(
					currentLayerInterfaces.getAndUpdate(currentLayerInterfaces2 ->
							currentLayerInterfaces2.stream().sequential()
									.map(TypeElement::getInterfaces)
									.flatMap(Collection::stream)
									.map(types::asElement)
									.map(TypeElement.class::cast)
									.collect(ImmutableSet.toImmutableSet())))));
		}

		ret.removeIf(Collection::isEmpty);
		return ImmutableList.copyOf(ret);
	}

	/**
	 * Same as {@link #getIntermediateSuperclasses(TypeElement, TypeElement, Types)}, but the upper bound is unbounded.
	 *
	 * @param types the Types utilities
	 * @param type  lower bound, exclusive
	 *
	 * @return a List of TypeElements that satisfy the lower bound
	 *
	 * @see TypeElement
	 * @see #getIntermediateSuperclasses(TypeElement, TypeElement, Types)
	 * @since 0.0.1
	 */
	@Immutable
	public static List<TypeElement> getSuperclasses(TypeElement type, Types types) { return getIntermediateSuperclasses(type, null, types); }

	/**
	 * Same as {@link #getLowerAndIntermediateSuperclasses(Types, TypeElement, TypeElement)}, but the lower bound is
	 * exclusive.
	 *
	 * @param types the Types utilities
	 * @param lower lower bound, exclusive
	 * @param upper upper bound, exclusive
	 *
	 * @return a List of TypeElements that satisfy the lower and upper bound
	 *
	 * @see TypeElement
	 * @see #getLowerAndIntermediateSuperclasses(Types, TypeElement, TypeElement)
	 * @since 0.0.1
	 */
	@Immutable
	public static List<TypeElement> getIntermediateSuperclasses(TypeElement lower, @Nullable TypeElement upper, Types types) { return getLowerAndIntermediateSuperclasses(types, (TypeElement) types.asElement(lower.getSuperclass()), upper); }

	/**
	 * Returns a List of TypeElements that are a supertype (inclusive) of {@code lower} and a subtype (exclusive) of
	 * {@code upper}.  The List returned is ordered from {@code lower} to {@code upper}.
	 *
	 * @param types the Types utilities
	 * @param lower lower bound, inclusive
	 * @param upper upper bound, exclusive
	 *
	 * @return a List of TypeElements that satisfy the lower and upper bound
	 *
	 * @see TypeElement
	 * @since 0.0.1
	 */
	@Immutable
	public static List<TypeElement> getLowerAndIntermediateSuperclasses(Types types, @Nullable TypeElement lower, @Nullable TypeElement upper) {
		ImmutableList.Builder<TypeElement> ret = ImmutableList.builder();
		for (@Nullable TypeElement current = lower;
		     !Objects.equals(current, upper) && current != null;
		     current = (TypeElement) types.asElement(current.getSuperclass()))
			ret.add(current);
		return ret.build();
	}

	/**
	 * Determines whether the provided Element is {@code abstract}.
	 *
	 * @param element the Element to be queried
	 *
	 * @return whether element is {@code abstract}
	 *
	 * @see Element
	 * @since 0.0.1
	 */
	public static boolean isElementAbstract(Element element) { return element.getModifiers().contains(Modifier.ABSTRACT) || element.getKind().isInterface(); }

	/**
	 * Determines whether the provided Element is {@code final}.
	 *
	 * @param element the Element to be queried
	 *
	 * @return whether element is {@code final}
	 *
	 * @see Element
	 * @since 0.0.1
	 */
	public static boolean isElementFinal(Element element) { return element.getModifiers().contains(Modifier.FINAL); }

	/**
	 * Same as {@link #getLowerAndIntermediateSuperclasses(Types, TypeElement, TypeElement)}, but the upper bound is
	 * unbounded.
	 *
	 * @param types the Types utilities
	 * @param type  lower bound, inclusive
	 *
	 * @return a List of TypeElements that satisfy the lower bound
	 *
	 * @see #getLowerAndIntermediateSuperclasses(Types, TypeElement, TypeElement)
	 * @see TypeElement
	 * @since 0.0.1
	 */
	@Immutable
	public static List<TypeElement> getThisAndSuperclasses(Types types, TypeElement type) { return getLowerAndIntermediateSuperclasses(types, type, null); }

	/**
	 * Same as {@see #getThisAndSuperclasses(Types, TypeElement)}, but an additional set consisting of the specified
	 * TypeElement is prepended to the result.
	 *
	 * @param types the Types utilities
	 * @param type  the lower bound, inclusive
	 *
	 * @return the superclasses and superinterfaces of the specified TypeElement
	 *
	 * @see #getThisAndSuperclasses(Types, TypeElement)
	 * @since 0.0.1
	 */
	@Immutable
	public static List<Set<TypeElement>> getThisAndSuperclassesAndInterfaces(Types types, TypeElement type) {
		return ImmutableList.<Set<TypeElement>>builder().add(ImmutableSet.of(type)).addAll(getSuperclassesAndInterfaces(types, type)).build();
	}
}
