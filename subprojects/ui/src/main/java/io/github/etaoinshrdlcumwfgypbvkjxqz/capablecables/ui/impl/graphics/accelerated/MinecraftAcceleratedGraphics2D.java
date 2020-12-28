package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.accelerated;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AbstractGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.utilities.EnumMinecraftCropMethod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
// TODO unfinished
public abstract class MinecraftAcceleratedGraphics2D
		extends AbstractGraphics2D {
	public static final int CROP_METHOD_Z = 1;
	private final EnumMinecraftCropMethod cropMethod;

	public MinecraftAcceleratedGraphics2D(EnumMinecraftCropMethod cropMethod) {
		this.cropMethod = cropMethod;
	}

	@Override
	protected void setInternalClip(@Nullable Shape internalClip) {
		super.setInternalClip(internalClip);
		if (internalClip == null)
			getCropMethod().clearCrop();
		else
			getCropMethod().setCrop(internalClip, getCropMethodZ());
	}

	protected EnumMinecraftCropMethod getCropMethod() {
		return cropMethod;
	}

	public static int getCropMethodZ() {
		return CROP_METHOD_Z;
	}
}
