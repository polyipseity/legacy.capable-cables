package $group__.client.gui.core.structures;

import $group__.client.gui.core.IGuiComponent;
import $group__.utilities.ObjectUtilities;
import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import jdk.nashorn.internal.ir.annotations.Immutable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
@Immutable
public final class GuiMouseButtonPressData {
	public static final ImmutableList<Function<GuiMouseButtonPressData, Object>> OBJECT_VARIABLES = ImmutableList.of(
			GuiMouseButtonPressData::getCursorView, GuiMouseButtonPressData::getButton);
	public static final ImmutableMap<String, Function<GuiMouseButtonPressData, Object>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("cursor", "button"), OBJECT_VARIABLES));
	protected final Point2D cursor;
	protected final int button;

	public GuiMouseButtonPressData(Point2D cursor, int button) {
		this.cursor = (Point2D) cursor.clone();
		this.button = button;
	}

	public Point2D getCursorView() { return (Point2D) cursor.clone(); }

	public int getButton() { return button; }

	@Override
	public int hashCode() {
		return ObjectUtilities.hashCode(this, null, OBJECT_VARIABLES);
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, OBJECT_VARIABLES); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }

	@OnlyIn(Dist.CLIENT)
	public static class Tracked {
		protected final IGuiComponent<?, ?, ?> component;
		protected final GuiMouseButtonPressData data;

		public Tracked(IGuiComponent<?, ?, ?> component, GuiMouseButtonPressData data) {
			this.component = component;
			this.data = data;
		}

		public IGuiComponent<?, ?, ?> getComponent() { return component; }

		public GuiMouseButtonPressData getData() { return data; }
	}
}
