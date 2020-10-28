package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftClientUtilities;
import net.minecraft.client.MainWindow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class MinecraftGraphicsConfiguration
		extends GraphicsConfiguration {
	private static final Supplier<MinecraftGraphicsConfiguration> INSTANCE = Suppliers.memoize(MinecraftGraphicsConfiguration::new);
	private static final ColorModel DEFAULT_COLOR_MODEL = new DirectColorModel(Integer.SIZE,
			0x000000ff,
			0x0000ff00,
			0x00ff0000,
			0xff000000
	);

	private MinecraftGraphicsConfiguration() {}

	public static MinecraftGraphicsConfiguration getInstance() {
		return AssertionUtilities.assertNonnull(INSTANCE.get());
	}

	@Override
	public GraphicsDevice getDevice() {
		return MinecraftGraphicsDevice.getInstance();
	}

	@Override
	public ColorModel getColorModel() {
		return getDefaultColorModel();
	}

	public static ColorModel getDefaultColorModel() {
		return DEFAULT_COLOR_MODEL;
	}

	@Override
	public ColorModel getColorModel(int transparency) {
		return getDefaultColorModel();
	}

	@Override
	public AffineTransform getDefaultTransform() {
		return AffineTransformUtilities.getIdentity();
	}

	@Override
	public AffineTransform getNormalizingTransform() {
		return AffineTransformUtilities.getIdentity();
	}

	@Override
	public Rectangle getBounds() {
		MainWindow mainWindow = MinecraftClientUtilities.getMinecraftNonnull().getMainWindow();
		return new Rectangle(mainWindow.getScaledWidth(), mainWindow.getScaledHeight());
	}
}
