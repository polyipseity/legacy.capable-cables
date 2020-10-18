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
	/**
	 * Returns the processing environment provided by the tool framework.
	 *
	 * @return the processing environment provided by the tool framework.
	 *
	 * @see ProcessingEnvironment
	 * @since Capable Cables 0.0.1
	 */
	@SuppressWarnings({"deprecation", "unused"})
	protected ProcessingEnvironment getProcessingEnv() { return processingEnv; }
}
