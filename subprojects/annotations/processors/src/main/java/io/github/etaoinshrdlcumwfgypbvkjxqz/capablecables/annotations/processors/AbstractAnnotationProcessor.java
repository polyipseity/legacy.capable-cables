package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.processors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;

/**
 * An abstract annotation processor that improves upon on {@link AbstractProcessor}.
 *
 * @author William So
 * @since Capable Cables 0.0.1
 */
public abstract class AbstractAnnotationProcessor
		extends AbstractProcessor {
	@SuppressWarnings("deprecation")
	protected ProcessingEnvironment getProcessingEnv() { return processingEnv; }
}
