package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.adapters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIContextContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.adapters.IUIAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.cursors.IUICursorHandleProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventKeyboard;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIEventUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.IUIMinecraftInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.extensions.IUIMinecraftScreenProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.extensions.UIImmutableMinecraftContainerProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.UIMinecraftBackgrounds;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.UIImmutableContextContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.viewmodels.UIImmutableViewModelContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.UIImmutableViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.UIInputUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftTextComponentUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftTooltipUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.inputs.MinecraftInputPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.INode;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.DoubleDimension2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IMouseButtonClickData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl.ImmutableInputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl.ImmutableKeyboardKeyPressData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl.ImmutableMouseButtonClickData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

@OnlyIn(Dist.CLIENT)
public class UIMinecraftScreenAdapter
		<I extends IUIMinecraftInfrastructure<?, ?, ?>, C extends Container>
		extends AbstractContainerScreenAdapter<I, C>
		implements IUIAdapter<I> {
	private final I infrastructure;
	@Nullable
	private final C containerObject;
	private final Set<Integer> closeKeys, changeFocusKeys;
	private final ConcurrentMap<Integer, IUIEventKeyboard>
			keyboardKeysBeingPressed = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
	private final ConcurrentMap<Integer, IUIEventMouse>
			mouseButtonsBeingPressed = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
	private final IUIContextContainer contextContainer =
			new UIImmutableContextContainer(
					new UIImmutableViewContext(
							new ImmutableInputDevices.Builder()
									.setPointerDevice(new MinecraftInputPointerDevice())
									.build()
					),
					new UIImmutableViewModelContext()
			);
	private boolean paused = false;
	@Nullable
	private IUIEventTarget targetBeingHoveredByMouse = null;
	private ITextComponent title;
	@Nullable
	private IUIEventTarget focus;
	@Nullable
	private ImmutableMouseButtonClickData lastMouseClickData = null;

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	protected UIMinecraftScreenAdapter(ITextComponent title, I infrastructure, @Nullable C containerObject, Set<Integer> closeKeys, Set<Integer> changeFocusKeys) {
		super(title);
		this.title = title;
		this.infrastructure = infrastructure;
		this.containerObject = containerObject;
		this.closeKeys = Collections.newSetFromMap(MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(closeKeys.size()).makeMap());
		this.closeKeys.addAll(closeKeys);
		this.changeFocusKeys = Collections.newSetFromMap(MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(changeFocusKeys.size()).makeMap());
		this.changeFocusKeys.addAll(changeFocusKeys);

		IExtensionContainer.addExtensionChecked(this.infrastructure, new UIDefaultMinecraftScreenProviderExtension(this));
		if (containerObject != null)
			IExtensionContainer.addExtensionChecked(this.infrastructure, new UIImmutableMinecraftContainerProviderExtension(this.containerObject));
	}

	@Override
	public ITextComponent getTitle() { return title; }

	public void setTitle(ITextComponent title) { this.title = title; }

	@Override
	@Deprecated
	public void render(int mouseX, int mouseY, float partialTicks) {
		IUIContextContainer context = getContextContainer();
		getInfrastructure().getView().render(context.getViewContext(), partialTicks);
		IUICursorHandleProviderExtension.StaticHolder.getType().getValue().find(getInfrastructure().getView()).ifPresent(ext ->
				GLFW.glfwSetCursor(
						MinecraftOpenGLUtilities.getWindowHandle(),
						ext.getCursorHandle().orElse(MemoryUtil.NULL)
				)
		);
	}

	protected IUIContextContainer getContextContainer() { return contextContainer; }

	@Override
	public I getInfrastructure() { return infrastructure; }

	@Override
	@Deprecated
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		IUIContextContainer context = getContextContainer();
		if (getFocus().map(f -> UIEventUtilities.dispatchEvent(
				addEventKeyboard(this, UIEventUtilities.Factory.createEventKeyDown(context.getViewContext(), f,
						new ImmutableKeyboardKeyPressData(key, scanCode, modifiers))))).orElse(true)) {
			if (getCloseKeys().contains(key))
				onClose();
			if (getChangeFocusKeys().contains(key))
				changeFocus((modifiers & GLFW.GLFW_MOD_SHIFT) == 0);
		}
		return true;
	}

	@Override
	@Deprecated
	public boolean shouldCloseOnEsc() { return getCloseKeys().contains(GLFW.GLFW_KEY_ESCAPE); }

	@Override
	@Deprecated
	protected void renderTooltip(ItemStack item, int mouseX, int mouseY) { MinecraftTooltipUtilities.renderTooltip(getMinecraft(), width, height, font, itemRenderer, item, mouseX, mouseY); }

	@Override
	@Deprecated
	public List<String> getTooltipFromItem(ItemStack item) { return MinecraftTooltipUtilities.getTooltipFromItem(getMinecraft(), item); }

	@Override
	@Deprecated
	public void renderTooltip(String tooltip, int mouseX, int mouseY) { MinecraftTooltipUtilities.renderTooltip(width, height, font, itemRenderer, tooltip, mouseX, mouseY); }

	@Override
	@Deprecated
	public void renderTooltip(List<String> tooltip, int mouseX, int mouseY) { MinecraftTooltipUtilities.renderTooltip(width, height, font, itemRenderer, tooltip, mouseX, mouseY); }

	@Override
	@Deprecated
	public void renderTooltip(List<String> tooltip, int mouseX, int mouseY, FontRenderer font) { MinecraftTooltipUtilities.renderTooltip(width, height, itemRenderer, tooltip, mouseX, mouseY, font); }

	@Override
	@Deprecated
	protected void renderComponentHoverEffect(ITextComponent component, int mouseX, int mouseY) { MinecraftTextComponentUtilities.renderComponentHoverEffect(getMinecraft(), width, height, font, component, mouseX, mouseY); }

	@Override
	@Deprecated
	public boolean handleComponentClicked(ITextComponent component) { return MinecraftTextComponentUtilities.handleComponentClicked(this, component); }

	@Override
	@Deprecated
	public void setSize(int width, int height) {
		super.setSize(width, height);
		getInfrastructure().getView().reshape(s ->
				s.adapt(new Rectangle2D.Double(0, 0, width, height)));
	}

	@Override
	@Deprecated
	public List<? extends IGuiEventListener> children() { return ImmutableList.of(); }

	@Override
	@Deprecated
	protected void init() {
		IUIInfrastructure.bindSafe(getInfrastructure(), getContextContainer());
		setSize(width, height);
		getInfrastructure().initialize();
	}

	@Override
	@Deprecated
	public void tick() { getInfrastructure().tick(); }

	@Override
	@Deprecated
	public void removed() {
		IUIContextContainer context = getContextContainer();

		GLFW.glfwSetCursor(MinecraftOpenGLUtilities.getWindowHandle(), MemoryUtil.NULL);
		{
			// COMMENT generate opposite synthetic events
			// COMMENT NO default actions
			ImmutableSet.copyOf(getKeyboardKeysBeingPressed().keySet()).stream().unordered()
					.forEach(k ->
							removeEventKeyboard(this, k).ifPresent(e2 ->
									UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.generateSyntheticEventKeyboardOpposite(e2))));
			Point2D cp = getContextContainer().getViewContext().getInputDevices().getPointerDevice().orElseThrow(AssertionError::new).getPositionView();
			ImmutableSet.copyOf(getMouseButtonsBeingPressed().keySet()).stream().unordered()
					.forEach(k ->
							removeEventMouse(this, AssertionUtilities.assertNonnull(k)).ifPresent(e2 ->
									UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.generateSyntheticEventMouseOpposite(e2, cp))));
			setTargetBeingHoveredByMouse(context, null, new ImmutableMouseButtonClickData(cp));
			setLastMouseClickData(context, null, null);
			setFocus(context, null);
		}
		getInfrastructure().removed();
		IUIInfrastructure.unbindSafe(getInfrastructure());
	}

	@Override
	@Deprecated
	public void renderBackground() { UIMinecraftBackgrounds.renderDefaultBackgroundAndNotify(getMinecraft(), width, height); }

	@Override
	@Deprecated
	public void renderBackground(int blitOffset) { UIMinecraftBackgrounds.renderDefaultBackgroundAndNotify(getMinecraft(), width, height, blitOffset); }

	@Override
	@Deprecated
	public void renderDirtBackground(int blitOffset) { UIMinecraftBackgrounds.renderDirtBackgroundAndNotify(getMinecraft(), width, height, blitOffset); }

	@Override
	@Deprecated
	public boolean isPauseScreen() { return isPaused(); }

	public boolean isPaused() { return paused; }

	public void setPaused(boolean paused) { this.paused = paused; }

	@Override
	@Deprecated
	public void resize(Minecraft client, int width, int height) {
		super.resize(client, width, height);
		// COMMENT resize calls init
	}

	protected Optional<? extends IUIEventTarget> getFocus() { return Optional.ofNullable(focus); }

	protected static <E extends IUIEventKeyboard> E addEventKeyboard(UIMinecraftScreenAdapter<?, ?> self, E event) {
		self.getKeyboardKeysBeingPressed().put(event.getData().getKey(), event);
		return event;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Integer> getCloseKeys() { return closeKeys; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Integer> getChangeFocusKeys() { return changeFocusKeys; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<Integer, IUIEventKeyboard> getKeyboardKeysBeingPressed() { return keyboardKeysBeingPressed; }

	protected boolean setFocus(IUIContextContainer context, @Nullable IUIEventTarget focus) {
		Optional<? extends IUIEventTarget> p = getFocus(), n = Optional.ofNullable(focus);
		if (n.map(IUIEventTarget::isFocusable).orElse(true) && !n.equals(p)) {
			@Nullable IUIEventTarget pv = p.orElse(null);
			p.ifPresent(f -> UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.createEventFocusOutPre(context.getViewContext(), f, focus)));
			n.ifPresent(f -> UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.createEventFocusInPre(context.getViewContext(), f, pv)));
			this.focus = focus;
			p.ifPresent(f -> UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.createEventFocusOutPost(context.getViewContext(), f, focus)));
			n.ifPresent(f -> UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.createEventFocusInPost(context.getViewContext(), f, pv)));
			return true;
		}
		return false;
	}

	public Set<Integer> getCloseKeysView() { return ImmutableSet.copyOf(getCloseKeys()); }

	@SuppressWarnings("UnstableApiUsage")
	public boolean addCloseKeys(Iterable<Integer> keys) {
		return Streams.stream(keys).unordered()
				.map(getCloseKeys()::add)
				.reduce(false, Boolean::logicalOr);
	}

	@SuppressWarnings("UnstableApiUsage")
	public boolean removeCloseKeys(Iterable<Integer> keys) {
		return Streams.stream(keys).unordered()
				.map(getCloseKeys()::remove)
				.reduce(false, Boolean::logicalOr);
	}

	public Set<Integer> getChangeFocusKeysView() { return ImmutableSet.copyOf(getChangeFocusKeys()); }

	@SuppressWarnings("UnstableApiUsage")
	public boolean addChangeFocusKeys(Iterable<Integer> keys) {
		return Streams.stream(keys).unordered()
				.map(getChangeFocusKeys()::add)
				.reduce(false, Boolean::logicalOr);
	}

	@SuppressWarnings("UnstableApiUsage")
	public boolean removeChangeFocusKeys(Iterable<Integer> keys) {
		return Streams.stream(keys).unordered()
				.map(getChangeFocusKeys()::remove)
				.reduce(false, Boolean::logicalOr);
	}

	@Override
	@Deprecated
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		IUIContextContainer context = getContextContainer();
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		ImmutableMouseButtonClickData d = new ImmutableMouseButtonClickData(cp, button);
		IUIEventTarget t = getInfrastructure().getView().getTargetAtPoint((Point2D) cp.clone());
		if (UIEventUtilities.dispatchEvent(addEventMouse(this, UIEventUtilities.Factory.createEventMouseDown(context.getViewContext(), t, d)))) {
			// TODO select
			// TODO drag or drop perhaps
			// TODO scroll/pan
		}
		return true;
	}

	@Override
	@Deprecated
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		IUIContextContainer context = getContextContainer();
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		ImmutableMouseButtonClickData d = new ImmutableMouseButtonClickData(cp, button);
		IUIEventTarget t = getInfrastructure().getView().getTargetAtPoint((Point2D) cp.clone());
		removeEventMouse(this, button).ifPresent(e -> {
			if (UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.generateSyntheticEventMouseOpposite(e, cp)))
				; // TODO context menu
			if (t.equals(e.getTarget())) {
				if (UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.createEventClick(context.getViewContext(), t, d)))
					setFocus(context, t);
				setLastMouseClickData(context, d, t);
			}
		});
		return true;
	}

	protected static Optional<IUIEventMouse> removeEventMouse(UIMinecraftScreenAdapter<?, ?> self, int key) { return Optional.ofNullable(self.getMouseButtonsBeingPressed().remove(key)); }

	protected void setLastMouseClickData(IUIContextContainer context, @Nullable ImmutableMouseButtonClickData newMouseClickData, @Nullable IUIEventTarget target) {
		boolean ret = getLastMouseClickData()
				.flatMap(dl -> Optional.ofNullable(newMouseClickData)
						.filter(d -> UIInputUtilities.isDoubleClick(dl, d)
								&& UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.createEventClickDouble(context.getViewContext(), AssertionUtilities.assertNonnull(target), d))))
				.isPresent();
		this.lastMouseClickData = newMouseClickData;
		if (ret)
			setFocus(context, target);
		// TODO select
	}

	protected Optional<? extends ImmutableMouseButtonClickData> getLastMouseClickData() { return Optional.ofNullable(lastMouseClickData); }

	@Override
	@Deprecated
	public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseXDiff, double mouseYDiff) { return getMouseButtonsBeingPressed().containsKey(button); }

	@Override
	@Deprecated
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		IUIContextContainer context = getContextContainer();
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		IUIEventTarget target = getInfrastructure().getView().getTargetAtPoint((Point2D) cp.clone());
		UIEventUtilities.dispatchEvent(
				UIEventUtilities.Factory.createEventWheel(false, context.getViewContext(), target, new ImmutableMouseButtonClickData(cp), target, delta)); // COMMENT nothing to be scrolled
		return true;
	}

	@Override
	@Deprecated
	public boolean keyReleased(int key, int scanCode, int modifiers) {
		return removeEventKeyboard(this, key).filter(e -> {
			UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.generateSyntheticEventKeyboardOpposite(e));
			return true;
		}).isPresent(); // COMMENT NO default action
	}

	protected static Optional<IUIEventKeyboard> removeEventKeyboard(UIMinecraftScreenAdapter<?, ?> self, int key) { return Optional.ofNullable(self.getKeyboardKeysBeingPressed().remove(key)); }

	@Override
	@Deprecated
	public boolean charTyped(char codePoint, int modifiers) {
		IUIContextContainer context = getContextContainer();
		getFocus().ifPresent(f -> UIEventUtilities.dispatchEvent(
				UIEventUtilities.Factory.createEventChar(context.getViewContext(), f, codePoint, modifiers)));
		return true;
	}

	@Override
	@Deprecated
	public boolean changeFocus(boolean next) {
		IUIContextContainer context = getContextContainer();
		return setFocus(context, getInfrastructure().getView().changeFocus(getFocus().orElse(null), next).orElse(null));
	}

	protected static <E extends IUIEventMouse> E addEventMouse(UIMinecraftScreenAdapter<?, ?> self, E event) {
		self.getMouseButtonsBeingPressed().put(event.getData().getButton(), event);
		return event;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<Integer, IUIEventMouse> getMouseButtonsBeingPressed() { return mouseButtonsBeingPressed; }

	@Override
	public boolean hasContainer() { return getContainerObject().isPresent(); }

	protected Optional<? extends C> getContainerObject() { return Optional.ofNullable(containerObject); }

	@Override
	public C getContainer()
			throws UnsupportedOperationException {
		return getContainerObject()
				.orElseThrow(UnsupportedOperationException::new);
	}

	@Override
	@Deprecated
	public void mouseMoved(double mouseX, double mouseY) {
		IUIContextContainer context = getContextContainer();
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		ImmutableMouseButtonClickData d = new ImmutableMouseButtonClickData(cp);
		UIEventUtilities.dispatchEvent(
				UIEventUtilities.Factory.createEventMouseMove(context.getViewContext(), getInfrastructure().getView().getTargetAtPoint((Point2D) cp.clone()), d));
		setTargetBeingHoveredByMouse(context, getInfrastructure().getView().getTargetAtPoint((Point2D) cp.clone()), d);
	}

	protected void setTargetBeingHoveredByMouse(IUIContextContainer context, @Nullable IUIEventTarget targetBeingHoveredByMouse, IMouseButtonClickData data) {
		Optional<? extends IUIEventTarget> o = getTargetBeingHoveredByMouse();
		Optional<IUIEventTarget> n = Optional.ofNullable(targetBeingHoveredByMouse);
		if (!n.equals(o)) {
			List<?>
					op = o.map(t ->
					CastUtilities.castChecked(INode.class, t)
							.<List<?>>map(UIEventUtilities::computeNodePath)
							.orElseGet(() -> ImmutableList.of(t)))
					.orElseGet(ImmutableList::of),
					np = n.map(t ->
							CastUtilities.castChecked(INode.class, t)
									.<List<?>>map(UIEventUtilities::computeNodePath)
									.orElseGet(() -> ImmutableList.of(t)))
							.orElseGet(ImmutableList::of);
			int equalParents = 0;
			for (@SuppressWarnings("UnstableApiUsage") Iterator<Boolean> iterator =
			     Streams.zip(op.stream(), np.stream(), Object::equals).iterator();
			     iterator.hasNext(); ) {
				if (!iterator.next())
					break;
				++equalParents;
			}
			int finalEqualParents = equalParents; // COMMENT paths equal for [0,equalParents)
			o.ifPresent(t -> {
				UIEventUtilities.dispatchEvent(
						UIEventUtilities.Factory.createEventMouseLeaveSelf(context.getViewContext(), t, data, n.orElse(null))); // COMMENT consider bubbling
				Lists.reverse(op.subList(finalEqualParents, op.size())).forEach(t2 ->
						CastUtilities.castChecked(IUIEventTarget.class, t2)
								.ifPresent(t3 -> UIEventUtilities.dispatchEvent(
										UIEventUtilities.Factory.createEventMouseLeave(context.getViewContext(), t3, data, n.orElse(null)))));
			});
			this.targetBeingHoveredByMouse = targetBeingHoveredByMouse;
			n.ifPresent(t -> {
				UIEventUtilities.dispatchEvent(
						UIEventUtilities.Factory.createEventMouseEnterSelf(context.getViewContext(), t, data, o.orElse(null))); // COMMENT consider bubbling
				np.subList(finalEqualParents, np.size()).forEach(t2 ->
						CastUtilities.castChecked(IUIEventTarget.class, t2)
								.ifPresent(t3 -> UIEventUtilities.dispatchEvent(
										UIEventUtilities.Factory.createEventMouseEnter(context.getViewContext(), t3, data, o.orElse(null)))));
			});
		}
	}

	protected Optional<? extends IUIEventTarget> getTargetBeingHoveredByMouse() { return Optional.ofNullable(targetBeingHoveredByMouse); }

	@Override
	@Deprecated
	protected void hLine(int x1, int x2, int y, int color) { MinecraftDrawingUtilities.hLine(AffineTransformUtilities.getIdentity(), x1, x2, y, color, getBlitOffset()); }

	@Override
	@Deprecated
	protected void vLine(int x, int y1, int y2, int color) { MinecraftDrawingUtilities.vLine(AffineTransformUtilities.getIdentity(), x, y1, y2, color, getBlitOffset()); }

	@Override
	@Deprecated
	protected void fillGradient(int x1, int y1, int x2, int y2, int colorTop, int colorBottom) {
		Rectangle2D shape = new Rectangle2D.Double();
		shape.setFrameFromDiagonal(x1, y1, x2, y2);
		MinecraftDrawingUtilities.fillGradient(AffineTransformUtilities.getIdentity(), shape, colorTop, colorBottom, getBlitOffset());
	}

	@Override
	@Deprecated
	public void drawCenteredString(FontRenderer font, String string, int x, int y, int color) { MinecraftDrawingUtilities.drawCenteredString(AffineTransformUtilities.getIdentity(), font, string, new Point2D.Double(x, y), color); }

	@Override
	@Deprecated
	public void drawRightAlignedString(FontRenderer font, String string, int x, int y, int color) { MinecraftDrawingUtilities.drawRightAlignedString(AffineTransformUtilities.getIdentity(), font, string, new Point2D.Double(x, y), color); }

	@Override
	@Deprecated
	public void drawString(FontRenderer font, String string, int x, int y, int color) { MinecraftDrawingUtilities.drawString(AffineTransformUtilities.getIdentity(), font, string, new Point2D.Double(x, y), color); }

	@SuppressWarnings("MagicNumber")
	@Override
	@Deprecated
	public void blit(int x, int y, int u, int v, int w, int h) { MinecraftDrawingUtilities.blit(AffineTransformUtilities.getIdentity(), new Rectangle2D.Double(x, y, w, h), new Point2D.Double(u, v), new DoubleDimension2D(256, 256), getBlitOffset()); }

	@OnlyIn(Dist.CLIENT)
	public static class Builder<I extends IUIMinecraftInfrastructure<?, ?, ?>, C extends Container> {
		private final ITextComponent title;
		private final I infrastructure;
		private Set<Integer> closeKeys = ImmutableSet.of(GLFW.GLFW_KEY_ESCAPE);
		private Set<Integer> changeFocusKeys = ImmutableSet.of(GLFW.GLFW_KEY_TAB);

		public Builder(ITextComponent title, I infrastructure) {
			this.title = title;
			this.infrastructure = infrastructure;
		}

		public AbstractScreenAdapter<I> build() {
			return new UIMinecraftScreenAdapter<>(
					getTitle(),
					getInfrastructure(),
					null,
					getCloseKeys(),
					getChangeFocusKeys());
		}

		protected ITextComponent getTitle() { return title; }

		protected I getInfrastructure() { return infrastructure; }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Set<Integer> getCloseKeys() { return closeKeys; }

		public Builder<I, C> setCloseKeys(Set<Integer> closeKeys) {
			this.closeKeys = ImmutableSet.copyOf(closeKeys);
			return this;
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Set<Integer> getChangeFocusKeys() { return changeFocusKeys; }

		public Builder<I, C> setChangeFocusKeys(Set<Integer> changeFocusKeys) {
			this.changeFocusKeys = ImmutableSet.copyOf(changeFocusKeys);
			return this;
		}

		@OnlyIn(Dist.CLIENT)
		public static class WithChildren<I extends IUIMinecraftInfrastructure<?, ?, ?>, C extends Container>
				extends Builder<I, C> {
			private final C containerObject;

			public WithChildren(ITextComponent titleIn, I infrastructure, C containerObject) {
				super(titleIn, infrastructure);
				this.containerObject = containerObject;
			}

			@Override
			public AbstractContainerScreenAdapter<I, C> build() {
				return new UIMinecraftScreenAdapter<>(
						getTitle(),
						getInfrastructure(),
						getContainerObject(),
						getCloseKeys(),
						getChangeFocusKeys());
			}

			protected C getContainerObject() { return containerObject; }
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class UIDefaultMinecraftScreenProviderExtension
			extends IHasGenericClass.Impl<IUIInfrastructure<?, ?, ?>>
			implements IUIMinecraftScreenProviderExtension {
		private final OptionalWeakReference<UIMinecraftScreenAdapter<?, ?>> owner;

		public UIDefaultMinecraftScreenProviderExtension(UIMinecraftScreenAdapter<?, ?> owner) {
			super(CastUtilities.castUnchecked(IUIInfrastructure.class)); // COMMENT class should not care about it
			this.owner = new OptionalWeakReference<>(owner);
		}

		@Override
		public Optional<? extends Screen> getScreen() { return getOwner(); }

		@Override
		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		public Optional<? extends Set<Integer>> getCloseKeys() { return getOwner().map(UIMinecraftScreenAdapter::getCloseKeys); }

		@Override
		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		public Optional<? extends Set<Integer>> getChangeFocusKeys() { return getOwner().map(UIMinecraftScreenAdapter::getChangeFocusKeys); }

		@Override
		public boolean setPaused(boolean paused) {
			return getOwner()
					.filter(owner -> {
						owner.setPaused(paused);
						return true;
					})
					.isPresent();
		}

		@Override
		public boolean setTitle(ITextComponent title) {
			return getOwner()
					.filter(owner -> {
						owner.setTitle(title);
						return true;
					})
					.isPresent();
		}

		protected Optional<? extends UIMinecraftScreenAdapter<?, ?>> getOwner() { return owner.getOptional(); }

		@Override
		public IExtensionType<INamespacePrefixedString, ?, IUIInfrastructure<?, ?, ?>> getType() { return StaticHolder.getType().getValue(); }
	}
}
