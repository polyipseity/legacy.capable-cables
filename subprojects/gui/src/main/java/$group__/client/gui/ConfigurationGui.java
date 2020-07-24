package $group__.client.gui;

import $group__.client.gui.structures.EnumCursor;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum ConfigurationGui {
	;

	@Nullable
	private static String modId;

	public static String getModId() {
		if (modId == null)
			throw new IllegalStateException("Setup not done");
		return modId;
	}

	public static void setup(String modId) {
		if (ConfigurationGui.modId != null)
			throw new IllegalStateException("Setup already done");
		ConfigurationGui.modId = modId;
	}

	public static void loadComplete() {
		EnumCursor.preload();
	}
}
