package $group__.client.gui.structures;

import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
@Immutable
public final class GuiKeyPressInfo {
	public final int key, scanCode, modifiers;

	public GuiKeyPressInfo(int key, int scanCode, int modifiers) {
		this.key = key;
		this.scanCode = scanCode;
		this.modifiers = modifiers;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GuiKeyPressInfo that = (GuiKeyPressInfo) o;
		return key == that.key &&
				scanCode == that.scanCode &&
				modifiers == that.modifiers;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, scanCode, modifiers);
	}
}
