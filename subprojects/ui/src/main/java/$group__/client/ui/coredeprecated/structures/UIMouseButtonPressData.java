package $group__.client.ui.coredeprecated.structures;

import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
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
public final class UIMouseButtonPressData {
	public static final ImmutableList<Function<UIMouseButtonPressData, Object>> OBJECT_VARIABLES = ImmutableList.of(
			UIMouseButtonPressData::getCursorView, UIMouseButtonPressData::getButton);
	public static final ImmutableMap<String, Function<UIMouseButtonPressData, Object>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("cursor", "button"), OBJECT_VARIABLES));
	protected final Point2D cursor;
	protected final int button;

	public UIMouseButtonPressData(Point2D cursor, int button) {
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
		protected final IUIComponentDOMLike component;
		protected final UIMouseButtonPressData data;

		public Tracked(IUIComponentDOMLike component, UIMouseButtonPressData data) {
			this.component = component;
			this.data = data;
		}

		public IUIComponentDOMLike getComponent() { return component; }

		public UIMouseButtonPressData getData() { return data; }
	}
}
