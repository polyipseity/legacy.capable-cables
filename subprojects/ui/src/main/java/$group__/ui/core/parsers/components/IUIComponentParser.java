package $group__.ui.core.parsers.components;

import $group__.ui.core.parsers.IUIResourceParser;
import $group__.utilities.functions.FunctionUtilities;
import $group__.utilities.functions.IConsumer3;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public interface IUIComponentParser<T, R>
		extends IUIResourceParser<T, R> {

	<H> void addHandler(Set<EnumHandlerType> types, Class<H> clazz, IConsumer3<? super IParserContext, ?, ? super H, ?> handler);

	@Immutable
	enum EnumHandlerType {
		VIEW_HANDLER(false) {
			@Override
			public EnumHandlerType getVariant(boolean element) { return element ? VIEW_ELEMENT_HANDLER : VIEW_HANDLER; }
		},
		COMPONENT_HANDLER(false) {
			@Override
			public EnumHandlerType getVariant(boolean element) { return element ? COMPONENT_ELEMENT_HANDLER : COMPONENT_HANDLER; }
		},
		VIEW_ELEMENT_HANDLER(true, VIEW_HANDLER),
		COMPONENT_ELEMENT_HANDLER(true, COMPONENT_HANDLER),
		;

		@Nullable
		protected final EnumHandlerType variantCounterpart;

		public static final ImmutableSet<EnumHandlerType> ALL = Sets.immutableEnumSet(EnumSet.allOf(EnumHandlerType.class));
		@SuppressWarnings("UnstableApiUsage")
		public static final ImmutableSet<EnumHandlerType> ELEMENTS_ONLY = ALL.stream().unordered()
				.filter(EnumHandlerType::isElement)
				.collect(Sets.toImmutableEnumSet());
		@SuppressWarnings("UnstableApiUsage")
		public static final ImmutableSet<EnumHandlerType> OBJECTS_ONLY = ALL.stream().unordered()
				.filter(FunctionUtilities.notPredicate(EnumHandlerType::isElement))
				.collect(Sets.toImmutableEnumSet());
		protected final boolean element;

		EnumHandlerType(@SuppressWarnings("SameParameterValue") boolean element) { this(element, null); }

		EnumHandlerType(boolean element, @Nullable EnumHandlerType variantCounterpart) {
			this.element = element;
			this.variantCounterpart = variantCounterpart;
		}

		public EnumHandlerType getVariant(boolean element) {
			return getVariantCounterpart().
					orElseThrow(AssertionError::new)
					.getVariant(element);
		}

		public boolean isElement() { return element; }

		private Optional<? extends EnumHandlerType> getVariantCounterpart() { return Optional.ofNullable(variantCounterpart); }
	}
}
