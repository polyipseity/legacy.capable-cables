package $group__.utilities.minecraft.client;

import $group__.utilities.*;
import $group__.utilities.collections.CacheUtilities;
import $group__.utilities.collections.ManualLoadingCache;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.templates.CommonConfigurationTemplate;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.MouseHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NonNls;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

@OnlyIn(Dist.CLIENT)
public enum GLUtilities {
	;

	public static final int
			GL_MASK_ALL_BITS = 0xFFFFFFFF;

	public static Point2D getCursorPos() {
		MouseHelper mh = ClientUtilities.getMinecraftNonnull().mouseHelper;
		return new Point2D.Double(mh.getMouseX(), mh.getMouseY());
	}

	public static long getWindowHandle() { return ClientUtilities.getMinecraftNonnull().getMainWindow().getHandle(); }

	@OnlyIn(Dist.CLIENT)
	public enum Stacks {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

		public static void clear(@NonNls String name) {
			Optional.ofNullable(STACKS.getIfPresent(name))
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

		public static final Runnable GL_SCISSOR_FALLBACK = () -> {
			MainWindow window = ClientUtilities.getMinecraftNonnull().getMainWindow();
			State.setIntegerValue(GL11.GL_SCISSOR_BOX, new int[]{0, 0, window.getFramebufferWidth(), window.getFramebufferHeight()}, (i, v) -> GL11.glScissor(v[0], v[1], v[2], v[3]));
		},
				STENCIL_MASK_FALLBACK = () -> RenderSystem.stencilMask(GLUtilities.GL_MASK_ALL_BITS),
				STENCIL_FUNC_FALLBACK = () -> RenderSystem.stencilFunc(GL11.GL_ALWAYS, 0, GLUtilities.GL_MASK_ALL_BITS),
				STENCIL_OP_FALLBACK = () -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP),
				COLOR_MASK_FALLBACK = () -> RenderSystem.colorMask(true, true, true, true);

		private static final LoadingCache<String, Deque<GLCall>> STACKS =
				ManualLoadingCache.newNestedLoadingCacheCollection(
						CacheUtilities.newCacheBuilderSingleThreaded()
								.initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM)
								.build(CacheLoader.from(k -> new ArrayDeque<>(CapacityUtilities.INITIAL_CAPACITY_MEDIUM))));

		public static void push(@NonNls String name, Runnable action, Runnable fallback) {
			STACKS.getUnchecked(name).push(new GLCall(action, fallback));
			action.run();
		}

		public static void clearAll() {
			STACKS.asMap().keySet().stream().unordered()
					.forEach(Stacks::clear);
			assert STACKS.asMap().isEmpty();
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

		public static void pop(@NonNls String name) {
			@Nullable Deque<GLCall> s = STACKS.getIfPresent(name);
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
			STACKS.cleanUp();
		}

		@OnlyIn(Dist.CLIENT)
		private static final class GLCall implements Runnable {
			private final Runnable action, fallback;

			private GLCall(Runnable action, Runnable fallback) {
				this.action = action;
				this.fallback = fallback;
			}

			@Override
			public void run() { action.run(); }

			private Runnable getAction() { return action; }

			private Runnable getFallback() { return fallback; }
		}
	}

	@OnlyIn(Dist.CLIENT)
	public enum State {
		;

		private static final ConcurrentMap<Integer, Object> STATE =
				MapUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();

		public static int getInteger(int name) { return (int) STATE.computeIfAbsent(name, GL11::glGetInteger); }

		public static void setInteger(int name, int param, BiConsumer<Integer, Integer> setter) {
			setter.accept(name, param);
			STATE.put(name, param);
		}

		public static void getIntegerValue(int name, int[] params) {
			int[] ret = (int[]) STATE.computeIfAbsent(name, n -> {
				int[] p = new int[params.length];
				GL11.glGetIntegerv(n, p);
				return p;
			});
			System.arraycopy(ret, 0, params, 0, params.length);
		}

		public static void setIntegerValue(int name, int[] params, BiConsumer<Integer, int[]> setter) {
			setter.accept(name, params);
			STATE.put(name, params.clone());
		}
	}
}
