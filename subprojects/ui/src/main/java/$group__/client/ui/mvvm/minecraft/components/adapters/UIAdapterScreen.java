package $group__.client.ui.mvvm.minecraft.components.adapters;

import $group__.client.ui.events.ui.UIEventUtilities;
import $group__.client.ui.mvvm.core.IUIInfrastructure;
import $group__.client.ui.mvvm.core.adapters.IUIAdapter;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.structures.IUIDataMouseButtonClick;
import $group__.client.ui.mvvm.core.views.events.IUIEventKeyboard;
import $group__.client.ui.mvvm.core.views.events.IUIEventMouse;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import $group__.client.ui.mvvm.core.views.nodes.IUINode;
import $group__.client.ui.mvvm.minecraft.core.IUIInfrastructureMinecraft;
import $group__.client.ui.mvvm.minecraft.core.extensions.IUIExtensionContainerProvider;
import $group__.client.ui.mvvm.minecraft.core.extensions.IUIExtensionScreenProvider;
import $group__.client.ui.mvvm.structures.Dimension2DDouble;
import $group__.client.ui.mvvm.structures.UIDataKeyboardKeyPress;
import $group__.client.ui.mvvm.structures.UIDataMouseButtonClick;
import $group__.client.ui.utilities.InputUtilities;
import $group__.client.ui.utilities.UIObjectUtilities;
import $group__.client.ui.utilities.minecraft.DrawingUtilities;
import $group__.client.ui.utilities.minecraft.TextComponentUtilities;
import $group__.client.ui.utilities.minecraft.TooltipUtilities;
import $group__.client.ui.utilities.minecraft.UIBackgrounds;
import $group__.utilities.CastUtilities;
import $group__.utilities.client.minecraft.GLUtilities;
import $group__.utilities.client.minecraft.TransformUtilities;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.specific.MapUtilities;
import $group__.utilities.structures.Registry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

