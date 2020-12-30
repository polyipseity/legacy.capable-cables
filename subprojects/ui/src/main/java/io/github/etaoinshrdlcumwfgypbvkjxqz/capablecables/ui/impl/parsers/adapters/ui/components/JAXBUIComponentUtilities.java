package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIViewComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventComponentType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IJAXBUIComponentAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.binding.UIImmutablePropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction.UIImmutableComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction.UIImmutableComponentEmbedPrototypeArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.construction.UIImmutableViewComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.ShapeDescriptorBuilderFactoryRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.TreeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.AnnotationUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IThrowingFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IUnion;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableUnion;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import java.awt.geom.AffineTransform;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Function;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressUnboxing;

// TODO needs improvement, too procedural
public enum JAXBUIComponentUtilities {
	;

	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private static final Map<IUIEventType, Function<@Nonnull IUnion<? extends Component, ? extends ComponentEmbed>, @Nonnull Optional<String>>> EVENT_TYPE_FUNCTION_MAP = ImmutableMap.<IUIEventType, Function<@Nonnull IUnion<? extends Component, ? extends ComponentEmbed>, @Nonnull Optional<String>>>builder()
			.put(EnumUIEventDOMType.LOAD, component -> component.map(Component::getLoad, ComponentEmbed::getLoad))
			.put(EnumUIEventDOMType.UNLOAD, component -> component.map(Component::getUnload, ComponentEmbed::getUnload))
			.put(EnumUIEventDOMType.ABORT, component -> component.map(Component::getAbort, ComponentEmbed::getAbort))
			.put(EnumUIEventDOMType.ERROR, component -> component.map(Component::getError, ComponentEmbed::getError))
			.put(EnumUIEventDOMType.SELECT, component -> component.map(Component::getSelect, ComponentEmbed::getSelect))
			.put(EnumUIEventDOMType.FOCUS_OUT_POST, component -> component.map(Component::getBlur, ComponentEmbed::getBlur))
			.put(EnumUIEventDOMType.FOCUS_IN_POST, component -> component.map(Component::getFocus, ComponentEmbed::getFocus))
			.put(EnumUIEventDOMType.FOCUS_IN_PRE, component -> component.map(Component::getFocusin, ComponentEmbed::getFocusin))
			.put(EnumUIEventDOMType.FOCUS_OUT_PRE, component -> component.map(Component::getFocusout, ComponentEmbed::getFocusout))
			.put(EnumUIEventDOMType.CLICK_AUXILIARY, component -> component.map(Component::getAuxclick, ComponentEmbed::getAuxclick))
			.put(EnumUIEventDOMType.CLICK, component -> component.map(Component::getClick, ComponentEmbed::getClick))
			.put(EnumUIEventDOMType.CLICK_DOUBLE, component -> component.map(Component::getDblclick, ComponentEmbed::getDblclick))
			.put(EnumUIEventDOMType.MOUSE_DOWN, component -> component.map(Component::getMousedown, ComponentEmbed::getMousedown))
			.put(EnumUIEventDOMType.MOUSE_ENTER, component -> component.map(Component::getMouseenter, ComponentEmbed::getMouseenter))
			.put(EnumUIEventDOMType.MOUSE_LEAVE, component -> component.map(Component::getMouseleave, ComponentEmbed::getMouseleave))
			.put(EnumUIEventDOMType.MOUSE_MOVE, component -> component.map(Component::getMousemove, ComponentEmbed::getMousemove))
			.put(EnumUIEventDOMType.MOUSE_LEAVE_SELF, component -> component.map(Component::getMouseout, ComponentEmbed::getMouseout))
			.put(EnumUIEventDOMType.MOUSE_ENTER_SELF, component -> component.map(Component::getMouseover, ComponentEmbed::getMouseover))
			.put(EnumUIEventDOMType.MOUSE_UP, component -> component.map(Component::getMouseup, ComponentEmbed::getMouseup))
			.put(EnumUIEventDOMType.WHEEL, component -> component.map(Component::getWheel, ComponentEmbed::getWheel))
			.put(EnumUIEventDOMType.INPUT_PRE, component -> component.map(Component::getBeforeinput, ComponentEmbed::getBeforeinput))
			.put(EnumUIEventDOMType.INPUT_POST, component -> component.map(Component::getInput, ComponentEmbed::getInput))
			.put(EnumUIEventDOMType.KEY_DOWN, component -> component.map(Component::getKeydown, ComponentEmbed::getKeydown))
			.put(EnumUIEventDOMType.KEY_UP, component -> component.map(Component::getKeyup, ComponentEmbed::getKeyup))
			.put(EnumUIEventDOMType.COMPOSITION_START, component -> component.map(Component::getCompositionstart, ComponentEmbed::getCompositionstart))
			.put(EnumUIEventDOMType.COMPOSITION_UPDATE, component -> component.map(Component::getCompositionupdate, ComponentEmbed::getCompositionupdate))
			.put(EnumUIEventDOMType.COMPOSITION_END, component -> component.map(Component::getCompositionend, ComponentEmbed::getCompositionend))
			.put(EnumUIEventComponentType.CHAR_TYPED, component -> component.map(Component::getCharTyped, ComponentEmbed::getCharTyped))
			.build();

