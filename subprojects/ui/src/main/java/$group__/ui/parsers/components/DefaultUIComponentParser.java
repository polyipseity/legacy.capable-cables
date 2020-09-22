package $group__.ui.parsers.components;

import $group__.jaxb.subprojects.ui.components.*;
import $group__.ui.UIConfiguration;
import $group__.ui.UIMarkers;
import $group__.ui.binding.UIPropertyMappingValue;
import $group__.ui.core.binding.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.mvvm.views.events.types.EnumUIEventComponentType;
import $group__.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import $group__.ui.core.mvvm.views.rendering.IUIRenderer;
import $group__.ui.core.mvvm.views.rendering.IUIRendererContainer;
import $group__.ui.core.parsers.UIParserCheckedException;
import $group__.ui.core.parsers.UIParserUncheckedException;
import $group__.ui.core.parsers.adapters.JAXBAdapterRegistries;
import $group__.ui.core.parsers.components.*;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import $group__.ui.structures.shapes.descriptors.ShapeDescriptorBuilderFactoryRegistry;
import $group__.ui.structures.shapes.interactions.ShapeAnchor;
import $group__.ui.structures.shapes.interactions.ShapeConstraint;
import $group__.utilities.*;
import $group__.utilities.collections.CacheUtilities;
import $group__.utilities.collections.ManualLoadingCache;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.extensions.core.IExtensionContainer;
import $group__.utilities.functions.*;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.templates.CommonConfigurationTemplate;
import $group__.utilities.throwable.ThrowableUtilities;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.*;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import javax.annotation.Nullable;
import java.awt.geom.AffineTransform;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.regex.Pattern;

