package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Ordered;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text.IAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.FloatUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.DoubleDimension2D;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

@SuppressWarnings("UnstableApiUsage")
public enum TextUtilities {
	;

	@SuppressWarnings("HardcodedLineSeparator")
	// COMMENT it is important that line separators are ordered by descending order of specificity
	private static final @Ordered Set<String> LINE_SEPARATORS = ImmutableSet.of("\r\n", // COMMENT Windows
			"\n", // COMMENT Unix and macOS
			"\r" // COMMENT Classic macOS
	);
	private static final Pattern DEFAULT_LINE_SEPARATOR_PATTERN = Pattern.compile(Pattern.quote(System.lineSeparator()));
	private static final @Ordered Set<Pattern> LINE_SEPARATOR_PATTERNS;
	private static final Font DEFAULT_FONT;
	private static final FontRenderContext DEFAULT_FONT_RENDER_CONTEXT;
	private static final IAttributedText EMPTY_ATTRIBUTED_TEXT;

	static {
		LINE_SEPARATOR_PATTERNS = getLineSeparators().stream() // COMMENT sequential, important
				.map(Pattern::quote)
				.map(Pattern::compile)
				.collect(ImmutableSet.toImmutableSet());
		DEFAULT_FONT = new Font(null);
		DEFAULT_FONT_RENDER_CONTEXT = new FontRenderContext(AffineTransformUtilities.getIdentity(), RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
		EMPTY_ATTRIBUTED_TEXT = ImmutableAttributedText.ofCharSequence("");
	}

	public static FontRenderContext getDefaultFontRenderContext() {
		return DEFAULT_FONT_RENDER_CONTEXT;
	}

	public static @Immutable @Ordered Set<String> getLineSeparators() {
		return ImmutableSet.copyOf(LINE_SEPARATORS);
	}

	public static boolean isEmpty(CharacterIterator text) {
		return text.getEndIndex() == text.getBeginIndex();
	}

	public static IAttributedText getEmptyAttributedText() {
		return EMPTY_ATTRIBUTED_TEXT;
	}

	public static CharacterIterator ensureNonEmpty(CharacterIterator text) {
		return isEmpty(text) ? getEmptyAttributedText().compile().getIterator() : text;
	}

	public static AttributedCharacterIterator ensureNonEmpty(AttributedCharacterIterator text) {
		return isEmpty(text) ? getEmptyAttributedText().compile().getIterator() : text;
	}

	public static String ensureNonEmpty(CharSequence text) {
		String text1 = text.toString();
		return text1.isEmpty() ? " " : text1;
	}

	public static void drawLines(Graphics2D graphics, Point2D position, double width, Iterable<? extends TextLayout> lines) {
		Point2D pen = (Point2D) position.clone();
		lines.forEach(line -> {
			drawLineWithCoordinates(graphics, pen, width, line);
			pen.setLocation(pen.getX(), pen.getY() + line.getAscent() + line.getDescent() + line.getLeading());
		});
	}

	public static void drawLineWithCoordinates(Graphics2D graphics, Point2D position, double width, TextLayout line) {
		drawLineWithBaseline(graphics, new Point2D.Double(position.getX(), position.getY() + line.getAscent()), width, line);
	}

	public static void drawLineWithBaseline(Graphics2D graphics, Point2D position, double width, TextLayout line) {
		line.draw(graphics,
				FloatUtilities.saturatedCast(position.getX()
						+ (line.isLeftToRight() ? 0D : (width - line.getAdvance()))),
				FloatUtilities.saturatedCast(position.getY()));
	}

	public static Dimension2D getLinesDimension(Iterable<? extends TextLayout> lines) {
		return new DoubleDimension2D(
				Streams.stream(lines)
						.mapToDouble(TextLayout::getAdvance)
						.max()
						.orElse(0D),
				Streams.stream(lines)
						.mapToDouble(line -> line.getAscent() + line.getDescent() + line.getLeading())
						.sum()
		);
	}

	@SuppressWarnings("ObjectAllocationInLoop")
	public static @Immutable List<AttributedCharacterIterator> separateLines(AttributedCharacterIterator text) {
		List<AttributedCharacterIterator> result = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());
		final AttributedStringBuilder[] builder = {new AttributedStringBuilder(CapacityUtilities.getInitialCapacityLarge())};
		forEachRun(text, text1 -> {
			Map<AttributedCharacterIterator.Attribute, Object> attributes = text1.getAttributes();
			String[] lines = getDefaultLineSeparatorPattern().split(unifyLineSeparators(currentRun(text1)), -1);
			boolean split = false;
			for (String line : lines) {
				if (split) {
					result.add(builder[0].build().getIterator());
					builder[0] = new AttributedStringBuilder(CapacityUtilities.getInitialCapacityLarge());
				}
				builder[0].addCharSequence(line).attachAttributes(attributes);
				split = true;
			}
		});
		result.add(builder[0].build().getIterator());
		return ImmutableList.copyOf(result);
	}

