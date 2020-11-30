package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.ManualLoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MainWindow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NonNls;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.*;
import java.util.function.BiConsumer;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressBoxing;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressUnboxing;

@OnlyIn(Dist.CLIENT)
public enum MinecraftOpenGLUtilities {
	;

	public static final int GL_MASK_ALL_BITS = 0xFFFFFFFF;

	public static long getWindowHandle() { return MinecraftClientUtilities.getMinecraftNonnull().getMainWindow().getHandle(); }

	public static int getGlMaskAllBits() {
		return GL_MASK_ALL_BITS;
	}

	@OnlyIn(Dist.CLIENT)
	public enum Stacks {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

		private static final Runnable GL_SCISSOR_FALLBACK = () -> {
			MainWindow window = MinecraftClientUtilities.getMinecraftNonnull().getMainWindow();
			State.setIntegerValue(GL11.GL_SCISSOR_BOX, new int[]{0, 0, window.getFramebufferWidth(), window.getFramebufferHeight()},
					(i, v) -> GL11.glScissor(v[0], v[1], v[2], v[3]));
		};
		private static final Runnable STENCIL_MASK_FALLBACK = () -> RenderSystem.stencilMask(MinecraftOpenGLUtilities.getGlMaskAllBits());
		private static final Runnable STENCIL_FUNC_FALLBACK = () -> RenderSystem.stencilFunc(GL11.GL_ALWAYS, 0, MinecraftOpenGLUtilities.getGlMaskAllBits());
		private static final Runnable STENCIL_OP_FALLBACK = () -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		private static final Runnable COLOR_MASK_FALLBACK = () -> RenderSystem.colorMask(true, true, true, true);
		private static final LoadingCache<String, Deque<GLCall>> STACKS =
				ManualLoadingCache.newNestedLoadingCacheCollection(
						CacheUtilities.newCacheBuilderSingleThreaded()
								.initialCapacity(CapacityUtilities.getInitialCapacityMedium())
								.build(CacheLoader.from(k -> new ArrayDeque<>(CapacityUtilities.getInitialCapacityMedium()))));

		public static void clearAll() {
			getStacks().asMap().keySet().stream().unordered()
					.forEach(Stacks::clear);
			assert getStacks().asMap().isEmpty();
		}

		private static LoadingCache<String, Deque<GLCall>> getStacks() {
			return STACKS;
		}

		public static void clear(@NonNls CharSequence name) {
			Optional.ofNullable(getStacks().getIfPresent(name.toString()))
					.filter(s -> !s.isEmpty())
					.ifPresent(s -> {
						UtilitiesConfiguration.getInstance().getLogger()
								.atWarn()
								.addMarker(UtilitiesMarkers.getInstance().getMarkerOpenGL())
								.addKeyValue("name", name)
								.addArgument(s)
								.log(() -> getResourceBundle().getString("clear.dirty"));
						while (!s.isEmpty())
							pop(name);
					});
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		public static void pop(@NonNls CharSequence name) {
			@Nullable Deque<GLCall> s = getStacks().getIfPresent(name.toString());
			if (s == null)
				throw new NoSuchElementException(
						new LogMessageBuilder()
								.addMarkers(UtilitiesMarkers.getInstance()::getMarkerOpenGL)
								.addKeyValue("name", name)
								.addMessages(() -> getResourceBundle().getString("pop.empty"))
								.build()
				);
			Runnable fb = s.pop().getFallback();
			(s.isEmpty() ? fb : AssertionUtilities.assertNonnull(s.element())).run();
			getStacks().cleanUp();
		}

		public static void push(@NonNls CharSequence name, Runnable action, Runnable fallback) {
			getStacks().getUnchecked(name.toString()).push(new GLCall(action, fallback));
			action.run();
		}

		public static Runnable getGlScissorFallback() {
			return GL_SCISSOR_FALLBACK;
		}

		public static Runnable getStencilMaskFallback() {
			return STENCIL_MASK_FALLBACK;
		}

		public static Runnable getStencilFuncFallback() {
			return STENCIL_FUNC_FALLBACK;
		}

		public static Runnable getStencilOpFallback() {
			return STENCIL_OP_FALLBACK;
		}

		public static Runnable getColorMaskFallback() {
			return COLOR_MASK_FALLBACK;
		}

		@OnlyIn(Dist.CLIENT)
		@Immutable
		private static final class GLCall
				implements Runnable {
			private final Runnable action, fallback;

			private GLCall(Runnable action, Runnable fallback) {
				this.action = action;
				this.fallback = fallback;
			}

			@Override
			public void run() { getAction().run(); }

			private Runnable getAction() { return action; }

			private Runnable getFallback() { return fallback; }
		}
	}

	@OnlyIn(Dist.CLIENT)
	public enum State {
		;

		private static final Int2ObjectMap<Object> STATE = new Int2ObjectOpenHashMap<>(CapacityUtilities.getInitialCapacityMedium());

		public static int getInteger(int name) {
			return suppressUnboxing((Integer)
					getState().computeIfAbsent(name, name1 -> suppressBoxing(GL11.glGetInteger(name1)))
			);
		}

		private static Int2ObjectMap<Object> getState() {
			return STATE;
		}

		public static void setInteger(int name, int param, BiConsumer<@Nonnull Integer, @Nonnull Integer> setter) {
			setter.accept(suppressBoxing(name), suppressBoxing(param));
			getState().put(name, Integer.valueOf(param));
		}

		public static void getIntegerValue(int name, int[] params) {
			int[] ret = (int[]) getState().computeIfAbsent(name, n -> {
				int[] p = new int[params.length];
				try (MemoryStack stack = MemoryStack.stackPush()) {
					IntBuffer p1 = stack.mallocInt(p.length);
					GL11.glGetIntegerv(n, p1);
					p1.get(p);
				}
				return p;
			});
			System.arraycopy(ret, 0, params, 0, params.length);
		}

		public static void setIntegerValue(int name, int[] params, @SuppressWarnings("NullableProblems") BiConsumer<@Nonnull Integer, @Nonnull int[]> setter) {
			setter.accept(suppressBoxing(name), params);
			getState().put(name, params.clone());
		}
	}
}
