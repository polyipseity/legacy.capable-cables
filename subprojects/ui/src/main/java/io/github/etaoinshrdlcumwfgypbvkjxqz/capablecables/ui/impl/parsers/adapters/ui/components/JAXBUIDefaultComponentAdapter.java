package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventComponentType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.IJAXBUIComponentAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IJAXBUIComponentAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IJAXBUIComponentBasedAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.annotations.ui.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.annotations.ui.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.binding.UIImmutablePropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.contexts.JAXBUIImmutableComponentAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers.JAXBUIDefaultComponentAdapterAnchorHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers.JAXBUIDefaultComponentAdapterExtensionHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers.JAXBUIDefaultComponentAdapterRendererContainerHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.ShapeDescriptorBuilderFactoryRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.TreeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.AnnotationUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.awt.geom.AffineTransform;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Function;

public class JAXBUIDefaultComponentAdapter
		extends JAXBUIAbstractComponentBasedAdapter<ComponentUI, IUIViewComponent<?, ?>>
		implements IJAXBUIComponentAdapter<ComponentUI, IUIViewComponent<?, ?>> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private static final @Immutable Map<IUIEventType, Function<@Nonnull Component, @Nonnull Optional<String>>> EVENT_TYPE_FUNCTION_MAP = ImmutableMap.<IUIEventType, Function<Component, Optional<String>>>builder()
			.put(EnumUIEventDOMType.LOAD, Component::getLoad)
			.put(EnumUIEventDOMType.UNLOAD, Component::getUnload)
			.put(EnumUIEventDOMType.ABORT, Component::getAbort)
			.put(EnumUIEventDOMType.ERROR, Component::getError)
			.put(EnumUIEventDOMType.SELECT, Component::getSelect)
			.put(EnumUIEventDOMType.FOCUS_OUT_POST, Component::getBlur)
			.put(EnumUIEventDOMType.FOCUS_IN_POST, Component::getFocus)
			.put(EnumUIEventDOMType.FOCUS_IN_PRE, Component::getFocusin)
			.put(EnumUIEventDOMType.FOCUS_OUT_PRE, Component::getFocusout)
			.put(EnumUIEventDOMType.CLICK_AUXILIARY, Component::getAuxclick)
			.put(EnumUIEventDOMType.CLICK, Component::getClick)
			.put(EnumUIEventDOMType.CLICK_DOUBLE, Component::getDblclick)
			.put(EnumUIEventDOMType.MOUSE_DOWN, Component::getMousedown)
			.put(EnumUIEventDOMType.MOUSE_ENTER, Component::getMouseenter)
			.put(EnumUIEventDOMType.MOUSE_LEAVE, Component::getMouseleave)
			.put(EnumUIEventDOMType.MOUSE_MOVE, Component::getMousemove)
			.put(EnumUIEventDOMType.MOUSE_LEAVE_SELF, Component::getMouseout)
			.put(EnumUIEventDOMType.MOUSE_ENTER_SELF, Component::getMouseover)
			.put(EnumUIEventDOMType.MOUSE_UP, Component::getMouseup)
			.put(EnumUIEventDOMType.WHEEL, Component::getWheel)
			.put(EnumUIEventDOMType.INPUT_PRE, Component::getBeforeinput)
			.put(EnumUIEventDOMType.INPUT_POST, Component::getInput)
			.put(EnumUIEventDOMType.KEY_DOWN, Component::getKeydown)
			.put(EnumUIEventDOMType.KEY_UP, Component::getKeyup)
			.put(EnumUIEventDOMType.COMPOSITION_START, Component::getCompositionstart)
			.put(EnumUIEventDOMType.COMPOSITION_UPDATE, Component::getCompositionupdate)
			.put(EnumUIEventDOMType.COMPOSITION_END, Component::getCompositionend)
			.put(EnumUIEventComponentType.CHAR_TYPED, Component::getCharTyped)
			.build();

	public static <T extends JAXBUIDefaultComponentAdapter> T makeParserStandard(T instance) {
		instance.addObjectHandler(Anchor.class, new JAXBUIDefaultComponentAdapterAnchorHandler());
		instance.addObjectHandler(RendererContainer.class, new JAXBUIDefaultComponentAdapterRendererContainerHandler());
		instance.addObjectHandler(Extension.class, new JAXBUIDefaultComponentAdapterExtensionHandler());
		return instance;
	}

	@Override
	@Deprecated
	public @Nonnull IUIViewComponent<?, ?> leftToRight(@Nonnull ComponentUI left) {
		return getThreadLocalContext()
				.map(context -> {
					try {
						// COMMENT raw
						@Immutable Map<String, Class<?>> aliases = JAXBUIComponentUtilities.adaptUsingFromJAXB(left.getUsing());
						View rawView = left.getView();
						Component rawComponent = left.getComponent();

						// COMMENT create hierarchy
						IUIViewComponent<?, ?> view = createView(
								context.withData(ImmutableMap.of(
										IJAXBUIComponentAdapterContext.class, new JAXBUIImmutableComponentAdapterContext(aliases, getObjectHandlers(), getElementHandlers(), null, null)
								)),
								rawView);
						IJAXBUIComponentAdapterContext viewContext = new JAXBUIImmutableComponentAdapterContext(aliases, getObjectHandlers(), getElementHandlers(), view, view);
						view.setManager(
								CastUtilities.castUnchecked(createComponent(
										context.withData(ImmutableMap.of(IJAXBUIComponentAdapterContext.class, viewContext)),
										rawComponent)) // COMMENT may throw
						);

						// COMMENT configure view
						Iterables.concat(
								rawView.getExtension(),
								rawView.getAnyContainer()
										.map(AnyContainer::getAny)
										.orElseGet(ImmutableList::of))
								.forEach(any -> {
											assert any != null;
											IJAXBUIComponentBasedAdapterContext.findHandler(viewContext, any)
													.ifPresent(handler -> handler.accept(
															context.withData(ImmutableMap.of(IJAXBUIComponentAdapterContext.class, viewContext)),
															CastUtilities.castUnchecked(any) // COMMENT should not throw
													));
										}
								);

						// COMMENT configure components
						TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, rawComponent,
								node -> {
									assert node != null;
									IUIComponent component = IUIView.getNamedTrackers(view).get(IUIComponent.class, node.getName())
											.orElseThrow(AssertionError::new);
									IJAXBUIComponentAdapterContext componentContext = new JAXBUIImmutableComponentAdapterContext(aliases, getObjectHandlers(), getElementHandlers(), view, component);
									Iterables.concat(
											node.getAnchor(),
											ImmutableSet.of(node.getRendererContainer()),
											node.getExtension(),
											node.getAnyContainer()
													.map(AnyContainer::getAny)
													.orElseGet(ImmutableList::of))
											.forEach(any -> {
												assert any != null;
												IJAXBUIComponentBasedAdapterContext.findHandler(componentContext, any)
														.ifPresent(handler -> handler.accept(
																context.withData(ImmutableMap.of(IJAXBUIComponentAdapterContext.class, componentContext)),
																CastUtilities.castUnchecked(any) // COMMENT should not throw
														));
											});
									return node;
								},
								Component::getComponent,
								null,
								node -> {
									throw new IllegalArgumentException(
											new LogMessageBuilder()
													.addMarkers(UIMarkers.getInstance()::getMarkerParser)
													.addKeyValue("node", node).addKeyValue("rawComponent", rawComponent)
													.addMessages(() -> getResourceBundle().getString("construct.view.tree.cyclic"))
													.build()
									);
								});
						return view;
					} catch (Throwable throwable) {
						throw ThrowableUtilities.propagate(throwable);
					}
				})
				.orElseThrow(IllegalStateException::new);
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static IUIViewComponent<?, ?> createView(IJAXBAdapterContext context, View view)
			throws Throwable {
		return context.getDatum(IJAXBUIComponentAdapterContext.class)
				.map(IThrowingFunction.executeNow(subContext -> {
					Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(context, view.getProperty());
					UIViewComponentConstructor.IArguments argument = new UIViewComponentConstructor.ImmutableArguments(mappings);

					Class<?> clazz = AssertionUtilities.assertNonnull(subContext.getAliasesView().get(view.getClazz()));
					Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIViewComponentConstructor.class, Arrays.asList(clazz.getDeclaredConstructors()));
					MethodHandle constructorHandle = InvokeUtilities.getImplLookup().unreflectConstructor(constructor);

					return (IUIViewComponent<?, ?>) constructorHandle.invoke(argument);
				}))
				.orElseThrow(IllegalArgumentException::new);
	}

	public static IUIComponent createComponent(IJAXBAdapterContext context, Component component)
			throws Throwable {
		return context.getDatum(IJAXBUIComponentAdapterContext.class)
				.map(IThrowingFunction.executeNow(subContext ->
						TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, component,
								IThrowingFunction.executeNow(node -> {
									assert node != null;
									Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(context, node.getProperty());
									// COMMENT attributes
									{
										Map<INamespacePrefixedString, IUIPropertyMappingValue> attributes = new HashMap<>(getEventTypeFunctionMap().size());

										getEventTypeFunctionMap()
												.forEach((key, value) -> {
													assert key != null;
													assert value != null;
													attributes.put(key.getEventType(),
															new UIImmutablePropertyMappingValue(null,
																	AssertionUtilities.assertNonnull(value.apply(node))
																			.map(IUIEventType.StaticHolder.getDefaultPrefix()::concat)
																			.map(ImmutableNamespacePrefixedString::of)
																			.orElse(null)));
												});

										mappings = MapUtilities.concatMaps(mappings, attributes);
									}
									IShapeDescriptorBuilder<?> shapeDescriptorBuilder = createShapeDescriptorBuilder(context, node.getShape());
									UIComponentConstructor.IArguments argument = new UIComponentConstructor.ImmutableArguments(node.getName(),
											mappings,
											shapeDescriptorBuilder.build());

									Class<?> clazz = AssertionUtilities.assertNonnull(subContext.getAliasesView().get(node.getClazz()));
									Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIComponentConstructor.class,
											Arrays.asList(clazz.getDeclaredConstructors()));
									MethodHandle constructorHandle = InvokeUtilities.getImplLookup().unreflectConstructor(constructor);

									return (IUIComponent) constructorHandle.invoke(argument);
								}),
								Component::getComponent,
								(p, c) -> {
									if (!Iterables.isEmpty(c)) {
										if (!(p instanceof IUIComponentContainer))
											throw new IllegalArgumentException(
													new LogMessageBuilder()
															.addMarkers(UIMarkers.getInstance()::getMarkerParser)
															.addKeyValue("p", p).addKeyValue("c", c)
															.addMessages(() -> getResourceBundle().getString("construct.component.container.instance_of.fail"))
															.build()
											);
										((IUIComponentContainer) p).addChildren(c);
									}
									return p;
								},
								n -> {
									throw new IllegalArgumentException(
											new LogMessageBuilder()
													.addMarkers(UIMarkers.getInstance()::getMarkerParser)
													.addKeyValue("n", n).addKeyValue("component", component)
													.addMessages(() -> getResourceBundle().getString("construct.view.tree.cyclic"))
													.build()
									);
								})
								.orElseThrow(AssertionError::new)
				))
				.orElseThrow(IllegalArgumentException::new);
	}

	public static @Immutable Map<IUIEventType, Function<@Nonnull Component, @Nonnull Optional<String>>> getEventTypeFunctionMap() { return EVENT_TYPE_FUNCTION_MAP; }

	@SuppressWarnings("UnstableApiUsage")
	public static Map<INamespacePrefixedString, IUIPropertyMappingValue> createMappings(IJAXBAdapterContext context, Iterable<? extends Property> properties) {
		return Streams.stream(properties).unordered()
				.map(p -> Maps.immutableEntry(ImmutableNamespacePrefixedString.of(p.getKey()),
						new UIImmutablePropertyMappingValue(p.getAny()
								.map(any -> IJAXBAdapterRegistry.adaptFromJAXB(context, any))
								.orElse(null),
								p.getBindingKey()
										.map(ImmutableNamespacePrefixedString::of)
										.orElse(null))))
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@SuppressWarnings("UnstableApiUsage")
	public static IShapeDescriptorBuilder<?> createShapeDescriptorBuilder(IJAXBAdapterContext context, Shape shape) {
		return context.getDatum(IJAXBUIComponentAdapterContext.class)
				.map(subContext -> {
					AffineTransform transform = shape.getAffineTransform()
							.map(transform1 -> IJAXBAdapterRegistry.adaptFromJAXB(context, transform1))
							.map(AffineTransform.class::cast)
							.orElseGet(AffineTransform::new);

					IShapeDescriptorBuilder<?> sdb = ShapeDescriptorBuilderFactoryRegistry.getDefaultFactory()
							.createBuilder(
									CastUtilities.castUnchecked(AssertionUtilities.assertNonnull(subContext.getAliasesView().get(shape.getClazz()))) // COMMENT should not throw
							);

					shape.getProperty().stream().unordered()
							.forEach(p -> {
								assert !p.getBindingKey().isPresent();
								sdb.setProperty(p.getKey(), p.getAny()
										.map(any -> IJAXBAdapterRegistry.adaptFromJAXB(context, any))
										.orElse(null));
							});

					return sdb.setX(shape.getX()).setY(shape.getY()).setWidth(shape.getWidth()).setHeight(shape.getHeight())
							.transformConcatenate(transform)
							.constrain(shape.getConstraint().stream()
									.map(c -> new ShapeConstraint(
											c.getMinX().orElse(null),
											c.getMinY().orElse(null),
											c.getMaxX().orElse(null),
											c.getMaxY().orElse(null),
											c.getMinWidth().orElse(null),
											c.getMinHeight().orElse(null),
											c.getMaxWidth().orElse(null),
											c.getMaxHeight().orElse(null)
									))
									.collect(ImmutableList.toImmutableList()));
				})
				.orElseThrow(IllegalArgumentException::new);
	}

	@Override
	@Deprecated
	public @Nonnull ComponentUI rightToLeft(@Nonnull IUIViewComponent<?, ?> right) {
		throw new UnsupportedOperationException(); // TODO implement
	}
}
