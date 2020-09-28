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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers.DefaultUIComponentParserAnchorHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers.DefaultUIComponentParserExtensionHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers.DefaultUIComponentParserRendererHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.descriptors.ShapeDescriptorBuilderFactoryRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.interactions.ShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.ManualLoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;

import javax.annotation.Nullable;
import java.awt.geom.AffineTransform;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
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
		instance.addHandler(EnumHandlerType.OBJECTS_ONLY, Anchor.class, new DefaultUIComponentParserAnchorHandler<>());
		instance.addHandler(EnumHandlerType.OBJECTS_ONLY, Renderer.class, new DefaultUIComponentParserRendererHandler());
		instance.addHandler(EnumHandlerType.OBJECTS_ONLY, Extension.class, new DefaultUIComponentParserExtensionHandler());
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
							.map(IThrowingFunction.execute(u -> Maps.immutableEntry(u.getAlias(), Class.forName(u.getTarget()))))
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
					.map(IThrowingFunction.execute(root -> {
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
								.forEach(IThrowingConsumer.execute(any -> IParserContext.StaticHolder.findHandler(viewContext, any)
										.ifPresent(IThrowingConsumer.execute(handler -> handler.accept(
												viewContext,
												CastUtilities.castUnchecked(view), // COMMENT may throw
												CastUtilities.castUnchecked(any) // COMMENT should not throw
										)))
								));
						final IUIComponent[] cc = {view.getManager()
								.orElseThrow(AssertionError::new)};
						TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, componentRaw,
								IThrowingFunction.execute(n -> {
									IUIComponent component = componentRaw.equals(n)
											? cc[0]
											: CastUtilities.castChecked(IUIComponentContainer.class, cc[0])
											.map(IUIComponentContainer::getNamedChildrenMapView)
											.map(children -> children.get(n.getName()))
											.orElseThrow(AssertionError::new);
									Iterables.concat(
											n.getAnchor(),
											n.getRenderer()
													.map(ImmutableSet::of)
													.orElseGet(ImmutableSet::of),
											n.getExtension(),
											n.getAnyContainer()
													.map(AnyContainer::getAny)
													.orElseGet(ImmutableList::of))
											.forEach(IThrowingConsumer.execute(any -> IParserContext.StaticHolder.findHandler(componentContext, any)
													.ifPresent(IThrowingConsumer.execute(handler -> handler.accept(
															componentContext,
															CastUtilities.castUnchecked(component), // COMMENT may throw
															CastUtilities.castUnchecked(any) // COMMENT should not throw
													)))));
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

	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	public static IUIViewComponent<?, ?> createView(IParserContext context, View view)
			throws Throwable {
		Class<?> vc = AssertionUtilities.assertNonnull(context.getAliases().get(view.getClazz()));
		UIViewComponentConstructor.EnumConstructorType ct = IConstructorType.StaticHolder.getConstructorType(vc, UIViewComponentConstructor.class, UIViewComponentConstructor::type);
		MethodHandle mh = InvokeUtilities.IMPL_LOOKUP.findConstructor(vc, ct.getMethodType());
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(view.getProperty());
		switch (ct) {
			case MAPPINGS:
				return (IUIViewComponent<?, ?>) mh.invoke(mappings);
			default:
				throw new AssertionError();
		}
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

	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	public static IUIComponent createComponent(IParserContext context, Component component)
			throws Throwable {
		return TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, component,
				IThrowingFunction.execute(n -> {
					Class<?> cc = AssertionUtilities.assertNonnull(context.getAliases().get(n.getClazz()));
					UIComponentConstructor.EnumConstructorType ct = IConstructorType.StaticHolder.getConstructorType(cc, UIComponentConstructor.class, UIComponentConstructor::type);
					Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(n.getProperty());
					// COMMENT attributes
					{
						Map<INamespacePrefixedString, IUIPropertyMappingValue> attributes = new HashMap<>(
								EnumUIEventDOMType.values().length + EnumUIEventComponentType.values().length
						);

						attributes.put(EnumUIEventDOMType.LOAD.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getLoad()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.UNLOAD.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getUnload()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.ABORT.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getAbort()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.ERROR.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getError()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.SELECT.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getSelect()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.FOCUS_OUT_POST.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getBlur()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getFocus()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.FOCUS_IN_PRE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getFocusin()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.FOCUS_OUT_PRE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getFocusout()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.CLICK_AUXILIARY.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getAuxclick()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.CLICK.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getClick()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.CLICK_DOUBLE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getDblclick()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_DOWN.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getMousedown()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_ENTER.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getMouseenter()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getMouseleave()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_MOVE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getMousemove()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getMouseout()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getMouseover()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.MOUSE_UP.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getMouseup()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.WHEEL.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getWheel()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.UPDATE_PRE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getBeforeinput()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.UPDATE_POST.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getInput()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.KEY_DOWN.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getKeydown()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.KEY_UP.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getKeyup()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.COMPOSITION_START.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getCompositionstart()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.COMPOSITION_UPDATE.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getCompositionupdate()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventDOMType.COMPOSITION_END.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getCompositionend()).map(ImmutableNamespacePrefixedString::new).orElse(null)));
						attributes.put(EnumUIEventComponentType.CHAR_TYPED.getEventType(),
								new UIPropertyMappingValue(null,
										Optional.ofNullable(component.getCharTyped()).map(ImmutableNamespacePrefixedString::new).orElse(null)));

						mappings = MapUtilities.concatMaps(mappings, attributes);
					}
					IShapeDescriptor<?> sd = createShapeDescriptor(context, n.getShape());
					MethodHandle mh = InvokeUtilities.IMPL_LOOKUP.findConstructor(cc, ct.getMethodType());
					switch (ct) {
						case MAPPINGS__NAME__SHAPE_DESCRIPTOR:
							return (IUIComponent) mh.invoke(mappings, n.getName(), sd);
						default:
							throw new AssertionError();
					}
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
	public static IShapeDescriptor<?> createShapeDescriptor(IParserContext context, Shape shape)
			throws Throwable {
		AffineTransform transform = new AffineTransform();
		shape.getAffineTransformDefiner()
				.ifPresent(IThrowingConsumer.execute(atd -> {
					atd.getAffineTransform()
							.ifPresent(at -> transform.setTransform(at.getScaleX(), at.getShearY(), at.getShearX(), at.getScaleY(), at.getTranslateX(), at.getTranslateY()));
					atd.getMethod().forEach(IThrowingConsumer.execute(m -> {
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
						CastUtilities.castUnchecked(AssertionUtilities.assertNonnull(context.getAliases().get(shape.getClazz()))) // COMMENT should not throw
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
						.collect(ImmutableList.toImmutableList()))
				.build();
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public <H> void addHandler(Set<EnumHandlerType> types, Class<H> clazz, IConsumer3<? super IParserContext, ?, ? super H, ?> handler) {
		types.forEach(type ->
				getHandlers().getUnchecked(type).put(clazz, handler));
	}
}
