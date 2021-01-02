package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.adapters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIContextContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.adapters.IUIAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.extensions.cursors.ICursorHandleProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.extensions.cursors.IUICursorHandleProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.IUIActiveLifecycle;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventKeyboard;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.cursors.EnumGLFWCursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIEventUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.software.MinecraftSoftwareGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.UIImmutableContextContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.viewmodels.UIImmutableViewModelContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.UIImmutableViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.UIInputUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.extensions.IUIMinecraftScreenProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.viewmodels.extensions.IUIMinecraftTickerExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.def.mvvm.views.extensions.IUIMinecraftBackgroundExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.extensions.UIImmutableMinecraftContainerProviderExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.mvvm.views.extensions.background.UIDefaultMinecraftBackgroundExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.utilities.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.impl.utilities.UIMinecraftBackgrounds;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CollectionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftTextComponentUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftTooltipUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.inputs.MinecraftKeyboardDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.inputs.MinecraftPointerDevice;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.PrimitiveStreamUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.paths.INode;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.DoubleDimension2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.ICursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IMouseButtonClickData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.impl.ImmutableInputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.impl.ImmutableKeyboardKeyPressData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.impl.ImmutableMouseButtonClickData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.impl.ImmutableOutputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.OptionalUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.impl.Tickers;
import it.unimi.dsi.fastutil.ints.*;
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

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool.*;

