package $group__.$modId__.annotations.processors;

import $group__.$modId__.annotations.OverridingStatus;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;
import static $group__.$modId__.utilities.helpers.Capacities.INITIAL_CAPACITY_4;
import static $group__.$modId__.utilities.helpers.Processors.*;
import static $group__.$modId__.utilities.helpers.Recursions.recurseAsDepthFirstLoopUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.StreamsExtension.streamSmart;

public final class OverridingStatusProcessor extends AbstractProcessor {
	/* SECTION methods */

	@Override
	public Set<String> getSupportedAnnotationTypes() { return Collections.singleton(OverridingStatus.class.getCanonicalName()); }

	@Override
	public SourceVersion getSupportedSourceVersion() { return SourceVersion.latest(); }

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (!roundEnv.processingOver()) return false;

		@SuppressWarnings("UnstableApiUsage") SetMultimap<TypeElement, ExecutableElement> typeToExesMMap =
				MultimapBuilder.hashKeys(INITIAL_CAPACITY_4).hashSetValues(INITIAL_CAPACITY_2).build();
		roundEnv.getElementsAnnotatedWith(OverridingStatus.class).forEach(e -> typeToExesMMap.put((TypeElement) e.getEnclosingElement(), (ExecutableElement) e));

		Elements elements = processingEnv.getElementUtils();
		Set<TypeElement> pkgFlat = recurseAsDepthFirstLoopUnboxedNonnull(elements.getPackageElement(""),
				p -> p.getEnclosedElements().stream().filter(t -> t.asType().getKind() == TypeKind.DECLARED).map(t -> (TypeElement) t).collect(Collectors.toSet()), Element::getEnclosedElements, t -> t.stream().reduce(Collections.emptySet(), Sets::union));

		Types types = processingEnv.getTypeUtils();
		Messager messager = processingEnv.getMessager();
		typeToExesMMap.asMap().forEach((superclass, exes) -> streamSmart(pkgFlat, 1).forEach(subclass -> {
			if (!isElementAbstract(subclass) && types.isSubtype(subclass.asType(), superclass.asType())) {
				exes.forEach(superMethod -> {
					OverridingStatus a = superMethod.getAnnotation(OverridingStatus.class);

					When when = a.when();
					@Nullable Boolean whenB = when == When.ALWAYS ? Boolean.TRUE : when == When.NEVER ? Boolean.FALSE
							: null;
					if (when == When.UNKNOWN) return;

					if (getEffectiveAnnotationIfInheritingConsidered(OverridingStatus.class, subclass, superMethod,
							elements, types) != a)
						return;

					for (Element subMember : elements.getAllMembers(subclass)) {
						if (subMember.getKind() == ElementKind.METHOD) {
							ExecutableElement subMethod = (ExecutableElement) subMember;
							if (elements.overrides(subMethod, superMethod, subclass)) {
								if (whenB == null || whenB)
									messager.printMessage(Diagnostic.Kind.NOTE,
											"Overrides super method '" + superMethod + '\'', subMethod);
								else
									messager.printMessage(Diagnostic.Kind.ERROR, "Requirement unfulfilled: should not" +
											" " +
											"override super method '" + superMethod + '\'', subMethod);
								return;
							}
						}
					}

					if (whenB == null || !whenB) {
						messager.printMessage(Diagnostic.Kind.NOTE,
								"Does not override super method '" + superMethod + '\'', subclass);
					} else {
						for (TypeElement superclass1 : getIntermediateSuperclasses(subclass,
								(TypeElement) types.asElement(superclass.getSuperclass()), types)) {
							for (Element superMethod1 : elements.getAllMembers(superclass1)) {
								if (superMethod1.getKind() == ElementKind.METHOD && isElementFinal(superMethod1) && elements.overrides((ExecutableElement) superMethod1, superMethod, superclass1)) {
									messager.printMessage(superclass1.getQualifiedName().toString().startsWith(a.group()) ? Diagnostic.Kind.WARNING : Diagnostic.Kind.NOTE, "Requirement impossible: cannot override final super method '" + superMethod1 + '\'');
									return;
								}
							}
						}
						messager.printMessage(Diagnostic.Kind.ERROR, "Requirement unfulfilled: should override super" +
								" " +
								"method '" + superMethod + '\'');
					}
				});
			}
		}));

		return true;
	}
}
