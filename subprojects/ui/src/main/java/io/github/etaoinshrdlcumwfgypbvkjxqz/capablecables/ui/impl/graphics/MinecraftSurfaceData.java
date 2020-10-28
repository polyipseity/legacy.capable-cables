package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.DoubleDimension2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.system.MemoryUtil;
import sun.awt.image.WritableRasterNative;
import sun.java2d.StateTrackableDelegate;
import sun.java2d.SurfaceData;
import sun.java2d.loops.SurfaceType;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.lang.invoke.MethodHandle;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class MinecraftSurfaceData
		extends SurfaceData
		implements AutoCloseable {
	private static final Supplier<MinecraftSurfaceData> INSTANCE = Suppliers.memoize(MinecraftSurfaceData::new);
	private static final MethodHandle NATIVE_IMAGE_IMAGE_POINTER_GETTER;

	static {
		try {
			NATIVE_IMAGE_IMAGE_POINTER_GETTER = InvokeUtilities.getImplLookup().findGetter(NativeImage.class, "imagePointer", long.class);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	private WritableRaster fullRaster;
	private NativeImageDataBuffer nativeImageDataBuffer;
	private DynamicTexture texture;

	private MinecraftSurfaceData() {
		super(StateTrackableDelegate.createInstance(State.STABLE),
				SurfaceType.Any,
				MinecraftGraphicsConfiguration.getInstance().getColorModel());
		/* COMMENT
		WARNING: This class is very dangerous.

		We steal the pointer from 'NativeImage' to write directly to it...
		 */

		Rectangle bounds = getBounds();
		this.texture = createTexture(bounds);
		this.nativeImageDataBuffer = new NativeImageDataBuffer(AssertionUtilities.assertNonnull(this.texture.getTextureData()));
		this.fullRaster = createRaster(bounds, getColorModel(), this.nativeImageDataBuffer);

		IEventBus eventBus = AssertionUtilities.assertNonnull(Bus.FORGE.bus().get());
		eventBus.addListener(EventPriority.NORMAL, true, this::onResize);
	}

	private static DynamicTexture createTexture(Rectangle2D bounds) {
		return new DynamicTexture((int) bounds.getWidth(), (int) bounds.getHeight(), true);
	}

	private static WritableRaster createRaster(Rectangle2D bounds, ColorModel colorModel, NativeImageDataBuffer nativeImageDataBuffer) {
		return WritableRasterNative.createNativeRaster(colorModel.createCompatibleSampleModel((int) bounds.getWidth(), (int) bounds.getHeight()),
				nativeImageDataBuffer);
	}

	private void onResize(GuiScreenEvent.InitGuiEvent.Pre event) {
		close();

		Rectangle bounds = getBounds();
		setTexture(createTexture(bounds));
		setNativeImageDataBuffer(new NativeImageDataBuffer(AssertionUtilities.assertNonnull(getTexture().getTextureData())));
		setFullRaster(createRaster(bounds, getColorModel(), getNativeImageDataBuffer()));
	}

	@Override
	public void close() {
		getNativeImageDataBuffer().close();
		// COMMENT must close the data buffer first
		getTexture().close();
	}

	protected DynamicTexture getTexture() {
		return texture;
	}

	protected NativeImageDataBuffer getNativeImageDataBuffer() {
		return nativeImageDataBuffer;
	}

	protected void setNativeImageDataBuffer(NativeImageDataBuffer nativeImageDataBuffer) {
		this.nativeImageDataBuffer = nativeImageDataBuffer;
	}

	protected void setTexture(DynamicTexture texture) {
		this.texture = texture;
	}

	public static MinecraftSurfaceData getInstance() {
		return AssertionUtilities.assertNonnull(INSTANCE.get());
	}

	public static MethodHandle getNativeImageImagePointerGetter() {
		return NATIVE_IMAGE_IMAGE_POINTER_GETTER;
	}

	@Override
	public SurfaceData getReplacement() {
		// COMMENT this should never be invalid
		return this;
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		return MinecraftGraphicsConfiguration.getInstance();
	}

	@Override
	public Raster getRaster(int x, int y, int w, int h) {
		return getFullRaster().createWritableChild(x, y, w, h, x, y, null);
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
		getNativeImageDataBuffer().clear();
	}

	public void draw() {
		Rectangle bounds = getBounds();
		DynamicTexture texture = getTexture();

		// COMMENT writes directly to 'NativeImage'
		texture.updateDynamicTexture();

		RenderSystem.bindTexture(texture.getGlTextureId());

		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.defaultBlendFunc();
		MinecraftDrawingUtilities.blit(AffineTransformUtilities.getIdentity(),
				bounds,
				new Point2D.Double(),
				new DoubleDimension2D(bounds.getWidth(), bounds.getHeight()));
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
	}

	@OnlyIn(Dist.CLIENT)
	private static final class NativeImageDataBuffer
			extends DataBuffer
			implements AutoCloseable {
		private final long pointer;
		private final long sizeInBytes;
		private final Object closedLockObject = new Object();
		private boolean closed = false;

		private NativeImageDataBuffer(NativeImage nativeImage) {
			super(DataBuffer.TYPE_INT, nativeImage.getWidth() * nativeImage.getHeight());
			assert nativeImage.getFormat() == NativeImage.PixelFormat.RGBA;
			try {
				this.pointer = (long) getNativeImageImagePointerGetter().invokeExact(nativeImage);
			} catch (Throwable throwable) {
				throw ThrowableUtilities.propagate(throwable);
			}
			this.sizeInBytes = Integer.BYTES * getSize();
		}

		@Override
		public int getElem(int bank, int i) {
			assert bank == 0;
			long offset = Integer.BYTES * i;
			assert offset >= 0;
			assert offset < getSizeInBytes();
			synchronized (getClosedLockObject()) {
				if (!isClosed())
					return MemoryUtil.memGetInt(getPointer() + offset);
			}
			return 0;
		}

		protected long getSizeInBytes() {
			return sizeInBytes;
		}

		protected final Object getClosedLockObject() {
			return closedLockObject;
		}

		protected boolean isClosed() {
			return closed;
		}

		protected long getPointer() {
			return pointer;
		}

		@Override
		public void setElem(int bank, int i, int val) {
			assert bank == 0;
			long offset = Integer.BYTES * i;
			assert offset >= 0;
			assert offset < getSizeInBytes();
			synchronized (getClosedLockObject()) {
				if (!isClosed())
					MemoryUtil.memPutInt(getPointer() + offset, val);
			}
		}

		@Override
		public void close() {
			synchronized (getClosedLockObject()) {
				markClosed();
			}
		}

		protected void markClosed() {
			this.closed = true;
		}

		protected void clear() {
			synchronized (getClosedLockObject()) {
				if (!isClosed())
					MemoryUtil.memSet(getPointer(), 0, getSizeInBytes());
			}
		}
	}
}
