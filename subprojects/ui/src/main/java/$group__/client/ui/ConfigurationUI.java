package $group__.client.ui;

import $group__.client.ui.mvvm.structures.EnumCursor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public enum ConfigurationUI {
	;

	@Nullable
	private static String modId;

	public static String getModId() {
		if (modId == null)
			throw new IllegalStateException("Setup not done");
		return modId;
	}

	public static void setup(String modId) {
		if (ConfigurationUI.modId != null)
			throw new IllegalStateException("Setup already done");
		ConfigurationUI.modId = modId;
	}

	public static void loadComplete() {
		Minecraft.getInstance().getFramebuffer().enableStencil();
		EnumCursor.preload();
	}
}
