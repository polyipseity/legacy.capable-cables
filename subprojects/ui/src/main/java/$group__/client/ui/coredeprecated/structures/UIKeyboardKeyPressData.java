package $group__.client.ui.coredeprecated.structures;

import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
import $group__.utilities.ObjectUtilities;
import $group__.utilities.specific.MapUtilities;
import $group__.utilities.specific.ThrowableUtilities.Try;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import jdk.nashorn.internal.ir.annotations.Immutable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
@Immutable
public final class UIKeyboardKeyPressData implements Cloneable {
	public static final ImmutableList<Function<UIKeyboardKeyPressData, Object>> OBJECT_VARIABLES = ImmutableList.of(
			UIKeyboardKeyPressData::getKey, UIKeyboardKeyPressData::getScanCode, UIKeyboardKeyPressData::getModifiers);
	public static final ImmutableMap<String, Function<UIKeyboardKeyPressData, Object>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("key", "scanCode", "modifiers"), OBJECT_VARIABLES));
	private static final Logger LOGGER = LogManager.getLogger();
	protected final int key, scanCode, modifiers;

	public UIKeyboardKeyPressData(int key, int scanCode, int modifiers) {
		this.key = key;
		this.scanCode = scanCode;
		this.modifiers = modifiers;
	}

	public int getKey() { return key; }

	public int getScanCode() { return scanCode; }

	public int getModifiers() { return modifiers; }

	@Override
	public int hashCode() {
		return ObjectUtilities.hashCode(this, null, OBJECT_VARIABLES);
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, OBJECT_VARIABLES); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	public UIKeyboardKeyPressData clone() { return (UIKeyboardKeyPressData) Try.call(() -> super.clone(), LOGGER).orElseThrow(InternalError::new); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }

	@OnlyIn(Dist.CLIENT)
	public static class Tracked {
		protected final IUIComponentDOMLike component;
		protected final UIKeyboardKeyPressData data;

		public Tracked(IUIComponentDOMLike component, UIKeyboardKeyPressData data) {
			this.component = component;
			this.data = data;
		}

		public IUIComponentDOMLike getComponent() { return component; }

		public UIKeyboardKeyPressData getData() { return data; }
	}
}