@OnlyIn(Dist.CLIENT)
public class UIAdapterScreen
		<I extends IUIInfrastructureMinecraft<?, ?, ?>>
		extends Screen
		implements IUIAdapter<I> {
	protected final ConcurrentMap<Integer, IUIEventKeyboard>
			keyboardKeysBeingPressed = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<Integer, IUIEventMouse>
			mouseButtonsBeingPressed = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();

	protected final I infrastructure;
	protected final Set<Integer> closeKeys, changeFocusKeys;
	protected boolean paused = false;
	@Nullable
	protected IUIEventTarget targetBeingHoveredByMouse = null;

	public UIAdapterScreen(ITextComponent titleIn, I infrastructure) { this(titleIn, infrastructure, ImmutableSet.of(GLFW.GLFW_KEY_ESCAPE), ImmutableSet.of(GLFW.GLFW_KEY_TAB)); }

	@Nullable
	protected IUIEventTarget focus;

	@Override
	public I getInfrastructure() { return infrastructure; }

	public Set<Integer> getCloseKeysView() { return ImmutableSet.copyOf(getCloseKeys()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Integer> getCloseKeys() { return closeKeys; }

	public boolean addCloseKeys(Iterable<Integer> keys) {
		boolean ret = false;
		for (Integer k : keys)
			ret |= getCloseKeys().add(k);
		return ret;
	}

	public boolean removeCloseKeys(Iterable<Integer> keys) {
		boolean ret = false;
		for (Integer k : keys)
			ret |= getCloseKeys().remove(k);
		return ret;
	}

	public Set<Integer> getChangeFocusKeysView() { return ImmutableSet.copyOf(getChangeFocusKeys()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Integer> getChangeFocusKeys() { return closeKeys; }

	public boolean addChangeFocusKeys(Iterable<Integer> keys) {
		boolean ret = false;
		for (Integer k : keys)
			ret |= getChangeFocusKeys().add(k);
		return ret;
	}

	public boolean removeChangeFocusKeys(Iterable<Integer> keys) {
		boolean ret = false;
		for (Integer k : keys)
			ret |= getChangeFocusKeys().remove(k);
		return ret;
	}

	@Override
	@Deprecated
	public void render(int mouseX, int mouseY, float partialTicks) { getInfrastructure().getView().render(new Point2D.Double(mouseX, mouseY), partialTicks); }

	@Nullable
	protected UIDataMouseButtonClick lastMouseClickData = null;

	protected static <E extends IUIEventKeyboard> E addEventKeyboard(UIAdapterScreen<?> self, E event) {
		self.getKeyboardKeysBeingPressed().put(event.getData().getKey(), event);
		return event;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<Integer, IUIEventKeyboard> getKeyboardKeysBeingPressed() { return keyboardKeysBeingPressed; }

	@Override
	@Deprecated
	public boolean shouldCloseOnEsc() { return getCloseKeys().contains(GLFW.GLFW_KEY_ESCAPE); }

	@Override
	@Deprecated
	protected void renderTooltip(ItemStack item, int mouseX, int mouseY) { TooltipUtilities.renderTooltip(getMinecraft(), width, height, font, itemRenderer, item, mouseX, mouseY); }

	@Override
	@Deprecated
	public List<String> getTooltipFromItem(ItemStack item) { return TooltipUtilities.getTooltipFromItem(getMinecraft(), item); }

	@Override
	@Deprecated
	public void renderTooltip(String tooltip, int mouseX, int mouseY) { TooltipUtilities.renderTooltip(width, height, font, itemRenderer, tooltip, mouseX, mouseY); }

	@Override
	@Deprecated
	public void renderTooltip(List<String> tooltip, int mouseX, int mouseY) { TooltipUtilities.renderTooltip(width, height, font, itemRenderer, tooltip, mouseX, mouseY); }

	@Override
	@Deprecated
	public void renderTooltip(List<String> tooltip, int mouseX, int mouseY, FontRenderer font) { TooltipUtilities.renderTooltip(width, height, itemRenderer, tooltip, mouseX, mouseY, font); }

	@Override
	@Deprecated
	protected void renderComponentHoverEffect(ITextComponent component, int mouseX, int mouseY) { TextComponentUtilities.renderComponentHoverEffect(getMinecraft(), width, height, font, component, mouseX, mouseY); }

	@Override
	@Deprecated
	public boolean handleComponentClicked(ITextComponent component) { return TextComponentUtilities.handleComponentClicked(this, component); }

	@Override
	@Deprecated
	public void setSize(int width, int height) {
		super.setSize(width, height);
		// TODO reimplement
		/*getInfrastructure().reshape(s -> {
			s.getShapeRef().setRect(0, 0, width, height);
			return true;
		});*/
	}

	@Override
	@Deprecated
	public List<? extends IGuiEventListener> children() { return ImmutableList.of(); }

	@Override
	@Deprecated
	protected void init() {
		setSize(width, height);
		// COMMENT constructor is favored over initialize
	}

	@Override
	@Deprecated
	public void tick() { getInfrastructure().tick(); }

	@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
	public UIAdapterScreen(ITextComponent titleIn, I infrastructure, Set<Integer> closeKeys, Set<Integer> changeFocusKeys) {
		super(titleIn);
		this.infrastructure = infrastructure;
		this.closeKeys = new HashSet<>(closeKeys);
		this.changeFocusKeys = new HashSet<>(changeFocusKeys);
		IExtensionContainer.addExtensionSafe(getInfrastructure(), new UIExtensionScreen<>(IUIInfrastructure.class, this));
	}

	protected static Optional<IUIEventKeyboard> removeEventKeyboard(UIAdapterScreen<?> self, int key) { return Optional.ofNullable(self.getKeyboardKeysBeingPressed().remove(key)); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<Integer, IUIEventMouse> getMouseButtonsBeingPressed() { return mouseButtonsBeingPressed; }

	protected static Optional<IUIEventMouse> removeEventMouse(UIAdapterScreen<?> self, int key) { return Optional.ofNullable(self.getMouseButtonsBeingPressed().remove(key)); }

	@Override
	@Deprecated
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		if (getCloseKeys().contains(key)) {
			onClose();
			return true;
		}
		if (getChangeFocusKeys().contains(key)) {
			changeFocus((modifiers & GLFW.GLFW_MOD_SHIFT) == 0);
			return true;
		}
		return getFocus().filter(f -> UIEventUtilities.dispatchEvent(
				addEventKeyboard(this, UIEventUtilities.Factory.createEventKeyDown(f,
						new UIDataKeyboardKeyPress(key, scanCode, modifiers))))).isPresent();
	}

	@Override
	@Deprecated
	public void removed() {
		{
			// COMMENT generate opposite synthetic events
			ImmutableMap.copyOf(getKeyboardKeysBeingPressed()).forEach((k, e) ->
					removeEventKeyboard(this, k).ifPresent(e2 ->
							UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.generateSyntheticEventKeyboardOpposite(e2))));
			Point2D cp = GLUtilities.getCursorPos();
			ImmutableMap.copyOf(getMouseButtonsBeingPressed()).forEach((k, e) ->
					removeEventMouse(this, k).ifPresent(e2 ->
							UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.generateSyntheticEventMouseOpposite(e2, cp))));
			setTargetBeingHoveredByMouse(null, new UIDataMouseButtonClick(cp));
			setLastMouseClickData(null, null);
			setFocus(null);
		}
		getInfrastructure().removed();
	}

	@SuppressWarnings("UnusedReturnValue")
	protected boolean setTargetBeingHoveredByMouse(@Nullable IUIEventTarget targetBeingHoveredByMouse, IUIDataMouseButtonClick data) {
		Optional<IUIEventTarget> o = getTargetBeingHoveredByMouse();
		Optional<IUIEventTarget> n = Optional.ofNullable(targetBeingHoveredByMouse);
		if (!n.equals(o)) {
			ImmutableList<?>
					op = o.map(t -> t instanceof IUINode ? UIEventUtilities.computeNodePath((IUINode) t) : ImmutableList.of(t)).orElseGet(ImmutableList::of),
					np = n.map(t -> t instanceof IUINode ? UIEventUtilities.computeNodePath((IUINode) t) : ImmutableList.of(t)).orElseGet(ImmutableList::of);
			int i = 0;
			for (int maxI = Math.min(op.size(), np.size()); i < maxI; ++i) {
				if (!op.get(i).equals(np.get(i)))
					break;
			}
			int iFinal = i; // COMMENT paths equal for [0,i)
			o.ifPresent(t -> {
				UIEventUtilities.dispatchEvent(
						UIEventUtilities.Factory.createEventMouseLeaveSelf(t, data, n.orElse(null))); // COMMENT consider bubbling
				Lists.reverse(op.subList(iFinal, op.size())).forEach(t2 -> {
					if (t2 instanceof IUIEventTarget)
						UIEventUtilities.dispatchEvent(
								UIEventUtilities.Factory.createEventMouseLeave((IUIEventTarget) t2, data, n.orElse(null)));
				});
			});
			this.targetBeingHoveredByMouse = targetBeingHoveredByMouse;
			n.ifPresent(t -> {
				UIEventUtilities.dispatchEvent(
						UIEventUtilities.Factory.createEventMouseEnterSelf(t, data, o.orElse(null))); // COMMENT consider bubbling
				np.subList(iFinal, np.size()).forEach(t2 -> {
					if (t2 instanceof IUIEventTarget)
						UIEventUtilities.dispatchEvent(
								UIEventUtilities.Factory.createEventMouseEnter((IUIEventTarget) t2, data, o.orElse(null)));
				});
			});
			return true;
		}
		return false;
	}

	protected boolean setLastMouseClickData(@Nullable UIDataMouseButtonClick newMouseClickData, @Nullable IUIEventTarget target) {
		boolean ret = getLastMouseClickData()
				.flatMap(dl -> Optional.ofNullable(newMouseClickData)
						.filter(d -> {
							assert target != null;
							return InputUtilities.isDoubleClick(dl, d)
									&& UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.createEventClickDouble(target, d));
						})).isPresent();
		this.lastMouseClickData = newMouseClickData;
		return ret;
	}

	protected Optional<IUIEventTarget> getTargetBeingHoveredByMouse() { return Optional.ofNullable(targetBeingHoveredByMouse); }

	@Override
	@Deprecated
	public void renderBackground() { UIBackgrounds.renderBackgroundAndNotify(getMinecraft(), width, height); }

	@Override
	@Deprecated
	public void renderBackground(int blitOffset) { UIBackgrounds.renderBackgroundAndNotify(getMinecraft(), width, height, blitOffset); }

	@Override
	@Deprecated
	public void renderDirtBackground(int blitOffset) { UIBackgrounds.renderDirtBackgroundAndNotify(getMinecraft(), width, height, blitOffset); }

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

	protected Optional<UIDataMouseButtonClick> getLastMouseClickData() { return Optional.ofNullable(lastMouseClickData); }

	protected Optional<IUIEventTarget> getFocus() { return Optional.ofNullable(focus); }

	protected boolean setFocus(@Nullable IUIEventTarget focus) {
		Optional<IUIEventTarget> p = getFocus(), n = Optional.ofNullable(focus);
		if (!p.equals(n)) {
			@Nullable IUIEventTarget pv = p.orElse(null);
			p.ifPresent(f -> UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.createEventFocusOutPre(f, focus)));
			n.ifPresent(f -> UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.createEventFocusInPre(f, pv)));
			this.focus = focus;
			p.ifPresent(f -> UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.createEventFocusOutPost(f, focus)));
			n.ifPresent(f -> UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.createEventFocusInPost(f, pv)));
			return true;
		}
		return false;
	}

	@Override
	@Deprecated
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		// TODO double click
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		IUIEventTarget t = getInfrastructure().getView().getTargetAtPoint(cp);
		UIDataMouseButtonClick d = new UIDataMouseButtonClick(cp, button);
		boolean ret = UIEventUtilities.dispatchEvent(addEventMouse(this, UIEventUtilities.Factory.createEventMouseDown(t, d)))
				| UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.createEventClick(t, d))
				| setLastMouseClickData(d, t);
		if (t.isFocusable())
			setFocus(t);
		return ret;
	}

	protected static <E extends IUIEventMouse> E addEventMouse(UIAdapterScreen<?> self, E event) {
		self.getMouseButtonsBeingPressed().put(event.getData().getButton(), event);
		return event;
	}

	@Override
	@Deprecated
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return removeEventMouse(this, button).map(e ->
				UIEventUtilities.dispatchEvent(
						UIEventUtilities.Factory.generateSyntheticEventMouseOpposite(e, new Point2D.Double(mouseX, mouseY)))).orElse(false);
	}

	@Override
	@Deprecated
	public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseXDiff, double mouseYDiff) { return getMouseButtonsBeingPressed().containsKey(button); }

	@Override
	@Deprecated
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		IUIEventTarget target = getInfrastructure().getView().getTargetAtPoint(cp);
		return UIEventUtilities.dispatchEvent(
				UIEventUtilities.Factory.createEventWheel(target, new UIDataMouseButtonClick(cp), target, delta));
	}

	@Override
	@Deprecated
	public boolean keyReleased(int key, int scanCode, int modifiers) {
		return removeEventKeyboard(this, key).map(e ->
				UIEventUtilities.dispatchEvent(
						UIEventUtilities.Factory.generateSyntheticEventKeyboardOpposite(e))).orElse(false);
	}

	@Override
	@Deprecated
	public boolean charTyped(char codePoint, int modifiers) {
		return getFocus().filter(f -> UIEventUtilities.dispatchEvent(
				UIEventUtilities.Factory.createEventChar(f, codePoint, modifiers))).isPresent();
	}

	@Override
	@Deprecated
	public boolean changeFocus(boolean next) { return setFocus(getInfrastructure().getView().changeFocus(getFocus().orElse(null), next).orElse(null)); }

	@Override
	@Deprecated
	public void mouseMoved(double mouseX, double mouseY) {
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		UIDataMouseButtonClick d = new UIDataMouseButtonClick(cp);
		UIEventUtilities.dispatchEvent(
				UIEventUtilities.Factory.createEventMouseMove(getInfrastructure().getView().getTargetAtPoint(cp), d));
		setTargetBeingHoveredByMouse(getInfrastructure().getView().getTargetAtPoint(cp), d);
	}

	@Override
	@Deprecated
	protected void hLine(int x1, int x2, int y, int color) { DrawingUtilities.hLine(TransformUtilities.AffineTransformUtilities.getIdentity(), x1, x2, y, color, getBlitOffset()); }

	@Override
	@Deprecated
	protected void vLine(int x, int y1, int y2, int color) { DrawingUtilities.vLine(TransformUtilities.AffineTransformUtilities.getIdentity(), x, y1, y2, color, getBlitOffset()); }

	@Override
	@Deprecated
	protected void fillGradient(int x1, int y1, int x2, int y2, int colorTop, int colorBottom) { DrawingUtilities.fillGradient(TransformUtilities.AffineTransformUtilities.getIdentity(), UIObjectUtilities.getRectangleFromDiagonal(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2)), colorTop, colorBottom, getBlitOffset()); }

	@Override
	@Deprecated
	public void drawCenteredString(FontRenderer font, String string, int x, int y, int color) { DrawingUtilities.drawCenteredString(TransformUtilities.AffineTransformUtilities.getIdentity(), font, string, new Point2D.Double(x, y), color); }

	@Override
	@Deprecated
	public void drawRightAlignedString(FontRenderer font, String string, int x, int y, int color) { DrawingUtilities.drawRightAlignedString(TransformUtilities.AffineTransformUtilities.getIdentity(), font, string, new Point2D.Double(x, y), color); }

	@Override
	@Deprecated
	public void drawString(FontRenderer font, String string, int x, int y, int color) { DrawingUtilities.drawString(TransformUtilities.AffineTransformUtilities.getIdentity(), font, string, new Point2D.Double(x, y), color); }

	@SuppressWarnings("MagicNumber")
	@Override
	@Deprecated
	public void blit(int x, int y, int u, int v, int w, int h) { DrawingUtilities.blit(TransformUtilities.AffineTransformUtilities.getIdentity(), new Rectangle2D.Double(x, y, w, h), new Point2D.Double(u, v), new Dimension2DDouble(256, 256), getBlitOffset()); }

	@OnlyIn(Dist.CLIENT)
	public static class WithContainer
			<I extends IUIInfrastructureMinecraft<?, ?, ?>,
					C extends Container>
			extends UIAdapterScreen<I>
			implements IHasContainer<C> {

		protected final C container;

		@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
		protected WithContainer(ITextComponent titleIn, I manager, C container) {
			super(titleIn, manager);
			this.container = container;
			IExtensionContainer.addExtensionSafe(getInfrastructure(), new UIExtensionContainer<>(IUIInfrastructure.class, getContainer()));
		}

		@Override
		public C getContainer() { return container; }

		@OnlyIn(Dist.CLIENT)
		public static class UIExtensionContainer<C extends IUIInfrastructure<?, ?, ?>>
				extends IHasGenericClass.Impl<C>
				implements IUIExtensionContainerProvider<C> {
			public static final Registry.RegistryObject<IType<IUIExtensionContainerProvider<IUIInfrastructure<?, ?, ?>>, IUIInfrastructure<?, ?, ?>>> TYPE =
					RegUIExtension.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));

			protected final Container container;

			public UIExtensionContainer(Class<C> genericClass, Container container) {
				super(genericClass);
				this.container = container;
			}

			@Override
			public Container getContainer() { return container; }

			@Override
			public IUIExtension.IType<?, ?> getType() { return TYPE.getValue(); }
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class UIExtensionScreen<C extends IUIInfrastructure<?, ?, ?>>
			extends IHasGenericClass.Impl<C>
			implements IUIExtensionScreenProvider<C> {
		public static final Registry.RegistryObject<IType<IUIExtensionScreenProvider<IUIInfrastructure<?, ?, ?>>, IUIInfrastructure<?, ?, ?>>> TYPE =
				RegUIExtension.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));
		protected final UIAdapterScreen<?> adapter;

		public UIExtensionScreen(Class<C> genericClass, UIAdapterScreen<?> adapter) {
			super(genericClass);
			this.adapter = adapter;
		}

		@Override
		public Screen getScreen() { return getAdapter(); }

		@Override
		public Set<Integer> getCloseKeys() { return getAdapter().getCloseKeys(); }

		@Override
		public Set<Integer> getChangeFocusKeys() { return getAdapter().getChangeFocusKeys(); }

		@Override
		public boolean isPaused() { return getAdapter().isPaused(); }

		@Override
		public void setPaused(boolean paused) { getAdapter().setPaused(paused); }

		protected UIAdapterScreen<?> getAdapter() { return adapter; }

		@Override
		public IType<?, ?> getType() { return TYPE.getValue(); }
	}
}
