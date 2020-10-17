package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.binding.UIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventComponentType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.UIParserCheckedException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.UIParserUncheckedException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.contexts.IUIAbstractDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.contexts.IUIDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.contexts.UIImmutableDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers.UIDefaultDefaultComponentParserAnchorHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers.UIDefaultDefaultComponentParserExtensionHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers.UIDefaultDefaultComponentParserRendererContainerHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.descriptors.ShapeDescriptorBuilderFactoryRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.interactions.ShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.TreeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.AnnotationUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingConsumer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;

import java.awt.geom.AffineTransform;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.regex.Pattern;

public class UIDefaultComponentParser<T extends IUIViewComponent<?, ?>>
		extends UIAbstractDefaultComponentParser<T, Ui, Ui, IUIDefaultComponentParserContext>
		implements IHasGenericClass<T> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private final Class<T> genericClass;

	public UIDefaultComponentParser(Class<T> genericClass) {
		this.genericClass = genericClass;
	}

	public static <T extends UIDefaultComponentParser<?>> T makeParserStandard(T instance) {
		instance.addObjectHandler(Anchor.class, new UIDefaultDefaultComponentParserAnchorHandler());
		instance.addObjectHandler(RendererContainer.class, new UIDefaultDefaultComponentParserRendererContainerHandler());
		instance.addObjectHandler(Extension.class, new UIDefaultDefaultComponentParserExtensionHandler());
		return instance;
	}

	@SuppressWarnings("RedundantThrows")
	@Override
	protected Iterable<? extends Using> getRawAliases(Ui resource)
			throws UIParserCheckedException, UIParserUncheckedException { return resource.getUsing(); }

	@SuppressWarnings("RedundantThrows")
	@Override
	public Ui parse1(Ui resource)
			throws Throwable {
		Class<?> viewClass = AssertionUtilities.assertNonnull(getAliases().get(resource.getView().getClazz()));
		if (!getGenericClass().isAssignableFrom(viewClass))
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerParser)
							.addKeyValue("viewClass", viewClass).addKeyValue("this::getGenericClass", this::getGenericClass)
							.addMessages(() -> getResourceBundle().getString("construct.view.instance_of.fail"))
							.build()
			);

		return resource;
	}

	@Override
	public Class<T> getGenericClass() { return genericClass; }

	@Override
	protected T construct0(Ui parsed)
			throws Throwable {
		// COMMENT raw
		View rawView = parsed.getView();
		Component rawComponent = parsed.getComponent();

		// COMMENT create hierarchy
		@SuppressWarnings("unchecked") T view = (T) createView(new UIImmutableDefaultComponentParserContext(getAliases(), getHandlers(), null, null), rawView); // COMMENT should be checked
		IUIDefaultComponentParserContext viewContext = new UIImmutableDefaultComponentParserContext(getAliases(), getHandlers(), view, view);
		view.setManager(
				CastUtilities.castUnchecked(createComponent(viewContext, rawComponent)) // COMMENT may throw
		);

		// COMMENT configure view
		Iterables.concat(
				rawView.getExtension(),
				rawView.getAnyContainer()
						.map(AnyContainer::getAny)
						.orElseGet(ImmutableList::of))
				.forEach(any -> {
							assert any != null;
							IUIAbstractDefaultComponentParserContext.findHandler(viewContext, any)
									.ifPresent(handler -> handler.acceptNonnull(
											viewContext,
											CastUtilities.castUnchecked(any) // COMMENT should not throw
									));
						}
				);

		// COMMENT configure components
		TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, rawComponent,
				node -> {
					assert node != null;
					IUIComponent component = view.getNamedTrackers().get(IUIComponent.class, node.getName())
							.orElseThrow(AssertionError::new);
					IUIDefaultComponentParserContext componentContext = new UIImmutableDefaultComponentParserContext(getAliases(), getHandlers(), view, component);
					Iterables.concat(
							node.getAnchor(),
							ImmutableSet.of(node.getRendererContainer()),
							node.getExtension(),
							node.getAnyContainer()
									.map(AnyContainer::getAny)
									.orElseGet(ImmutableList::of))
							.forEach(any -> {
								assert any != null;
								IUIAbstractDefaultComponentParserContext.findHandler(componentContext, any)
										.ifPresent(handler -> handler.acceptNonnull(
												componentContext,
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
	}

	public static IUIViewComponent<?, ?> createView(IUIDefaultComponentParserContext context, View view)
			throws Throwable {
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(view.getProperty());
		UIViewComponentConstructor.IArguments argument = new UIViewComponentConstructor.ImmutableArguments(mappings);

		Class<?> clazz = AssertionUtilities.assertNonnull(context.getAliasesView().get(view.getClazz()));
		Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIViewComponentConstructor.class, Arrays.asList(clazz.getDeclaredConstructors()));
		MethodHandle constructorHandle = InvokeUtilities.IMPL_LOOKUP.unreflectConstructor(constructor);

		return (IUIViewComponent<?, ?>) constructorHandle.invoke(argument);
	}

	public static IUIComponent createComponent(IUIDefaultComponentParserContext context, Component component)
			throws Throwable {
		return TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, component,
				IThrowingFunction.executeNow(node -> {
					assert node != null;
					Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(node.getProperty());
					// COMMENT attributes
					{
						Map<INamespacePrefixedString, IUIPropertyMappingValue> attributes = new HashMap<>(
								EnumUIEventDOMType.values().length + EnumUIEventComponentType.values().length
						);

						// TODO use a map or something like that
						attributes.put(EnumUIEventDOMType.LOAD.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getLoad()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.UNLOAD.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getUnload()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.ABORT.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getAbort()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.ERROR.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getError()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.SELECT.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getSelect()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.FOCUS_OUT_POST.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getBlur()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getFocus()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.FOCUS_IN_PRE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getFocusin()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.FOCUS_OUT_PRE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getFocusout()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.CLICK_AUXILIARY.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getAuxclick()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.CLICK.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getClick()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.CLICK_DOUBLE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getDblclick()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_DOWN.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMousedown()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_ENTER.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMouseenter()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMouseleave()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_MOVE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMousemove()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMouseout()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMouseover()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_UP.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMouseup()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.WHEEL.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getWheel()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.INPUT_PRE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getBeforeinput()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.INPUT_POST.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getInput()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.KEY_DOWN.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getKeydown()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.KEY_UP.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getKeyup()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.COMPOSITION_START.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getCompositionstart()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.COMPOSITION_UPDATE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getCompositionupdate()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventDOMType.COMPOSITION_END.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getCompositionend()).map(ImmutableNamespacePrefixedString::of).orElse(null)));
						attributes.put(EnumUIEventComponentType.CHAR_TYPED.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getCharTyped()).map(ImmutableNamespacePrefixedString::of).orElse(null)));

						mappings = MapUtilities.concatMaps(mappings, attributes);
					}
					IShapeDescriptorBuilder<?> shapeDescriptorBuilder = createShapeDescriptorBuilder(context, node.getShape());
					UIComponentConstructor.IArguments argument = new UIComponentConstructor.ImmutableArguments(node.getName(),
							mappings,
							shapeDescriptorBuilder.build());

					Class<?> clazz = AssertionUtilities.assertNonnull(context.getAliasesView().get(node.getClazz()));
					Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIComponentConstructor.class,
							Arrays.asList(clazz.getDeclaredConstructors()));
					MethodHandle constructorHandle = InvokeUtilities.IMPL_LOOKUP.unreflectConstructor(constructor);

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
				.orElseThrow(AssertionError::new);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static Map<INamespacePrefixedString, IUIPropertyMappingValue> createMappings(Iterable<? extends Property> properties) {
		return Streams.stream(properties).unordered()
				.map(p -> Maps.immutableEntry(ImmutableNamespacePrefixedString.of(p.getKey()),
						new UIPropertyMappingValue(p.getAny()
								.map(value -> JAXBAdapterRegistries.getAdapter(value).leftToRight(value))
								.orElse(null),
								Optional.ofNullable(p.getBindingKey())
										.map(ImmutableNamespacePrefixedString::of)
										.orElse(null))))
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@SuppressWarnings("UnstableApiUsage")
	public static IShapeDescriptorBuilder<?> createShapeDescriptorBuilder(IUIDefaultComponentParserContext context, Shape shape)
			throws Throwable {
		AffineTransform transform = new AffineTransform();
		shape.getAffineTransformDefiner()
				.ifPresent(IThrowingConsumer.executeNow(atd -> {
					assert atd != null;
					atd.getAffineTransform()
							.ifPresent(at -> transform.setTransform(at.getScaleX(), at.getShearY(), at.getShearX(), at.getScaleY(), at.getTranslateX(), at.getTranslateY()));
					atd.getMethod().forEach(IThrowingConsumer.executeNow(m -> {
						assert m != null;
						Double[] args = Arrays.stream(
								AssertionUtilities.assertNonnull(m.getValue()).split(Pattern.quote(m.getDelimiter()))).sequential()
								.map(Double::parseDouble)
								.toArray(Double[]::new);

						InvokeUtilities.PUBLIC_LOOKUP.findVirtual(
								AffineTransform.class,
								m.getName(),
								MethodType.methodType(void.class, Collections.nCopies(args.length, double.class)))
								.bindTo(transform)
								.invokeWithArguments((Object[]) args);
					}));
				}));

		IShapeDescriptorBuilder<?> sdb = ShapeDescriptorBuilderFactoryRegistry.getDefaultFactory()
				.createBuilder(
						CastUtilities.castUnchecked(AssertionUtilities.assertNonnull(context.getAliasesView().get(shape.getClazz()))) // COMMENT should not throw
				);

		shape.getProperty().stream().unordered()
				.forEach(p -> {
					assert p.getBindingKey() == null;
					sdb.setProperty(p.getKey(), p.getAny()
							.map(value -> JAXBAdapterRegistries.getAdapter(value).leftToRight(value))
							.orElse(null));
				});

		return sdb.setX(shape.getX()).setY(shape.getY()).setWidth(shape.getWidth()).setHeight(shape.getHeight())
				.transformConcatenate(transform)
				.constrain(shape.getConstraint().stream().sequential()
						.map(c -> new ShapeConstraint(c.getMinX(), c.getMinY(), c.getMaxX(), c.getMaxY(), c.getMinWidth(), c.getMinHeight(), c.getMaxWidth(), c.getMaxHeight()))
						.collect(ImmutableList.toImmutableList()));
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
