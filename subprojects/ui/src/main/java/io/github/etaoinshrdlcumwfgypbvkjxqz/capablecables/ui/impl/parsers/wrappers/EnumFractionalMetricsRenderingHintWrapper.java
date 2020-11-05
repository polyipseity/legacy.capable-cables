package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.wrappers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.wrappers.IFractionalMetricsRenderingHintWrapper;

import java.awt.*;
import java.util.Arrays;

public enum EnumFractionalMetricsRenderingHintWrapper
		implements IFractionalMetricsRenderingHintWrapper {
	DEFAULT {
		@Override
		public Object getData() {
			return RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT;
		}
	},
	ON {
		@Override
		public Object getData() {
			return RenderingHints.VALUE_FRACTIONALMETRICS_ON;
		}
	},
	OFF {
		@Override
		public Object getData() {
			return RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
		}
	},
	;

	public static EnumFractionalMetricsRenderingHintWrapper valueOfData(Object data) {
		return Arrays.stream(values()).unordered()
				.filter(value -> value.getData().equals(data))
				.findAny()
				.orElseThrow(IllegalArgumentException::new);
	}
}
