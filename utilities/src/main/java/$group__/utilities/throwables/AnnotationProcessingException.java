package $group__.utilities.throwables;

public class AnnotationProcessingException extends RuntimeException {
	private static final long serialVersionUID = 6003555440382442147L;

	public AnnotationProcessingException() { super(); }

	public AnnotationProcessingException(String message) { super(message); }

	public AnnotationProcessingException(String message, Throwable cause) { super(message, cause); }

	public AnnotationProcessingException(Throwable cause) { super(cause); }
}
