package $group__.client.gui;

import $group__.client.gui.core.structures.EnumCursor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
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
		Minecraft.getInstance().getFramebuffer().enableStencil();
		EnumCursor.preload();
	}
}
