package $group__.client.ui.mvvm.minecraft.extensions;

import $group__.client.ui.events.bus.EventBusEntryPoint;
import $group__.client.ui.mvvm.core.IUICommon;
import $group__.client.ui.mvvm.minecraft.core.extensions.IUIExtensionBackgroundRenderer;
import $group__.client.ui.mvvm.minecraft.core.extensions.IUIExtensionScreenProvider;
import $group__.client.ui.mvvm.minecraft.core.views.IUIViewComponentMinecraft;
import $group__.client.ui.mvvm.minecraft.events.bus.EventUIViewMinecraft;
import $group__.client.ui.utilities.minecraft.UIBackgrounds;
import $group__.utilities.CastUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.extensions.ExtensionContainerAware;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.geom.Point2D;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public abstract class UIExtensionBackgroundMinecraft<E extends IUIViewComponentMinecraft<?, ?>>
		extends ExtensionContainerAware<ResourceLocation, IUIViewComponentMinecraft<?, ?>, E>
		implements IUIExtensionBackgroundRenderer {
	public UIExtensionBackgroundMinecraft(Class<E> extendedClass) {
		super(CastUtilities.castUnchecked(IUIViewComponentMinecraft.class), // COMMENT the generics should not matter here
				extendedClass);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(IUIViewComponentMinecraft<?, ?> container) {
		super.onExtensionAdded(container);
		EventBusEntryPoint.INSTANCE.register(this);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionRemoved() {
		super.onExtensionRemoved();
		EventBusEntryPoint.INSTANCE.unregister(this);
	}

	@SubscribeEvent
	protected void onRender(EventUIViewMinecraft.Render event) {
		if (event.getStage() == EnumEventHookStage.PRE) {
			getContainer().filter(Predicate.isEqual(event.getView()))
					.flatMap(IUICommon::getInfrastructure)
					.flatMap(c -> IUIExtensionScreenProvider.TYPE.getValue().get(c))
					.ifPresent(extScr -> renderBackground(extScr.getScreen(), event.getCursorPositionView(), event.getPartialTicks()));
		}
	}

	protected void renderBackground(Screen screen, Point2D mouse, double partialTicks) { UIBackgrounds.notifyBackgroundDrawn(screen); }

	@Override
	public IType<? extends ResourceLocation, ?, ? extends IUIViewComponentMinecraft<?, ?>> getType() { return IUIExtensionBackgroundRenderer.TYPE.getValue(); }

	@OnlyIn(Dist.CLIENT)
	public static class Default<E extends IUIViewComponentMinecraft<?, ?>>
			extends UIExtensionBackgroundMinecraft<E> {
		public Default(Class<E> extendedClass) { super(extendedClass); }

		@Override
		protected void renderBackground(Screen screen, Point2D mouse, double partialTicks) { UIBackgrounds.renderBackgroundAndNotify(screen.getMinecraft(), screen.width, screen.height, 0); }
	}

	@OnlyIn(Dist.CLIENT)
	public static class Dirt<E extends IUIViewComponentMinecraft<?, ?>>
			extends UIExtensionBackgroundMinecraft<E> {
		public Dirt(Class<E> extendedClass) { super(extendedClass); }

		@Override
		protected void renderBackground(Screen screen, Point2D mouse, double partialTicks) { UIBackgrounds.renderDirtBackgroundAndNotify(screen.getMinecraft(), screen.width, screen.height, 0); }
	}
}
