package $group__.client.ui.core.mvvm.binding;

public class BindingTransformerNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 7374170655174983888L;

	public BindingTransformerNotFoundException() {}

	public BindingTransformerNotFoundException(String message) { super(message); }

	public BindingTransformerNotFoundException(String message, Throwable cause) { super(message, cause); }

	public BindingTransformerNotFoundException(Throwable cause) { super(cause); }
}
