package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContextMutator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.UIDefaultComponentContextLeakNotifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.paths.IPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.paths.FunctionalPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import sun.misc.Cleaner;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UIDefaultComponentContext
		extends UIAbstractComponentContext {
	private static final long PATH_FIELD_OFFSET;
	private static final long GRAPHICS_FIELD_OFFSET;
	private static final long TRANSFORM_STACK_FIELD_OFFSET;
	private static final long CLIP_STACK_FIELD_OFFSET;
	private static final long CLOSED_FIELD_OFFSET;

	static {
		try {
			GRAPHICS_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(UIDefaultComponentContext.class.getDeclaredField("graphics"));
			PATH_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(UIDefaultComponentContext.class.getDeclaredField("path"));
			TRANSFORM_STACK_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(UIDefaultComponentContext.class.getDeclaredField("transformStack"));
			CLIP_STACK_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(UIDefaultComponentContext.class.getDeclaredField("clipStack"));
			CLOSED_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(UIDefaultComponentContext.class.getDeclaredField("closed"));
		} catch (NoSuchFieldException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	private final IUIViewContext viewContext;
	private final IUIViewComponent<?, ?> view;
	private final IUIComponentContextMutator mutator;
	private final @Nullable Graphics2D graphics;
	private final IPath<IUIComponent> path;
	private final Deque<AffineTransform> transformStack;
	private final Deque<Shape> clipStack;
	private final boolean[] closed;

	public UIDefaultComponentContext(IUIViewContext viewContext,
	                                 IUIViewComponent<?, ?> view,
	                                 IUIComponentContextMutator mutator) {
		this.viewContext = viewContext;
		this.view = view;
		this.mutator = mutator;
		this.graphics = viewContext.getOutputDevices().createGraphics().orElse(null);

		this.path = new FunctionalPath<>(ImmutableList.of(), Lists::newArrayList);
		this.transformStack = new ArrayDeque<>(CapacityUtilities.getInitialCapacityMedium());
		this.transformStack.push(new AffineTransform());
		this.clipStack = new ArrayDeque<>(CapacityUtilities.getInitialCapacityMedium());

		this.closed = new boolean[]{false};

		registerLeakNotifier(suppressThisEscapedWarning(() -> this));
	}

	protected static void registerLeakNotifier(UIDefaultComponentContext instance) {
		boolean[] closed = instance.getClosed();
		Cleaner.create(instance,
				new UIDefaultComponentContextLeakNotifier(() -> closed[0], UIConfiguration.getInstance().getLogger()));
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected final boolean[] getClosed() {
		return closed;
	}

	@Override
	public IPath<IUIComponent> getPathRef() {
		return getPath();
	}

	protected IPath<IUIComponent> getPath() {
		return path;
	}

	@Override
	public Deque<AffineTransform> getTransformStackRef() {
		return getTransformStack();
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Deque<AffineTransform> getTransformStack() {
		return transformStack;
	}

	@Override
	public Deque<Shape> getClipStackRef() { return getClipStack(); }

	@Override
	public Optional<? extends Graphics2D> getGraphicsRef() {
		return getGraphics();
	}

	protected Optional<? extends Graphics2D> getGraphics() {
		return Optional.ofNullable(graphics);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Deque<Shape> getClipStack() {
		return clipStack;
	}

	@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
	@Override
	public UIDefaultComponentContext clone() {
		UIDefaultComponentContext result;
		try {
			result = (UIDefaultComponentContext) super.clone();
			DynamicUtilities.getUnsafe().putObject(result, getGraphicsFieldOffset(), Optional.ofNullable(result.graphics).map(Graphics::create).orElse(null));
			DynamicUtilities.getUnsafe().putObject(result, getPathFieldOffset(), result.path.clone());
			DynamicUtilities.getUnsafe().putObject(result, getTransformStackFieldOffset(), new ArrayDeque<>(result.transformStack));
			DynamicUtilities.getUnsafe().putObject(result, getClipStackFieldOffset(), new ArrayDeque<>(result.clipStack));
			DynamicUtilities.getUnsafe().putObjectVolatile(result, getClosedFieldOffset(), result.closed.clone());
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
		registerLeakNotifier(result);
		return result;
	}

	@Override
	public void close() {
		getClosed()[0] = true;
		super.close();
	}

	protected static long getGraphicsFieldOffset() {
		return GRAPHICS_FIELD_OFFSET;
	}

	protected static long getPathFieldOffset() {
		return PATH_FIELD_OFFSET;
	}

	protected static long getTransformStackFieldOffset() {
		return TRANSFORM_STACK_FIELD_OFFSET;
	}

	protected static long getClipStackFieldOffset() {
		return CLIP_STACK_FIELD_OFFSET;
	}

	protected static long getClosedFieldOffset() {
		return CLOSED_FIELD_OFFSET;
	}

	@Override
	public IUIViewComponent<?, ?> getView() {
		return view;
	}

	@Override
	public IUIComponentContextMutator getMutator() {
		return mutator;
	}

	@Override
	public @Immutable IUIViewContext getViewContext() {
		return viewContext;
	}
}
