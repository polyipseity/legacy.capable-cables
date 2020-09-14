package $group__.ui.core.parsers.components;

import $group__.utilities.functions.IConsumer3;
import jakarta.xml.bind.JAXBElement;

import java.util.Map;
import java.util.Optional;

public interface IParserContext {
	IUIComponentParser.EnumHandlerType getHandlingType();

	Map<String, Class<?>> getAliases();

	Map<IUIComponentParser.EnumHandlerType, ? extends Map<Class<?>, IConsumer3<? super IParserContext, ?, ?>>> getHandlers();

	enum StaticHolder {
		;

		public static Optional<? extends IConsumer3<? super IParserContext, ?, ?>> findHandler(IParserContext self,
		                                                                                       Object any) {
			boolean element = any instanceof JAXBElement;
			return Optional.ofNullable(self.getHandlers().get(self.getHandlingType().getVariant(element)))
					.map(map -> map.get(element ? ((JAXBElement<?>) any).getDeclaredType() : any.getClass()));
		}
	}
}