public class DefaultUIComponentParser<T extends IUIViewComponent<?, ?>>
		extends IHasGenericClass.Impl<T>
		implements IUIComponentParser<T, Function<? super Unmarshaller, ?>> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private final ConcurrentMap<String, Class<?>> aliases = MapUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	private final LoadingCache<EnumHandlerType, ConcurrentMap<Class<?>, IConsumer3<? super IParserContext, ?, ?, ?>>> handlers =
			ManualLoadingCache.newNestedLoadingCacheMap(
					CacheUtilities.newCacheBuilderSingleThreaded()
							.initialCapacity(EnumHandlerType.values().length)
							.build(CacheLoader.from(
									new MappableSupplier<>(
											ConstantSupplier.of(MapUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM))
									).map(MapMaker::makeMap))));
	@Nullable
	private Ui root;

	public DefaultUIComponentParser(Class<T> genericClass) {
		super(genericClass);
	}

	public static <T extends IUIComponentParser<?, ?>> T makeParserStandard(T instance) {
		instance.addHandler(EnumHandlerType.OBJECTS_ONLY, Anchor.class, DefaultHandlers::handleAnchor);
		instance.addHandler(EnumHandlerType.OBJECTS_ONLY, Renderer.class, DefaultHandlers::handleRenderer);
		instance.addHandler(EnumHandlerType.OBJECTS_ONLY, Extension.class, DefaultHandlers::handleExtension);
		return instance;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void parse(Function<? super Unmarshaller, ?> resource)
			throws UIParserCheckedException, UIParserUncheckedException {
		Unmarshaller um;
		try {
			um = DefaultSchemaHolder.getContext().createUnmarshaller();
		} catch (JAXBException e) {
			throw ThrowableUtilities.propagate(e, UIParserCheckedException::new, UIParserUncheckedException::new);
		}
		// COMMENT do NOT set a schema
		Ui root = (Ui) resource.apply(um);
		reset();

		try {
			getAliases().putAll(
					root.getUsing().stream().unordered()
							.map(IThrowingFunction.execute(u -> Maps.immutableEntry(u.getAlias(), Class.forName(u.getTarget()))))
							.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)));
		} catch (ClassNotFoundException e) {
			throw ThrowableUtilities.propagate(e, UIParserCheckedException::new, UIParserUncheckedException::new);
		}

		{
			Class<?> viewClass = AssertionUtilities.assertNonnull(getAliases().get(root.getView().getClazz()));
			if (!getGenericClass().isAssignableFrom(viewClass))
				throw new IllegalArgumentException(
						new LogMessageBuilder()
								.addMarkers(UIMarkers.getInstance()::getMarkerParser)
								.addKeyValue("viewClass", viewClass).addKeyValue("this::getGenericClass", this::getGenericClass)
								.addMessages(() -> getResourceBundle().getString("construct.view.instance_of.fail"))
								.build()
				);
		}

		setRoot(root);
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
											.map(children -> children.get(n.getId()))
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
												.map(m -> AssertionUtilities.assertNonnull(m.get(n.getId())))
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
		MethodHandle mh = DynamicUtilities.IMPL_LOOKUP.findConstructor(vc, ct.getMethodType());
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(view.getProperty());
		switch (ct) {
			case MAPPINGS:
				return (IUIViewComponent<?, ?>) mh.invoke(mappings);
			default:
				throw new AssertionError();
		}
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
					MethodHandle mh = DynamicUtilities.IMPL_LOOKUP.findConstructor(cc, ct.getMethodType());
					switch (ct) {
						case MAPPINGS__ID__SHAPE_DESCRIPTOR:
							return (IUIComponent) mh.invoke(mappings, n.getId(), sd);
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

	@SuppressWarnings("UnstableApiUsage")
	public static IShapeDescriptor<?> createShapeDescriptor(IParserContext context, $group__.jaxb.subprojects.ui.components.Shape shape)
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

						DynamicUtilities.PUBLIC_LOOKUP.findVirtual(
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
					sdb.setProperty(p.getKey(), Optional.ofNullable(p.getAny())
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

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<String, Class<?>> getAliases() { return aliases; }

	@SuppressWarnings("UnstableApiUsage")
	public static Map<INamespacePrefixedString, IUIPropertyMappingValue> createMappings(Iterable<? extends Property> properties) {
		return Streams.stream(properties).unordered()
				.map(p -> Maps.immutableEntry(new ImmutableNamespacePrefixedString(p.getKey()),
						new UIPropertyMappingValue(Optional.ofNullable(p.getAny())
								.map(value -> JAXBAdapterRegistries.getAdapter(value).leftToRight(value))
								.orElse(null),
								Optional.ofNullable(p.getBindingKey())
										.map(ImmutableNamespacePrefixedString::new)
										.orElse(null))))
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public <H> void addHandler(Set<EnumHandlerType> types, Class<H> clazz, IConsumer3<? super IParserContext, ?, ? super H, ?> handler) {
		types.forEach(type ->
				getHandlers().getUnchecked(type).put(clazz, handler));
	}

	public enum DefaultHandlers {
		;

		public static void handleAnchor(@SuppressWarnings("unused") IParserContext context, Object container, Anchor object) {
			if (!(container instanceof IUIComponent))
				return;
			IUIComponent component = (IUIComponent) container;
			component.getManager()
					.flatMap(IUIComponentManager::getView)
					.ifPresent(view ->
							view.getShapeAnchorController().addAnchors(component,
									ImmutableList.of(new ShapeAnchor(
											IUIViewComponent.StaticHolder.getComponentByID(view, object.getTarget()),
											object.getOriginSide().toJava(),
											object.getTargetSide().toJava(),
											object.getBorderThickness()))));
		}

		@SuppressWarnings("deprecation")
		public static void handleRenderer(IParserContext context, Object container, Renderer object)
				throws Throwable {
			if (!(container instanceof IUIRendererContainer))
				return;
			IUIRendererContainer<?> rendererContainer = ((IUIRendererContainer<?>) container);
			Class<?> oc = Optional.ofNullable(object.getClazz())
					.<Class<?>>map(classAlias -> AssertionUtilities.assertNonnull(context.getAliases().get(classAlias)))
					.orElseGet(rendererContainer::getDefaultRendererClass);
			UIRendererConstructor.EnumConstructorType ct = IConstructorType.StaticHolder.getConstructorType(oc, UIRendererConstructor.class, UIRendererConstructor::type);
			MethodHandle mh = DynamicUtilities.IMPL_LOOKUP.findConstructor(oc, ct.getMethodType());
			Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(object.getProperty());
			IUIRenderer<?> ret;
			switch (ct) {
				case MAPPINGS__CONTAINER_CLASS:
					ret = (IUIRenderer<?>) mh.invoke(mappings, container.getClass());
					break;
				case MAPPINGS:
					ret = (IUIRenderer<?>) mh.invoke(mappings);
					break;
				case CONTAINER_CLASS:
					ret = (IUIRenderer<?>) mh.invoke(container.getClass());
					break;
				case NO_ARGS:
					ret = (IUIRenderer<?>) mh.invoke();
					break;
				default:
					throw new AssertionError();
			}
			rendererContainer.setRenderer(CastUtilities.castUnchecked(ret)); // COMMENT setRenderer should check
		}

		@SuppressWarnings("deprecation")
		public static void handleExtension(IParserContext context, Object container, Extension object)
				throws Throwable {
			if (!(container instanceof IExtensionContainer))
				return;
			Class<?> oc = AssertionUtilities.assertNonnull(context.getAliases().get(object.getClazz()));
			UIExtensionConstructor.EnumConstructorType ct = IConstructorType.StaticHolder.getConstructorType(oc, UIExtensionConstructor.class, UIExtensionConstructor::type);
			MethodHandle mh = DynamicUtilities.IMPL_LOOKUP.findConstructor(oc, ct.getMethodType());
			Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(object.getProperty());
			IUIExtension<?, ?> ret;
			switch (ct) {
				case MAPPINGS__CONTAINER_CLASS:
					ret = (IUIExtension<?, ?>) mh.invoke(mappings, container.getClass());
					break;
				case MAPPINGS:
					ret = (IUIExtension<?, ?>) mh.invoke(mappings);
					break;
				case CONTAINER_CLASS:
					ret = (IUIExtension<?, ?>) mh.invoke(container.getClass());
					break;
				case NO_ARGS:
					ret = (IUIExtension<?, ?>) mh.invoke();
					break;
				default:
					throw new AssertionError();
			}
			Iterables.concat(
					object.getRenderer()
							.map(ImmutableSet::of)
							.orElseGet(ImmutableSet::of),
					object.getAnyContainer()
							.map(AnyContainer::getAny)
							.orElseGet(ImmutableList::of))
					.forEach(IThrowingConsumer.execute(any -> IParserContext.StaticHolder.findHandler(context, any)
							.ifPresent(IThrowingConsumer.execute(handler -> handler.accept(
									context,
									CastUtilities.castUnchecked(ret), // COMMENT may throw
									CastUtilities.castUnchecked(any) // COMMENT should not throw
							)))));
			((IExtensionContainer<?>) container).addExtension(CastUtilities.castUnchecked(ret)); // COMMENT addExtension should check
		}
	}
}
