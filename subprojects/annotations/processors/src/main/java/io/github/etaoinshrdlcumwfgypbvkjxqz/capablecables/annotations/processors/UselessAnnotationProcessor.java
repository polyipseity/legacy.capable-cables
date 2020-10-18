package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.processors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.LoggingThrowableHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThreadLocalThrowableHandler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * An annotation processor that does nothing.
 *
 * @author William So
 * @since Capable Cables 0.0.1
 */
public final class UselessAnnotationProcessor
		extends AbstractProcessor {
	static {
		CommonConfigurationTemplate.configureSafe(UtilitiesConfiguration.getInstance(),
				() -> new UtilitiesConfiguration.ConfigurationData(UtilitiesConfiguration.getBootstrapLogger(),
						new LoggingThrowableHandler<>(new ThreadLocalThrowableHandler<>(), UtilitiesConfiguration.getBootstrapLogger()),
						Locale::getDefault));
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		return true;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() { return Collections.emptySet(); }

	@Override
	public SourceVersion getSupportedSourceVersion() { return SourceVersion.latestSupported(); }
}
