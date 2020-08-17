package $group__.annotations.processors;

import $group__.annotations.OverridingStatus;
import $group__.utilities.CastUtilities;
import $group__.utilities.ProcessorUtilities;
import $group__.utilities.StreamUtilities;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

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

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_ENORMOUS;
import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

@SuppressWarnings("unused")
public final class OverridingStatusProcessor extends AbstractProcessor {
	@Override
	public Set<String> getSupportedAnnotationTypes() { return Collections.singleton(OverridingStatus.class.getCanonicalName()); }

	@Override
	public SourceVersion getSupportedSourceVersion() { return SourceVersion.latest(); }

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (annotations.size() != 1) return false;

		@SuppressWarnings("UnstableApiUsage") SetMultimap<TypeElement, ExecutableElement> typeToExesMMap = MultimapBuilder.hashKeys(INITIAL_CAPACITY_ENORMOUS).hashSetValues(INITIAL_CAPACITY_SMALL).build();
		roundEnv.getElementsAnnotatedWith(OverridingStatus.class).forEach(e -> typeToExesMMap.put((TypeElement) e.getEnclosingElement(), (ExecutableElement) e));

		@SuppressWarnings("UnstableApiUsage") Set<TypeElement> pkgFlat = StreamUtilities.streamSmart(roundEnv.getRootElements(), 5).unordered().filter(e -> e.asType().getKind() == TypeKind.DECLARED).map(CastUtilities::<TypeElement>castUnchecked).collect(ImmutableSet.toImmutableSet());

		Elements elements = processingEnv.getElementUtils();
		Types types = processingEnv.getTypeUtils();
		Messager messager = processingEnv.getMessager();
		typeToExesMMap.asMap().forEach((superclass, exes) -> pkgFlat.forEach(subclass -> {
			if (subclass != superclass && types.isSubtype(types.erasure(subclass.asType()), types.erasure(superclass.asType()))) {
				exes.forEach(superMethod -> {
					OverridingStatus a = superMethod.getAnnotation(OverridingStatus.class);

					When when = a.when();
					@Nullable Boolean whenB = when == When.ALWAYS ? Boolean.TRUE : when == When.NEVER ? Boolean.FALSE : null;
					if (when == When.UNKNOWN) return;

					@Nullable ExecutableElement subMethod = null;
					for (Element e : subclass.getEnclosedElements()) {
						if (e.getKind() == ElementKind.METHOD) {
							ExecutableElement m = (ExecutableElement) e;
							if (elements.overrides(m, superMethod, subclass)) {
								subMethod = m;
								break;
							}
						}
					}

					if (subMethod == null) {
						if (whenB == null)
							messager.printMessage(Diagnostic.Kind.NOTE, "Does not override super method '" + superMethod + '\'', subclass);
						else if (whenB) {
							for (TypeElement superclass1 : ProcessorUtilities.getIntermediateSuperclasses(subclass,
									(TypeElement) types.asElement(superclass.getSuperclass()), types)) {
								for (Element superMethod1 : superclass1.getEnclosedElements()) {
									if (superMethod1.getKind() == ElementKind.METHOD && ProcessorUtilities.isElementFinal(superMethod1) && elements.overrides((ExecutableElement) superMethod1, superMethod, superclass1)) {
										messager.printMessage(superclass1.getQualifiedName().toString().startsWith(a.group()) ? Diagnostic.Kind.WARNING : Diagnostic.Kind.NOTE, "Requirement impossible: cannot override final super method '" + superMethod1 + '\'');
										return;
									}
								}
							}
							messager.printMessage(Diagnostic.Kind.ERROR, "Requirement unfulfilled: should override super" + ' ' + "method '" + superMethod + '\'');
						}
					} else if (ProcessorUtilities.getEffectiveAnnotationWithInheritingConsidered(OverridingStatus.class, subMethod, elements, types).equals(a)) {
						if (whenB == null)
							messager.printMessage(Diagnostic.Kind.NOTE, "Overrides super method '" + superMethod + '\'', subMethod);
						else if (!whenB)
							messager.printMessage(Diagnostic.Kind.ERROR, "Requirement unfulfilled: should " + "NOT" + ' ' + "override super method '" + superMethod + '\'', subMethod);
					}
				});
			}
		}));
		return true;
	}
}
