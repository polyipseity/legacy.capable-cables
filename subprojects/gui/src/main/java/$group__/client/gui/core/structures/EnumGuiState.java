package $group__.client.gui.core.structures;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;

import static $group__.utilities.specific.ComparableUtilities.greaterThanOrEqualTo;

@OnlyIn(Dist.CLIENT)
public enum EnumGuiState {
	NEW {
		@Override
		public EnumSet<EnumGuiState> getValidNextStates() { return EnumSet.of(NEW, READY); }
	},
	READY {
		@Override
		public EnumSet<EnumGuiState> getValidNextStates() { return EnumSet.of(READY, CLOSED); }
	},
	CLOSED {
		@Override
		public EnumSet<EnumGuiState> getValidNextStates() { return EnumSet.of(CLOSED, NEW); }
	};

	public abstract EnumSet<EnumGuiState> getValidNextStates();

	public boolean isReachedBy(EnumGuiState state) { return greaterThanOrEqualTo(state, this); }
}
