package $group__.ui.parsers.components;

import $group__.jaxb.subprojects.ui.components.*;
import $group__.ui.UIConfiguration;
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
import $group__.ui.core.parsers.adapters.JAXBAdapterRegistries;
import $group__.ui.core.parsers.components.*;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import $group__.ui.structures.shapes.descriptors.ShapeDescriptorBuilderFactoryRegistry;
import $group__.ui.structures.shapes.interactions.ShapeAnchor;
import $group__.ui.structures.shapes.interactions.ShapeConstraint;
import $group__.utilities.*;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.collections.CacheUtilities;
import $group__.utilities.collections.ManualLoadingCache;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.functions.ConstantSupplier;
import $group__.utilities.functions.IConsumer3;
import $group__.utilities.functions.MappableSupplier;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.templates.CommonConfigurationTemplate;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.*;
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
	private final LoadingCache<EnumHandlerType, ConcurrentMap<Class<?>, IConsumer3<? super IParserContext, ?, ?>>> handlers =
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

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void parse(Function<? super Unmarshaller, ?> resource) throws Throwable {
		Unmarshaller um = DefaultSchemaHolder.getContext().createUnmarshaller();
		// COMMENT do NOT set a schema
		Ui root = (Ui) resource.apply(um);
		reset();

		getAliases().putAll(
				root.getUsing().stream().unordered()
						.map(u -> Maps.immutableEntry(u.getAlias(), Try.call(() -> Class.forName(u.getTarget()), UIConfiguration.getInstance().getLogger()).orElseThrow(ThrowableCatcher::rethrow)))
						.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)));

		{
			Class<?> viewClass = AssertionUtilities.assertNonnull(getAliases().get(root.getView().getClazz()));
			if (!getGenericClass().isAssignableFrom(viewClass))
				throw ThrowableUtilities.logAndThrow(new IllegalArgumentException(
						new LogMessageBuilder()
								.addKeyValue("viewClass", viewClass).addKeyValue("getGenericClass()", getGenericClass())
								.appendMessages(getResourceBundle().getString("construct.view.instance_of.fail"))
								.build()
				), UIConfiguration.getInstance().getLogger());
		}

		setRoot(root);
	}

	public static <T extends IUIComponentParser<?, ?>> T makeParserStandard(T instance) {
		instance.addHandler(EnumHandlerType.OBJECTS_ONLY, Anchor.class, DefaultHandlers::handleAnchor);
		instance.addHandler(EnumHandlerType.OBJECTS_ONLY, Renderer.class, DefaultHandlers::handleRenderer);
		instance.addHandler(EnumHandlerType.OBJECTS_ONLY, Extension.class, DefaultHandlers::handleExtension);
		return instance;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	public void reset() {
		setRoot(null);
		getAliases().clear();
	}

	@Override
	public T construct() {
		return getRoot()
				.map(root ->
						Try.call(() -> {
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
									.forEach(any -> IParserContext.StaticHolder.findHandler(viewContext, any)
											.ifPresent(handler -> handler.accept(
													viewContext,
													CastUtilities.castUnchecked(view), // COMMENT may throw
													CastUtilities.castUnchecked(any) // COMMENT should not throw
											))
									);
							final IUIComponent[] cc = {view.getManager()
									.orElseThrow(IllegalStateException::new)};
							TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, componentRaw,
									n -> {
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
												.forEach(any -> IParserContext.StaticHolder.findHandler(componentContext, any)
														.ifPresent(handler -> handler.accept(
																componentContext,
																CastUtilities.castUnchecked(component), // COMMENT may throw
																CastUtilities.castUnchecked(any) // COMMENT should not throw
														)));
										return n;
									},
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
									}, n -> {
										throw ThrowableUtilities.logAndThrow(
												new IllegalArgumentException(
														new LogMessageBuilder()
																.addKeyValue("n", n).addKeyValue("componentRaw", componentRaw)
																.appendMessages(getResourceBundle().getString("construct.view.tree.cyclic"))
																.build()
												),
												UIConfiguration.getInstance().getLogger()
										);
									});
							return view;
						}, UIConfiguration.getInstance().getLogger())
								.orElseThrow(ThrowableCatcher::rethrow))
				.orElseThrow(() -> new IllegalStateException("Prototype has not been created"));
	}

	protected Optional<? extends Ui> getRoot() { return Optional.ofNullable(root); }

	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	public static IUIViewComponent<?, ?> createView(IParserContext context, View view) {
		Class<?> vc = AssertionUtilities.assertNonnull(context.getAliases().get(view.getClazz()));
		UIViewComponentConstructor.EnumConstructorType ct = IConstructorType.StaticHolder.getConstructorType(vc, UIViewComponentConstructor.class, UIViewComponentConstructor::type);
		return Try.call(() -> {
			MethodHandle mh = DynamicUtilities.IMPL_LOOKUP.findConstructor(vc, ct.getMethodType());
			Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(view.getProperty());
			switch (ct) {
				case MAPPINGS:
					return (IUIViewComponent<?, ?>) mh.invoke(
							mappings
					);
				default:
					throw new InternalError();
			}
		}, UIConfiguration.getInstance().getLogger())
				.orElseThrow(ThrowableCatcher::rethrow);
	}

	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	public static IUIComponent createComponent(IParserContext context, Component component) {
		return TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, component,
				n -> {
					Class<?> cc = AssertionUtilities.assertNonnull(context.getAliases().get(n.getClazz()));
					UIComponentConstructor.EnumConstructorType ct = IConstructorType.StaticHolder.getConstructorType(cc, UIComponentConstructor.class, UIComponentConstructor::type);
					return Try.call(() -> {
						MethodHandle mh = DynamicUtilities.IMPL_LOOKUP.findConstructor(cc, ct.getMethodType());
						Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(n.getProperty());
						// COMMENT attributes
						{
							Map<INamespacePrefixedString, IUIPropertyMappingValue> attributes = new HashMap<>(
									EnumUIEventDOMType.values().length + EnumUIEventComponentType.values().length
							);

							attributes.put(EnumUIEventDOMType.LOAD.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getLoad()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.UNLOAD.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getUnload()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.ABORT.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getAbort()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.ERROR.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getError()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.SELECT.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getSelect()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.FOCUS_OUT_POST.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getBlur()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.FOCUS_IN_POST.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getFocus()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.FOCUS_IN_PRE.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getFocusin()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.FOCUS_OUT_PRE.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getFocusout()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.CLICK_AUXILIARY.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getAuxclick()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.CLICK.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getClick()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.CLICK_DOUBLE.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getDblclick()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.MOUSE_DOWN.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getMousedown()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.MOUSE_ENTER.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getMouseenter()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.MOUSE_LEAVE.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getMouseleave()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.MOUSE_MOVE.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getMousemove()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.MOUSE_LEAVE_SELF.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getMouseout()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.MOUSE_ENTER_SELF.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getMouseover()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.MOUSE_UP.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getMouseup()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.WHEEL.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getWheel()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.UPDATE_PRE.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getBeforeinput()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.UPDATE_POST.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getInput()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.KEY_DOWN.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getKeydown()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.KEY_UP.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getKeyup()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.COMPOSITION_START.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getCompositionstart()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.COMPOSITION_UPDATE.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getCompositionupdate()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventDOMType.COMPOSITION_END.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getCompositionend()).map(NamespacePrefixedString::new).orElse(null)));
							attributes.put(EnumUIEventComponentType.CHAR_TYPED.getEventType(),
									new UIPropertyMappingValue(null,
											Optional.ofNullable(component.getCharTyped()).map(NamespacePrefixedString::new).orElse(null)));

							mappings = MapUtilities.concatMaps(mappings, attributes);
						}
						IShapeDescriptor<?> sd = createShapeDescriptor(context, n.getShape());
						switch (ct) {
							case MAPPINGS__ID__SHAPE_DESCRIPTOR:
								return (IUIComponent) mh.invoke(mappings, n.getId(), sd);
							default:
								throw new InternalError();
						}
					}, UIConfiguration.getInstance().getLogger())
							.orElseThrow(ThrowableCatcher::rethrow);
				},
				Component::getComponent,
				(p, c) -> {
					if (!Iterables.isEmpty(c)) {
						if (!(p instanceof IUIComponentContainer))
							throw ThrowableUtilities.logAndThrow(
									new IllegalArgumentException(
											new LogMessageBuilder()
													.addKeyValue("p", p).addKeyValue("c", c)
													.appendMessages(getResourceBundle().getString("construct.component.container.instance_of.fail"))
													.build()
									),
									UIConfiguration.getInstance().getLogger()
							);
						((IUIComponentContainer) p).addChildren(c);
					}
					return p;
				}, n -> {
					throw ThrowableUtilities.logAndThrow(
							new IllegalArgumentException(
									new LogMessageBuilder()
											.addKeyValue("n", n).addKeyValue("component", component)
											.appendMessages(getResourceBundle().getString("construct.view.tree.cyclic"))
											.build()
							),
							UIConfiguration.getInstance().getLogger()
					);
				})
				.orElseThrow(InternalError::new);
	}

	protected LoadingCache<EnumHandlerType, ConcurrentMap<Class<?>, IConsumer3<? super IParserContext, ?, ?>>> getHandlers() { return handlers; }

	@SuppressWarnings("UnstableApiUsage")
	public static Map<INamespacePrefixedString, IUIPropertyMappingValue> createMappings(Iterable<? extends Property> properties) {
		return Streams.stream(properties).unordered()
				.map(p -> Maps.immutableEntry(new NamespacePrefixedString(p.getKey()),
						new UIPropertyMappingValue(Optional.ofNullable(p.getAny())
								.map(value -> JAXBAdapterRegistries.getAdapter(value).leftToRight(value))
								.orElse(null),
								Optional.ofNullable(p.getBindingKey())
										.map(NamespacePrefixedString::new)
										.orElse(null))))
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@SuppressWarnings("UnstableApiUsage")
	public static IShapeDescriptor<?> createShapeDescriptor(IParserContext context, $group__.jaxb.subprojects.ui.components.Shape shape) {
		AffineTransform transform = new AffineTransform();
		shape.getAffineTransformDefiner()
				.ifPresent(atd -> {
					atd.getAffineTransform()
							.ifPresent(at -> transform.setTransform(at.getScaleX(), at.getShearY(), at.getShearX(), at.getScaleY(), at.getTranslateX(), at.getTranslateY()));
					atd.getMethod().forEach(m -> {
						Double[] args = Arrays.stream(
								AssertionUtilities.assertNonnull(m.getValue()).split(Pattern.quote(m.getDelimiter()))).sequential()
								.map(Double::parseDouble)
								.toArray(Double[]::new);
						ThrowableUtilities.Try.run(() ->
								DynamicUtilities.PUBLIC_LOOKUP.findVirtual(
										AffineTransform.class,
										m.getName(),
										MethodType.methodType(void.class, Collections.nCopies(args.length, double.class)))
										.bindTo(transform)
										.invokeWithArguments((Object[]) args), UIConfiguration.getInstance().getLogger());
						ThrowableUtilities.ThrowableCatcher.rethrow(true);
					});
				});

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

	protected void setRoot(@Nullable Ui root) { this.root = root; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<String, Class<?>> getAliases() { return aliases; }

	@Override
	public <H> void addHandler(Set<EnumHandlerType> types, Class<H> clazz, IConsumer3<? super IParserContext, ?, ? super H> handler) {
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
		public static void handleRenderer(IParserContext context, Object container, Renderer object) {
			if (!(container instanceof IUIRendererContainer))
				return;
			IUIRendererContainer<?> rendererContainer = ((IUIRendererContainer<?>) container);
			Class<?> oc = Optional.ofNullable(object.getClazz())
					.<Class<?>>map(classAlias -> AssertionUtilities.assertNonnull(context.getAliases().get(classAlias)))
					.orElseGet(rendererContainer::getDefaultRendererClass);
			UIRendererConstructor.EnumConstructorType ct = IConstructorType.StaticHolder.getConstructorType(oc, UIRendererConstructor.class, UIRendererConstructor::type);
			IUIRenderer<?> ret = Try.call(() -> {
				MethodHandle mh = DynamicUtilities.IMPL_LOOKUP.findConstructor(oc, ct.getMethodType());
				Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(object.getProperty());
				switch (ct) {
					case MAPPINGS__CONTAINER_CLASS:
						return (IUIRenderer<?>) mh.invoke(mappings, container.getClass());
					case MAPPINGS:
						return (IUIRenderer<?>) mh.invoke(mappings);
					case CONTAINER_CLASS:
						return (IUIRenderer<?>) mh.invoke(container.getClass());
					case NO_ARGS:
						return (IUIRenderer<?>) mh.invoke();
					default:
						throw new InternalError();
				}
			}, UIConfiguration.getInstance().getLogger()).orElseThrow(ThrowableCatcher::rethrow);
			rendererContainer.setRenderer(CastUtilities.castUnchecked(ret)); // COMMENT setRenderer should check
		}

		public static void handleExtension(IParserContext context, Object container, Extension object) {
			if (!(container instanceof IExtensionContainer))
				return;
			Class<?> oc = AssertionUtilities.assertNonnull(context.getAliases().get(object.getClazz()));
			UIExtensionConstructor.EnumConstructorType ct = IConstructorType.StaticHolder.getConstructorType(oc, UIExtensionConstructor.class, UIExtensionConstructor::type);
			IUIExtension<?, ?> ret = Try.call(() -> {
				MethodHandle mh = DynamicUtilities.IMPL_LOOKUP.findConstructor(oc, ct.getMethodType());
				Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(object.getProperty());
				switch (ct) {
					case MAPPINGS__CONTAINER_CLASS:
						return (IUIExtension<?, ?>) mh.invoke(mappings, container.getClass());
					case MAPPINGS:
						return (IUIExtension<?, ?>) mh.invoke(mappings);
					case CONTAINER_CLASS:
						return (IUIExtension<?, ?>) mh.invoke(container.getClass());
					case NO_ARGS:
						return (IUIExtension<?, ?>) mh.invoke();
					default:
						throw new InternalError();
				}
			}, UIConfiguration.getInstance().getLogger()).orElseThrow(ThrowableCatcher::rethrow);
			Iterables.concat(
					object.getRenderer()
							.map(ImmutableSet::of)
							.orElseGet(ImmutableSet::of),
					object.getAnyContainer()
							.map(AnyContainer::getAny)
							.orElseGet(ImmutableList::of))
					.forEach(any -> IParserContext.StaticHolder.findHandler(context, any)
							.ifPresent(handler -> handler.accept(
									context,
									CastUtilities.castUnchecked(ret), // COMMENT may throw
									CastUtilities.castUnchecked(any) // COMMENT should not throw
							)));
			((IExtensionContainer<?>) container).addExtension(CastUtilities.castUnchecked(ret)); // COMMENT addExtension should check
		}
	}
}
