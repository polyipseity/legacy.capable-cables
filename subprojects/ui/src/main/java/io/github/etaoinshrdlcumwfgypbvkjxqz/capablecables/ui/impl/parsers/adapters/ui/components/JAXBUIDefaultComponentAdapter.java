package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.IJAXBUIComponentAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IJAXBUIComponentAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IJAXBUIComponentBasedAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.contexts.JAXBUIImmutableComponentAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers.JAXBUIDefaultComponentAdapterAnchorHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers.JAXBUIDefaultComponentAdapterExtensionHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.TreeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IUnion;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableUnion;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class JAXBUIDefaultComponentAdapter
		extends JAXBUIAbstractComponentBasedAdapter<ComponentUI, IUIViewComponent<?, ?>>
		implements IJAXBUIComponentAdapter<ComponentUI, IUIViewComponent<?, ?>> {
	public static <T extends JAXBUIDefaultComponentAdapter> T makeParserStandard(T instance) {
		instance.addObjectHandler(Anchor.class, new JAXBUIDefaultComponentAdapterAnchorHandler());
		instance.addObjectHandler(Extension.class, new JAXBUIDefaultComponentAdapterExtensionHandler());
		return instance;
	}

	@Override
	@Deprecated
	@SuppressWarnings({"unchecked", "RedundantSuppression", "UnstableApiUsage", "CallToSuspiciousStringMethod"})
	public @Nonnull IUIViewComponent<?, ?> leftToRight(@Nonnull ComponentUI left) {
		return getThreadLocalContext()
				.map(context -> {
					try {
						// COMMENT raw
						@Immutable Map<String, Class<?>> aliases = JAXBUIComponentUtilities.adaptUsingFromJAXB(left.getUsing());
						View rawView = left.getView();
						Component rawComponent = left.getComponent();

						// COMMENT create hierarchy
						IUIViewComponent<?, ?> view = JAXBUIComponentUtilities.createView(
								context.withData(
										MapUtilities.concatMaps(context.getDataView(),
												ImmutableMap.of(
														IJAXBUIComponentAdapterContext.class, new JAXBUIImmutableComponentAdapterContext(aliases, getObjectHandlers(), getElementHandlers(), null, null))
										)),
								rawView);
						IJAXBUIComponentAdapterContext viewContext = new JAXBUIImmutableComponentAdapterContext(aliases, getObjectHandlers(), getElementHandlers(), view, view);
						view.setManager(
								CastUtilities.castUnchecked(JAXBUIComponentUtilities.createComponent(
										context.withData(
												MapUtilities.concatMaps(context.getDataView(),
														ImmutableMap.of(IJAXBUIComponentAdapterContext.class, viewContext))
										),
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
															context.withData(
																	MapUtilities.concatMaps(context.getDataView(),
																			ImmutableMap.of(IJAXBUIComponentAdapterContext.class, viewContext))
															),
															CastUtilities.castUnchecked(any) // COMMENT should not throw
													));
										}
								);

						// COMMENT configure components
						Map<String, IUIComponent> componentMap = INamed.toNamedMap(view.getChildrenFlatView()); // COMMENT need to build it ourselves
						Deque<@NonNls String>
								componentNames = new ArrayDeque<>(CapacityUtilities.getInitialCapacityMedium()),
								embedComponentNames = new ArrayDeque<>(CapacityUtilities.getInitialCapacitySmall());
						TreeUtilities.<IUnion<Component, ComponentEmbed>, IUnion<Component, ComponentEmbed>>visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, ImmutableUnion.ofLeft(rawComponent),
								node -> {
									IUIComponent component = IUnion.get(
											node.mapBoth(Component::getName,
													componentEmbed -> IUIComponentArguments.computeEmbedName(componentNames.element(),
															Iterables.concat(embedComponentNames, ImmutableSet.of(componentEmbed.getName()))))
													.mapBoth(componentMap::get, componentMap::get)
													.accept(AssertionUtilities::assertNonnull, leftNodeResult -> {
														if (leftNodeResult == null)
															throw new IllegalArgumentException();
													})
									);
									IJAXBUIComponentAdapterContext componentContext = new JAXBUIImmutableComponentAdapterContext(aliases, getObjectHandlers(), getElementHandlers(), view, component);
									node
											.map(
													leftNode -> Iterables.concat(
															leftNode.getAnchor(),
															leftNode.getExtension(),
															leftNode.getAnyContainer()
																	.map(AnyContainer::getAny)
																	.orElseGet(ImmutableList::of)
													),
													rightNode -> Iterables.concat(
															rightNode.getExtension(),
															rightNode.getAnyContainer()
																	.map(AnyContainer::getAny)
																	.orElseGet(ImmutableList::of)
													)
											)
											.forEach(any -> {
												assert any != null;
												IJAXBUIComponentBasedAdapterContext.findHandler(componentContext, any)
														.ifPresent(handler -> handler.accept(
																context.withData(
																		MapUtilities.concatMaps(context.getDataView(),
																				ImmutableMap.of(IJAXBUIComponentAdapterContext.class, componentContext))
																),
																CastUtilities.castUnchecked(any) // COMMENT should not throw
														));
											});
									return node;
								},
								node -> {
									node
											.mapBoth(Component::getName, ComponentEmbed::getName)
											.accept(componentNames::push, embedComponentNames::push);
									return Streams.stream(node
											.<Iterable<?>>map(
													nodeLeft -> Iterables.concat(
															nodeLeft.getComponent(),
															nodeLeft.getComponentEmbed()
													),
													ComponentEmbed::getComponentEmbed
											))
											.map(nodeChild -> ImmutableUnion.of(nodeChild, Component.class, ComponentEmbed.class))
											.collect(ImmutableList.toImmutableList());
								},
								(node, nodeChildren) ->
										node.accept(leftNode -> {
											assert componentNames.element().equals(leftNode.getName());
											String name = componentNames.pop(); // TODO ASM aaaAAA
										}, rightNode -> {
											assert embedComponentNames.element().equals(rightNode.getName());
											String name = embedComponentNames.pop(); // TODO ASM aaaAAA
										}),
								node -> { throw new AssertionError(); });
						return view;
					} catch (Throwable throwable) {
						throw ThrowableUtilities.propagate(throwable);
					}
				})
				.orElseThrow(IllegalStateException::new);
	}

	@Override
	@Deprecated
	public @Nonnull ComponentUI rightToLeft(@Nonnull IUIViewComponent<?, ?> right) {
		throw new UnsupportedOperationException(); // TODO implement
	}
}