	@SuppressWarnings("UnstableApiUsage")
	public static @Immutable Map<String, Class<?>> adaptUsingFromJAXB(Iterator<? extends Using> using)
			throws ClassNotFoundException {
		return Streams.stream(using).unordered()
				.map(IThrowingFunction.executeNow(u -> Maps.immutableEntry(u.getAlias(), Class.forName(u.getTarget()))))
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@SuppressWarnings("cast")
	public static IUIViewComponent<?, ?> createView(IJAXBAdapterContext context, View view)
			throws Throwable {
		return context.getDatum(IJAXBUIComponentAdapterContext.class)
				.map(IThrowingFunction.executeNow(subContext -> {
					Map<IIdentifier, IUIPropertyMappingValue> mappings = createMappings(context, view.getProperty().iterator());
					IUIViewComponentArguments argument = UIImmutableViewComponentArguments.of(mappings);

					Class<?> clazz = AssertionUtilities.assertNonnull(subContext.getAliasesView().get(view.getClazz()));
					Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIViewComponentConstructor.class, ImmutableList.copyOf(clazz.getDeclaredConstructors()).iterator());
					MethodHandle constructorHandle = InvokeUtilities.getImplLookup().unreflectConstructor(constructor);
					constructorHandle = constructorHandle.asType(constructorHandle.type().changeReturnType(IUIViewComponent.class));

					return (IUIViewComponent<?, ?>) constructorHandle.invokeExact((IUIViewComponentArguments) argument);
				}))
				.orElseThrow(IllegalArgumentException::new);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static Map<IIdentifier, IUIPropertyMappingValue> createMappings(IJAXBAdapterContext context, Iterator<? extends Property> properties) {
		return Streams.stream(properties).unordered()
				.map(p -> Maps.immutableEntry(ImmutableIdentifier.ofInterning(p.getKey()),
						UIImmutablePropertyMappingValue.of(p.getAny()
										.map(any -> IJAXBAdapterRegistry.adaptFromJAXB(context, any))
										.orElse(null),
								p.getBindingKey()
										.map(ImmutableIdentifier::ofInterning)
										.orElse(null))))
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@SuppressWarnings("cast")
	public static IUIComponent createComponent(IJAXBAdapterContext context, Component component)
			throws Throwable {
		return context.getDatum(IJAXBUIComponentAdapterContext.class)
				.map(IThrowingFunction.executeNow(subContext ->
						TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, component,
								IThrowingFunction.executeNow(node -> {
									Map<IIdentifier, IUIPropertyMappingValue> mappings =
											MapUtilities.concatMaps(createMappings(context, node.getProperty().iterator()),
													createEventMappings(ImmutableUnion.ofLeft(node)));

									IShapeDescriptorBuilder<?> shapeDescriptorBuilder = createShapeDescriptorBuilder(context, node.getShape());
									IUIComponentArguments argument = UIImmutableComponentArguments.of(node.getName(),
											mappings,
											shapeDescriptorBuilder.build(),
											node.getRendererName(),
											createComponentEmbedPrototypeMap(context, node.getComponentEmbed().iterator())
									);

									Class<?> clazz = AssertionUtilities.assertNonnull(subContext.getAliasesView().get(node.getClazz()));
									Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIComponentConstructor.class,
											ImmutableList.copyOf(clazz.getDeclaredConstructors()).iterator());
									MethodHandle constructorHandle = InvokeUtilities.getImplLookup().unreflectConstructor(constructor);
									constructorHandle = constructorHandle.asType(constructorHandle.type().changeReturnType(IUIComponent.class));

									return (IUIComponent) constructorHandle.invokeExact((IUIComponentArguments) argument);
								}),
								Component::getComponent,
								(p, c) -> {
									IUIComponent.addContentChildren(p, c.iterator());
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

	@SuppressWarnings("UnstableApiUsage")
	public static @Immutable Map<String, IUIComponentArguments.IEmbedPrototype> createComponentEmbedPrototypeMap(IJAXBAdapterContext context,
	                                                                                                             Iterator<? extends ComponentEmbed> jaxbComponentEmbeds) {
		return Streams.stream(jaxbComponentEmbeds)
				.map(jaxbComponentEmbed -> Maps.immutableEntry(jaxbComponentEmbed.getName(),
						UIImmutableComponentEmbedPrototypeArguments.of(createMappings(context, jaxbComponentEmbed.getProperty().iterator()),
								jaxbComponentEmbed.getRendererName().orElse(null),
								createComponentEmbedPrototypeMap(context, jaxbComponentEmbed.getComponentEmbed().iterator()))))
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public static @Immutable Map<IIdentifier, IUIPropertyMappingValue> createEventMappings(IUnion<? extends Component, ? extends ComponentEmbed> component) {
		Map<IIdentifier, IUIPropertyMappingValue> attributes = new HashMap<>(getEventTypeFunctionMapView().size());
		getEventTypeFunctionMapView()
				.forEach((key, value) -> {
					assert key != null;
					assert value != null;
					attributes.put(key.getEventType(),
							UIImmutablePropertyMappingValue.ofKey(
									AssertionUtilities.assertNonnull(value.apply(component))
											.map(IUIEventType.StaticHolder.getDefaultPrefix()::concat)
											.map(ImmutableIdentifier::ofInterning)
											.orElse(null)));
				});
		return ImmutableMap.copyOf(attributes);
	}

	public static IShapeDescriptorBuilder<?> createShapeDescriptorBuilder(IJAXBAdapterContext context, Shape shape) {
		return context.getDatum(IJAXBUIComponentAdapterContext.class)
				.map(subContext -> {
					java.awt.geom.AffineTransform transform = shape.getAffineTransform()
							.map(transform1 -> IJAXBAdapterRegistry.adaptFromJAXB(context, transform1))
							.map(java.awt.geom.AffineTransform.class::cast)
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

					return sdb.setX(suppressUnboxing(shape.getX()))
							.setY(suppressUnboxing(shape.getY()))
							.setWidth(suppressUnboxing(shape.getWidth()))
							.setHeight(suppressUnboxing(shape.getHeight()))
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
									.iterator());
				})
				.orElseThrow(IllegalArgumentException::new);
	}

	protected static ResourceBundle getResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	public static @Immutable Map<IUIEventType, Function<@Nonnull IUnion<? extends Component, ? extends ComponentEmbed>, @Nonnull Optional<String>>> getEventTypeFunctionMapView() {
		return ImmutableMap.copyOf(getEventTypeFunctionMap());
	}

	private static Map<IUIEventType, Function<@Nonnull IUnion<? extends Component, ? extends ComponentEmbed>, @Nonnull Optional<String>>> getEventTypeFunctionMap() {
		return EVENT_TYPE_FUNCTION_MAP;
	}
}
