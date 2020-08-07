package $group__.client.gui.core.structures;

public enum EnumGuiMouseClickResult {
	CLICK(true),
	DRAG(true),
	PASS(false);

	public final boolean result;

	EnumGuiMouseClickResult(boolean result) {this.result = result;}
}