	public static <T extends AttributedCharacterIterator> void forEachRun(T text, Consumer<@Nonnull ? super T> action) {
		while (text.getRunStart() < text.getEndIndex() /* COMMENT equal means the last run */) {
			int runLimit = text.getRunLimit(); // COMMENT in case the action changes the index
			action.accept(text);
			text.setIndex(runLimit);
		}
	}

	public static Pattern getDefaultLineSeparatorPattern() {
		return DEFAULT_LINE_SEPARATOR_PATTERN;
	}

	public static String unifyLineSeparators(CharSequence charSequence) {
		return unifyLineSeparators(charSequence, System.lineSeparator());
	}

	public static String currentRun(AttributedCharacterIterator text) {
		return next(text, text.getRunLimit() - text.getRunStart());
	}

	public static String unifyLineSeparators(CharSequence charSequence, CharSequence lineSeparator) {
		final String[][] lines = {{charSequence.toString()}};
		getLineSeparatorPatterns().forEach(lineSeparatorPattern -> // COMMENT sequential, important
				lines[0] = Arrays.stream(lines[0])
						.map(line -> lineSeparatorPattern.split(line, -1))
						.flatMap(Arrays::stream)
						.toArray(String[]::new)
		);
		return String.join(lineSeparator, lines[0]);
	}

	public static String next(CharacterIterator text, int length) {
		char[] result = new char[length]; // COMMENT doing it directly
		for (int i = 0; i < length; ++i, text.next())
			result[i] = text.current();
		return String.valueOf(result);
	}

	public static @Immutable @Ordered Set<Pattern> getLineSeparatorPatterns() {
		return ImmutableSet.copyOf(LINE_SEPARATOR_PATTERNS);
	}

	public static Font getDefaultFont() {
		return DEFAULT_FONT;
	}

	public static class LineBreakMeasurerAsTextLayoutIterator
			implements Iterator<TextLayout> {
		private final LineBreakMeasurer lineBreakMeasurer;
		private final Function<@Nonnull ? super LineBreakMeasurer, @Nullable ? extends TextLayout> nextFunction;

		private final Object nextLockObject;
		private @Nullable TextLayout next;

		public LineBreakMeasurerAsTextLayoutIterator(LineBreakMeasurer lineBreakMeasurer,
		                                             Function<@Nonnull ? super LineBreakMeasurer, @Nullable ? extends TextLayout> nextFunction) {
			this.lineBreakMeasurer = lineBreakMeasurer;
			this.nextFunction = nextFunction;

			this.nextLockObject = new Object();
			this.next = nextFunction.apply(lineBreakMeasurer);
		}

		@Override
		public boolean hasNext() {
			synchronized (getNextLockObject()) {
				return getNext().isPresent();
			}
		}

		protected Object getNextLockObject() {
			return nextLockObject;
		}

		protected Optional<? extends TextLayout> getNext() {
			return Optional.ofNullable(next);
		}

		protected void setNext(@Nullable TextLayout next) {
			this.next = next;
		}

		@Override
		public TextLayout next() {
			TextLayout next;
			synchronized (getNextLockObject()) {
				next = getNext()
						.orElseThrow(NoSuchElementException::new);
				setNext(getNextFunction().apply(getLineBreakMeasurer()));
			}
			return next;
		}

		protected Function<? super LineBreakMeasurer, ? extends TextLayout> getNextFunction() {
			return nextFunction;
		}

		protected LineBreakMeasurer getLineBreakMeasurer() {
			return lineBreakMeasurer;
		}
	}
}