@OnlyIn(Dist.CLIENT)
public class UIMinecraftScreenAdapter
		<I extends IUIInfrastructure<?, ?, ?>, C extends Container>
		extends AbstractContainerScreenAdapter<I, C>
		implements IUIAdapter<I> {
	private final I infrastructure;
	@Nullable
	private final C containerObject;
	private final IntSet closeKeys;
	private final IntSet changeFocusKeys;
	private final Int2ObjectMap<IUIEventKeyboard>
			keyboardKeysBeingPressed = new Int2ObjectOpenHashMap<>(CapacityUtilities.getInitialCapacitySmall());
	private final Int2ObjectMap<IUIEventMouse>
			mouseButtonsBeingPressed = new Int2ObjectOpenHashMap<>(CapacityUtilities.getInitialCapacitySmall());
	private final IUIContextContainer contextContainer;
	private boolean paused = false;
	@Nullable
	private IUIEventTarget targetBeingHoveredByMouse = null;
	private ITextComponent title;
	@Nullable
	private IUIEventTarget focus;
	@Nullable
	private ImmutableMouseButtonClickData lastMouseClickData = null;
	private double lastPartialTicks = 0D;
	@Nullable
	private ICursor lastCursor = null;

	protected UIMinecraftScreenAdapter(ITextComponent title, I infrastructure, @Nullable C containerObject, IntIterable closeKeys, IntIterable changeFocusKeys) {
		super(title);
		this.title = title;
		this.infrastructure = infrastructure;
		this.containerObject = containerObject;
		this.closeKeys = new IntOpenHashSet(closeKeys.iterator());
		this.changeFocusKeys = new IntOpenHashSet(changeFocusKeys.iterator());

		try (AutoCloseableGraphics2D graphics = MinecraftSoftwareGraphics2D.createGraphics()) {
			this.contextContainer = new UIImmutableContextContainer(
					UIImmutableViewContext.of(
							new ImmutableInputDevices.Builder(Tickers.SYSTEM)
									.setPointerDevice(new MinecraftPointerDevice())
									.setKeyboardDevice(new MinecraftKeyboardDevice())
									.build(),
							new ImmutableOutputDevices.Builder()
									.setGraphics(graphics)
									.build()
					),
					new UIImmutableViewModelContext()
			);
		}

		// COMMENT add extensions
		// COMMENT infrastructure
		IExtensionContainer.addExtensionChecked(this.infrastructure, new UIDefaultMinecraftScreenProviderExtension(suppressThisEscapedWarning(() -> this)));
		if (containerObject != null)
			IExtensionContainer.addExtensionChecked(this.infrastructure, new UIImmutableMinecraftContainerProviderExtension(this.containerObject));

		// COMMENT view
		IUIView<?> view = this.infrastructure.getView();
		if (!IExtensionContainer.containsExtension(view, IUIMinecraftBackgroundExtension.StaticHolder.getKey())) {
			// COMMENT to ensure that 'GuiScreenEvent.BackgroundDrawnEvent' is fired
			IExtensionContainer.addExtensionChecked(view, new UIDefaultMinecraftBackgroundExtension());
		}
	}

	protected double getLastPartialTicks() {
		return lastPartialTicks;
	}

	protected void setLastPartialTicks(double lastPartialTicks) {
		this.lastPartialTicks = lastPartialTicks;
	}

	@Override
	public @Nonnull ITextComponent getTitle() { return title; }

	public void setTitle(ITextComponent title) { this.title = title; }

	@Override
	@Deprecated
	public void render(int mouseX, int mouseY, float partialTicks) {
		setLastPartialTicks(partialTicks);

		MinecraftSoftwareGraphics2D.clear();
		getInfrastructure().getView().render();
		MinecraftSoftwareGraphics2D.draw();

		setCursor(
				OptionalUtilities.<ICursor>upcast(
						IUICursorHandleProviderExtension.StaticHolder.getType().getValue().find(getInfrastructure().getView())
								.flatMap(ICursorHandleProvider::getCursorHandle)
				).orElse(EnumGLFWCursor.DEFAULT_CURSOR)
		);
	}

	protected static <E extends IUIEventKeyboard> E addEventKeyboard(UIMinecraftScreenAdapter<?, ?> self, E event) {
		self.getKeyboardKeysBeingPressed().put(event.getData().getKey(), event);
		return event;
	}

	protected void setCursor(ICursor cursor) {
		if (!cursor.equals(this.lastCursor)) {
			this.lastCursor = cursor;
			getContextContainer().getViewContext().getInputDevices().getPointerDevice()
					.ifPresent(pointerDevice -> pointerDevice.setCursor(cursor));
		}
	}

	@Override
	@Deprecated
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		IUIContextContainer context = getContextContainer();
		if (!getFocus()
				.filter(f -> !UIEventUtilities.dispatchEvent(
						addEventKeyboard(this, UIEventUtilities.Factory.createEventKeyDown(context.getViewContext(), f,
								new ImmutableKeyboardKeyPressData(key, scanCode, modifiers)))))
				.isPresent()) {
			if (getCloseKeys().contains(key))
				onClose();
			if (getChangeFocusKeys().contains(key))
				changeFocus(!MaskUtilities.containsAll(modifiers, GLFW.GLFW_MOD_SHIFT));
		}
		return true;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected IntSet getCloseKeys() { return closeKeys; }

	@Override
	public I getInfrastructure() { return infrastructure; }

	protected IUIContextContainer getContextContainer() { return contextContainer; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected IntSet getChangeFocusKeys() { return changeFocusKeys; }

	@Override
	@Deprecated
	protected void init() {
		getInfrastructure().bind(getContextContainer()); // COMMENT does nothing if already bound
		setSize(width, height);
		IUIActiveLifecycle.initializeV(getInfrastructure());
		setCursor(EnumGLFWCursor.DEFAULT_CURSOR);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Int2ObjectMap<IUIEventKeyboard> getKeyboardKeysBeingPressed() { return keyboardKeysBeingPressed; }

	protected Optional<? extends IUIEventTarget> getFocus() { return Optional.ofNullable(focus); }

	@Override
	@Deprecated
	public boolean shouldCloseOnEsc() { return getCloseKeys().contains(GLFW.GLFW_KEY_ESCAPE); }

	@Override
	@Deprecated
	protected void renderTooltip(ItemStack item, int mouseX, int mouseY) { MinecraftTooltipUtilities.renderTooltip(getMinecraft(), width, height, font, itemRenderer, item, mouseX, mouseY); }

	@Override
	@Deprecated
	public @Nonnull List<String> getTooltipFromItem(ItemStack item) { return MinecraftTooltipUtilities.getTooltipFromItem(getMinecraft(), item); }

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
	public @Nonnull List<? extends IGuiEventListener> children() { return ImmutableList.of(); }

	@Override
	@Deprecated
	public void removed() {
		IUIContextContainer context = getContextContainer();

		setCursor(EnumGLFWCursor.DEFAULT_CURSOR);
		{
			// COMMENT generate opposite synthetic events
			// COMMENT NO default actions
			new IntOpenHashSet(getKeyboardKeysBeingPressed().keySet())
					.forEach((int k) ->
							removeEventKeyboard(this, k).ifPresent(e2 ->
									UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.generateSyntheticEventKeyboardOpposite(e2))));
			Point2D cp = getContextContainer().getViewContext().getInputDevices().getPointerDevice().orElseThrow(AssertionError::new).getPositionView();
			new IntOpenHashSet(getMouseButtonsBeingPressed().keySet())
					.forEach((int k) ->
							removeEventMouse(this, k).ifPresent(e2 ->
									UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.generateSyntheticEventMouseOpposite(e2, cp))));
			setTargetBeingHoveredByMouse(context, null, new ImmutableMouseButtonClickData(cp));
			setLastMouseClickData(context, null, null);
			setFocus(context, null);
		}
		IUIActiveLifecycle.cleanupV(getInfrastructure());
	}

	@Override
	@Deprecated
	public void tick() {
		IUIMinecraftTickerExtension.StaticHolder.getType().getValue().find(getInfrastructure().getViewModel())
				.ifPresent(IUIMinecraftTickerExtension::tick);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Int2ObjectMap<IUIEventMouse> getMouseButtonsBeingPressed() { return mouseButtonsBeingPressed; }

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

	protected Optional<? extends ICursor> getLastCursor() {
		return Optional.ofNullable(lastCursor);
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
			int equalParents = Math.toIntExact(CollectionUtilities.countEqualPrefixes(op.iterator(), np.iterator())); // COMMENT paths equal for [0,equalParents)
			o.ifPresent(t -> {
				UIEventUtilities.dispatchEvent(
						UIEventUtilities.Factory.createEventMouseLeaveSelf(context.getViewContext(), t, data, n.orElse(null))); // COMMENT consider bubbling
				Lists.reverse(op.subList(equalParents, op.size())).forEach(t2 ->
						CastUtilities.castChecked(IUIEventTarget.class, t2)
								.ifPresent(t3 -> UIEventUtilities.dispatchEvent(
										UIEventUtilities.Factory.createEventMouseLeave(context.getViewContext(), t3, data, n.orElse(null)))));
			});
			this.targetBeingHoveredByMouse = targetBeingHoveredByMouse;
			n.ifPresent(t -> {
				UIEventUtilities.dispatchEvent(
						UIEventUtilities.Factory.createEventMouseEnterSelf(context.getViewContext(), t, data, o.orElse(null))); // COMMENT consider bubbling
				np.subList(equalParents, np.size()).forEach(t2 ->
						CastUtilities.castChecked(IUIEventTarget.class, t2)
								.ifPresent(t3 -> UIEventUtilities.dispatchEvent(
										UIEventUtilities.Factory.createEventMouseEnter(context.getViewContext(), t3, data, o.orElse(null)))));
			});
		}
	}

	protected boolean setFocus(IUIContextContainer context, @Nullable IUIEventTarget focus) {
		Optional<? extends IUIEventTarget> p = getFocus(), n = Optional.ofNullable(focus);
		if (!(n.filter(FunctionUtilities.notPredicate(IUIEventTarget::isFocusable)).isPresent()
				|| n.equals(p))) {
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

	public IntSet getCloseKeysView() {
		return IntSets.unmodifiable(new IntOpenHashSet(getCloseKeys()));
	}

	public boolean addCloseKeys(PrimitiveIterator.OfInt keys) {
		return stripBool(
				PrimitiveStreamUtilities.stream(keys)
						.map(key -> padBool(getCloseKeys().add(key)))
						.reduce(fBool(), PaddedBool::orBool)
		);
	}

	public boolean removeCloseKeys(PrimitiveIterator.OfInt keys) {
		return stripBool(
				PrimitiveStreamUtilities.stream(keys)
						.map(key -> padBool(getCloseKeys().remove(key)))
						.reduce(fBool(), PaddedBool::orBool)
		);
	}

	public IntSet getChangeFocusKeysView() {
		return IntSets.unmodifiable(new IntOpenHashSet(getChangeFocusKeys()));
	}

	@Override
	@Deprecated
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		IUIContextContainer context = getContextContainer();
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		ImmutableMouseButtonClickData d = new ImmutableMouseButtonClickData(cp, button);
		IUIEventTarget t = getInfrastructure().getView().getTargetAtPoint((Point2D) cp.clone());
		if (UIEventUtilities.dispatchEvent(addEventMouse(this, UIEventUtilities.Factory.createEventMouseDown(context.getViewContext(), t, d)))) {
			// TODO select, drag or drop perhaps, scroll/pan
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

	public boolean addChangeFocusKeys(PrimitiveIterator.OfInt keys) {
		return stripBool(
				PrimitiveStreamUtilities.stream(keys)
						.map(key -> padBool(getChangeFocusKeys().add(key)))
						.reduce(fBool(), PaddedBool::orBool)
		);
	}

	@Override
	public boolean hasContainer() { return getContainerObject().isPresent(); }

	protected Optional<? extends C> getContainerObject() { return Optional.ofNullable(containerObject); }

	@Override
	public @Nonnull C getContainer()
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

	public boolean removeChangeFocusKeys(PrimitiveIterator.OfInt keys) {
		return stripBool(
				PrimitiveStreamUtilities.stream(keys)
						.map(key -> padBool(getChangeFocusKeys().remove(key)))
						.reduce(fBool(), PaddedBool::orBool)
		);
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
	public static class Builder<I extends IUIInfrastructure<?, ?, ?>, C extends Container> {
		private final ITextComponent title;
		private final I infrastructure;
		private IntIterable closeKeys = IntSets.singleton(GLFW.GLFW_KEY_ESCAPE);
		private IntIterable changeFocusKeys = IntSets.singleton(GLFW.GLFW_KEY_TAB);

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

		protected IntIterable getCloseKeys() { return closeKeys; }

		public Builder<I, C> setCloseKeys(IntIterable closeKeys) {
			this.closeKeys = IntSets.unmodifiable(new IntOpenHashSet(closeKeys.iterator()));
			return this;
		}

		protected IntIterable getChangeFocusKeys() { return changeFocusKeys; }

		public Builder<I, C> setChangeFocusKeys(IntIterable changeFocusKeys) {
			this.changeFocusKeys = IntSets.unmodifiable(new IntOpenHashSet(changeFocusKeys.iterator()));
			return this;
		}

		@OnlyIn(Dist.CLIENT)
		public static class WithChildren<I extends IUIInfrastructure<?, ?, ?>, C extends Container>
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
			implements IUIMinecraftScreenProviderExtension {
		@SuppressWarnings("UnstableApiUsage")
		private final TypeToken<IUIInfrastructure<?, ?, ?>> typeToken = TypeToken.of(CastUtilities.castUnchecked(IUIInfrastructure.class));
		private final OptionalWeakReference<UIMinecraftScreenAdapter<?, ?>> owner;

		public UIDefaultMinecraftScreenProviderExtension(UIMinecraftScreenAdapter<?, ?> owner) {
			this.owner = OptionalWeakReference.of(owner);
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
		public OptionalDouble getPartialTicks() {
			return getOwner()
					.map(owner -> OptionalDouble.of(owner.getLastPartialTicks()))
					.orElseGet(OptionalDouble::empty);
		}

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
		public IExtensionType<IIdentifier, ?, IUIInfrastructure<?, ?, ?>> getType() { return StaticHolder.getType().getValue(); }

		@SuppressWarnings("UnstableApiUsage")
		@Override
		public TypeToken<? extends IUIInfrastructure<?, ?, ?>> getTypeToken() {
			return typeToken;
		}
	}
}
