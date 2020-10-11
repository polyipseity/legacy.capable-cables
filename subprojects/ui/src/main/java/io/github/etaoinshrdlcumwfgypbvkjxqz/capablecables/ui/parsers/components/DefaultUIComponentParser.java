package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IUIComponentParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers.UIDefaultComponentParserAnchorHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers.UIDefaultComponentParserExtensionHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers.UIDefaultComponentParserRendererHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.descriptors.ShapeDescriptorBuilderFactoryRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.interactions.ShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.ManualLoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.AnnotationUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;

import javax.annotation.Nullable;
import java.awt.geom.AffineTransform;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

public class DefaultUIComponentParser<T extends IUIViewComponent<?, ?>>
		extends IHasGenericClass.Impl<T>
		implements IUIComponentParser<T, Ui> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private final ConcurrentMap<String, Class<?>> aliases = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	private final LoadingCache<EnumHandlerType, ConcurrentMap<Class<?>, IConsumer3<? super IParserContext, ?, ?, ?>>> handlers =
			ManualLoadingCache.newNestedLoadingCacheMap(
					CacheUtilities.newCacheBuilderSingleThreaded()
							.initialCapacity(EnumHandlerType.values().length)
							.build(CacheLoader.from(
									new MappableSupplier<>(
											ConstantSupplier.of(MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM))
									).map(MapMaker::makeMap))));
	@Nullable
	private Ui root;

	public DefaultUIComponentParser(Class<T> genericClass) {
		super(genericClass);
	}

	public static <T extends IUIComponentParser<?, ?>> T makeParserStandard(T instance) {
		instance.addHandler(EnumHandlerType.OBJECTS_ONLY, Anchor.class, new UIDefaultComponentParserAnchorHandler<>());
		instance.addHandler(EnumHandlerType.OBJECTS_ONLY, Renderer.class, new UIDefaultComponentParserRendererHandler());
		instance.addHandler(EnumHandlerType.OBJECTS_ONLY, Extension.class, new UIDefaultComponentParserExtensionHandler());
		return instance;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void parse(Ui resource)
			throws UIParserCheckedException, UIParserUncheckedException {
		reset();

		try {
			getAliases().putAll(
					resource.getUsing().stream().unordered()
							.map(IThrowingFunction.executeNow(u -> {
								assert u != null;
								return Maps.immutableEntry(u.getAlias(), Class.forName(u.getTarget()));
							}))
							.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)));
		} catch (ClassNotFoundException e) {
			throw ThrowableUtilities.propagate(e, UIParserCheckedException::new, UIParserUncheckedException::new);
		}

		{
			Class<?> viewClass = AssertionUtilities.assertNonnull(getAliases().get(resource.getView().getClazz()));
			if (!getGenericClass().isAssignableFrom(viewClass))
				throw new IllegalArgumentException(
						new LogMessageBuilder()
								.addMarkers(UIMarkers.getInstance()::getMarkerParser)
								.addKeyValue("viewClass", viewClass).addKeyValue("this::getGenericClass", this::getGenericClass)
								.addMessages(() -> getResourceBundle().getString("construct.view.instance_of.fail"))
								.build()
				);
		}

		setRoot(resource);
	}

	@Override
	public void reset() {
		setRoot(null);
		getAliases().clear();
	}

	@Override
	public T construct()
			throws UIParserCheckedException, UIParserUncheckedException {
		try {
			return getRoot()
					.map(IThrowingFunction.executeNow(root -> {
						assert root != null;
						IParserContext viewContext = new ImmutableParserContext(EnumHandlerType.VIEW_HANDLER, getAliases(), getHandlers().asMap());
						IParserContext componentContext = new ImmutableParserContext(EnumHandlerType.COMPONENT_HANDLER, getAliases(), getHandlers().asMap());
						View viewRaw = root.getView();
						Component componentRaw = root.getComponent();
						@SuppressWarnings("unchecked") T view = (T) createView(viewContext, viewRaw); // COMMENT should be checked
						view.setManager(
								CastUtilities.castUnchecked(createComponent(viewContext, componentRaw)) // COMMENT may throw
						);
						Iterables.concat(
								viewRaw.getExtension(),
								viewRaw.getAnyContainer()
										.map(AnyContainer::getAny)
										.orElseGet(ImmutableList::of))
								.forEach(IThrowingConsumer.executeNow(any -> {
											assert any != null;
											IParserContext.StaticHolder.findHandler(viewContext, any)
													.ifPresent(IThrowingConsumer.executeNow(handler -> {
														assert handler != null;
														handler.accept(
																viewContext,
																CastUtilities.castUnchecked(view), // COMMENT may throw
																CastUtilities.castUnchecked(any) // COMMENT should not throw
														);
													}));
										}
								));
						final IUIComponent[] cc = {view.getManager()
								.orElseThrow(AssertionError::new)};
						TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, componentRaw,
								IThrowingFunction.executeNow(n -> {
									assert n != null;
									IUIComponent component = componentRaw.equals(n)
											? cc[0]
											: CastUtilities.castChecked(IUIComponentContainer.class, cc[0])
											.map(IUIComponentContainer::getNamedChildrenMapView)
											.map(children -> children.get(n.getName()))
											.orElseThrow(AssertionError::new);
									Iterables.concat(
											n.getAnchor(),
											ImmutableSet.of(n.getRenderer()),
											n.getExtension(),
											n.getAnyContainer()
													.map(AnyContainer::getAny)
													.orElseGet(ImmutableList::of))
											.forEach(IThrowingConsumer.executeNow(any -> {
												assert any != null;
												IParserContext.StaticHolder.findHandler(componentContext, any)
														.ifPresent(IThrowingConsumer.executeNow(handler -> {
															assert handler != null;
															handler.accept(
																	componentContext,
																	CastUtilities.castUnchecked(component), // COMMENT may throw
																	CastUtilities.castUnchecked(any) // COMMENT should not throw
															);
														}));
											}));
									return n;
								}),
								n -> {
									if (!componentRaw.equals(n))
										cc[0] = CastUtilities.castChecked(IUIComponentContainer.class, cc[0])
												.map(IUIComponentContainer::getNamedChildrenMapView)
												.map(m -> AssertionUtilities.assertNonnull(m.get(n.getName())))
												.orElseGet(() -> cc[0]);
									return n.getComponent();
								},
								(p, ch) -> {
									if (!componentRaw.equals(p))
										cc[0] = cc[0].getParent()
												.orElseThrow(AssertionError::new);
									return p;
								},
								n -> {
									throw new IllegalArgumentException(
											new LogMessageBuilder()
													.addMarkers(UIMarkers.getInstance()::getMarkerParser)
													.addKeyValue("n", n).addKeyValue("componentRaw", componentRaw)
													.addMessages(() -> getResourceBundle().getString("construct.view.tree.cyclic"))
													.build()
									);
								});
						return view;
					}))
					.orElseThrow(() -> new IllegalStateException(
							new LogMessageBuilder()
									.addMarkers(UIMarkers.getInstance()::getMarkerParser)
									.addMessages(() -> getResourceBundle().getString("construct.parsed.missing"))
									.build()
					));
		} catch (Throwable throwable) {
			throw ThrowableUtilities.propagate(throwable, UIParserCheckedException::new, UIParserUncheckedException::new);
		}
	}

	protected Optional<? extends Ui> getRoot() { return Optional.ofNullable(root); }

	protected void setRoot(@Nullable Ui root) { this.root = root; }

	protected LoadingCache<EnumHandlerType, ConcurrentMap<Class<?>, IConsumer3<? super IParserContext, ?, ?, ?>>> getHandlers() { return handlers; }

	public static IUIViewComponent<?, ?> createView(IParserContext context, View view)
			throws Throwable {
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(view.getProperty());
		UIViewComponentConstructor.IArguments argument = new UIViewComponentConstructor.ImmutableArguments(mappings);

		Class<?> clazz = AssertionUtilities.assertNonnull(context.getAliasesView().get(view.getClazz()));
		Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIViewComponentConstructor.class, Arrays.asList(clazz.getDeclaredConstructors()));
		MethodHandle constructorHandle = InvokeUtilities.IMPL_LOOKUP.unreflectConstructor(constructor);

		return (IUIViewComponent<?, ?>) constructorHandle.invoke(argument);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static Map<INamespacePrefixedString, IUIPropertyMappingValue> createMappings(Iterable<? extends Property> properties) {
		return Streams.stream(properties).unordered()
				.map(p -> Maps.immutableEntry(new ImmutableNamespacePrefixedString(p.getKey()),
						new UIPropertyMappingValue(p.getAny()
								.map(value -> JAXBAdapterRegistries.getAdapter(value).leftToRight(value))
								.orElse(null),
								Optional.ofNullable(p.getBindingKey())
										.map(ImmutableNamespacePrefixedString::new)
										.orElse(null))))
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public static IUIComponent createComponent(IParserContext context, Component component)
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

						attributes.put(EnumUIEventDOMType.LOAD.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getLoad()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.UNLOAD.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getUnload()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.ABORT.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getAbort()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.ERROR.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getError()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.SELECT.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getSelect()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.FOCUS_OUT_POST.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getBlur()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getFocus()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.FOCUS_IN_PRE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getFocusin()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.FOCUS_OUT_PRE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getFocusout()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.CLICK_AUXILIARY.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getAuxclick()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.CLICK.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getClick()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.CLICK_DOUBLE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getDblclick()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_DOWN.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMousedown()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_ENTER.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMouseenter()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMouseleave()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_MOVE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMousemove()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMouseout()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMouseover()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_UP.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getMouseup()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.WHEEL.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getWheel()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.INPUT_PRE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getBeforeinput()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.INPUT_POST.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getInput()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.KEY_DOWN.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getKeydown()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.KEY_UP.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getKeyup()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.COMPOSITION_START.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getCompositionstart()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.COMPOSITION_UPDATE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getCompositionupdate()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.COMPOSITION_END.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getCompositionend()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventComponentType.CHAR_TYPED.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(node.getCharTyped()).map(ImmutableNamespacePrefixedString::new).orElse(null)));

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

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<String, Class<?>> getAliases() { return aliases; }

	@SuppressWarnings("UnstableApiUsage")
	public static IShapeDescriptorBuilder<?> createShapeDescriptorBuilder(IParserContext context, Shape shape)
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

	@Override
	public <H> void addHandler(Set<EnumHandlerType> types, Class<H> clazz, IConsumer3<? super IParserContext, ?, ? super H, ?> handler) {
		types.forEach(type ->
				getHandlers().getUnchecked(type).put(clazz, handler));
	}
}
