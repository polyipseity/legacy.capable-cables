package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.EnumModifyStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRendererInvokerModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.annotations.components.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.MinecraftGraphics;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.IUIMinecraftViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.components.modifiers.IUIComponentMinecraftLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.views.rendering.IUIMinecraftComponentRenderer.EnumRenderStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.events.bus.UIImmutableMinecraftRenderEventExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.mvvm.extensions.background.UIDefaultMinecraftBackgroundExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.utilities.EnumMinecraftCropMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIAbstractViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultComponentContextMutator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIImmutableComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.paths.ArrayAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.paths.FunctionalPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class UIDefaultMinecraftViewComponent<S extends Shape, M extends IUIComponentManager<S>>
		extends UIAbstractViewComponent<S, M>
		implements IUIMinecraftViewComponent<S, M> {

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIViewComponentConstructor
	public UIDefaultMinecraftViewComponent(UIViewComponentConstructor.IArguments arguments) {
		super(arguments);

		IExtensionContainer.addExtensionChecked(this, new UIDefaultMinecraftBackgroundExtension()); // COMMENT to ensure that 'GuiScreenEvent.BackgroundDrawnEvent' is fired
	}

	@Override
	public void render(IUIViewContext context, double partialTicks) {
		MinecraftGraphics.clear();
		IUIView.getAnimationController(this).update();
		IUIView.getAnimationController(this).render();
		getManager()
				.ifPresent(manager -> {
							try (IUIComponentContext componentContext = createComponentContext()
									.orElseThrow(IllegalStateException::new)) {
								EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
											EnumMinecraftCropMethod cropMethod = EnumMinecraftCropMethod.getBestMethod();
											cropMethod.enable();
											IUIViewComponent.traverseComponentTreeDefault(componentContext,
													manager,
													(componentContext2, result) -> {
														assert result != null;
														assert componentContext2 != null;
														IUIComponentMinecraft component = (IUIComponentMinecraft) result.getComponent();
														IUIComponentModifier.streamSpecificModifiersUnion(result.getModifiersView(), IUIComponentRendererInvokerModifier.class)
																.forEachOrdered(modifierIntersection -> {
																	IUIComponentModifier left = modifierIntersection.getLeft();
																	IUIComponentRendererInvokerModifier right = modifierIntersection.getRight();
																	EnumModifyStage.PRE.advanceModifyStage(left);
																	right.invokeRenderer(componentContext2); // COMMENT pre
																	left.resetModifyStage();
																});
														component.getRendererContainer().getRenderer()
																.ifPresent(renderer ->
																		renderer.render(componentContext2, EnumRenderStage.PRE_CHILDREN,
																				CastUtilities.castUnchecked(component),
																				partialTicks)
																);
													},
													(componentContext2, result, children) -> {
														assert result != null;
														assert componentContext2 != null;
														IUIComponentMinecraft component = (IUIComponentMinecraft) result.getComponent();
														component.getRendererContainer().getRenderer().ifPresent(renderer ->
																renderer.render(componentContext2, EnumRenderStage.POST_CHILDREN,
																		CastUtilities.castUnchecked(component),
																		partialTicks)
														);
														IUIComponentModifier.streamSpecificModifiersUnion(result.getModifiersView(), IUIComponentRendererInvokerModifier.class)
																.forEachOrdered(modifierIntersection -> {
																	IUIComponentModifier left = modifierIntersection.getLeft();
																	IUIComponentRendererInvokerModifier right = modifierIntersection.getRight();
																	EnumModifyStage.POST.advanceModifyStage(left);
																	right.invokeRenderer(componentContext2); // COMMENT post
																	left.resetModifyStage();
																});
													},
													component -> component.isVisible() && component instanceof IUIComponentMinecraft);
											cropMethod.disable();
											return true;
										},
										FunctionUtilities.accept(new UIAbstractViewBusEvent.Render(EnumHookStage.PRE, this),
												event -> IExtensionContainer.addExtensionChecked(event,
														UIImmutableMinecraftRenderEventExtension.of(partialTicks))),
										FunctionUtilities.accept(new UIAbstractViewBusEvent.Render(EnumHookStage.POST, this),
												event -> IExtensionContainer.addExtensionChecked(event,
														UIImmutableMinecraftRenderEventExtension.of(partialTicks))));
							}
						}
				);
		MinecraftOpenGLUtilities.Stacks.clearAll();
		MinecraftGraphics.draw();
	}

	@Override
	public Optional<? extends IUIComponentContext> createComponentContext() throws IllegalStateException {
		return getContext()
				.map(context -> {
							try (AutoCloseableGraphics2D graphics = MinecraftGraphics.createGraphics()) {
								return new UIImmutableComponentContext(
										context,
										this,
										new UIDefaultComponentContextMutator(),
										graphics,
										new FunctionalPath<>(ImmutableList.of(), Lists::newArrayList),
										new ArrayAffineTransformStack(CapacityUtilities.getInitialCapacityMedium()));
							}
						}
				);
	}

	@Override
	public void tick() {
		getManager().ifPresent(manager -> {
			try (IUIComponentContext componentContext = createComponentContext().orElseThrow(IllegalStateException::new)) {
				IUIViewComponent.<RuntimeException>traverseComponentTreeDefault(componentContext,
						manager,
						(componentContext2, result) -> {
							assert componentContext2 != null;
							assert result != null;
							CastUtilities.castChecked(IUIComponentMinecraft.class, result.getComponent())
									.ifPresent(cc ->
											IUIComponentMinecraftLifecycleModifier.handleComponentModifiers(cc,
													result.getModifiersView(),
													componentContext2,
													IUIComponentMinecraftLifecycleModifier::tick)
									);
						},
						IConsumer3.StaticHolder.getEmpty(),
						IUIEventTarget::isActive);
			}
		});
	}
}
