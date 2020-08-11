package $group__.client.ui.mvvm.structures;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;

import static $group__.utilities.specific.ComparableUtilities.greaterThanOrEqualTo;

@OnlyIn(Dist.CLIENT)
public enum EnumUIState {
	NEW {
		@Override
		public EnumSet<EnumUIState> getValidNextStates() { return EnumSet.of(NEW, READY); }
	},
	READY {
		@Override
		public EnumSet<EnumUIState> getValidNextStates() { return EnumSet.of(READY, CLOSED); }
	},
	CLOSED {
		@Override
		public EnumSet<EnumUIState> getValidNextStates() { return EnumSet.of(CLOSED, NEW); }
	};

	public abstract EnumSet<EnumUIState> getValidNextStates();

	public boolean isReachedBy(EnumUIState state) { return greaterThanOrEqualTo(state, this); }
}
