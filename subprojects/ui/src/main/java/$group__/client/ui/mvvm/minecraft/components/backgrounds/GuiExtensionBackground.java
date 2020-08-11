package $group__.client.ui.mvvm.views.components.minecraft.backgrounds;

import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.minecraft.components.adapters.UIAdapterScreen.UIExtensionScreen;
import $group__.client.ui.utilities.minecraft.UIBackgrounds;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.structures.Registry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import sun.misc.Cleaner;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public abstract class GuiExtensionBackground /* TODO implements IUIExtension<C> */ {
	public static final ResourceLocation KEY = new ResourceLocation(NamespaceUtilities.NAMESPACE_DEFAULT_PREFIX + "background");
	public static final Registry.RegistryObject<IUIExtension.IType<GuiExtensionBackground, IUIComponent>> TYPE = IUIExtension.RegUIExtension.INSTANCE.register(KEY, new IUIExtension.IType<GuiExtensionBackground, IUIComponent>() {
		@Override
		public Optional<GuiExtensionBackground> get(IUIComponent component) { return component.getExtension(KEY).map(CastUtilities::castUnchecked); }

		@Override
		public ResourceLocation getKey() { return KEY; }
	});
	protected WeakReference<IUIComponent> container = new WeakReference<>(null);

	@Override
	public void onExtensionAdded(IUIComponent container) {
		setContainer(container);
		Bus.FORGE.bus().get().register(this);
		Cleaner.create(container, this::onExtensionRemoved);
	}

	@Override
	public void onExtensionRemoved() {
		Bus.FORGE.bus().get().unregister(this);
		setContainer(null);
	}

	@SubscribeEvent
	protected void onRender(EventUIComponentView.Render event) {
		if (event.getStage() == EnumEventHookStage.PRE) {
			getContainer().filter(Predicate.isEqual(event.getComponent()))
					.flatMap(c -> UIExtensionScreen.TYPE.getValue().get(c))
					.ifPresent(extScr -> renderBackground(event.getStack(), extScr.getScreen(), event.getCursorView(), event.getPartialTicks()));
		}
	}

	protected Optional<IUIComponent> getContainer() { return Optional.ofNullable(container.get()); }

	protected void setContainer(@Nullable IUIComponent container) { this.container = new WeakReference<>(container); }

	protected void renderBackground(final IAffineTransformStack stack, Screen screen, Point2D mouse, float partialTicks) { UIBackgrounds.notifyBackgroundDrawn(screen); }

	@Override
	public IType<?, ?> getType() { return TYPE.getValue(); }

	@OnlyIn(Dist.CLIENT)
	public static class Default extends GuiExtensionBackground {
		@Override
		protected void renderBackground(final IAffineTransformStack stack, Screen screen, Point2D mouse, float partialTicks) { UIBackgrounds.renderBackgroundAndNotify(screen.getMinecraft(), screen.width, screen.height, 0); }
	}

	@OnlyIn(Dist.CLIENT)
	public static class Dirt extends GuiExtensionBackground {
		@Override
		protected void renderBackground(final AAffineTransformStack stack, Screen screen, Point2D mouse, float partialTicks) { UIBackgrounds.renderDirtBackgroundAndNotify(screen.getMinecraft(), screen.width, screen.height, 0); }
	}
}
