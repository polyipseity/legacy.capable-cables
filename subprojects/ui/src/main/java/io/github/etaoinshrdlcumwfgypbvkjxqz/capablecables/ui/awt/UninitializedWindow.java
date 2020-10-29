package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.awt;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.MinecraftGraphics;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.MinecraftGraphicsConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.MinecraftSurfaceData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import sun.awt.CausedFocusEvent;
import sun.awt.image.ToolkitImage;
import sun.font.FontDesignMetrics;
import sun.java2d.pipe.Region;

import java.awt.*;
import java.awt.event.PaintEvent;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.peer.ComponentPeer;
import java.awt.peer.ContainerPeer;
import java.security.AccessController;
import java.util.function.Supplier;

@Deprecated
public final class UninitializedWindow
		extends Window {
	private static final long serialVersionUID = -2340013652599225354L;
	private static final Supplier<UninitializedWindow> INSTANCE;

	static {
		INSTANCE = Suppliers.memoize(() -> {
			UninitializedWindow result;
			try {
				result = (UninitializedWindow) DynamicUtilities.getUnsafe().allocateInstance(UninitializedWindow.class);
				AWTMinecraftHeadlessHacks.getWindowInputContextLockSetter().invokeExact((Window) result, new Object());
				AWTMinecraftHeadlessHacks.getComponentVisibleSetter().invokeExact((Component) result, true);
				AWTMinecraftHeadlessHacks.getAccessControlContextSetter().invokeExact((Component) result, AccessController.getContext());

				AWTMinecraftHeadlessHacks.getComponentPeerSetter().invokeExact((Component) result, (ComponentPeer) Peer.getInstance());
			} catch (Throwable throwable) {
				throw ThrowableUtilities.propagate(throwable);
			}
			return result;
		});
	}

	private UninitializedWindow(Window owner) {
		super(owner);
	}

	public static UninitializedWindow getInstance() {
		return AssertionUtilities.assertNonnull(INSTANCE.get());
	}

	@Override
	public Graphics getGraphics() {
		return MinecraftGraphics.createGraphics();
	}

	private static final class Peer
			implements ComponentPeer {
		private static final Supplier<Peer> INSTANCE = Suppliers.memoize(Peer::new);

		public static Peer getInstance() {
			return AssertionUtilities.assertNonnull(INSTANCE.get());
		}

		@Override
		public boolean isObscured() {
			return false;
		}

		@Override
		public boolean canDetermineObscurity() {
			return true;
		}

		@Override
		public void setVisible(boolean v) {

		}

		@Override
		public void setEnabled(boolean e) {

		}

		@Override
		public void paint(Graphics g) {
			// COMMENT NOOP
		}

		@Override
		public void print(Graphics g) {
			// COMMENT NOOP
		}

		@Override
		public void setBounds(int x, int y, int width, int height, int op) {
			// COMMENT NOOP
		}

		@Override
		public void handleEvent(AWTEvent e) {
			// COMMENT NOOP
		}

		@Override
		public void coalescePaintEvent(PaintEvent e) {
			// COMMENT NOOP
		}

		@Override
		public Point getLocationOnScreen() {
			return new Point();
		}

		@Override
		public Dimension getPreferredSize() {
			return MinecraftSurfaceData.getInstance().getBounds().getSize();
		}

		@Override
		public Dimension getMinimumSize() {
			return new Dimension();
		}

		@Override
		public ColorModel getColorModel() {
			return MinecraftSurfaceData.getInstance().getColorModel();
		}

		@Override
		public Graphics2D getGraphics() {
			return MinecraftGraphics.createGraphics();
		}

		@Override
		public FontMetrics getFontMetrics(Font font) {
			return FontDesignMetrics.getMetrics(font);
		}

		@Override
		public void dispose() {
			// COMMENT NOOP
		}

		@Override
		public void setForeground(Color c) {
			getGraphics().setColor(c);
		}

		@Override
		public void setBackground(Color c) {
			getGraphics().setBackground(c);
		}

		@Override
		public void setFont(Font f) {
			getGraphics().setFont(f);
		}

		@Override
		public void updateCursorImmediately() {
			// COMMENT NOOP
		}

		@Override
		public boolean requestFocus(Component lightweightChild, boolean temporary, boolean focusedWindowChangeAllowed, long time, CausedFocusEvent.Cause cause) {
			// COMMENT NOOP
			return false;
		}

		@Override
		public boolean isFocusable() {
			return false;
		}

		@Override
		public Image createImage(ImageProducer producer) {
			return new ToolkitImage(producer);
		}

		@Override
		public Image createImage(int width, int height) {
			return getGraphicsConfiguration().createCompatibleImage(width, height);
		}

		@Override
		public VolatileImage createVolatileImage(int width, int height) {
			return getGraphicsConfiguration().createCompatibleVolatileImage(width, height);
		}

		@Override
		public boolean prepareImage(Image img, int w, int h, ImageObserver o) {
			return Toolkit.getDefaultToolkit().prepareImage(img, w, h, o);
		}

		@Override
		public int checkImage(Image img, int w, int h, ImageObserver o) {
			return Toolkit.getDefaultToolkit().checkImage(img, w, h, o);
		}

		@Override
		public GraphicsConfiguration getGraphicsConfiguration() {
			return MinecraftGraphicsConfiguration.getInstance();
		}

		@Override
		public boolean handlesWheelScrolling() {
			return false;
		}

		@Override
		public void createBuffers(int numBuffers, BufferCapabilities caps) {
			// COMMENT NOOP
		}

		@Override
		public Image getBackBuffer() {
			assert false;
			return null;
		}

		@Override
		public void flip(int x1, int y1, int x2, int y2, BufferCapabilities.FlipContents flipAction) {
			// COMMENT NOOP
		}

		@Override
		public void destroyBuffers() {
			// COMMENT NOOP
		}

		@Override
		public void reparent(ContainerPeer newContainer) {
			// COMMENT NOOP
		}

		@Override
		public boolean isReparentSupported() {
			return false;
		}

		@Override
		public void layout() {
			// COMMENT NOOP
		}

		@Override
		public void applyShape(Region shape) {
			// COMMENT NOOP
		}

		@Override
		public void setZOrder(ComponentPeer above) {
			// COMMENT NOOP
		}

		@Override
		public boolean updateGraphicsData(GraphicsConfiguration gc) {
			// COMMENT NOOP
			return false;
		}
	}
}
