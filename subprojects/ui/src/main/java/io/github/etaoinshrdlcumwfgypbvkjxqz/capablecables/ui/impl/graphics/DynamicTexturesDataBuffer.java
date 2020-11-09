package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ArrayUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ColorUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LoopUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.PrimitiveUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.DataBuffer;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public final class DynamicTexturesDataBuffer
		extends DataBuffer
		implements AutoCloseable {
	public static final int IDEAL_TEXTURE_SIZE = 256;
	private static final int TARGET_TEXTURE_SIZE;
	private static final MethodHandle NATIVE_IMAGE_IMAGE_POINTER_GETTER;

	static {
		TARGET_TEXTURE_SIZE = Math.min(getIdealTextureSize(), RenderSystem.maxSupportedTextureSize());
		try {
			NATIVE_IMAGE_IMAGE_POINTER_GETTER = InvokeUtilities.getImplLookup().findGetter(NativeImage.class, "imagePointer", long.class);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	private final int width;
	private final int height;
	private final int textureSize;
	private final long textureMemorySize;
	private final boolean closeSynchronized;
	private final int texturesWidth;
	private final int texturesHeight;
	private final DynamicTexture[][] textures;
	private final long[][] texturePointers;
	private final Lock closeLock = new ReentrantLock();
	private boolean closed = false;

	public DynamicTexturesDataBuffer(int width, int height, int textureSize, boolean closeSynchronized) {
		super(DataBuffer.TYPE_INT, width * height);
		this.width = width;
		this.height = height;
		this.textureSize = textureSize;
		this.textureMemorySize = Integer.BYTES * this.textureSize * this.textureSize;
		this.closeSynchronized = closeSynchronized;

		this.texturesWidth = Math.toIntExact(PrimitiveUtilities.toIntegerExact(Math.ceil((double) this.width / this.textureSize)));
		this.texturesHeight = Math.toIntExact(PrimitiveUtilities.toIntegerExact(Math.ceil((double) this.height / this.textureSize)));
		this.textures = new DynamicTexture[this.texturesWidth][this.texturesHeight];
		this.texturePointers = new long[this.texturesWidth][this.texturesHeight];

		try {
			LoopUtilities.doNTimesNested(IThrowingConsumer.executeNow(indexes -> {
						int x = Math.toIntExact(indexes[0]);
						int y = Math.toIntExact(indexes[1]);
						@SuppressWarnings("ObjectAllocationInLoop") DynamicTexture texture = new DynamicTexture(this.textureSize, this.textureSize, true);
						assert texture.getTextureData().getFormat() == NativeImage.PixelFormat.RGBA;
						this.textures[x][y] = texture;
						this.texturePointers[x][y] = (long) getNativeImageImagePointerGetter().invokeExact((NativeImage) texture.getTextureData());
					})
					, this.texturesWidth, this.texturesHeight);
		} catch (Throwable throwable) {
			throw ThrowableUtilities.propagate(throwable);
		}
	}

	public static MethodHandle getNativeImageImagePointerGetter() {
		return NATIVE_IMAGE_IMAGE_POINTER_GETTER;
	}

	public static DynamicTexture getTexture(DynamicTexturesDataBuffer instance, int x, int y) {
		return instance.getTextures()[x][y];
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected DynamicTexture[][] getTextures() {
		return textures;
	}

	public static int getIdealTextureSize() {
		return IDEAL_TEXTURE_SIZE;
	}

	public static int getTargetTextureSize() {
		return TARGET_TEXTURE_SIZE;
	}

	public int getTexturesWidth() {
		return texturesWidth;
	}

	public int getTexturesHeight() {
		return texturesHeight;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public int getElem(int bank, int i) {
		/* COMMENT
		avoid creating objects, this method is called thousands of times every frame
		this also means we will avoid using 'runIfOpen' as it creates functional objects
		*/
		if (isCloseSynchronized()) {
			Lock closeLock = getCloseLock();
			closeLock.lock();
			try {
				return isClosed() ? 0 : MemoryUtil.memGetInt(getElemPointer(this, bank, i));
			} finally {
				closeLock.unlock();
			}
		} else
			return isClosed() ? 0 : MemoryUtil.memGetInt(getElemPointer(this, bank, i));
	}

	@Override
	public void setElem(int bank, int i, int val) {
		/* COMMENT
		avoid creating objects, this method is called thousands of times every frame
		this also means we will avoid using 'runIfOpen' as it creates functional objects
		*/
		if (isCloseSynchronized()) {
			Lock closeLock = getCloseLock();
			closeLock.lock();
			try {
				if (!isClosed())
					MemoryUtil.memPutInt(getElemPointer(this, bank, i), val);
			} finally {
				closeLock.unlock();
			}
		} else {
			if (!isClosed())
				MemoryUtil.memPutInt(getElemPointer(this, bank, i), val);
		}
	}

	public boolean isCloseSynchronized() {
		return closeSynchronized;
	}

	protected Lock getCloseLock() {
		return closeLock;
	}

	public boolean isClosed() {
		return closed;
	}

	protected static long getElemPointer(DynamicTexturesDataBuffer instance, int bank, int i) {
		assert bank == 0; // COMMENT we should only have 1 bank
		int x = Math.floorMod(i, instance.getWidth()), y = i / instance.getWidth(),
				textureX = toTextureCoordinate(instance, x), textureY = toTextureCoordinate(instance, y),
				textureOffsetX = x - toAbsoluteCoordinate(instance, textureX), textureOffsetY = y - toAbsoluteCoordinate(instance, textureY),
				textureOffset = Integer.BYTES * (instance.getTextureSize() * textureOffsetY + textureOffsetX);
		// COMMENT important assertion to ensure that data is read or written to a valid memory address
		assert textureOffset <= instance.getTextureMemorySize() - Integer.BYTES;
		return getTexturePointer(instance, textureX, textureY) + textureOffset;
	}

	public int getWidth() {
		return width;
	}

	public static int toTextureCoordinate(DynamicTexturesDataBuffer instance, int coordinate) {
		return coordinate / instance.getTextureSize();
	}

	public static int toAbsoluteCoordinate(DynamicTexturesDataBuffer instance, int coordinate) {
		return coordinate * instance.getTextureSize();
	}

	public int getTextureSize() {
		return textureSize;
	}

	public long getTextureMemorySize() {
		return textureMemorySize;
	}

	public static long getTexturePointer(DynamicTexturesDataBuffer instance, int x, int y) {
		return instance.getTexturePointers()[x][y];
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected long[][] getTexturePointers() {
		return texturePointers;
	}

	public DynamicTexture[][] getTexturesView() {
		return ArrayUtilities.deepClone(getTextures());
	}

	@Override
	public void close() {
		runIfOpen(this, instance -> {
			instance.markClosed();
			Arrays.stream(instance.getTextures()).unordered()
					.flatMap(Arrays::stream)
					.forEach(DynamicTexture::close);
		});
	}

	public static <T extends DynamicTexturesDataBuffer> void runIfOpen(T instance, Consumer<@Nonnull ? super T> action) {
		applyIfOpen(instance, FunctionUtilities.asVoidFunction(action));
	}

	protected void markClosed() {
		this.closed = true;
	}

	public static <T extends DynamicTexturesDataBuffer, R> Optional<R> applyIfOpen(T instance, Function<@Nonnull ? super T, @Nullable ? extends R> action) {
		if (instance.isCloseSynchronized()) {
			Lock closeLock = instance.getCloseLock();
			closeLock.lock();
			try {
				return instance.isClosed() ? Optional.empty() : Optional.ofNullable(action.apply(instance));
			} finally {
				closeLock.unlock();
			}
		} else
			return instance.isClosed() ? Optional.empty() : Optional.ofNullable(action.apply(instance));
	}

	public void clear() {
		runIfOpen(this, instance ->
				Arrays.stream(instance.getTexturePointers()).unordered()
						.flatMapToLong(Arrays::stream)
						.forEach(pointer -> MemoryUtil.memSet(pointer, ColorUtilities.getColorless().getRGB(), instance.getTextureMemorySize()))
		);
	}
}
