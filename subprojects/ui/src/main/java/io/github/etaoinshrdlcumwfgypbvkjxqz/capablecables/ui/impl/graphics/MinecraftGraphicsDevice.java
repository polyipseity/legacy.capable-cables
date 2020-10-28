package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class MinecraftGraphicsDevice
		extends GraphicsDevice {
	private static final Supplier<MinecraftGraphicsDevice> INSTANCE = Suppliers.memoize(MinecraftGraphicsDevice::new);

	private MinecraftGraphicsDevice() {}

	public static MinecraftGraphicsDevice getInstance() {
		return AssertionUtilities.assertNonnull(INSTANCE.get());
	}

	@Override
	public int getType() {
		return TYPE_RASTER_SCREEN;
	}

	@Override
	public String getIDstring() {
		return getClass().getSimpleName();
	}

	@Override
	public GraphicsConfiguration[] getConfigurations() {
		return new GraphicsConfiguration[]{
				MinecraftGraphicsConfiguration.getInstance()
		};
	}

	@Override
	public GraphicsConfiguration getDefaultConfiguration() {
		return MinecraftGraphicsConfiguration.getInstance();
	}
}
