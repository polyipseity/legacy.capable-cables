package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components;

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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IUnion;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableUnion;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import java.util.Map;

public enum JAXBUIComponentUtilities {
	;

	@SuppressWarnings("UnstableApiUsage")
	public static @Immutable Map<String, Class<?>> adaptUsingFromJAXB(Iterable<? extends Using> using)
			throws ClassNotFoundException {
		return Streams.stream(using).unordered()
				.map(IThrowingFunction.executeNow(u -> {
					assert u != null;
					return Maps.immutableEntry(u.getAlias(), Class.forName(u.getTarget()));
				}))
				.orElseThrow(IllegalArgumentException::new);
	}

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

	@SuppressWarnings("cast")
	public static IUIComponent createComponent(IJAXBAdapterContext context, Component component)
			throws Throwable {
		return context.getDatum(IJAXBUIComponentAdapterContext.class)
				.map(IThrowingFunction.executeNow(subContext ->
						TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, component,
								IThrowingFunction.executeNow(node -> {
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
									IUIComponentArguments argument = UIImmutableComponentArguments.of(node.getName(),
											mappings,
											shapeDescriptorBuilder.build(),
											node.getRendererName());

									Class<?> clazz = AssertionUtilities.assertNonnull(subContext.getAliasesView().get(node.getClazz()));
									Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIComponentConstructor.class,
											Arrays.asList(clazz.getDeclaredConstructors()));
									MethodHandle constructorHandle = InvokeUtilities.getImplLookup().unreflectConstructor(constructor);
									constructorHandle = constructorHandle.asType(constructorHandle.type().changeReturnType(IUIComponent.class));

									return (IUIComponent) constructorHandle.invokeExact((IUIComponentArguments) argument);
								}),
								Component::getComponent,
								(p, c) -> {
									p.addChildren(c);
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

	protected static ResourceBundle getResourceBundle() {
		return RESOURCE_BUNDLE;
	}
}
