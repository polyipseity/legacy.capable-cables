package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.awt;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.MinecraftSurfaceData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sun.awt.HeadlessToolkit;

import java.awt.*;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
@Deprecated
public final class MinecraftToolkit
		extends HeadlessToolkit {
	private static final Supplier<MinecraftToolkit> INSTANCE = Suppliers.memoize(MinecraftToolkit::new);

	private MinecraftToolkit() {
		super(Toolkit.getDefaultToolkit());
	}

	public static MinecraftToolkit getInstance() {
		return AssertionUtilities.assertNonnull(INSTANCE.get());
	}

	@Override
	public Dimension getScreenSize() throws HeadlessException {
		return MinecraftSurfaceData.getInstance().getBounds().getSize();
	}
}
