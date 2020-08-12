package $group__.client.ui.mvvm.minecraft.components.backgrounds;

import $group__.client.ui.events.bus.EventBusEntryPoint;
import $group__.client.ui.mvvm.core.IUICommon;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.minecraft.components.adapters.UIAdapterScreen.UIExtensionScreen;
import $group__.client.ui.mvvm.minecraft.core.extensions.IUIExtensionBackgroundRenderer;
import $group__.client.ui.mvvm.minecraft.core.views.IUIViewComponentMinecraft;
import $group__.client.ui.mvvm.minecraft.events.bus.EventUIViewMinecraft;
import $group__.client.ui.utilities.minecraft.UIBackgrounds;
import $group__.utilities.CastUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.extensions.ExtensionContainerAware;
import $group__.utilities.structures.Registry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sun.misc.Cleaner;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.geom.Point2D;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public abstract class UIExtensionBackgroundMinecraft
		extends ExtensionContainerAware<ResourceLocation, IUIViewComponentMinecraft<?, ?>>
		implements IUIExtensionBackgroundRenderer {
	public static final Registry.RegistryObject<IUIExtension.IType<ResourceLocation, IUIExtensionBackgroundRenderer, IUIViewComponentMinecraft<?, ?>>> TYPE =
			RegExtension.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));

	public UIExtensionBackgroundMinecraft() {
		super(CastUtilities.castUnchecked(IUIViewComponentMinecraft.class)); // COMMENT the generics should not matter here
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onExtensionAdded(IUIViewComponentMinecraft<?, ?> container) {
		super.onExtensionAdded(container);
		EventBusEntryPoint.INSTANCE.register(this);
		Cleaner.create(container, this::onExtensionRemoved);
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
					.flatMap(c -> UIExtensionScreen.TYPE.getValue().get(c))
					.ifPresent(extScr -> renderBackground(extScr.getScreen(), event.getCursorPositionView(), event.getPartialTicks()));
		}
	}

	protected void renderBackground(Screen screen, Point2D mouse, double partialTicks) { UIBackgrounds.notifyBackgroundDrawn(screen); }

	@Override
	public IType<? extends ResourceLocation, ?, ? extends IUIViewComponentMinecraft<?, ?>> getType() { return TYPE.getValue(); }

	@OnlyIn(Dist.CLIENT)
	public static class Default
			extends UIExtensionBackgroundMinecraft {
		@Override
		protected void renderBackground(Screen screen, Point2D mouse, double partialTicks) { UIBackgrounds.renderBackgroundAndNotify(screen.getMinecraft(), screen.width, screen.height, 0); }
	}

	@OnlyIn(Dist.CLIENT)
	public static class Dirt
			extends UIExtensionBackgroundMinecraft {
		@Override
		protected void renderBackground(Screen screen, Point2D mouse, double partialTicks) { UIBackgrounds.renderDirtBackgroundAndNotify(screen.getMinecraft(), screen.width, screen.height, 0); }
	}
}
