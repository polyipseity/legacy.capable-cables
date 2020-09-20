package $group__.annotations.processors;

import $group__.annotations.OverridingStatus;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.ProcessorUtilities;
import $group__.utilities.StreamUtilities;
import $group__.utilities.internationalization.ChangingLocaleResourceBundle;
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
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Set;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_ENORMOUS;
import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

@SuppressWarnings("unused")
public final class OverridingStatusProcessor
		extends AbstractProcessor {
	private static final ResourceBundle RESOURCE_BUNDLE = new ChangingLocaleResourceBundle.Builder().build();

	@SuppressWarnings("MagicNumber")
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (annotations.size() != 1)
			return false;

		@SuppressWarnings("UnstableApiUsage") SetMultimap<TypeElement, ExecutableElement> typeToExesMMap = MultimapBuilder.hashKeys(INITIAL_CAPACITY_ENORMOUS).hashSetValues(INITIAL_CAPACITY_SMALL).build();
		StreamUtilities.streamSmart(roundEnv.getElementsAnnotatedWith(OverridingStatus.class), 3).unordered()
				.forEach(e -> typeToExesMMap.put((TypeElement) e.getEnclosingElement(), (ExecutableElement) e));

		@SuppressWarnings("UnstableApiUsage") Set<TypeElement> pkgFlat = StreamUtilities.streamSmart(roundEnv.getRootElements(), 5).unordered()
				.filter(e -> e.asType().getKind() == TypeKind.DECLARED)
				.map(TypeElement.class::cast)
				.collect(ImmutableSet.toImmutableSet());

		Elements elements = processingEnv.getElementUtils();
		Types types = processingEnv.getTypeUtils();
		Messager messager = processingEnv.getMessager();
		StreamUtilities.streamSmart(typeToExesMMap.asMap().entrySet(), 3).unordered()
				.forEach(entry -> {
					TypeElement superclass = AssertionUtilities.assertNonnull(entry.getKey());
					Collection<ExecutableElement> exes = AssertionUtilities.assertNonnull(entry.getValue());
					StreamUtilities.streamSmart(pkgFlat, 3).unordered()
							.filter(subclass -> !subclass.equals(superclass) && types.isSubtype(types.erasure(subclass.asType()), types.erasure(superclass.asType())))
							.forEach(subclass ->
									StreamUtilities.streamSmart(exes, 25).unordered()
											.forEach(superMethod -> {
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
														messager.printMessage(Diagnostic.Kind.NOTE, new LogMessageBuilder()
																.addMessages(() -> getResourceBundle().getString("info.override.false"))
																.addArguments(superMethod)
																.build(), subclass);
													else if (whenB) {
														for (TypeElement superclass1 : ProcessorUtilities.getIntermediateSuperclasses(subclass,
																(TypeElement) types.asElement(superclass.getSuperclass()), types)) {
															for (Element superMethod1 : superclass1.getEnclosedElements()) {
																if (superMethod1.getKind() == ElementKind.METHOD && ProcessorUtilities.isElementFinal(superMethod1) && elements.overrides((ExecutableElement) superMethod1, superMethod, superclass1)) {
																	messager.printMessage(superclass1.getQualifiedName().toString().startsWith(a.group()) ? Diagnostic.Kind.WARNING : Diagnostic.Kind.NOTE,
																			new LogMessageBuilder()
																					.addMessages(() -> getResourceBundle().getString("warning.always.impossible"))
																					.addArguments(superMethod1)
																					.build(), subclass);
																	return;
																}
															}
														}
														messager.printMessage(Diagnostic.Kind.ERROR, new LogMessageBuilder()
																.addMessages(() -> getResourceBundle().getString("error.always.fail"))
																.addArguments(superMethod)
																.build(), subclass);
													}
												} else if (ProcessorUtilities.getEffectiveAnnotationWithInheritingConsidered(OverridingStatus.class, subMethod, elements, types).equals(a)) {
													if (whenB == null)
														messager.printMessage(Diagnostic.Kind.NOTE, new LogMessageBuilder()
																.addMessages(() -> getResourceBundle().getString("info.override.true"))
																.addArguments(superMethod)
																.build(), subMethod);
													else if (!whenB)
														messager.printMessage(Diagnostic.Kind.ERROR, new LogMessageBuilder()
																.addMessages(() -> getResourceBundle().getString("error.never.fail"))
																.addArguments(superMethod)
																.build(), subMethod);
												}
											}));
				});
		return true;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() { return Collections.singleton(OverridingStatus.class.getCanonicalName()); }

	@Override
	public SourceVersion getSupportedSourceVersion() { return SourceVersion.latest(); }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
