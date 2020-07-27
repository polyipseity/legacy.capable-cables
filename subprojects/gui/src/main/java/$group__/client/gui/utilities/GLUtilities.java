package $group__.client.gui.utilities;

import $group__.utilities.specific.Maps;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentMap;

import static $group__.utilities.Capacities.INITIAL_CAPACITY_2;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum GLUtilities {
	;

	public static long getWindowHandle() { return Minecraft.getInstance().getMainWindow().getHandle(); }

	public static Point2D getCursorPos() {
		double[] xPos = new double[1], yPos = new double[1];
		GLFW.glfwGetCursorPos(getWindowHandle(), xPos, yPos);
		return new Point2D.Double(xPos[0], yPos[0]);
	}

	@OnlyIn(CLIENT)
	public enum GLStacks {
		;

		public static final Runnable GL_SCISSOR_FALLBACK = () -> {
			MainWindow window = Minecraft.getInstance().getMainWindow();
			GL11.glScissor(0, 0, window.getFramebufferWidth(), window.getFramebufferHeight());
		};

		private static final ConcurrentMap<String, Deque<GLCall>> STACKS = Maps.MAP_MAKER_SINGLE_THREAD.makeMap();

		private static Deque<GLCall> getStack(String name) { return STACKS.computeIfAbsent(name, s -> new ArrayDeque<>(INITIAL_CAPACITY_2)); }

		public static void push(String name, Runnable action, Runnable fallback) {
			getStack(name).push(new GLCall(action, fallback));
			action.run();
		}

		public static void pop(String name) {
			Deque<GLCall> stack = getStack(name);
			Runnable fallback = stack.pop().fallback;
			(stack.isEmpty() ? fallback : stack.peek()).run();
		}

		public static void reset(String name) {
			Deque<GLCall> stack = getStack(name);
			while (!stack.isEmpty()) pop(name);
		}

		public static void resetAll() {
			STACKS.keySet().forEach(GLStacks::reset);
			STACKS.clear();
		}

		@OnlyIn(CLIENT)
		private static class GLCall implements Runnable {
			private final Runnable action, fallback;

			private GLCall(Runnable action, Runnable fallback) {
				this.action = action;
				this.fallback = fallback;
			}

			@Override
			public void run() { action.run(); }
		}
	}
}
