package $group__.ui.parsers.components;

import $group__.jaxb.subprojects.ui.components.*;
import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRenderer;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRendererContainer;
import $group__.ui.core.parsers.components.*;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilderFactory;
import $group__.ui.mvvm.structures.UIPropertyMappingValue;
import $group__.ui.structures.shapes.interactions.ShapeConstraint;
import $group__.utilities.*;
import $group__.utilities.ThrowableUtilities.ThrowableCatcher;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.functions.IConsumer3;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import com.google.common.collect.*;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.awt.geom.AffineTransform;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.regex.Pattern;

public class UIDefaultComponentParser<T extends IUIViewComponent<?, ?>>
		extends IHasGenericClass.Impl<T>
		implements IUIComponentParser<T, Function<? super Unmarshaller, ?>> {
	private static final Logger LOGGER = LogManager.getLogger();

	protected final ConcurrentMap<String, Class<?>> aliases = MapUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	protected final ConcurrentMap<Class<?>, IConsumer3<? super IParserContext, ?, ?>> viewHandler = MapUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	protected final ConcurrentMap<Class<?>, IConsumer3<? super IParserContext, ?, ?>> componentHandler = MapUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	@Nullable
	protected Ui root;

	public UIDefaultComponentParser(Class<T> genericClass) {
		super(genericClass);
	}

	public static <T extends IUIComponentParser<?, ?>> T makeParserStandard(T instance) {
		instance.addHandler(EnumSet.allOf(EnumHandlerType.class), Extension.class, DefaultHandlers::handleExtension);
		instance.addHandler(EnumSet.allOf(EnumHandlerType.class), Renderer.class, DefaultHandlers::handleRenderer);
		return instance;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void parse(Function<? super Unmarshaller, ?> resource) throws Throwable {
		Unmarshaller um = SchemaHolder.CONTEXT.createUnmarshaller();
		um.setSchema(SchemaHolder.SCHEMA);
		Ui root = (Ui) resource.apply(um);
		reset();

		getAliases().putAll(
				root.getUsing().stream().unordered()
						.map(u -> Maps.immutableEntry(u.getAlias(), Try.call(() -> Class.forName(u.getTarget()), LOGGER).orElseThrow(ThrowableCatcher::rethrow)))
						.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)));

		{
			Class<?> viewClass = AssertionUtilities.assertNonnull(getAliases().get(root.getView().getClazz()));
			if (!getGenericClass().isAssignableFrom(viewClass))
				throw ThrowableUtilities.BecauseOf.illegalArgument("Generic class is not assignable from view type",
						"viewClass", viewClass,
						"getGenericClass()", getGenericClass(),
						"resource", resource);
		}

		setRoot(root);
	}

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
							IParserContext ctx = new ImmutableParserContext(getAliases());
							View v = root.getView();
							Component c = root.getComponent();
							@SuppressWarnings("unchecked") T ret = (T) createView(ctx, v); // COMMENT should be checked
							ret.setManager(
									CastUtilities.castUnchecked(createComponent(ctx, c)) // COMMENT may throw
							);
							Iterables.concat(v.getExtension(), v.getAnyContainer().getAny())
									.forEach(t -> Optional.ofNullable(getViewHandler().get(t.getClass()))
											.ifPresent(h -> h.accept(
													ctx,
													CastUtilities.castUnchecked(ret), // COMMENT may throw
													CastUtilities.castUnchecked(t) // COMMENT should not throw
											)));
							final IUIComponent[] cc = {ret.getManager()
									.orElseThrow(IllegalStateException::new)};
							TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, c,
									n -> {
										IUIComponent component = CastUtilities.castChecked(IUIComponentContainer.class, cc[0])
												.map(IUIComponentContainer::getNamedChildrenMapView)
												.map(m -> AssertionUtilities.assertNonnull(m.get(n.getId())))
												.orElseThrow(AssertionError::new);
										Iterables.concat(ImmutableList.of(n.getRenderer()), n.getExtension(), n.getAnyContainer().getAny())
												.forEach(t -> Optional.ofNullable(getComponentHandler().get(t.getClass()))
														.ifPresent(h -> h.accept(
																ctx,
																CastUtilities.castUnchecked(component), // COMMENT may throw
																CastUtilities.castUnchecked(t) // COMMENT should not throw
														)));
										return n;
									},
									n -> {
										if (!c.equals(n))
											cc[0] = CastUtilities.castChecked(IUIComponentContainer.class, cc[0])
													.map(IUIComponentContainer::getNamedChildrenMapView)
													.map(m -> AssertionUtilities.assertNonnull(m.get(n.getId())))
													.orElseGet(() -> cc[0]);
										return n.getComponent();
									},
									(p, ch) -> {
										if (!c.equals(p))
											cc[0] = cc[0].getParent()
													.orElseThrow(AssertionError::new);
										return p;
									}, n -> {
										throw ThrowableUtilities.BecauseOf.illegalArgument("Cyclic hierarchy detected",
												"n", n,
												"c", c);
									});
							return ret;
						}, LOGGER)
								.orElseThrow(ThrowableCatcher::rethrow))
				.orElseThrow(() -> new IllegalStateException("Prototype has not been created"));
	}

	protected Optional<? extends Ui> getRoot() { return Optional.ofNullable(root); }

	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	public static IUIViewComponent<?, ?> createView(IParserContext context, View view) {
		Class<?> vc = AssertionUtilities.assertNonnull(context.getAliases().get(view.getClazz()));
		UIViewComponentConstructor.EnumConstructorType ct = IConstructorType.getConstructorType(vc, UIViewComponentConstructor.class, UIViewComponentConstructor::type);
		return Try.call(() -> {
			MethodHandle mh = DynamicUtilities.IMPL_LOOKUP.findConstructor(vc, ct.getMethodType());
			Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(view.getProperty(), view.getBinding());
			switch (ct) {
				case MAPPINGS:
					return (IUIViewComponent<?, ?>) mh.invoke(
							mappings
					);
				default:
					throw new InternalError();
			}
		}, LOGGER)
				.orElseThrow(ThrowableCatcher::rethrow);
	}

	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	public static IUIComponent createComponent(IParserContext context, Component component) {
		return TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, component,
				n -> {
					Class<?> cc = AssertionUtilities.assertNonnull(context.getAliases().get(n.getClazz()));
					UIComponentConstructor.EnumConstructorType ct = IConstructorType.getConstructorType(cc, UIComponentConstructor.class, UIComponentConstructor::type);
					return Try.call(() -> {
						MethodHandle mh = DynamicUtilities.IMPL_LOOKUP.findConstructor(cc, ct.getMethodType());
						Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(n.getProperty(), n.getBinding());
						IShapeDescriptor<?> sd = createShapeDescriptor(context, n.getShape());
						switch (ct) {
							case MAPPINGS__ID__SHAPE_DESCRIPTOR:
								return (IUIComponent) mh.invoke(mappings, sd);
							default:
								throw new InternalError();
						}
					}, LOGGER)
							.orElseThrow(ThrowableCatcher::rethrow);
				},
				Component::getComponent,
				(p, c) -> {
					if (!Iterables.isEmpty(c)) {
						if (!(p instanceof IUIComponentContainer))
							throw ThrowableUtilities.BecauseOf.illegalArgument("UI component is not a container",
									"p.getClass()", p.getClass(),
									"p", p);
						((IUIComponentContainer) p).addChildren(c);
					}
					return p;
				}, n -> {
					throw ThrowableUtilities.BecauseOf.illegalArgument("Cyclic hierarchy detected",
							"n", n,
							"component", component);
				})
				.orElseThrow(InternalError::new);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static Map<INamespacePrefixedString, IUIPropertyMappingValue> createMappings(List<PropertyType> properties, List<BindingType> bindings) {
		return Streams.concat(
				properties.stream().unordered()
						.map(p -> Maps.immutableEntry(new NamespacePrefixedString(p.getKey()),
								new UIPropertyMappingValue(p.getAny(), null))),
				bindings.stream().unordered()
						.map(b -> Maps.immutableEntry(new NamespacePrefixedString(b.getKey()),
								new UIPropertyMappingValue(b.getAny(),
										Optional.ofNullable(b.getBinding())
												.map(NamespacePrefixedString::new)
												.orElse(null))))
		).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@SuppressWarnings("UnstableApiUsage")
	public static IShapeDescriptor<?> createShapeDescriptor(IParserContext context, $group__.jaxb.subprojects.ui.components.Shape shape) {
		AffineTransform transform = new AffineTransform();
		Optional.ofNullable(shape.getAffineTransformDefiner())
				.ifPresent(atd -> {
					$group__.jaxb.subprojects.ui.components.AffineTransform at = atd.getAffineTransform();
					transform.setTransform(at.getScaleX(), at.getShearY(), at.getShearX(), at.getScaleY(), at.getTranslateX(), at.getTranslateY());
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
										.invokeWithArguments((Object[]) args), LOGGER);
						ThrowableUtilities.ThrowableCatcher.rethrow(true);
					});
				});

		IShapeDescriptorBuilder<?> sdb = IShapeDescriptorBuilderFactory.getDefault()
				.createBuilder(
						CastUtilities.castUnchecked(AssertionUtilities.assertNonnull(context.getAliases().get(shape.getClazz()))) // COMMENT should not throw
				);

		shape.getProperty().stream().unordered()
				.forEach(p -> sdb.setProperty(p.getKey(), p.getAny())); // todo IMPORTANT serializer

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
	public Map<String, Class<?>> getAliasesView() { return ImmutableMap.copyOf(getAliases()); }

	@Override
	public <H> void addHandler(Set<EnumHandlerType> types, Class<H> clazz, IConsumer3<? super IParserContext, ?, ? super H> handler) {
		types.forEach(type -> {
			switch (type) {
				case VIEW_HANDLER:
					getViewHandler().put(clazz, handler);
					break;
				case COMPONENT_HANDLER:
					getComponentHandler().put(clazz, handler);
					break;
				default:
					throw new InternalError();
			}
		});
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Class<?>, IConsumer3<? super IParserContext, ?, ?>> getViewHandler() { return viewHandler; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Class<?>, IConsumer3<? super IParserContext, ?, ?>> getComponentHandler() { return componentHandler; }

	public enum DefaultHandlers {
		;

		private static final Logger LOGGER = LogManager.getLogger();

		public static void handleExtension(IParserContext context, Object container, Extension object) {
			if (!(container instanceof IExtensionContainer))
				return;
			Class<?> oc = AssertionUtilities.assertNonnull(context.getAliases().get(object.getClazz()));
			UIExtensionConstructor.EnumConstructorType ct = IConstructorType.getConstructorType(oc, UIExtensionConstructor.class, UIExtensionConstructor::type);
			IUIExtension<?, ?> ret = Try.call(() -> {
				MethodHandle mh = DynamicUtilities.IMPL_LOOKUP.findConstructor(oc, ct.getMethodType());
				Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(object.getProperty(), object.getBinding());
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
			}, LOGGER).orElseThrow(ThrowableCatcher::rethrow);
			((IExtensionContainer<?>) container).addExtension(CastUtilities.castUnchecked(ret)); // COMMENT addExtension should check
		}

		public static void handleRenderer(IParserContext context, Object container, Renderer object) {
			if (!(container instanceof IUIComponentRendererContainer))
				return;
			Class<?> oc = AssertionUtilities.assertNonnull(context.getAliases().get(object.getClazz()));
			UIRendererConstructor.EnumConstructorType ct = IConstructorType.getConstructorType(oc, UIRendererConstructor.class, UIRendererConstructor::type);
			IUIComponentRenderer<?> ret = Try.call(() -> {
				MethodHandle mh = DynamicUtilities.IMPL_LOOKUP.findConstructor(oc, ct.getMethodType());
				Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(object.getProperty(), object.getBinding());
				switch (ct) {
					case MAPPINGS__CONTAINER_CLASS:
						return (IUIComponentRenderer<?>) mh.invoke(mappings, container.getClass());
					case MAPPINGS:
						return (IUIComponentRenderer<?>) mh.invoke(mappings);
					case CONTAINER_CLASS:
						return (IUIComponentRenderer<?>) mh.invoke(container.getClass());
					case NO_ARGS:
						return (IUIComponentRenderer<?>) mh.invoke();
					default:
						throw new InternalError();
				}
			}, LOGGER).orElseThrow(ThrowableCatcher::rethrow);
			((IUIComponentRendererContainer<?>) container).setRenderer(CastUtilities.castUnchecked(ret)); // COMMENT setRenderer should check
		}
	}

	@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
	@Immutable
	public interface IParserContext {
		Map<String, Class<?>> getAliases();
	}

	@Immutable
	public static class ImmutableParserContext
			implements IParserContext {
		protected final Map<String, Class<?>> aliases;

		public ImmutableParserContext(Map<String, Class<?>> aliases) {
			this.aliases = ImmutableMap.copyOf(aliases);
		}

		@Override
		public Map<String, Class<?>> getAliases() { return aliases; }
	}
}
