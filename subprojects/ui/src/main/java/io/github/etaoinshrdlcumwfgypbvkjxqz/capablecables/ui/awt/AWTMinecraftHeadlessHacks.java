package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.awt;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sun.awt.NullComponentPeer;

import java.awt.*;
import java.awt.peer.ComponentPeer;
import java.awt.peer.LightweightPeer;
import java.lang.invoke.MethodHandle;
import java.security.AccessControlContext;

@OnlyIn(Dist.CLIENT)
@Deprecated
public enum AWTMinecraftHeadlessHacks {
	;

	private static final MethodHandle WINDOW_INPUT_CONTEXT_LOCK_SETTER;
	private static final MethodHandle COMPONENT_VISIBLE_SETTER;
	private static final MethodHandle ACCESS_CONTROL_CONTEXT_SETTER;
	private static final MethodHandle COMPONENT_PARENT_SETTER;
	private static final MethodHandle COMPONENT_PEER_SETTER;
	private static final LightweightPeer LIGHTWEIGHT_PEER = new NullComponentPeer();
	private static final MethodHandle TOOLKIT_TOOLKIT_SETTER;

	static {
		try {
			TOOLKIT_TOOLKIT_SETTER = InvokeUtilities.getImplLookup().findStaticSetter(Toolkit.class, "toolkit", Toolkit.class);
			COMPONENT_PARENT_SETTER = InvokeUtilities.getImplLookup().findSetter(Component.class, "parent", Container.class);
			COMPONENT_PEER_SETTER = InvokeUtilities.getImplLookup().findSetter(Component.class, "peer", ComponentPeer.class);
			WINDOW_INPUT_CONTEXT_LOCK_SETTER = InvokeUtilities.getImplLookup().findSetter(Window.class, "inputContextLock", Object.class);
			COMPONENT_VISIBLE_SETTER = InvokeUtilities.getImplLookup().findSetter(Component.class, "visible", boolean.class);
			ACCESS_CONTROL_CONTEXT_SETTER = InvokeUtilities.getImplLookup().findSetter(Component.class, "acc", AccessControlContext.class);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	static LightweightPeer getLightweightPeer() {
		return LIGHTWEIGHT_PEER;
	}

	static MethodHandle getComponentPeerSetter() {
		return COMPONENT_PEER_SETTER;
	}

	static MethodHandle getComponentParentSetter() {
		return COMPONENT_PARENT_SETTER;
	}

	public static void hack() {
		try {
			getToolkitToolkitSetter().invokeExact((Toolkit) MinecraftToolkit.getInstance());
		} catch (Throwable throwable) {
			throw ThrowableUtilities.propagate(throwable);
		}
	}

	static MethodHandle getToolkitToolkitSetter() {
		return TOOLKIT_TOOLKIT_SETTER;
	}

	static MethodHandle getAccessControlContextSetter() {
		return ACCESS_CONTROL_CONTEXT_SETTER;
	}

	static MethodHandle getComponentVisibleSetter() {
		return COMPONENT_VISIBLE_SETTER;
	}

	static MethodHandle getWindowInputContextLockSetter() {
		return WINDOW_INPUT_CONTEXT_LOCK_SETTER;
	}
}
