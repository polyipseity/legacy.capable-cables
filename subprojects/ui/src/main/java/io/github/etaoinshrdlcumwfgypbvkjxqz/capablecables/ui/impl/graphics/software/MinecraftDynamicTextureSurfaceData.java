package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.software;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.MinecraftGraphicsDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.utilities.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LoopUtilities;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import sun.awt.image.WritableRasterNative;
import sun.java2d.StateTrackableDelegate;
import sun.java2d.SurfaceData;
import sun.java2d.loops.SurfaceType;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class MinecraftDynamicTextureSurfaceData
		extends SurfaceData
		implements AutoCloseable {
	private static final Supplier<@Nonnull MinecraftDynamicTextureSurfaceData> INSTANCE = Suppliers.memoize(MinecraftDynamicTextureSurfaceData::new);

	private WritableRaster fullRaster;
	private DynamicTexturesDataBuffer dataBuffer;

	private MinecraftDynamicTextureSurfaceData() {
		super(StateTrackableDelegate.createInstance(State.STABLE),
				SurfaceType.Any,
				MinecraftGraphicsDevice.getInstance().getDefaultConfiguration().getColorModel());
		/* COMMENT
		WARNING: This class is very dangerous.

		We steal the pointer from 'NativeImage' to write directly to it...
		 */

		Rectangle bounds = getBounds();
		this.dataBuffer = new DynamicTexturesDataBuffer(bounds.width, bounds.height, DynamicTexturesDataBuffer.getTargetTextureSize());
		this.fullRaster = createRaster(bounds, getColorModel(), this.dataBuffer);

		IEventBus eventBus = AssertionUtilities.assertNonnull(Bus.FORGE.bus().get());
		eventBus.addListener(EventPriority.NORMAL, true, this::onResize);
	}

	private static WritableRaster createRaster(Rectangle bounds, ColorModel colorModel, DynamicTexturesDataBuffer dataBuffer) {
		return WritableRasterNative.createNativeRaster(colorModel.createCompatibleSampleModel(bounds.width, bounds.height),
				dataBuffer);
	}

	private void onResize(GuiScreenEvent.InitGuiEvent.Pre event) {
		close();

		Rectangle bounds = getBounds();
		setDataBuffer(new DynamicTexturesDataBuffer(bounds.width, bounds.height, DynamicTexturesDataBuffer.getTargetTextureSize()));
		setFullRaster(createRaster(bounds, getColorModel(), getDataBuffer()));

		MinecraftSoftwareGraphics2D.recreateGraphics(); // COMMENT resets the clip
	}

	@Override
	public void close() {
		getDataBuffer().close();
	}

	protected DynamicTexturesDataBuffer getDataBuffer() {
		return dataBuffer;
	}

	protected void setDataBuffer(DynamicTexturesDataBuffer dataBuffer) {
		this.dataBuffer = dataBuffer;
	}

	public static MinecraftDynamicTextureSurfaceData getInstance() {
		return INSTANCE.get();
	}

	@Override
	public SurfaceData getReplacement() {
		// COMMENT this should never be invalid
		return this;
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		return MinecraftGraphicsDevice.getInstance().getDefaultConfiguration();
	}

	@Override
	public Raster getRaster(int x, int y, int w, int h) {
		return getFullRaster(); // COMMENT avoid creation of objects
	}

	protected WritableRaster getFullRaster() {
		return fullRaster;
	}

	protected void setFullRaster(WritableRaster fullRaster) {
		this.fullRaster = fullRaster;
	}

	@Override
	public Rectangle getBounds() {
		return getDeviceConfiguration().getBounds();
	}

	@Override
	@Nullable
	public Object getDestination() {
		return null;
	}

	public void clear() {
		getDataBuffer().clear();
	}

	public void draw() {
		DynamicTexturesDataBuffer.runIfOpen(getDataBuffer(), instance -> {
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableAlphaTest();

			LoopUtilities.doNTimesNested(indexes -> {
				// COMMENT gather data
				int textureX = Math.toIntExact(indexes[0]), textureY = Math.toIntExact(indexes[1]);
				DynamicTexture texture = DynamicTexturesDataBuffer.getTexture(instance, textureX, textureY);
				Rectangle textureBounds = new Rectangle(
						DynamicTexturesDataBuffer.toAbsoluteCoordinate(instance, textureX), DynamicTexturesDataBuffer.toAbsoluteCoordinate(instance, textureY),
						instance.getTextureSize(), instance.getTextureSize()
				);

				// COMMENT upload texture
				texture.updateDynamicTexture(); // COMMENT we write directly to 'NativeImage'

				// COMMENT draw texture
				RenderSystem.bindTexture(texture.getGlTextureId());
				MinecraftDrawingUtilities.blit(AffineTransformUtilities.getIdentity(),
						textureBounds,
						new Point2D.Double(),
						textureBounds.getSize());

			}, instance.getTexturesWidth(), instance.getTexturesHeight());

			RenderSystem.enableAlphaTest();
			RenderSystem.disableBlend();
		});
	}
}
