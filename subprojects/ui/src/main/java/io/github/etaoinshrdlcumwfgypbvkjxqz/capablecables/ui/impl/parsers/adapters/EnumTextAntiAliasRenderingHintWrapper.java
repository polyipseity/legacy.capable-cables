package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ITextAntiAliasRenderingHintWrapper;

import java.awt.*;
import java.util.Arrays;

public enum EnumTextAntiAliasRenderingHintWrapper
		implements ITextAntiAliasRenderingHintWrapper {
	DEFAULT {
		@Override
		public Object getData() {
			return RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
		}
	},
	ON {
		@Override
		public Object getData() {
			return RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
		}
	},
	OFF {
		@Override
		public Object getData() {
			return RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
		}
	},
	GASP {
		@Override
		public Object getData() {
			return RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;
		}
	},
	LCD_HRGB {
		@Override
		public Object getData() {
			return RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
		}
	},
	LCD_HBGR {
		@Override
		public Object getData() {
			return RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR;
		}
	},
	LCD_VRGB {
		@Override
		public Object getData() {
			return RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB;
		}
	},
	LCD_VBGR {
		@Override
		public Object getData() {
			return RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR;
		}
	},
	;

	public static EnumTextAntiAliasRenderingHintWrapper valueOfData(Object data) {
		return Arrays.stream(values()).unordered()
				.filter(value -> value.getData().equals(data))
				.findAny()
				.orElseThrow(IllegalArgumentException::new);
	}
}
